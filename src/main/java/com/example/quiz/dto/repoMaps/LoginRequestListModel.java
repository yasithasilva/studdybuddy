package com.example.quiz.dto.repoMaps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestListModel {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password; // Add password here
    }

