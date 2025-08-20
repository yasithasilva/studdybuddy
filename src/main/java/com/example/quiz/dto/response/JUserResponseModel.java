package com.example.quiz.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class JUserResponseModel {
    @NotNull
    @JsonProperty("username")
    public String username;

    @NotNull
    @JsonProperty("firstName")
    public String firstName;

    @NotNull
    @JsonProperty("lastName")
    public String lastName;

    @NotNull
    @JsonProperty("email")
    public String email;

    @NotNull
    @JsonProperty("createdAt")
    public Date createdAt;

    @JsonProperty("updatedAt")
    public Date updatedAt;
}
