package com.example.quiz.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JUserLoginModel {

    @JsonProperty("usernameOrEmail")
    @JsonAlias({"username", "email"})
    public String usernameOrEmail;

    @JsonProperty("password")
    public String password;

    @Override
    public String toString() {
        return "JUserLoginModel{" +
                "username='" + usernameOrEmail+ '\'' +
                ", password='" + (password != null ? "***" : "NULL") + '\'' +
                '}';
    }
}
