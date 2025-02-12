package com.healthcare.userservice.service.interfaces;

import com.healthcare.userservice.domain.request.OtpVerificationRequest;
import com.healthcare.userservice.domain.request.RegisterRequest;
import com.healthcare.userservice.domain.response.TfaResponse;
import com.healthcare.userservice.presenter.rest.event.NotificationEvent;

public interface INotificationService {
    NotificationEvent prepareNotificationEventForOtp(TfaResponse response, OtpVerificationRequest request);

    NotificationEvent prepareNotificationEventForSignup(RegisterRequest request);
}
