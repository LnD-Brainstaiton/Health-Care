package com.healthcare.userservice.domain.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Designation {
    INTERN("Intern"),
    RESIDENT("Resident"),
    FELLOW("Fellow"),
    LECTURER("Lecturer"),
    ASSISTANT_PROFESSOR("Assistant Professor"),
    ASSOCIATE_PROFESSOR("Associate Professor"),
    PROFESSOR("Professor"),
    HEAD_OF_DEPARTMENT("Head of Department"),
    DEAN("Dean"),
    DIRECTOR("Director");

    private final String designation;

    Designation(String s) {
        this.designation = s;
    }

    public String getDesignation() {
        return designation;
    }

    public static List<String> allDesignation() {
        return Arrays.stream(Designation.values())
                .map(Designation::getDesignation)
                .collect(Collectors.toList());
    }
}
