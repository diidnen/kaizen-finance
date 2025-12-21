package com.loginapp.loginbackend.repository;

import com.loginapp.loginbackend.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByIsActiveTrue();
    List<Contract> findByCategory(String category);
    List<Contract> findByUploadedBy(String uploadedBy);
    List<Contract> findByUsernameAndIsActiveTrue(String username);
    List<Contract> findByUsername(String username);
} 