package com.loginapp.loginbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.loginapp.loginbackend.repository.ContractRepository;
import com.loginapp.loginbackend.repository.LoginRepository;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {
    
    @Autowired
    private ContractRepository contractRepository;
    
    @Autowired
    private LoginRepository loginRepository;
    
    private final Path uploadPath = Paths.get("contracts");
    
    public ContractController() {
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }
    
    /**
     * 获取用户自己的合同（需要认证）
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserContracts(@RequestHeader("Authorization") String authorization) {
        try {
            // 验证token
            String token = authorization.replace("Bearer ", "");
            Optional<User> user = loginRepository.findByToken(token);
            
            if (!user.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            String username = user.get().getUsername();
            List<Contract> contracts = contractRepository.findByUsernameAndIsActiveTrue(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("contracts", contracts);
            response.put("message", "User contracts retrieved successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to retrieve user contracts: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * 获取所有合同（管理员可访问）
     */
    @GetMapping("/admin")
    public ResponseEntity<?> getAllContracts(@RequestHeader("Authorization") String authorization) {
        try {
            // 验证token和权限
            String token = authorization.replace("Bearer ", "");
            Optional<User> user = loginRepository.findByToken(token);
            
            if (!user.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User currentUser = user.get();
            if (!"chuhan".equals(currentUser.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 403);
                errorResponse.put("message", "Access denied. Only managers can access this resource.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            List<Contract> contracts = contractRepository.findAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("contracts", contracts);
            response.put("message", "Contracts retrieved successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to retrieve contracts: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * 上传合同（管理员功能）
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadContract(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("username") String targetUsername,
            @RequestHeader("Authorization") String authorization) {
        try {
            // 验证token和权限
            String token = authorization.replace("Bearer ", "");
            Optional<User> user = loginRepository.findByToken(token);
            
            if (!user.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User currentUser = user.get();
            if (!"chuhan".equals(currentUser.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 403);
                errorResponse.put("message", "Access denied. Only managers can access this resource.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // 验证文件
            if (file.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 400);
                errorResponse.put("message", "Please select a file to upload");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // 验证目标用户是否存在
            Optional<User> targetUser = loginRepository.findByUsername(targetUsername);
            if (!targetUser.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 400);
                errorResponse.put("message", "Target user not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // 生成唯一文件名
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + fileExtension;
            
            // 保存文件
            Path targetPath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            // 创建合同记录
            Contract contract = new Contract();
            contract.setTitle(title);
            contract.setDescription(description);
            contract.setCategory(category);
            contract.setUsername(targetUsername);
            contract.setFileName(fileName);
            contract.setOriginalFileName(originalFileName);
            contract.setFilePath(targetPath.toString());
            contract.setFileType(file.getContentType());
            contract.setFileSize(file.getSize());
            contract.setUploadedBy(currentUser.getUsername());
            contract.setUploadTime(LocalDateTime.now());
            contract.setIsActive(true);
            
            Contract savedContract = contractRepository.save(contract);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Contract uploaded successfully");
            response.put("contract", savedContract);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to upload contract: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * 下载合同
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadContract(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        try {
            // 验证token
            String token = authorization.replace("Bearer ", "");
            Optional<User> user = loginRepository.findByToken(token);
            
            if (!user.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            Optional<Contract> contractOptional = contractRepository.findById(id);
            
            if (!contractOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Contract contract = contractOptional.get();
            
            // 检查合同是否活跃
            if (!contract.getIsActive()) {
                return ResponseEntity.notFound().build();
            }
            
            // 检查用户是否有权限下载此合同
            String currentUsername = user.get().getUsername();
            if (!currentUsername.equals(contract.getUsername()) && !"chuhan".equals(currentUsername)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            Path filePath = uploadPath.resolve(contract.getFileName());
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + contract.getOriginalFileName() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除合同（管理员功能）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContract(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        try {
            // 验证token和权限
            String token = authorization.replace("Bearer ", "");
            Optional<User> user = loginRepository.findByToken(token);
            
            if (!user.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User currentUser = user.get();
            if (!"chuhan".equals(currentUser.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 403);
                errorResponse.put("message", "Access denied. Only managers can access this resource.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // 检查合同是否存在
            Optional<Contract> contractOptional = contractRepository.findById(id);
            if (!contractOptional.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 404);
                errorResponse.put("message", "Contract not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            Contract contract = contractOptional.get();
            
            // 删除文件
            try {
                Path filePath = uploadPath.resolve(contract.getFileName());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // 文件删除失败，但继续删除数据库记录
                e.printStackTrace();
            }
            
            // 删除数据库记录
            contractRepository.deleteById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Contract deleted successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to delete contract: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * 更新合同状态（管理员功能）
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateContractStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, Boolean> request,
            @RequestHeader("Authorization") String authorization) {
        try {
            // 验证token和权限
            String token = authorization.replace("Bearer ", "");
            Optional<User> user = loginRepository.findByToken(token);
            
            if (!user.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User currentUser = user.get();
            if (!"chuhan".equals(currentUser.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 403);
                errorResponse.put("message", "Access denied. Only managers can access this resource.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // 检查合同是否存在
            Optional<Contract> contractOptional = contractRepository.findById(id);
            if (!contractOptional.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 404);
                errorResponse.put("message", "Contract not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            Contract contract = contractOptional.get();
            Boolean isActive = request.get("isActive");
            
            if (isActive != null) {
                contract.setIsActive(isActive);
                Contract savedContract = contractRepository.save(contract);
                
                Map<String, Object> response = new HashMap<>();
                response.put("code", 200);
                response.put("message", "Contract status updated successfully");
                response.put("contract", savedContract);
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 400);
                errorResponse.put("message", "isActive field is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to update contract status: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
} 