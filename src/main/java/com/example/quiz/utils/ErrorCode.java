package com.example.quiz.utils;

public enum ErrorCode {
    USER_ALREADY_EXISTS(001, "Username already exists"),
    USER_NOT_FOUND(003, "User not found"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    USER_CREATE_SUCCESSFUL(201, "User Create Successful"),
    LOGIN_SUCCESSFUL(200,"Login successful"),
    INVALID_CREDENTIALS(401,"Invalid credentials"),
    SUCCESS(200, "Operation successful"),
    INVALID_INPUT(400, "Invalid input provided");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
