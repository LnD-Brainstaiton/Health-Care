package com.healthcare.userservice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WeekDays {
    MONDAY("Mon", 1),
    TUESDAY("Tue", 2),
    WEDNESDAY("Wed", 3),
    THURSDAY("Thu", 4),
    FRIDAY("Fri", 5),
    SATURDAY("Sat", 6),
    SUNDAY("Sun", 7);

    private String day;
    private int value;

    public static String getDay(int value) {
        for (WeekDays day : WeekDays.values()) {
            if (day.getValue() == value) {
                return day.getDay();
            }
        }
        return null;
    }
}
