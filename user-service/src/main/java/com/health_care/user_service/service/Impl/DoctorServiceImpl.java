package com.health_care.user_service.service.Impl;

import com.health_care.user_service.common.exceptions.InvalidRequestDataException;
import com.health_care.user_service.common.exceptions.RecordNotFoundException;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.Doctor;
import com.health_care.user_service.domain.entity.DoctorTimeSlot;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.enums.WeekDays;
import com.health_care.user_service.domain.mapper.DoctorMapper;
import com.health_care.user_service.domain.request.DoctorInfoUpdateRequest;
import com.health_care.user_service.domain.request.TimeSlotRequest;
import com.health_care.user_service.domain.response.CountResponse;
import com.health_care.user_service.domain.response.DoctorInfoResponse;
import com.health_care.user_service.domain.response.PaginationResponse;
import com.health_care.user_service.domain.response.TimeSlotResponse;
import com.health_care.user_service.repository.DoctorRepository;
import com.health_care.user_service.repository.DoctorTimeSlotRepository;
import com.health_care.user_service.service.IDoctorService;
import com.health_care.user_service.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements IDoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final DoctorTimeSlotRepository timeSlotRepository;
    private final IntegrationService integrationService;

    @Override
    public ApiResponse<Void> updateDoctor(DoctorInfoUpdateRequest request) {
        return doctorRepository.getDoctorByMobileAndIsActive(request.getMobile(), Boolean.TRUE)
                .map(doctor -> updateDoctorDetails(doctor, request))
                .orElseGet(() -> ApiResponse.<Void>builder()
                        .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                        .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                        .build());
    }


    @Override
    public ApiResponse<DoctorInfoResponse> getDoctorById(String id) {
        Optional<Doctor> doctorOptional = doctorRepository.getDoctorByDoctorIdAndIsActive(id, Boolean.TRUE);

        return doctorOptional.map(doctor -> {
            DoctorInfoResponse response = doctorMapper.toDoctorInfoResponse(doctor);
            return ApiResponse.<DoctorInfoResponse>builder()
                    .data(response)
                    .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                    .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                    .build();
        }).orElseGet(() -> ApiResponse.<DoctorInfoResponse>builder()
                .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                .build());
    }

    @Override
    public PaginationResponse<DoctorInfoResponse> getAllDoctorInfo(int page, int size, String sort) {
        Sort.Order sortOrder;
        try {
            sortOrder = Sort.Order.asc(sort);
        } catch (IllegalArgumentException ex) {
            return new PaginationResponse<>(
                    Collections.emptyList(),
                    0L
            );
        }
        // Check if page = -1, fetch all active doctors without pagination
        if (page == -1) {
            List<Doctor> activeDoctors = doctorRepository.findAllByIsActiveTrue(Sort.by(sortOrder));
            List<DoctorInfoResponse> doctorInfoResponses = activeDoctors.stream()
                    .map(doctorMapper::toDoctorInfoResponse)
                    .collect(Collectors.toList());
            return new PaginationResponse<>(doctorInfoResponses, (long) doctorInfoResponses.size());
        }

        int validatedPage = Math.max(0, page);
        int validatedSize = Math.max(1, size);

        Pageable pageable = PageRequest.of(validatedPage, validatedSize, Sort.by(sortOrder));
        Page<Doctor> activeDoctorsPage = doctorRepository.findAllByIsActiveTrue(pageable);

        if (activeDoctorsPage.isEmpty()) {
            return new PaginationResponse<>(
                    Collections.emptyList(),
                    0L
            );
        }

        List<DoctorInfoResponse> doctorInfoResponses = activeDoctorsPage.getContent().stream()
                .map(doctorMapper::toDoctorInfoResponse)
                .collect(Collectors.toList());

        return new PaginationResponse<>(
                activeDoctorsPage.getNumber(),
                activeDoctorsPage.getSize(),
                activeDoctorsPage.getTotalElements(),
                activeDoctorsPage.getTotalPages(),
                doctorInfoResponses
        );
    }



    @Override
    public ApiResponse<CountResponse> getDoctorsCount() {
        List<Doctor> doctors = doctorRepository.findAllByIsActiveTrue();
        CountResponse countResponse = new CountResponse();
        countResponse.setCount(doctors.size());
        return ApiResponse.<CountResponse>builder()
                .data(countResponse)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }

    @Override
    public TimeSlotResponse getTimeSlotList(TimeSlotRequest request) {
        validateTimeSlotRequest(request);

        DayOfWeek dayofWeek = request.getDate().getDayOfWeek();
        String day = WeekDays.getDay(dayofWeek.getValue());

        Optional<DoctorTimeSlot> timeSlotOpt = timeSlotRepository.findByDoctorIdAndDaysOfWeekContainingIgnoreCase(request.getDoctorId(), day);
        if(timeSlotOpt.isEmpty()) {
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);
        }

        DoctorTimeSlot timeSlot = timeSlotOpt.get();
        List<LocalTime> appointedTimeSlots = integrationService.getTimeSlots(request);

        List<LocalTime> responseSlot = new ArrayList<>();

        while (!timeSlot.getEndTime().isAfter(timeSlot.getStartTime())){
            int flag = 0;
            for(LocalTime appointedTimeSlot : appointedTimeSlots){
                if(appointedTimeSlot.equals(timeSlot.getStartTime())){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                responseSlot.add(timeSlot.getStartTime());
            }

            timeSlot.setStartTime(timeSlot.getStartTime().plusMinutes(30));
        }

        TimeSlotResponse timeSlotResponse = new TimeSlotResponse();
        timeSlotResponse.setDoctorId(request.getDoctorId());
        timeSlotResponse.setTimeSlotList(responseSlot);

        return timeSlotResponse;
    }

    private ApiResponse<Void> updateDoctorDetails(Doctor doctor, DoctorInfoUpdateRequest request) {
        doctor.setDepartment(request.getDepartment());
        doctor.setDesignation(request.getDesignation());
        doctor.setFee(request.getFee());
        doctor.setGender(request.getGender());
        doctor.setSpecialities(request.getSpecialities());
        doctorRepository.save(doctor);

        return ApiResponse.<Void>builder()
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }

    private void validateTimeSlotRequest(TimeSlotRequest request){

        if(Objects.isNull(request) ||
                StringUtils.isEmpty(request.getDoctorId()) ||
                Objects.isNull(request.getDate())){

            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
    }
}
