package com.example.service.impl;

import com.example.domain.Client;
import com.example.domain.Manager;
import com.example.dto.*;
import com.example.listener.MessageHelper;
import com.example.mapper.ManagerMapper;
import com.example.repository.ClientRepository;
import com.example.repository.ManagerRepository;
import com.example.secutiry.service.TokenService;
import com.example.service.ManagerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {

    private TokenService tokenService;
    private ManagerRepository managerRepository;
    private ManagerMapper managerMapper;
    private RestTemplate resevationServiceRestTemplate;
    private ClientRepository clientRepository;
    @Autowired
    private JmsTemplate jmsTemplate;
    private String registrationMessage;
    private MessageHelper messageHelper;

    public ManagerServiceImpl(TokenService tokenService, ManagerRepository managerRepository, ManagerMapper managerMapper, ClientRepository clientRepository,
                              @Value("${destination.registrationMessage}") String registrationMessage, MessageHelper messageHelper, RestTemplate resevationServiceRestTemplate) {
        this.tokenService = tokenService;
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
        this.clientRepository = clientRepository;
        this.registrationMessage = registrationMessage;
        this.resevationServiceRestTemplate = resevationServiceRestTemplate;
        this.messageHelper = messageHelper;
    }

    @Override
    public Page<ManagerDto> findAll(Pageable pageable) {
        return managerRepository.findAll(pageable)
                .map(managerMapper::managerToManagerDto);
    }

    @Override
    public ManagerDto add(ManagerCreateDto managerCreateDto) {
        Client clientUserName = clientRepository.findByUser_Username(managerCreateDto.getUserDto().getUsername()).orElse(null);;
        Client clientEmail = clientRepository.findByUser_Email(managerCreateDto.getUserDto().getEmail()).orElse(null);;
        if(clientUserName != null || clientEmail != null)
            return null;
        Manager manager = managerMapper.managerCreateDtoToManager(managerCreateDto);
        managerRepository.save(manager);

        HallDto hallDto = new HallDto(manager.getHallName(), manager.getId());
        System.out.println(hallDto);
        ResponseEntity<Integer> responseEntity = resevationServiceRestTemplate.exchange("/hall/setHallManager",
                HttpMethod.PUT, new HttpEntity<>(hallDto), Integer.class);
        if(responseEntity.getBody() == 0)
            throw new NoSuchElementException("Hall not found");

        NotificationCreateDto nDto = new NotificationCreateDto(managerCreateDto.getUserDto().getFirstName(),managerCreateDto.getUserDto().getLastName(),
                managerCreateDto.getUserDto().getEmail(),managerCreateDto.getUserDto().getUsername());
        jmsTemplate.convertAndSend(registrationMessage, messageHelper.createTextMessage(nDto));
        return managerMapper.managerToManagerDto(manager);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        Manager user = null;
        user = managerRepository
                .findByUser_EmailAndUser_Password(tokenRequestDto.getEmail(), tokenRequestDto.getPassword())
                .orElse(null);
        if(user == null){
            System.out.println("Manager not found");
            return null;
        }
        if(user.getUser().isPermission() == false){
            System.out.println("No permission");
            return null;
        }
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("hallName", user.getHallName());
        claims.put("username", user.getUser().getUsername());
        claims.put("email", user.getUser().getEmail());
        claims.put("rola", user.getRola());
        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public ManagerDto findByUsername(String username) {
        Optional<Manager> manager = managerRepository.findByUser_Username(username);
        ManagerDto md = manager.map(managerMapper::managerToManagerDto)
                .orElseThrow(() -> new NoSuchElementException("Manager not found"));
        return md;
    }


}
