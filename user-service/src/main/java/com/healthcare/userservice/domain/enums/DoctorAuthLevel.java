package com.healthcare.userservice.domain.enums;

public enum DoctorAuthLevel {
    LEVEL1(1),
    LEVEL2(2);

    private final int doctorAuthLevel;

    DoctorAuthLevel(int value) {
        this.doctorAuthLevel = value;
    }

    public int getAuthLevel() {
        return doctorAuthLevel;
    }
}
