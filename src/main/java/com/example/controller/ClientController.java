package com.example.controller;


import com.example.dto.*;
import com.example.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @Operation(summary = "Get all users")
    @GetMapping
    //@CheckSecurity(classTypes = {"Client"})
    public ResponseEntity<Page<ClientDto>> getAllUsers(//@RequestHeader("Authorization") String authorization,
                                                       Pageable pageable) {

        return new ResponseEntity<>(clientService.findAll(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get client by token")
    @GetMapping("/clientInfo")
    public ResponseEntity<ClientDto> getClient(String token){
        return new ResponseEntity<>(clientService.findClient(token), HttpStatus.OK);
    }

    @Operation(summary = "Register user")
    @PostMapping
    public ResponseEntity<ClientDto> saveUser(@RequestBody @Valid ClientCreateDto clientCreateDto) {
        return new ResponseEntity<>(clientService.add(clientCreateDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update client")
    @PutMapping("/update")
    public ResponseEntity<ClientDto> updateClient(@RequestBody UpdateClientDto updateClientDto) {
        return new ResponseEntity<>(clientService.update(updateClientDto), HttpStatus.OK);
    }

    @Operation(summary = "Update clients password")
    @PutMapping("/updatepassword")
    public ResponseEntity<ClientDto> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
        return new ResponseEntity<>(clientService.updatePassword(updatePasswordDto), HttpStatus.OK);
    }
    //fali login
}
