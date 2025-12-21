package com.loginapp.loginbackend.repository;

import com.loginapp.loginbackend.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
    List<Testimonial> findByIsActiveTrueOrderByDisplayOrderAsc();
    List<Testimonial> findAllByOrderByDisplayOrderAsc();
}