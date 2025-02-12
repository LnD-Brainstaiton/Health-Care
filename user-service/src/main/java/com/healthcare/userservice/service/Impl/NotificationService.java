package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.domain.mapper.NotificationMapper;
import com.healthcare.userservice.domain.request.OtpVerificationRequest;
import com.healthcare.userservice.domain.request.RegisterRequest;
import com.healthcare.userservice.domain.response.TfaResponse;
import com.healthcare.userservice.presenter.rest.event.NotificationEvent;
import com.healthcare.userservice.service.interfaces.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    NotificationMapper notificationMapper;

    @Override
    public NotificationEvent prepareNotificationEventForOtp(TfaResponse response, OtpVerificationRequest request) {
        return notificationMapper.prepareNotificationEventForOtp(response, request);
    }

    @Override
    public NotificationEvent prepareNotificationEventForSignup(RegisterRequest request) {
        return notificationMapper.prepareNotificationEventForSignup(request);
    }
}
