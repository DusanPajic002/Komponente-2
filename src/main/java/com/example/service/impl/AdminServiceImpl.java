package com.example.service.impl;

import com.example.domain.Admin;
import com.example.domain.Client;
import com.example.domain.Manager;
import com.example.dto.*;
import com.example.mapper.ClientMapper;
import com.example.mapper.ManagerMapper;
import com.example.repository.AdminRepository;
import com.example.repository.ClientRepository;
import com.example.repository.ManagerRepository;
import com.example.secutiry.service.TokenService;
import com.example.service.AdminService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private TokenService tokenService;
    private AdminRepository adminRepository;
    private ClientRepository clientRepository;
    private ClientMapper clientMapper;
    private ManagerRepository managerRepository;
    private ManagerMapper managerMapper;

    public AdminServiceImpl(TokenService tokenService, AdminRepository adminRepository, ClientRepository clientRepository, ClientMapper clientMapper, ManagerRepository managerRepository, ManagerMapper managerMapper) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
    }

    @Override
    public Page<ClientDto> findAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(clientMapper::clientToClientDto);
    }

    @Override
    public Page<ManagerDto> findAllManageres(Pageable pageable) {
        return managerRepository.findAll(pageable)
                .map(managerMapper::managerToManagerDto);
    }

    @Override
    public ClientDto updatePermissionClient(UpdatePermissionDto updatePermissionDto) {
        Client client = clientRepository.findByUser_EmailAndUser_Username(updatePermissionDto.getEmail(),updatePermissionDto.getUsername()).get();
        if(client == null){
            return null;
        }
        client =  clientMapper.updatePermissionDtoToClient(client,updatePermissionDto);
        client = clientRepository.save(client);
        return clientMapper.clientToClientDto(client);
    }

    @Override
    public ManagerDto updatePermissionManager(UpdatePermissionDto updatePermissionDto) {
        Manager manager = managerRepository.findByUser_EmailAndUser_Username(updatePermissionDto.getEmail(),updatePermissionDto.getUsername()).get();
        if(manager == null){
            return null;
        }
        manager = managerMapper.updatePermissionDtoToManager(manager,updatePermissionDto);
        manager = managerRepository.save(manager);
        return managerMapper.managerToManagerDto(manager);
    }


    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        //Try to find active user for specified credentials
        Admin user = null;
        user = adminRepository
                .findByUser_EmailAndUser_Password(tokenRequestDto.getEmail(), tokenRequestDto.getPassword())
                .orElse(null);
        if(user == null){
            System.out.println("Admin not found");
            return null;
        }
        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("username", user.getUser().getUsername());
        claims.put("email", user.getUser().getEmail());
        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }
}
