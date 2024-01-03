package com.example.service;

import com.example.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManagerService {
    Page<ManagerDto> findAll(Pageable pageable);

    ManagerDto add(ManagerCreateDto managerCreateDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    ManagerDto findByUsername(String username);

    Integer updateHallName(HallDto hallDto);
}
