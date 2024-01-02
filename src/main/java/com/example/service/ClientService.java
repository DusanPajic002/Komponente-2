package com.example.service;

import com.example.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    Page<ClientDto> findAll(Pageable pageable);

    ClientDto add(ClientCreateDto clientCreateDto);
    ClientDto findClient(String token);

    ClientDto update(UpdateClientDto updateClientDto);

    ClientDto updatePassword(UpdatePasswordDto updatePasswordDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    ClientDto findByUsername(String username);

    int updateNumberOfTrainings(Long id, int i);

}
