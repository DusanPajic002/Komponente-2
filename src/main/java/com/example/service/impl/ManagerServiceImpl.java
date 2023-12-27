package com.example.service.impl;

import com.example.domain.Client;
import com.example.domain.Manager;
import com.example.dto.*;
import com.example.mapper.ManagerMapper;
import com.example.repository.ManagerRepository;
import com.example.secutiry.service.TokenService;
import com.example.service.ManagerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {

    private TokenService tokenService;
    private ManagerRepository managerRepository;
    private ManagerMapper managerMapper;

    public ManagerServiceImpl(TokenService tokenService, ManagerRepository managerRepository, ManagerMapper managerMapper) {
        this.tokenService = tokenService;
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
    }

    @Override
    public Page<ManagerDto> findAll(Pageable pageable) {
        return managerRepository.findAll(pageable)
                .map(managerMapper::managerToManagerDto);
    }

    @Override
    public ManagerDto add(ManagerCreateDto managerCreateDto) {
        Manager manager = managerMapper.managerCreateDtoToManager(managerCreateDto);
        managerRepository.save(manager);
        return managerMapper.managerToManagerDto(manager);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        //Try to find active user for specified credentials
        Manager user = null;
        try {
            user = managerRepository
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
        claims.put("class", "Manager");
        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public ManagerDto findByUsername(String username) {
        Optional<Manager> manager = managerRepository.findByUser_Username(username);
        ManagerDto md = manager.map(managerMapper::managerToManagerDto)
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
        return md;
    }
}
