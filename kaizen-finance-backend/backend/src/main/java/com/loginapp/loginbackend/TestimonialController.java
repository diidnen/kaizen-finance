package com.loginapp.loginbackend;

import com.loginapp.loginbackend.repository.LoginRepository;
import com.loginapp.loginbackend.repository.TestimonialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/manager")
public class TestimonialController {

    @Autowired
    private TestimonialRepository testimonialRepository;

    @Autowired
    private LoginRepository loginRepository;

    /**
     * Helper method to centralize manager permission checks.
     * It verifies the token and ensures the user is the designated manager.
     *
     * @param authorizationHeader The "Authorization" header from the request.
     * @return A ResponseEntity with an error if auth fails, otherwise null.
     */
    private ResponseEntity<?> checkManagerPermission(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        Optional<User> userOptional = loginRepository.findByToken(token);

        if (!userOptional.isPresent()) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid token. Please log in again.");
        }

        User currentUser = userOptional.get();
        // Check if the user is the designated manager (e.g., 'chuhan')
        if (!"chuhan".equals(currentUser.getUsername())) {
            return buildErrorResponse(HttpStatus.FORBIDDEN, "Access denied. You do not have permission to perform this action.");
        }

        // Return null to indicate permission is granted
        return null;
    }

    /**
     * Helper method to build a standardized error response map.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, status);
    }

    // --- Testimonial Management Endpoints ---

    /**
     * Get all testimonials for the manager dashboard.
     */
    @GetMapping("/testimonials")
    public ResponseEntity<?> getAllTestimonialsForManager(@RequestHeader("Authorization") String authorization) {
        ResponseEntity<?> permissionError = checkManagerPermission(authorization);
        if (permissionError != null) {
            return permissionError;
        }

        try {
            List<Testimonial> testimonials = testimonialRepository.findAllByOrderByDisplayOrderAsc();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("testimonials", testimonials);
            response.put("message", "Testimonials retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve testimonials: " + e.getMessage());
        }
    }

    /**
     * Create a new testimonial.
     */
    @PostMapping("/testimonials")
    public ResponseEntity<?> createTestimonial(@RequestBody Testimonial newTestimonial, @RequestHeader("Authorization") String authorization) {
        ResponseEntity<?> permissionError = checkManagerPermission(authorization);
        if (permissionError != null) {
            return permissionError;
        }

        try {
            // Validate required fields
            if (newTestimonial.getCustomerName() == null || newTestimonial.getCustomerName().trim().isEmpty()) {
                return buildErrorResponse(HttpStatus.BAD_REQUEST, "Customer name is required.");
            }
            if (newTestimonial.getTestimonial() == null || newTestimonial.getTestimonial().trim().isEmpty()) {
                return buildErrorResponse(HttpStatus.BAD_REQUEST, "Testimonial content is required.");
            }

            // Set creation timestamp and save
            newTestimonial.setCreatedAt(LocalDateTime.now());
            Testimonial savedTestimonial = testimonialRepository.save(newTestimonial);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Testimonial created successfully");
            response.put("testimonial", savedTestimonial);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create testimonial: " + e.getMessage());
        }
    }

    /**
     * Update an existing testimonial by its ID.
     */
    @PutMapping("/testimonials/{id}")
    public ResponseEntity<?> updateTestimonial(@PathVariable Long id, @RequestBody Testimonial updatedTestimonial, @RequestHeader("Authorization") String authorization) {
        ResponseEntity<?> permissionError = checkManagerPermission(authorization);
        if (permissionError != null) {
            return permissionError;
        }

        try {
            Optional<Testimonial> existingTestimonialOptional = testimonialRepository.findById(id);
            if (!existingTestimonialOptional.isPresent()) {
                return buildErrorResponse(HttpStatus.NOT_FOUND, "Testimonial with ID " + id + " not found.");
            }

            Testimonial existingTestimonial = existingTestimonialOptional.get();

            // Update fields from the request
            existingTestimonial.setCustomerName(updatedTestimonial.getCustomerName());
            existingTestimonial.setCompanyName(updatedTestimonial.getCompanyName());
            existingTestimonial.setTestimonial(updatedTestimonial.getTestimonial());
            existingTestimonial.setDisplayOrder(updatedTestimonial.getDisplayOrder());
            existingTestimonial.setIsActive(updatedTestimonial.getIsActive());

            Testimonial savedTestimonial = testimonialRepository.save(existingTestimonial);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Testimonial updated successfully");
            response.put("testimonial", savedTestimonial);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update testimonial: " + e.getMessage());
        }
    }

    /**
     * Delete a testimonial by its ID.
     */
    @DeleteMapping("/testimonials/{id}")
    public ResponseEntity<?> deleteTestimonial(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        ResponseEntity<?> permissionError = checkManagerPermission(authorization);
        if (permissionError != null) {
            return permissionError;
        }

        try {
            if (!testimonialRepository.existsById(id)) {
                return buildErrorResponse(HttpStatus.NOT_FOUND, "Testimonial with ID " + id + " not found.");
            }

            testimonialRepository.deleteById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Testimonial deleted successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete testimonial: " + e.getMessage());
        }
    }
}
