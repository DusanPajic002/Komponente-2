package com.example.runner;

import com.example.domain.Admin;
import com.example.domain.User;
import com.example.repository.AdminRepository;
import com.example.repository.ClientRepository;
import com.example.repository.ManagerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private AdminRepository adminRepository;
    private ClientRepository clientRepository;
    private ManagerRepository managerRepository;

    public TestDataRunner(AdminRepository adminRepository, ClientRepository clientRepository, ManagerRepository managerRepository) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Insert roles
        //Insert admin
        Admin admin = new Admin(new User("admin","admin1234","admin@raf.rs","","","",true));
        adminRepository.save(admin);
    }
}

