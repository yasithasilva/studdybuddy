package com.example.quiz.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JPdfUploadResponseModel {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("pdf_name")
    private String pdfName;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("create_at")
    private LocalDateTime createdAt;
    
    // Helper method to set userId from User object
    public void setUserIdFromUser(com.example.quiz.entity.User user) {
        if (user != null) {
            this.userId = user.getId();
        }
    }
}
