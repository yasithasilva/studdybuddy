package com.example.quiz.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JUserLoginResponseModel {
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
    @JsonProperty("token")
    public String token;
}
