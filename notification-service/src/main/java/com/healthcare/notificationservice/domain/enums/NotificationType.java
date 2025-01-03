package com.healthcare.notificationservice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    SMS,
    EMAIL,
    BOTH
}
