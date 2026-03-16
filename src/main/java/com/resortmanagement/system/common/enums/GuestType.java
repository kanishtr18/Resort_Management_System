package com.resortmanagement.system.common.enums;

public enum GuestType {
    ADULT, CHILD, INFANT, SENIOR;

    public static GuestType fromAge(int age) {
        if (age < 2) return INFANT;
        if (age < 12) return CHILD;
        if (age >= 60) return SENIOR;
        return ADULT;
    }
}