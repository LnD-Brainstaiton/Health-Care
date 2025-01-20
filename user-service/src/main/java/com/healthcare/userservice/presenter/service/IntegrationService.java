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
import com.healthcare.userservice.presenter.rest.external.BmdcVerificationClient;
import com.healthcare.userservice.presenter.rest.external.NotificationFeignClient;
import com.healthcare.userservice.presenter.rest.external.TfaFeignClient;
import com.healthcare.userservice.service.BaseService;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class IntegrationService extends BaseService {

    private final TfaFeignClient tfaFeignClient;

    private final NotificationFeignClient notificationFeignClient;

    private final AppointmentClient appointmentClient;

    private final BmdcVerificationClient bmdcVerificationClient;

    @Value("${bmdc_csrf_cookie}")
    private String bmdcCookie;

    public TfaResponse generateOtp(TfaRequest request) {
        ApiResponse<TfaResponse> tfaResponse
                = tfaFeignClient.generateOtp(request);

        if (ApiResponseCode.isNotOperationSuccessful(tfaResponse)
                || Objects.isNull(tfaResponse.getData())) {
            throw new FeignClientException(ResponseMessage.INTERNAL_SERVICE_EXCEPTION);
        }

        return tfaResponse.getData();

    }

    public Boolean verifyOtp(TfaVerifyRequest request) {
        ApiResponse<Boolean> tfaResponse = tfaFeignClient.validateOtp(request);
        if (ApiResponseCode.isNotOperationSuccessful(tfaResponse)
                || Objects.isNull(tfaResponse.getData())) {
            throw new FeignClientException(ResponseMessage.INTERNAL_SERVICE_EXCEPTION);
        }

        return tfaResponse.getData();
    }

    public Boolean sendNotification(NotificationEvent request) {
        ApiResponse<Boolean> notificationResponse
                = notificationFeignClient.sendNotification(request);

        return Boolean.TRUE;

    }

    public List<LocalTime> getTimeSlots(TimeSlotRequest request){

        ApiResponse<List<LocalTime>> response = appointmentClient.getTimeSlot(request);

        if(ApiResponseCode.isNotOperationSuccessful(response)){
            throw new FeignClientException(response.getResponseCode(), response.getResponseMessage());
        }
        return response.getData();
    }

    public String fetchCaptcha() {
        String responseBody = "";
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            Response response = bmdcVerificationClient.getCaptcha(timestamp);
            responseBody = readResponseBody(response);

            // Extract cookies from the headers
            String csrfCookie = extractCookie(response);
            if (csrfCookie == null) {
                throw new FeignClientException(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode(), ResponseMessage.COOKIE_NOT_FOUND.getResponseMessage());
            }
        } catch (Exception e) {
            throw new FeignClientException(ApiResponseCode.INTER_SERVICE_COMMUNICATION_ERROR.getResponseCode(), ResponseMessage.FAIL_BMDC_API_CALL.getResponseMessage());
        }
        return responseBody;
    }

    private String readResponseBody(Response response) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream()));
        StringBuilder responseBody = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            responseBody.append(line);
        }

        return responseBody.toString();
    }

    private String extractCookie(Response response) {
        List<String> cookies = new ArrayList<>(response.headers().get("Set-Cookie"));
        if (!cookies.isEmpty()) {
            Optional<String> cookie = cookies.stream()
                    .filter(c -> c.startsWith(bmdcCookie + "="))
                    .findFirst();
            return cookie.map(c -> c.split(";")[0].split("=")[1]).orElse(null);
        }
        return null;
    }

}
