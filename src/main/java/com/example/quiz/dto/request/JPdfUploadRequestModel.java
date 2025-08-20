package com.example.quiz.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JPdfUploadRequestModel {

    @NotNull
    @JsonProperty("file")
    private MultipartFile file;

    @NotNull
    @JsonProperty("user_id")
    private Long userId;
}
