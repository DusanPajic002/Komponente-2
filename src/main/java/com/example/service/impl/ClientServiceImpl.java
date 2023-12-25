package com.example.service.impl;

import com.example.domain.Client;
import com.example.dto.*;
import com.example.mapper.ClientMapper;
import com.example.repository.ClientRepository;
import com.example.secutiry.service.TokenService;
import com.example.service.ClientService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private TokenService tokenService;
    private ClientRepository clientRepository;
    private ClientMapper clientMapper;

    public ClientServiceImpl(TokenService tokenService, ClientRepository clientRepository, ClientMapper clientMapper) {
        this.tokenService = tokenService;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
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
        return clientMapper.clientToClientDto(client);
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
        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("class", "Client");
        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }
}
