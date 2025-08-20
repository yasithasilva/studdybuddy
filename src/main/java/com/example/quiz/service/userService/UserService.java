package com.example.quiz.service.userService;

import com.example.quiz.dto.repoMaps.LoginRequestListModel;
import com.example.quiz.dto.request.JUserCreateRequestModel;
import com.example.quiz.dto.request.JUserLoginModel;
import com.example.quiz.dto.response.JUserLoginResponseModel;
import com.example.quiz.dto.response.JUserResponseModel;
import com.example.quiz.entity.User;
import com.example.quiz.repository.UserRepository;
import com.example.quiz.utils.ErrorCode;
import com.example.quiz.utils.JwtUtil;
import com.example.quiz.utils.ServiceResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtTokenProvider;

    /**
     * Saves a new User based on the request model.
     * Performs validation and maps the request to DB entities.
     **/
    @Override
    public ServiceResponse registerUser(JUserCreateRequestModel userCreateRequestModel) {
        try {
            System.out.println("=== User Registration Request ===");
            System.out.println("Request Model: " + userCreateRequestModel);

            // Validate required fields
            if (userCreateRequestModel.getUsername() == null || userCreateRequestModel.getUsername().trim().isEmpty()) {
                System.out.println("Validation failed: Username is null or empty");
                return new ServiceResponse<>("error", String.valueOf(ErrorCode.USER_ALREADY_EXISTS.getCode()), "Username is required", null);
            }

            if (userCreateRequestModel.getPassword() == null || userCreateRequestModel.getPassword().trim().isEmpty()) {
                System.out.println("Validation failed: Password is null or empty");
                return new ServiceResponse<>("error", String.valueOf(ErrorCode.USER_ALREADY_EXISTS.getCode()), "Password is required", null);
            }

            if (userCreateRequestModel.getEmail() == null || userCreateRequestModel.getEmail().trim().isEmpty()) {
                System.out.println("Validation failed: Email is null or empty");
                return new ServiceResponse<>("error", String.valueOf(ErrorCode.USER_ALREADY_EXISTS.getCode()), "Email is required", null);
            }

            if (userCreateRequestModel.getFirstName() == null || userCreateRequestModel.getFirstName().trim().isEmpty()) {
                System.out.println("Validation failed: First name is null or empty");
                return new ServiceResponse<>("error", String.valueOf(ErrorCode.USER_ALREADY_EXISTS.getCode()), "First name is required", null);
            }

            if (userCreateRequestModel.getLastName() == null || userCreateRequestModel.getLastName().trim().isEmpty()) {
                System.out.println("Validation failed: Last name is null or empty");
                return new ServiceResponse<>("error", String.valueOf(ErrorCode.USER_ALREADY_EXISTS.getCode()), "Last name is required", null);
            }

            System.out.println("All validations passed");
            System.out.println("Username: " + userCreateRequestModel.getUsername());
            System.out.println("Password length: " + userCreateRequestModel.getPassword().length());
            System.out.println("Email: " + userCreateRequestModel.getEmail());

            Optional<User> userOptional = userRepository.findByUsername(userCreateRequestModel.getUsername());
            System.out.println("Existing user check: " + userOptional);

            if (userOptional.isEmpty()) {
                System.out.println("Creating new user...");

                User user = new User();
                user.setUsername(userCreateRequestModel.getUsername());
                user.setPassword(passwordEncoder.encode(userCreateRequestModel.getPassword()));
                user.setEmail(userCreateRequestModel.getEmail());
                user.setFirstName(userCreateRequestModel.getFirstName());
                user.setLastName(userCreateRequestModel.getLastName());
                user.setCreatedAt(new Date());
                user.setUpdatedAt(new Date());

                System.out.println("User entity created: " + user);
                User savedUser = userRepository.save(user);
                System.out.println("User saved to database: " + savedUser);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                JUserResponseModel userResponseModel = objectMapper.convertValue(savedUser, JUserResponseModel.class);
                System.out.println("Response model created: " + userResponseModel);

                return new ServiceResponse<>("true", null, "", userResponseModel);
            }

            System.out.println("User already exists");
            return new ServiceResponse<>("error", String.valueOf(ErrorCode.USER_ALREADY_EXISTS.getCode()), String.valueOf(ErrorCode.USER_ALREADY_EXISTS.getMessage()), null);

        } catch (Exception e) {
            System.err.println("Error in registerUser: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServiceResponse loginUser(JUserLoginModel loginRequestModel) {
        var user = userRepository.findByUsernameOrEmail(loginRequestModel.getUsernameOrEmail());
        System.out.println(user);
        if (user.isEmpty()) {
            return new ServiceResponse<>("error", String.valueOf(ErrorCode.USER_ALREADY_EXISTS.getCode()), "User not found", null);
        }
        
        System.out.println("user firstname"+user.get().getFirstName());
        System.out.println("get User Password "+user.get().getPassword());
        System.out.println("loginRequest Password "+loginRequestModel.getPassword());

        boolean matches = passwordEncoder.matches(loginRequestModel.getPassword(), user.get().getPassword());
        if (!matches) {
            return new ServiceResponse<>("error", String.valueOf(ErrorCode.INVALID_CREDENTIALS.getCode()), ErrorCode.INVALID_CREDENTIALS.getMessage(), null);
        }
        
        String token = jwtTokenProvider.generateToken(user.get().getUsername());
        JUserLoginResponseModel response = new JUserLoginResponseModel(
                user.get().getUsername(),
                user.get().getFirstName(),
                user.get().getLastName(),
                user.get().getEmail(),
                token
        );

        return new ServiceResponse<>("success", String.valueOf(ErrorCode.LOGIN_SUCCESSFUL.getCode()), ErrorCode.LOGIN_SUCCESSFUL.getMessage(), response);
    }
}