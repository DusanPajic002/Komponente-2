package com.example.service.impl;

import com.example.domain.Client;
import com.example.dto.*;
import com.example.listener.MessageHelper;
import com.example.mapper.ClientMapper;
import com.example.repository.ClientRepository;
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
    private ClientMapper clientMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    private String registrationMessage;
    private MessageHelper messageHelper;

    public ClientServiceImpl(TokenService tokenService, ClientRepository clientRepository, ClientMapper clientMapper, JmsTemplate jmsTemplate,
                             @Value("${destination.registrationMessage}") String registrationMessage, MessageHelper messageHelper) {
        this.tokenService = tokenService;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.jmsTemplate = jmsTemplate;
        this.registrationMessage = registrationMessage;
        this.messageHelper = messageHelper;
    }

    @Override
    public Page<ClientDto> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(clientMapper::clientToClientDto);
    }

    @Override
    public ClientDto add(ClientCreateDto clientCreateDto) {
        Client client = clientMapper.clientCreateDtoToClient(clientCreateDto);
        clientRepository.save(client);
        NotificationCreateDto nDto = new NotificationCreateDto(clientCreateDto.getUserDto().getFirstName(),clientCreateDto.getUserDto().getLastName(),
                clientCreateDto.getUserDto().getEmail(),clientCreateDto.getUserDto().getUsername());
        System.out.println(nDto);
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
        Client client = clientRepository.findByUser_EmailAndUser_Password(updatePasswordDto.getEmail(), updatePasswordDto.getOldPassword()).get();
        if(client == null){
            return null;
        }
        System.out.println(client);
        client =  clientMapper.updatePassword(client,updatePasswordDto);
        client = clientRepository.save(client);
        return clientMapper.clientToClientDto(client);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        //Try to find active user for specified credentials
        System.out.println(tokenRequestDto);
        Client user = null;
        try {
            user = clientRepository
                    .findByUser_EmailAndUser_Password(tokenRequestDto.getEmail(), tokenRequestDto.getPassword())
                    .orElseThrow(() -> new NotFoundException(String
                    .format("User with username: %s and password: %s not found.", tokenRequestDto.getEmail(),
                            tokenRequestDto.getPassword())));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("uniqueCardNumber", user.getUniqueCardNumber());
        claims.put("username", user.getUser().getUsername());
        claims.put("email", user.getUser().getEmail());
        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public ClientDto findByUsername(String username) {
        Optional<Client> client = clientRepository.findByUser_Username(username);
        ClientDto cd = client.map(clientMapper::clientToClientDto)
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
        return cd;
    }
}
