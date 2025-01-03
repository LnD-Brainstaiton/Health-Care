package com.healthcare.userservice.domain.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private final String gender;

    Gender(String s) {
        this.gender = s;
    }

    public String getGender() {
        return gender;
    }

    public static List<String> allGender() {
        return Arrays.stream(Gender.values())
                .map(Gender::getGender)
                .collect(Collectors.toList());
    }
}
