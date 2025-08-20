package com.example.quiz.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JUserCreateRequestModel {

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
    @JsonProperty("password")
    public String password;

    @Override
    public String toString() {
        return "JUserCreateRequestModel{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + (password != null ? "***" : "NULL") + '\'' +
                '}';
    }
}
