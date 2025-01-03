package com.healthcare.userservice.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum YesNo {
    YES('Y'),
    NO('N');

    private char code;

    YesNo(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static Map getYesNoTypeMap() {
        Map<Character, String> statusTypeMap = new HashMap<>();
        for (YesNo type : YesNo.values()) {
            statusTypeMap.put(type.getCode(), type.name());
        }
        return statusTypeMap;
    }
}
