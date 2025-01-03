package com.healthcare.userservice.domain.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BloodGroup {
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-");

    private final String bloodGroup;

    BloodGroup(String s) {
        this.bloodGroup = s;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public static List<String> allBloodGroup() {
        return Arrays.stream(BloodGroup.values())
                .map(BloodGroup::getBloodGroup)
                .collect(Collectors.toList());
    }
}
