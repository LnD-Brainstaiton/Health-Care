package com.healthcare.userservice.domain.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Department {

    ANESTHESIOLOGY("Anesthesiology"),
    CARDIOLOGY("Cardiology"),
    DERMATOLOGY("Dermatology"),
    EMERGENCY_MEDICINE("Emergency Medicine"),
    ENDOCRINOLOGY("Endocrinology"),
    FAMILY_MEDICINE("Family Medicine"),
    GASTROENTEROLOGY("Gastroenterology"),
    GENERAL_SURGERY("General Surgery"),
    HEMATOLOGY("Hematology"),
    INFECTIOUS_DISEASES("Infectious Diseases"),
    INTERNAL_MEDICINE("Internal Medicine"),
    NEPHROLOGY("Nephrology"),
    NEUROLOGY("Neurology"),
    OBSTETRICS_AND_GYNECOLOGY("Obstetrics and Gynecology"),
    ONCOLOGY("Oncology"),
    OPHTHALMOLOGY("Ophthalmology"),
    ORTHOPEDICS("Orthopedics"),
    OTORHINOLARYNGOLOGY("Otorhinolaryngology (ENT)"),
    PEDIATRICS("Pediatrics"),
    PLASTIC_SURGERY("Plastic Surgery"),
    PSYCHIATRY("Psychiatry"),
    PULMONOLOGY("Pulmonology"),
    RADIOLOGY("Radiology"),
    RHEUMATOLOGY("Rheumatology"),
    UROLOGY("Urology"),
    PATHOLOGY("Pathology"),
    PHYSICAL_MEDICINE("Physiatry (Physical Medicine and Rehabilitation)"),
    VASCULAR_SURGERY("Vascular Surgery");

    private final String department;

    Department(String s) {
        this.department = s;
    }

    public String getDepartment() {
        return department;
    }

    public static List<String> allDepartment() {
        return Arrays.stream(Department.values())
                .map(Department::getDepartment)
                .collect(Collectors.toList());
    }
}
