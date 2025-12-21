package com.loginapp.loginbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.loginapp.loginbackend.UserDocument;
import java.util.List;

public interface UserDocumentRepository extends JpaRepository<UserDocument, Long> {
    List<UserDocument> findByUsername(String username);
} 