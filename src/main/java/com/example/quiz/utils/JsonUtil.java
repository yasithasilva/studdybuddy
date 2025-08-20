package com.example.quiz.utils;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonUtil {
    /**
     * @param status
     * @param errorCode
     * @param content
     * @return
     */
    public static ResponseEntity<Map<String, Object>> generateResponseEntity(boolean status, ErrorCode errorCode, Object content) {

        Map<String, Object> responseEntity = new LinkedHashMap<>();
        responseEntity.put("status", status);
        responseEntity.put("error_code", errorCode.getCode());
        responseEntity.put("message", errorCode.getMessage());
        responseEntity.put("content", content);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);

    }


    /**
     * @param status
     * @param errorCode
     * @param message
     * @param content
     * @return
     */
    public static ResponseEntity<Map<String, Object>> generateResponseEntity(boolean status, String errorCode, String message, Object content) {
        Map<String, Object> responseEntity = getResponseEntity(status, errorCode, message, content);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }
    public static Map<String, Object> getResponseEntity(boolean status, String errorCode, String message, Object content) {
        Map<String, Object> responseEntity = new LinkedHashMap<>();
        responseEntity.put("status", status);
        responseEntity.put("error_code", errorCode);
        responseEntity.put("message", message);
        responseEntity.put("content", content);
        return responseEntity;
    }


    /**
     * @param dataObject
     * @param fieldName
     * @return
     */
    public static JSONObject getJson(Object dataObject, String fieldName) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(fieldName, dataObject);
        return jsonObject;

    }


}
