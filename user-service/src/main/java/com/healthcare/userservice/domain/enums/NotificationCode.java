package com.healthcare.userservice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationCode {

    OTP_FOR_SIGNUP("OTPSIGNUP101"),

    SIGNUP("SIGNUP101");

    String notificationCode;

}
