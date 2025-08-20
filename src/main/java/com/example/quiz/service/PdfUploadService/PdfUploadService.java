package com.example.quiz.service.PdfUploadService;

import com.example.quiz.dto.request.JPdfUploadRequestModel;
import com.example.quiz.dto.response.JPdfUploadResponseModel;
import com.example.quiz.dto.response.JUserResponseModel;
import com.example.quiz.entity.PdfUpload;
import com.example.quiz.entity.User;
import com.example.quiz.repository.PdfUploadRepository;
import com.example.quiz.repository.UserRepository;
import com.example.quiz.utils.ErrorCode;
import com.example.quiz.utils.ServiceResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PdfUploadService implements IPdfUploadService {

    private final PdfUploadRepository pdfUploadRepository;
    private final UserRepository userRepository;
    private final String uploadDir;

    public PdfUploadService(PdfUploadRepository pdfUploadRepository, UserRepository userRepository,
                           @Value("${app.upload.dir:${user.home}/quiz_uploads}") String uploadDir) {
        this.pdfUploadRepository = pdfUploadRepository;
        this.userRepository = userRepository;
        this.uploadDir = uploadDir;
    }

    @Override
    public ServiceResponse uploadPdf(JPdfUploadRequestModel uploadRequestModel) {
        try {
            System.out.println("Starting PDF upload process...");
            System.out.println("Upload directory: " + uploadDir);
            
            Optional<User> userOptional = userRepository.findById(uploadRequestModel.getUserId());

            if (userOptional.isEmpty()) {
                System.out.println("User not found with ID: " + uploadRequestModel.getUserId());
                return new ServiceResponse<>("error", String.valueOf(ErrorCode.USER_NOT_FOUND.getCode()), "User not found", null);
            }

            // Validate file
            if (uploadRequestModel.getFile() == null || uploadRequestModel.getFile().isEmpty()) {
                System.out.println("File validation failed: file is null or empty");
                return new ServiceResponse<>("error", String.valueOf(ErrorCode.INVALID_INPUT.getCode()), "File is required", null);
            }

            // Validate file type
            String contentType = uploadRequestModel.getFile().getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                System.out.println("File type validation failed: content type is " + contentType);
                return new ServiceResponse<>("error", String.valueOf(ErrorCode.INVALID_INPUT.getCode()), "Only PDF files are allowed", null);
            }

            System.out.println("File validation passed. Original filename: " + uploadRequestModel.getFile().getOriginalFilename());
            System.out.println("File size: " + uploadRequestModel.getFile().getSize() + " bytes");

            // Create uploads directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                System.out.println("Creating upload directory: " + uploadPath.toAbsolutePath());
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename to avoid conflicts
            String originalFilename = uploadRequestModel.getFile().getOriginalFilename();
            String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
            Path filePath = uploadPath.resolve(uniqueFilename);

            System.out.println("Saving file to: " + filePath.toAbsolutePath());

            // Save the file
            uploadRequestModel.getFile().transferTo(filePath.toFile());

            System.out.println("File saved successfully");

            PdfUpload pdf = new PdfUpload();
            pdf.setPdfName(uniqueFilename);
            pdf.setUser(userOptional.get());
            pdf.setCreatedAt(LocalDateTime.now());

            System.out.println("PDF entity created: " + pdf);
            System.out.println("User: " + userOptional.get().getUsername());

            PdfUpload upload;
            try {
                upload = pdfUploadRepository.save(pdf);
                System.out.println("PDF record saved to database with ID: " + upload.getId());
            } catch (Exception dbException) {
                System.err.println("Database error: " + dbException.getMessage());
                dbException.printStackTrace();
                
                // Check if it's a schema issue (ID type mismatch)
                if (dbException.getMessage().contains("id") || dbException.getMessage().contains("ID")) {
                    System.err.println("Possible schema issue with ID field type. Check database table structure.");
                }
                
                // Try to clean up the uploaded file if database save fails
                try {
                    Files.deleteIfExists(filePath);
                    System.out.println("Uploaded file cleaned up due to database error");
                } catch (Exception cleanupException) {
                    System.err.println("Failed to cleanup file: " + cleanupException.getMessage());
                }
                throw new RuntimeException("Failed to save PDF record to database: " + dbException.getMessage());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            
            // Register JavaTimeModule to handle Java 8 date/time types
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            
            JPdfUploadResponseModel responseModel = objectMapper.convertValue(upload, JPdfUploadResponseModel.class);
            
            // Set the userId from the User object since it's not automatically mapped
            responseModel.setUserIdFromUser(upload.getUser());
            
            System.out.println("Response model created: " + responseModel);
            System.out.println("File saved to: " + filePath.toAbsolutePath());

            return new ServiceResponse<>("success", String.valueOf(ErrorCode.SUCCESS.getCode()), "PDF uploaded successfully", responseModel);

        } catch (Exception e) {
            System.err.println("Error during PDF upload: " + e.getMessage());
            e.printStackTrace();
            return new ServiceResponse<>("error", String.valueOf(ErrorCode.INTERNAL_SERVER_ERROR.getCode()), "Failed to upload PDF: " + e.getMessage(), null);
        }
    }
}
