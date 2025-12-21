package com.loginapp.loginbackend;

import com.loginapp.loginbackend.repository.TestimonialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Import entity
import com.loginapp.loginbackend.Testimonial;

@RestController
public class PublicTestimonialController {

    @Autowired
    private TestimonialRepository testimonialRepository;

    // Public, no auth required
    @GetMapping("/api/testimonials")
    public Map<String, Object> getPublicTestimonials() {
        List<Testimonial> testimonials = testimonialRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("testimonials", testimonials);
        response.put("message", "Testimonials retrieved successfully");
        return response;
    }
}


