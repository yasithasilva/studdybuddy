package com.example.quiz.service.PdfUploadService;

import com.example.quiz.dto.request.JPdfUploadRequestModel;
import com.example.quiz.utils.ServiceResponse;

public interface IPdfUploadService {

    ServiceResponse uploadPdf(JPdfUploadRequestModel uploadRequestModel);
}
