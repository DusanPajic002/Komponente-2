package com.example.controller;

import com.example.dto.*;
import com.example.secutiry.CheckSecurity;
import com.example.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    private ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Operation(summary = "Get all users")
    @GetMapping
    @CheckSecurity(role = {"Manager"})
    public ResponseEntity<Page<ManagerDto>> getAllUsers(@RequestHeader("Authorization") String authorization, Pageable pageable) {

        return new ResponseEntity<>(managerService.findAll(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Register user")
    @PostMapping
    @CheckSecurity(role = {"Manager"})
    public ResponseEntity<ManagerDto> saveUser(@RequestHeader("Authorization") String authorization, @RequestBody @Valid ManagerCreateDto managerCreateDto) {
        return new ResponseEntity<>(managerService.add(managerCreateDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Register user")
    @PutMapping("/updateHallName")
    @CheckSecurity(role = {"Manager"})
    public ResponseEntity<Integer> updateHallName(@RequestHeader("Authorization") String authorization, @RequestBody HallDto hallDto) {
        return new ResponseEntity<>(managerService.updateHallName(hallDto), HttpStatus.CREATED);
    }

    //fali login
}
