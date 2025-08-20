package com.example.quiz.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class JUserLIstResponseModel {
    @JsonProperty
    List<JUserResponseModel> data;
}
