package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.domain.enums.KafkaTopicEnum;
import com.healthcare.userservice.domain.request.OtpVerificationRequest;
import com.healthcare.userservice.domain.request.TfaRequest;
import com.healthcare.userservice.domain.request.TfaVerifyRequest;
import com.healthcare.userservice.domain.response.TfaResponse;
import com.healthcare.userservice.presenter.rest.event.NotificationEvent;
import com.healthcare.userservice.presenter.service.IntegrationService;
import com.healthcare.userservice.presenter.service.KafkaProducerService;
import com.healthcare.userservice.service.interfaces.IOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService implements IOtpService {

    @Autowired
    IntegrationService integrationService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Override
    public TfaResponse generateAndSendOtp(OtpVerificationRequest otpVerificationRequest) {
        TfaResponse tfaResponse = integrationService.generateOtp(new TfaRequest(otpVerificationRequest.getUserId()));

        NotificationEvent notificationEvent = notificationService.prepareNotificationEventForOtp(tfaResponse, otpVerificationRequest);

        kafkaProducerService.publishEvent(KafkaTopicEnum.DYNAMIC_NOTIFICATION.getTopic(), notificationEvent);

        return tfaResponse;
    }

    @Override
    public Boolean verifyOtp(TfaVerifyRequest tfaVerifyRequest) {
        return integrationService.verifyOtp(tfaVerifyRequest);
    }
}
