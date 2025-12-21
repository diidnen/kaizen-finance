package com.loginapp.loginbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.loginapp.loginbackend.OrderData;

@Repository
public interface OrderRepository extends JpaRepository<OrderData, Long> {
    @Query("SELECT o FROM OrderData o WHERE o.username = :username")
    List<OrderData> findByUsername(@Param("username") String username);
} 