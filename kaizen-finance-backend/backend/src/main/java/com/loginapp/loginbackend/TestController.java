package com.loginapp.loginbackend;

import com.loginapp.loginbackend.repository.TestimonialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Hello from Test Controller!");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "pong");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    // Public testimonials endpoint (no auth required)
    @Autowired
    private TestimonialRepository testimonialRepository;

    @GetMapping("/../testimonials")
    public Map<String, Object> publicTestimonials() {
        List<Testimonial> testimonials = testimonialRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("testimonials", testimonials);
        response.put("message", "Testimonials retrieved successfully");
        return response;
    }
}