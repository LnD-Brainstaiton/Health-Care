package com.health_care.user_service.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum OperationType {
    DEFAULT(-1, "Default"),
    ALL(0, "All"),
    CREATE(1, "Create"),
    UPDATE(2, "Update"),
    REMOVE(3, "Remove"),
    SELECT(4,"Select");


    private int code;
    private String text;

    OperationType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return this.code;
    }

    public String getText() {
        return text;
    }

    public static String getNameByValue(int code) {
        String name = "";
        for (OperationType operationType : OperationType.values()) {
            if (operationType.getCode() == code) {
                name = operationType.name();
                break;
            }
        }
        return name;
    }

}
