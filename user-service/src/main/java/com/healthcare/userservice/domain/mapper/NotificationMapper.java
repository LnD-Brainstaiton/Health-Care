package com.healthcare.userservice.domain.mapper;

import com.healthcare.userservice.domain.dto.ReceiverDto;
import com.healthcare.userservice.domain.enums.NotificationCode;
import com.healthcare.userservice.domain.enums.NotificationType;
import com.healthcare.userservice.domain.request.OtpVerificationRequest;
import com.healthcare.userservice.domain.response.TfaResponse;
import com.healthcare.userservice.presenter.rest.event.NotificationEvent;
import org.mapstruct.Mapper;

import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = "spring")
public class NotificationMapper {
    public NotificationEvent prepareNotificationEventForSignup(TfaResponse response, OtpVerificationRequest request) {
        NotificationEvent notificationEvent = new NotificationEvent();

        notificationEvent.setNotificationCode(NotificationCode.SIGNUP.getNotificationCode());
        notificationEvent.setNotificationType(NotificationType.EMAIL);

        ReceiverDto receiverDto = new ReceiverDto();

        receiverDto.setReceiverEmail(request.getEmail());
        receiverDto.setTemplateData(generateTemplateDataForSignup(request, response));

        notificationEvent.setReceiverDto(receiverDto);

        return notificationEvent;
    }

    private Map<String, String> generateTemplateDataForSignup(OtpVerificationRequest request, TfaResponse tfaResponse) {
        Map<String, String> templateMap = new HashMap<>();

        templateMap.put("title", "Welcome to Health Care");
        templateMap.put("name", request.getFirstName().concat(" ").concat(request.getLastName()));
        templateMap.put("otp", tfaResponse.getGeneratedOtp());
        templateMap.put("minutes", tfaResponse.getOtpValidityInMinute().toString());

        return templateMap;
    }
}
