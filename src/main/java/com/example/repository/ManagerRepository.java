package com.example.repository;


import com.example.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByUser_EmailAndUser_Password(String email, String password);

    Optional<Manager> findByUser_EmailAndUser_Username(String email, String password);

    Optional<Manager> findByUser_Username(String username);
}
