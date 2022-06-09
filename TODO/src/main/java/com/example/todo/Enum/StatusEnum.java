package com.example.todo.Enum;

public enum StatusEnum {
    TODO(0, "Todo"),
    INPROGRESS(1, "Inprogress"),
    DONE(2, "Done");

    private final int code;
    private final String status;

    StatusEnum(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

}
