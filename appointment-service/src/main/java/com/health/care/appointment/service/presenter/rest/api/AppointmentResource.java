package com.health.care.appointment.service.presenter.rest.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.health.care.appointment.service.common.utils.AppUtils;
import com.health.care.appointment.service.common.utils.ResponseUtils;
import com.health.care.appointment.service.domain.common.ApiResponse;
import com.health.care.appointment.service.domain.enums.ResponseMessage;
import com.health.care.appointment.service.domain.request.CreateAppointmentRequest;
import com.health.care.appointment.service.domain.request.TimeSlotRequest;
import com.health.care.appointment.service.domain.request.UpdateAppointmentRequest;
import com.health.care.appointment.service.domain.response.AppointmentResponse;
import com.health.care.appointment.service.domain.response.CountResponse;
import com.health.care.appointment.service.domain.response.PaginationResponse;
import com.health.care.appointment.service.service.IAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppUtils.BASE_URL)
public class AppointmentResource {

    private final IAppointmentService appointmentService;

    @PostMapping("/create")
    public ApiResponse<Void> createAppointment(@RequestBody CreateAppointmentRequest request) {
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, appointmentService.addAppointment(request));
    }

    @PutMapping("/update")
    public ApiResponse<Void> updateAppointment(@RequestBody UpdateAppointmentRequest request) {
       return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, appointmentService.updateAppointment(request));
    }

    @GetMapping("/doctor/upcoming/appointment/count")
    public ApiResponse<CountResponse> getDoctorsUpcomingAppointmentCount(
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) @JsonFormat(pattern = "yyyy-MM-dd") String date,
            @RequestParam(required = false) @JsonFormat(pattern = "HH:mm:ss") String time
    ){
        ApiResponse<CountResponse> response = appointmentService.getDoctorsUpcomingAppointmentCount(doctorId,date, time);
        return response;
    }

    @GetMapping("/list")
    ApiResponse<PaginationResponse<AppointmentResponse>> listAppointments(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String appointmentId,
            @RequestParam(required = false) @JsonFormat(pattern = "yyyy-MM-dd") String date,
            @RequestParam(required = false) @JsonFormat(pattern = "HH:mm:ss") String time
    ){
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, appointmentService.listOfAppointments(pageNumber, pageSize, sortBy, sortOrder, doctorId, patientId, appointmentId, date, time));
    }

    @PostMapping("/time-slot")
    public ApiResponse<List<LocalTime>> getAppointmentTimeSlot(@RequestBody TimeSlotRequest request) {
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, appointmentService.getAppointedTimeSlot(request));
    }

}
