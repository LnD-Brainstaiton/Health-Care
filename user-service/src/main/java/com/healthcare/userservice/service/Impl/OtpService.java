package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.domain.mapper.NotificationMapper;
import com.healthcare.userservice.domain.request.OtpVerificationRequest;
import com.healthcare.userservice.domain.request.TfaRequest;
import com.healthcare.userservice.domain.response.TfaResponse;
import com.healthcare.userservice.presenter.rest.event.NotificationEvent;
import com.healthcare.userservice.presenter.service.IntegrationService;
import com.healthcare.userservice.service.interfaces.IOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService implements IOtpService {

    @Autowired
    IntegrationService integrationService;

    @Autowired
    NotificationMapper notificationMapper;

    @Override
    public Boolean generateAndSendOtp(OtpVerificationRequest otpVerificationRequest) {
        TfaResponse tfaResponse = integrationService.generateOtp(new TfaRequest(otpVerificationRequest.getUserId()));

        NotificationEvent notificationEvent = notificationMapper.prepareNotificationEventForSignup(tfaResponse, otpVerificationRequest);
        return integrationService.sendNotification(notificationEvent);
    }
}
