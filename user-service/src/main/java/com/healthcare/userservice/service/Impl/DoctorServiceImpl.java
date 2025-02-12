package com.healthcare.userservice.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.userservice.config.AuthConfig;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.Doctor;
import com.healthcare.userservice.domain.entity.Rating;
import com.healthcare.userservice.domain.entity.DoctorSetting;
import com.healthcare.userservice.domain.entity.User;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.mapper.DoctorMapper;
import com.healthcare.userservice.domain.request.BmdcValidationRequest;
import com.healthcare.userservice.domain.mapper.RatingMapper;
import com.healthcare.userservice.domain.request.DoctorInfoUpdateRequest;
import com.healthcare.userservice.domain.request.DoctorVacationRequest;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.DoctorInfoResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;
import com.healthcare.userservice.presenter.service.BmdcClientService;
import com.healthcare.userservice.domain.response.*;
import com.healthcare.userservice.presenter.service.IntegrationService;
import com.healthcare.userservice.repository.DoctorRepository;
import com.healthcare.userservice.repository.DoctorSettingRepository;
import com.healthcare.userservice.repository.RatingRepository;
import com.healthcare.userservice.repository.UserRepository;
import com.healthcare.userservice.repository.specification.DoctorSpecification;
import com.healthcare.userservice.service.BaseService;
import com.healthcare.userservice.service.IDoctorService;
import com.healthcare.userservice.common.exceptions.InvalidRequestDataException;
import com.healthcare.userservice.common.exceptions.RecordNotFoundException;
import com.healthcare.userservice.domain.entity.DoctorTimeSlot;
import com.healthcare.userservice.domain.enums.WeekDays;
import com.healthcare.userservice.domain.request.TimeSlotRequest;
import com.healthcare.userservice.repository.DoctorTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class DoctorServiceImpl extends BaseService implements IDoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final DoctorTimeSlotRepository timeSlotRepository;
    private final IntegrationService integrationService;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    private final AuthConfig authConfig;
    private final BmdcClientService bmdcClientService;
    private final DoctorSettingRepository settingRepository;

    @Override
    public ApiResponse<Void> updateDoctor(DoctorInfoUpdateRequest request) {
        return doctorRepository.getDoctorByDoctorIdAndIsActive(request.getDoctorId(), Boolean.TRUE)
                .map(doctor -> updateDoctorDetails(doctor, request))
                .orElseGet(() -> ApiResponse.<Void>builder()
                        .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                        .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                        .build());
    }


    @Override
    public ApiResponse<DoctorInfoResponse> getDoctorById(String id) {
        Optional<Doctor> doctorOptional = doctorRepository.getDoctorByDoctorIdAndIsActive(id, Boolean.TRUE);
        List<Rating> ratingList = ratingRepository.findAllByDoctorId(id);

        List<RatingResponse> parentCommentList = ratingMapper.mapToRatingResponse(ratingList);

        return doctorOptional.map(doctor -> {
            DoctorInfoResponse response = doctorMapper.toDoctorInfoResponse(doctor);
            response.setRating(BigDecimal.valueOf(ratingList.stream()
                    .filter(reply -> reply.getCommentParentId().equals("parent"))
                    .map(Rating::getRating)
                    .mapToDouble(BigDecimal::doubleValue)
                    .average()
                    .orElse(0.0))
            );
            response.setRatingResponseList(parentCommentList);

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
    public PaginationResponse<DoctorInfoResponse> getAllDoctorInfo(int page, int size, String sort, String firstnameLastname, String id,
                                                                   String department, String designation, String gender) {
        Sort.Order sortOrder;
        try {
            sortOrder = Sort.Order.asc(sort);
        } catch (IllegalArgumentException ex) {
            return new PaginationResponse<>(
                    Collections.emptyList(),
                    0L
            );
        }

        // Retrieve all unavailable doctors
        List<DoctorSetting> unAvailableDoctors = settingRepository.findDoctorsWithUnavailabilitySchedule();

        // Map unavailable doctors by doctorId for quick lookup
        Map<String, String> unavailableDoctorMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        LocalDate today = LocalDate.now();

        for (DoctorSetting setting : unAvailableDoctors) {
            try {
                // Deserialize unavailabilitySchedule
                List<Map<String, String>> schedules = objectMapper.readValue(
                        setting.getUnavailabilitySchedule(),
                        new TypeReference<List<Map<String, String>>>() {}
                );

                // Filter schedules based on today's date
                List<Map<String, String>> validSchedules = schedules.stream()
                        .filter(schedule -> {
                            String endDateStr = schedule.get("endDate");
                            LocalDate endDate = LocalDate.parse(endDateStr);
                            return !today.isAfter(endDate);
                        })
                        .collect(Collectors.toList());

                // If valid schedules exist, add to unavailableDoctorMap
                if (!validSchedules.isEmpty()) {
                    unavailableDoctorMap.put(setting.getDoctorId(), objectMapper.writeValueAsString(validSchedules));
                }
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        // Check if page = -1, fetch all active doctors without pagination
        if (page == -1) {
            List<Doctor> activeDoctors = doctorRepository.findAllByIsActiveTrue(Sort.by(sortOrder));

            List<DoctorInfoResponse> doctorInfoResponses = activeDoctors.stream()
                    .map(doctor -> {
                        DoctorInfoResponse response = doctorMapper.toDoctorInfoResponse(doctor);

                        // Check if the doctor is in the unavailable map
                        if (unavailableDoctorMap.containsKey(doctor.getDoctorId())) {
                            response.setIsAvailable(false);
                            response.setUnavailableDate(unavailableDoctorMap.get(doctor.getDoctorId()));
                        } else {
                            response.setIsAvailable(true);
                            response.setUnavailableDate(null);
                        }
                        return response;
                    })
                    .collect(Collectors.toList());

            return new PaginationResponse<>(doctorInfoResponses, (long) doctorInfoResponses.size());
        }

        int validatedPage = Math.max(0, page);
        int validatedSize = Math.max(1, size);

        Pageable pageable = PageRequest.of(validatedPage, validatedSize, Sort.by(sortOrder));
        Specification<Doctor> spec = Specification.where(DoctorSpecification.hasDesignation(designation))
                .and(DoctorSpecification.hasDepartment(department))
                .and(DoctorSpecification.hasSpecialities(gender))
                .and(DoctorSpecification.hasFirstnameLastname(firstnameLastname))
                .and(DoctorSpecification.hasId(id))
                .and(DoctorSpecification.isActive());
        Page<Doctor> activeDoctorsPage = doctorRepository.findAll(spec, pageable);

        if (activeDoctorsPage.isEmpty()) {
            return new PaginationResponse<>(
                    Collections.emptyList(),
                    0L
            );
        }

        List<DoctorInfoResponse> doctorInfoResponses = activeDoctorsPage.getContent().stream()
                .map(doctor -> {
                    DoctorInfoResponse response = doctorMapper.toDoctorInfoResponse(doctor);

                    // Check if the doctor is in the unavailable map
                    if (unavailableDoctorMap.containsKey(doctor.getDoctorId())) {
                        response.setIsAvailable(false);
                        response.setUnavailableDate(unavailableDoctorMap.get(doctor.getDoctorId()));
                    } else {
                        response.setIsAvailable(true);
                        response.setUnavailableDate(null);
                    }
                    return response;
                })
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
    public ApiResponse<String> deleteDoctorById(String id) {
        Doctor doctor = doctorRepository.findByDoctorId(id);
        if (doctor == null) {
            return ApiResponse.<String>builder()
                    .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                    .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                    .build();
        }
        doctor.setIsActive(false);
        doctorRepository.save(doctor);
        Optional<User> user = userRepository.findByUserId(id);
        if (user.isPresent()) {
            user.get().setIsActive(false);
            userRepository.save(user.get());
        } else {
            return ApiResponse.<String>builder()
                    .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                    .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                    .build();
        }

        return ApiResponse.<String>builder()
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

        while (!timeSlot.getStartTime().isAfter(timeSlot.getEndTime())){
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


    @Override
    public ApiResponse<String> getCaptcha() {
        ApiResponse<String> response = new ApiResponse<>();
        String base64Captcha = bmdcClientService.fetchCaptcha();

        if (base64Captcha == null || base64Captcha.isEmpty()) {
            response.setResponseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode());
            response.setResponseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage());
            response.setData(null);
            return response;
        }

        byte[] captchaBytes = decodeBase64Image(base64Captcha);
        if (captchaBytes == null) {
            response.setResponseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode());
            response.setResponseMessage("Failed to fetch captcha image");
            response.setData(null);
            return response;
        }

        // Convert byte[] to Base64 String
        String base64EncodedCaptcha = Base64.getEncoder().encodeToString(captchaBytes);

        response.setResponseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
        response.setResponseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage());
        response.setData(base64EncodedCaptcha);

        return response;
    }



    private byte[] decodeBase64Image(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        // If the input is already a Base64-encoded image (not wrapped in an <img> tag)
        if (input.startsWith("data:image/")) {
            String base64Data = input.substring(input.indexOf(",") + 1); // Remove the prefix
            return Base64.getDecoder().decode(base64Data);
        }

        // Otherwise, attempt to extract from an HTML <img> tag
        Pattern pattern = Pattern.compile("<img\\s+src=\"(data:image/\\w+;base64,([^\"]+)|https?://[^\"]+)\"");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String base64Data = matcher.group(2);
            if (base64Data != null) {
                return Base64.getDecoder().decode(base64Data);
            }

            String imageUrl = matcher.group(1);
            return downloadImageAsBytes(imageUrl);
        }

        return null;
    }



    private byte[] downloadImageAsBytes(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (InputStream inputStream = connection.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead); // Fix incorrect writing
                }

                return outputStream.toByteArray();
            } finally {
                connection.disconnect(); // Ensure connection is closed
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public ApiResponse<String> validateRegistration(BmdcValidationRequest request) {
        ApiResponse<String> response = new ApiResponse<>();
        String data = bmdcClientService.validateRegistration(request);
        if(data == null || data.isEmpty()) {
            response.setResponseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode());
            response.setResponseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage());

            return response;
        }
        response.setResponseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
        response.setResponseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage());
        response.setData(data);
        return response;
    }

    @Override
    public ApiResponse<Void> vacationRequest(DoctorVacationRequest request) {
        // Validate input
        if (request.getStartDate() == null || request.getEndDate() == null) {
            return new ApiResponse<>(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "Start date and end date are required.", null);
        }

        try {
            LocalDate startDate = LocalDate.parse(request.getStartDate());
            LocalDate endDate = LocalDate.parse(request.getEndDate());
            LocalDate today = LocalDate.now();

            if (startDate.equals(today)) {
                return new ApiResponse<>(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "Start date cannot be today's date.", null);
            }

            if (endDate.isBefore(startDate)) {
                return new ApiResponse<>(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "End date cannot be before the start date.", null);
            }

            Optional<DoctorSetting> optionalDoctorSetting = settingRepository.findByDoctorId(getUserIdentity());
            if (optionalDoctorSetting.isEmpty()) {
                return new ApiResponse<>(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "Doctor settings not found.", null);
            }

            // Update the unavailability schedule
            DoctorSetting doctorSetting = optionalDoctorSetting.get();
            String unavailabilitySchedule = doctorSetting.getUnavailabilitySchedule();
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> scheduleList;

            if (unavailabilitySchedule == null || unavailabilitySchedule.isEmpty()) {
                scheduleList = new ArrayList<>();
            } else {
                scheduleList = objectMapper.readValue(unavailabilitySchedule, new TypeReference<>() {});
            }

            // Add the new vacation period
            Map<String, String> newVacation = new HashMap<>();
            newVacation.put("startDate", request.getStartDate());
            newVacation.put("endDate", request.getEndDate());
            newVacation.put("reason", request.getReason());
            scheduleList.add(newVacation);

            // Save updated schedule
            doctorSetting.setUnavailabilitySchedule(objectMapper.writeValueAsString(scheduleList));
            settingRepository.save(doctorSetting);

            // Return success response
            return new ApiResponse<>(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode(), "Vacation request submitted successfully.", null);

        } catch (DateTimeParseException e) {
            return new ApiResponse<>(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "Invalid date format. Use YYYY-MM-DD.", null);
        } catch (Exception e) {
            return new ApiResponse<>(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "An error occurred while processing the vacation request.", null);
        }
    }

    private ApiResponse<Void> updateDoctorDetails(Doctor doctor, DoctorInfoUpdateRequest request) {
        doctor.setFirstname(request.getFirstname());
        doctor.setLastname(request.getLastname());
        doctor.setMobile(request.getMobile());
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
