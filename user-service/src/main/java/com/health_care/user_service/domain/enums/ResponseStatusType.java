package com.health_care.user_service.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum ResponseStatusType {

    ACCEPTED(1,"Accepted"),
    REJECTED(2, "Rejected"),
    PENDING(3, "Pending");

    private int code;
    private String text;

    ResponseStatusType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return this.code;
    }
    public String getText() {
        return this.text;
    }

    public static String getNameByValue(int code) {
        String name = "";
        for (ResponseStatusType responseStatusType : ResponseStatusType.values()) {
            if (responseStatusType.getCode() == code) {
                name = responseStatusType.name();
                break;
            }
        }
        return name;
    }
}