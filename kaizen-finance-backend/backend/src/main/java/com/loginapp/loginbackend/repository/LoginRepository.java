package com.loginapp.loginbackend.repository;

import com.loginapp.loginbackend.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPassword(String password);
    Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token);
    
    // JpaRepository 已经提供了以下方法，无需实现：
    // save()
    // findById()
    // findAll()
    // delete()
    // 等等...
} 