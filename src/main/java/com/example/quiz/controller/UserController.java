package com.example.quiz.controller;

import com.example.quiz.dto.request.JUserCreateRequestModel;
import com.example.quiz.dto.request.JUserLoginModel;
import com.example.quiz.service.userService.IUserService;
import com.example.quiz.utils.ErrorCode;
import com.example.quiz.utils.JsonUtil;
import com.example.quiz.utils.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/v1")
public class UserController {
    @Autowired
    IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody JUserCreateRequestModel userCreateRequestModel) {
        try {
            ServiceResponse response = userService.registerUser(userCreateRequestModel);
            return JsonUtil.generateResponseEntity(Boolean.parseBoolean(response.getStatus()), response.getErrorCode(), response.getMessage(), response.getData());

        } catch (Exception e) {
            return JsonUtil.generateResponseEntity(false, String.valueOf(ErrorCode.INTERNAL_SERVER_ERROR), e.getMessage(), null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody JUserLoginModel loginRequestModel) {
      try {
          ServiceResponse response = userService.loginUser(loginRequestModel);
          return JsonUtil.generateResponseEntity(Boolean.parseBoolean(response.getStatus()), response.getErrorCode(), response.getMessage(), response.getData());

      } catch (Exception e) {
          return JsonUtil.generateResponseEntity(false, String.valueOf(ErrorCode.INTERNAL_SERVER_ERROR), e.getMessage(), null);
      }

    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        try {
            // Clear the security context
            SecurityContextHolder.clearContext();
            return JsonUtil.generateResponseEntity(true, "200", "Logout successful", null);
        } catch (Exception e) {
            return JsonUtil.generateResponseEntity(false, String.valueOf(ErrorCode.INTERNAL_SERVER_ERROR), e.getMessage(), null);
        }
    }
}
