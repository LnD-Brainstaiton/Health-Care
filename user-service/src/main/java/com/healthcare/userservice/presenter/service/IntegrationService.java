package com.healthcare.userservice.presenter.service;

import com.healthcare.userservice.common.exceptions.FeignClientException;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.request.TfaRequest;
import com.healthcare.userservice.domain.response.TfaResponse;
import com.healthcare.userservice.presenter.rest.event.NotificationEvent;
import com.healthcare.userservice.presenter.rest.external.NotificationFeignClient;
import com.healthcare.userservice.presenter.rest.external.TfaFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final TfaFeignClient tfaFeignClient;

    private final NotificationFeignClient notificationFeignClient;

    public TfaResponse generateOtp(TfaRequest request) {
        ApiResponse<TfaResponse> tfaResponse
                = tfaFeignClient.generateOtp(request);

        if (ApiResponseCode.isNotOperationSuccessful(tfaResponse)
                || Objects.isNull(tfaResponse.getData())) {
            throw new FeignClientException(ResponseMessage.INTERNAL_SERVICE_EXCEPTION);
        }

        return tfaResponse.getData();

    }

    public Boolean sendNotification(NotificationEvent request) {
        ApiResponse<Boolean> notificationResponse
                = notificationFeignClient.sendNotification(request);

        if (ApiResponseCode.isNotOperationSuccessful(notificationResponse)
                || Objects.isNull(notificationResponse.getData())) {
            throw new FeignClientException(ResponseMessage.INTERNAL_SERVICE_EXCEPTION);
        }

        return notificationResponse.getData();

    }

}
