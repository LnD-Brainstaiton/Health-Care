package com.healthcare.userservice.presenter.service;

import com.healthcare.userservice.common.exceptions.FeignClientException;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.request.TfaRequest;
import com.healthcare.userservice.domain.request.TfaVerifyRequest;
import com.healthcare.userservice.domain.request.TimeSlotRequest;
import com.healthcare.userservice.domain.response.TfaResponse;
import com.healthcare.userservice.presenter.rest.event.NotificationEvent;
import com.healthcare.userservice.presenter.rest.external.AppointmentClient;
import com.healthcare.userservice.presenter.rest.external.NotificationFeignClient;
import com.healthcare.userservice.presenter.rest.external.TfaFeignClient;
import com.healthcare.userservice.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class IntegrationService extends BaseService {

    private final TfaFeignClient tfaFeignClient;
    private final NotificationFeignClient notificationFeignClient;
    private final AppointmentClient appointmentClient;

    public TfaResponse generateOtp(TfaRequest request) {
        ApiResponse<TfaResponse> tfaResponse = tfaFeignClient.generateOtp(request);
        if (ApiResponseCode.isNotOperationSuccessful(tfaResponse) || Objects.isNull(tfaResponse.getData())) {
            throw new FeignClientException(ResponseMessage.INTERNAL_SERVICE_EXCEPTION);
        }
        return tfaResponse.getData();
    }

    public Boolean verifyOtp(TfaVerifyRequest request) {
        ApiResponse<Boolean> tfaResponse = tfaFeignClient.validateOtp(request);
        if (ApiResponseCode.isNotOperationSuccessful(tfaResponse) || Objects.isNull(tfaResponse.getData())) {
            throw new FeignClientException(ResponseMessage.INTERNAL_SERVICE_EXCEPTION);
        }
        return tfaResponse.getData();
    }

    public Boolean sendNotification(NotificationEvent request) {
        ApiResponse<Boolean> notificationResponse = notificationFeignClient.sendNotification(request);
        return Boolean.TRUE;
    }

    public List<LocalTime> getTimeSlots(TimeSlotRequest request) {
        ApiResponse<List<LocalTime>> response = appointmentClient.getTimeSlot(request);
        if (ApiResponseCode.isNotOperationSuccessful(response)) {
            throw new FeignClientException(response.getResponseCode(), response.getResponseMessage());
        }
        return response.getData();
    }
}
