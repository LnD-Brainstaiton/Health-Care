package com.healthcare.userservice.domain.enums;

import lombok.Getter;

@Getter
public enum GlobalFeatureCode {
    DOCTOR(1, "DOCTOR"),
    ADMIN(2, "ADMIN"),
    APPOINTMENT(3, "APPOINTMENT"),
    PATIENT(4, "PATIENT");

    private int code;
    private String text;

    GlobalFeatureCode(int code, String text) {
        this.code = code;
        this.text = text;
    }
}
