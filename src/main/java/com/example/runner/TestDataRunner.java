package com.example.runner;

import com.example.domain.Admin;
import com.example.domain.User;
import com.example.repository.AdminRepository;
import com.example.repository.ClientRepository;
import com.example.repository.ManagerRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

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
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("JWT Secret: " + base64Key);
        Admin admin = new Admin(new User("admin","admin1234","admin@raf.rs","","","",true));
        adminRepository.save(admin);
    }
}

