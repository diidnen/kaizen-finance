package com.loginapp.loginbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.loginapp.loginbackend.servicedata;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<servicedata, Long> {
    // 基础的CRUD操作由JpaRepository提供
    @Query("SELECT new servicedata(s.id, s.title, s.description, s.icon, " +
           "(SELECT COUNT(us) FROM servicedata us WHERE us.username = s.username), " +
           "s.lastUpdate, s.path, s.username) " +
           "FROM servicedata s WHERE s.username = :username")
    List<servicedata> findByUsername(@Param("username") String username);
}
