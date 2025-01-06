package com.healthcare.userservice.domain.enums;

import java.util.stream.Stream;

public enum OperationType {
    DEFAULT(-1, "Default"),
    ALL(0, "All"),
    CREATE(1, "Create"),
    UPDATE(2, "Update"),
    REMOVE(3, "Remove"),
    SELECT(4, "Select");


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

    public static OperationType fromString(String value) {
        return Stream.of(OperationType.values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid OperationType: " + value));
    }

}
