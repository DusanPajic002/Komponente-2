package com.example.controller;


import com.example.dto.*;
import com.example.service.AdminService;
import com.example.service.ClientService;
import com.example.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private AdminService adminService;
    private ClientService clientService;
    private ManagerService managerService;

    public UserController(AdminService adminService, ClientService clientService, ManagerService managerService) {
        this.adminService = adminService;
        this.clientService = clientService;
        this.managerService = managerService;
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {

        TokenResponseDto tokenResponseDto = clientService.login(tokenRequestDto);
        
        if(tokenResponseDto != null)
            return  new ResponseEntity<>(tokenResponseDto, HttpStatus.OK);

        tokenResponseDto = managerService.login(tokenRequestDto);
        if(tokenResponseDto != null)
            return  new ResponseEntity<>(tokenResponseDto, HttpStatus.OK);

        tokenResponseDto = adminService.login(tokenRequestDto);
        return  new ResponseEntity<>(tokenResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "Get by username")
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getByUsername(@PathVariable("username") String username) {
        ClientDto clientDto = clientService.findByUsername(username);
        if(clientDto != null)
            return new ResponseEntity<>(HttpStatus.OK);

        ManagerDto managerDto = managerService.findByUsername(username);
        if(managerDto != null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
