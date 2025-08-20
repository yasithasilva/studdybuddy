package com.example.quiz.controller;

import com.example.quiz.dto.request.JPdfUploadRequestModel;
import com.example.quiz.service.PdfUploadService.IPdfUploadService;
import com.example.quiz.utils.ErrorCode;
import com.example.quiz.utils.JsonUtil;
import com.example.quiz.utils.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("api/pdf/v1")
public class PdfUploadController {

    @Autowired
    private IPdfUploadService pdfUploadService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> uploadPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam("user_id") Long userId) {
        try {
            JPdfUploadRequestModel pdfUploadRequestModel = new JPdfUploadRequestModel();
            pdfUploadRequestModel.setFile(file);
            pdfUploadRequestModel.setUserId(userId);
            
            ServiceResponse response = pdfUploadService.uploadPdf(pdfUploadRequestModel);
            return JsonUtil.generateResponseEntity(Boolean.parseBoolean(response.getStatus()), response.getErrorCode(), response.getMessage(), response.getData());

        } catch (Exception e) {
            return JsonUtil.generateResponseEntity(false, String.valueOf(ErrorCode.INTERNAL_SERVER_ERROR), e.getMessage(), null);
        }
    }
}
