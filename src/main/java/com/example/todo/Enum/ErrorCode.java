package com.example.todo.Enum;

public enum ErrorCode {

    TASK_NOT_FOUND("E40001", "Task is not found"),
    INVALID_DUE_DATE_FORMAT("E40002", "Due date format is invalid"),
    INVALID_DUE_DATE("E40003", "Due date must be later than or equals to today"),
    UNACCEPTABLE_ACTION("E40004", "Invalid action for current status");


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
