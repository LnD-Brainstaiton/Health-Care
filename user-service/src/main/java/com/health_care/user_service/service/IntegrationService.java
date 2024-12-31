package com.health_care.user_service.service;

import com.health_care.user_service.common.exceptions.FeignClientException;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.request.TimeSlotRequest;
import com.health_care.user_service.feign.AppointmentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final AppointmentClient appointmentClient;


    public List<LocalTime> getTimeSlots(TimeSlotRequest request){

        ApiResponse<List<LocalTime>> response = appointmentClient.getTimeSlot(request);

        if(ApiResponseCode.isNotOperationSuccessful(response)){
            throw new FeignClientException(response.getResponseCode(), response.getResponseMessage());
        }
        return response.getData();
    }
}
