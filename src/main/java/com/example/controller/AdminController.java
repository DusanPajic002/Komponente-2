package com.example.controller;

import com.example.domain.Admin;
import com.example.dto.ClientDto;
import com.example.dto.ManagerDto;
import com.example.dto.UpdatePermissionDto;
import com.example.repository.AdminRepository;
import com.example.secutiry.CheckSecurity;
import com.example.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;


    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Get all clients")
    @GetMapping("/clients")
    @CheckSecurity(role = {"Admin"})
    public ResponseEntity<Page<ClientDto>> getAllClients(@RequestHeader("Authorization") String authorization, Pageable pageable) {
        return new ResponseEntity<>(adminService.findAllClients(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get all managers")
    @GetMapping("/managers")
    @CheckSecurity(role = {"Admin"})
    public ResponseEntity<Page<ManagerDto>> getAllManagers(@RequestHeader("Authorization") String authorization, Pageable pageable) {
        return new ResponseEntity<>(adminService.findAllManageres(pageable), HttpStatus.OK);
    }


    @Operation(summary = "Change client permission")
    @PutMapping("/clientpermission")
    @CheckSecurity(role = {"Admin"})
    public ResponseEntity<ClientDto> updateClientPermission(@RequestHeader("Authorization") String authorization, @RequestBody UpdatePermissionDto updatePermissionDto) {
        return new ResponseEntity<>(adminService.updatePermissionClient(updatePermissionDto), HttpStatus.OK);
    }

    @Operation(summary = "Change manager permission")
    @PutMapping("/managerpermission")
    @CheckSecurity(role = {"Admin"})
    public ResponseEntity<ManagerDto> updateManagerPermission(@RequestHeader("Authorization") String authorization, @RequestBody UpdatePermissionDto updatePermissionDto) {
        return new ResponseEntity<>(adminService.updatePermissionManager(updatePermissionDto), HttpStatus.OK);
    }
}
