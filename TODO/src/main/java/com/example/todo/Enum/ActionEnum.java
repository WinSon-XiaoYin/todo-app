package com.example.todo.Enum;

public enum ActionEnum {

    START("start"),
    DONE("done"),
    ROLLBACK("rollback");

    private final String action;

    ActionEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
