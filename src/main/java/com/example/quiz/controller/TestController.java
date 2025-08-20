package com.example.quiz.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/secure")
    public ResponseEntity<String> secureEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("Hello, " + authentication.getName() + "! You have accessed a secured endpoint.");
        } else {
            return ResponseEntity.status(401).body("Unauthorized access");
        }
    }

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("This is a public endpoint - no authentication required!");
    }

    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> userInfo = new HashMap<>();
        
        if (authentication != null && authentication.isAuthenticated()) {
            userInfo.put("username", authentication.getName());
            userInfo.put("authorities", authentication.getAuthorities());
            userInfo.put("authenticated", true);
            return ResponseEntity.ok(userInfo);
        } else {
            userInfo.put("authenticated", false);
            userInfo.put("message", "No authentication found");
            return ResponseEntity.status(401).body(userInfo);
        }
    }

    @GetMapping("/test-jwt")
    public ResponseEntity<Map<String, Object>> testJwt(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            response.put("token_received", true);
            response.put("token_length", token.length());
            response.put("token_preview", token.substring(0, Math.min(20, token.length())) + "...");
        } else {
            response.put("token_received", false);
            response.put("message", "No Bearer token found in Authorization header");
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test-upload")
    public ResponseEntity<Map<String, Object>> testFileUpload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        if (file != null && !file.isEmpty()) {
            response.put("file_received", true);
            response.put("file_name", file.getOriginalFilename());
            response.put("file_size", file.getSize());
            response.put("content_type", file.getContentType());
            response.put("message", "File upload test successful");
        } else {
            response.put("file_received", false);
            response.put("message", "No file received");
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-upload-dir")
    public ResponseEntity<Map<String, Object>> testUploadDirectory() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String userHome = System.getProperty("user.home");
            String uploadDir = userHome + File.separator + "quiz_uploads";
            Path uploadPath = Paths.get(uploadDir);
            
            response.put("user_home", userHome);
            response.put("upload_directory", uploadDir);
            response.put("upload_path_exists", Files.exists(uploadPath));
            response.put("upload_path_absolute", uploadPath.toAbsolutePath().toString());
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                response.put("directory_created", true);
                response.put("message", "Upload directory created successfully");
            } else {
                response.put("directory_created", false);
                response.put("message", "Upload directory already exists");
            }
            
            // Test write permissions
            Path testFile = uploadPath.resolve("test.txt");
            Files.write(testFile, "test".getBytes());
            Files.delete(testFile);
            response.put("write_permission", true);
            
        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("write_permission", false);
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-jackson-datetime")
    public ResponseEntity<Map<String, Object>> testJacksonDateTime() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Test LocalDateTime serialization
            LocalDateTime now = LocalDateTime.now();
            response.put("current_datetime", now);
            response.put("datetime_iso", now.toString());
            response.put("message", "Jackson date/time handling test successful");
            
        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("message", "Jackson date/time handling test failed");
        }
        
        return ResponseEntity.ok(response);
    }
}
