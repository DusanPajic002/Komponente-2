package com.example.service.impl;

import com.example.domain.Client;
import com.example.domain.Manager;
import com.example.dto.*;
import com.example.listener.MessageHelper;
import com.example.mapper.ClientMapper;
import com.example.repository.ClientRepository;
import com.example.repository.ManagerRepository;
import com.example.secutiry.service.TokenService;
import com.example.service.ClientService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private TokenService tokenService;
    private ClientRepository clientRepository;

    private ManagerRepository managerRepository;
    private ClientMapper clientMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    private String registrationMessage;;
    private String changedPassword;
    private MessageHelper messageHelper;

    public ClientServiceImpl(TokenService tokenService, ClientRepository clientRepository, ClientMapper clientMapper, JmsTemplate jmsTemplate,
                             @Value("${destination.registrationMessage}") String registrationMessage, @Value("${destination.changedPasswordMessage}") String changedPassword
                                    ,MessageHelper messageHelper,  ManagerRepository managerRepository ) {
        this.tokenService = tokenService;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.jmsTemplate = jmsTemplate;
        this.registrationMessage = registrationMessage;
        this.changedPassword = changedPassword;
        this.messageHelper = messageHelper;
        this.managerRepository = managerRepository;
    }

    @Override
    public Page<ClientDto> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(clientMapper::clientToClientDto);
    }

    @Override
    public ClientDto add(ClientCreateDto clientCreateDto) {
        Manager managerUserName = managerRepository.findByUser_Username(clientCreateDto.getUserDto().getUsername()).orElse(null);;
        Manager managerEmail = managerRepository.findByUser_Email(clientCreateDto.getUserDto().getEmail()).orElse(null);;
        if(managerUserName != null || managerEmail != null)
            return null;

        Client client = clientMapper.clientCreateDtoToClient(clientCreateDto);
        clientRepository.save(client);
        NotificationCreateDto nDto = new NotificationCreateDto(clientCreateDto.getUserDto().getFirstName(),clientCreateDto.getUserDto().getLastName(),
                clientCreateDto.getUserDto().getEmail(),clientCreateDto.getUserDto().getUsername());
        jmsTemplate.convertAndSend(registrationMessage, messageHelper.createTextMessage(nDto));
        return clientMapper.clientToClientDto(client);
    }

    @Override
    public ClientDto findClient(String token) {
        Claims claims = tokenService.parseToken(token);
        Optional<Client> client = clientRepository.findByuniqueCardNumber(claims.get("uniqueCardNumber", String.class));
        ClientDto cd = client.map(clientMapper::clientToClientDto)
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
        return cd;
    }

    @Override
    public ClientDto update(UpdateClientDto updateClientDto) {
        Client client = clientRepository.findByuniqueCardNumber(updateClientDto.getUniqueCardNumber()).get();
        if(client == null){
            return null;
        }
        client =  clientMapper.updateClientDtoToClient(client,updateClientDto);
        client = clientRepository.save(client);
        return clientMapper.clientToClientDto(client);
    }

    @Override
    public ClientDto updatePassword(UpdatePasswordDto updatePasswordDto) {
        Claims claims = tokenService.parseToken(updatePasswordDto.getToken());
        Optional<Client> cd = clientRepository.findByuniqueCardNumber(claims.get("uniqueCardNumber", String.class));
        Client client = cd.map(clientMapper::client)
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
        if(client == null)
            return null;
        client =  clientMapper.updatePassword(client,updatePasswordDto);
        client = clientRepository.save(client);

        NotificationCreateDto nDto = new NotificationCreateDto(client.getUser().getFirstName(),client.getUser().getLastName(),
                client.getUser().getEmail(),client.getUser().getUsername());
        jmsTemplate.convertAndSend(changedPassword, messageHelper.createTextMessage(nDto));
        return clientMapper.clientToClientDto(client);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        Client user = null;
        user = clientRepository
                .findByUser_EmailAndUser_Password(tokenRequestDto.getEmail(), tokenRequestDto.getPassword())
                .orElse(null);

        if(user == null){
            System.out.println("Client not found");
            return null;
        }
        if(user.getUser().isPermission() == false){
            System.out.println("No permission");
            return null;
        }
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("uniqueCardNumber", user.getUniqueCardNumber());
        claims.put("username", user.getUser().getUsername());
        claims.put("email", user.getUser().getEmail());
        claims.put("firstName", user.getUser().getFirstName());
        claims.put("lastName", user.getUser().getLastName());
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public ClientDto findByUsername(String username) {
        Optional<Client> client = clientRepository.findByUser_Username(username);
        ClientDto cd = client.map(clientMapper::clientToClientDto)
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
        return cd;
    }

    @Override
    public int updateNumberOfTrainings(Long id, int i) {
        Optional<Client> client = clientRepository.findById(id);
        if(client.isPresent()){
            Client c = client.get();
            c.setNubmerOfTrainings(c.getNubmerOfTrainings() + i);
            clientRepository.save(c);
            return c.getNubmerOfTrainings();
        }
        return -1;
    }


}











//        try {
//            user = clientRepository
//                    .findByUser_EmailAndUser_Password(tokenRequestDto.getEmail(), tokenRequestDto.getPassword())
//                    .orElse(null);
//        } catch (NotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }