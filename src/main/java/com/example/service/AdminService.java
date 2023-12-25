package com.example.service;

import com.example.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    Page<ClientDto> findAllClients(Pageable pageable);

    Page<ManagerDto> findAllManageres(Pageable pageable);

    ClientDto updatePermissionClient(UpdatePermissionDto updatePermissionDto);

    ManagerDto updatePermissionManager(UpdatePermissionDto updatePermissionDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);
    //login
}
