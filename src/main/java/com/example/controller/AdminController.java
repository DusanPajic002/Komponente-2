package com.example.controller;

import com.example.domain.Admin;
import com.example.dto.ClientDto;
import com.example.dto.ManagerDto;
import com.example.dto.UpdatePermissionDto;
import com.example.repository.AdminRepository;
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

    private AdminRepository adminRepository;


    public AdminController(AdminService adminService, AdminRepository adminRepository) {
        this.adminService = adminService;
        this.adminRepository = adminRepository;
    }

    class  Test {
        public String str;
    }
//    @Operation(summary = "Get all clients")
//    @GetMapping("/test")
//    //@CheckSecurity(classTypes = {"Client"})
//    public ResponseEntity<List<Admin>> greet(Test tes) {
//        return new ResponseEntity<Admin>(adminRepository.findAll(),HttpStatus.OK);
//                //new ResponseEntity<>(adminRepository.findAll(), HttpStatus.OK);
//    }

    @Operation(summary = "Get all clients")
    @GetMapping("/clients")
    //@CheckSecurity(classTypes = {"Admin"})
    public ResponseEntity<Page<ClientDto>> getAllClients(//@RequestHeader("Authorization") String authorization,
                                                         Pageable pageable) {
        return new ResponseEntity<>(adminService.findAllClients(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get all clients")
    @GetMapping("/managers")
    //@CheckSecurity(classTypes = {"Admin"})
    public ResponseEntity<Page<ManagerDto>> getAllManagers(//@RequestHeader("Authorization") String authorization,
                                                           Pageable pageable) {

        return new ResponseEntity<>(adminService.findAllManageres(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Change client permission")
    @PutMapping("/clientpermission")
    public ResponseEntity<ClientDto> updateClientPermission(@RequestBody UpdatePermissionDto updatePermissionDto) {
        return new ResponseEntity<>(adminService.updatePermissionClient(updatePermissionDto), HttpStatus.OK);
    }

    @Operation(summary = "Change manager permission")
    @PutMapping("/managerpermission")
    public ResponseEntity<ManagerDto> updateManagerPermission(@RequestBody UpdatePermissionDto updatePermissionDto) {
        return new ResponseEntity<>(adminService.updatePermissionManager(updatePermissionDto), HttpStatus.OK);
    }
}
