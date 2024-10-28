package com.example.BankingApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.BankingApplication.entity.user;

import java.util.Optional;

public interface UserRepository extends JpaRepository<user,Long> {

    public Boolean existsByEmail(String email);

 public   boolean existsByAccountNumber(String accountNumber);

   public user findByAccountNumber(String accountNumber);

 public Optional<user> findByEmail(String email);
}
