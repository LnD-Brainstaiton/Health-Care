package com.health.care.appointment.service.service;

import com.health.care.appointment.service.common.exceptions.InvalidRequestDataException;
import com.health.care.appointment.service.common.exceptions.RecordNotFoundException;
import com.health.care.appointment.service.common.utils.DateTimeUtils;
import com.health.care.appointment.service.common.utils.PageUtils;
import com.health.care.appointment.service.domain.entity.Appointment;
import com.health.care.appointment.service.domain.enums.ResponseMessage;
import com.health.care.appointment.service.domain.request.CreateAppointmentRequest;
import com.health.care.appointment.service.domain.request.PaginationRequest;
import com.health.care.appointment.service.domain.request.UpdateAppointmentRequest;
import com.health.care.appointment.service.domain.response.AppointmentResponse;
import com.health.care.appointment.service.domain.response.PaginationResponse;
import com.health.care.appointment.service.repository.AppointmentRepository;
import com.health_care.id.generator.Api.UniqueIdGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final UniqueIdGenerator uniqueIdGenerator;

    @Override
    public Void addAppointment(CreateAppointmentRequest request) {
        validateAppointmentRequest(request);

        Appointment appointment = new Appointment();
        appointment.setDoctorId(request.getDoctorId());
        appointment.setPatientId(request.getPatientId());
        appointment.setAppointmentNo(request.getAppointmentNo());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setPatientAge(request.getPatientAge());
        appointment.setPatientName(request.getPatientName());
        appointment.setPatientGender(request.getPatientGender());
        appointment.setPatientContactNo(request.getPatientContactNo());
        appointment.setFee(request.getFee());
        appointment.setReason(request.getReason());
        appointment.setIsPaymentDone(true);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setCreatedBy("System");

        appointmentRepository.save(appointment);
        return null;
    }

    @Override
    public Void updateAppointment(UpdateAppointmentRequest request) {
        validateUpdateRequest(request);

        Optional<Appointment> appointmentOpt = appointmentRepository.findById(request.getId());
        if(appointmentOpt.isEmpty()){
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);
        }

        Appointment appointment = appointmentOpt.get();
        appointment.setPatientName(request.getPatientName());
        appointment.setPatientContactNo(request.getPatientContactNo());
        appointment.setPatientGender(request.getPatientGender());
        appointment.setPatientAge(request.getPatientAge());

        appointmentRepository.save(appointment);
        return null;
    }

    @Override
    public PaginationResponse<AppointmentResponse> listOfAppointments(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String doctorId, String patientId, String date, String time) {

        final PaginationRequest paginationRequest = PageUtils.mapToPaginationRequest(pageNumber, pageSize, sortBy, sortOrder);
        final Pageable pageable = PageUtils.getPageable(paginationRequest);

        LocalDate convertedDate = LocalDate.of(1998,1,1);
        LocalTime convertedTime = LocalTime.MIDNIGHT;

        if(!StringUtils.isEmpty(time)){
            convertedDate = DateTimeUtils.convertToLocalDate(date, "yyyy-MM-dd");
            convertedTime = DateTimeUtils.convertToLocalTime(time, "HH:mm:ss");
        }

        final Page<AppointmentResponse> page = appointmentRepository.findByParam(doctorId, patientId, convertedDate, convertedTime, pageable).map(AppointmentResponse::from);

        return page.getContent().isEmpty() ?
                PageUtils.mapToPaginationResponseDto(Page.empty(), paginationRequest) :
                PageUtils.mapToPaginationResponseDto(page, paginationRequest);
    }


    private void validateAppointmentRequest(CreateAppointmentRequest request) {
        if(Objects.isNull(request) ||
                StringUtils.isBlank(request.getDoctorId()) ||
                StringUtils.isBlank(request.getPatientId()) ||
                StringUtils.isBlank(request.getPatientName()) ||
                StringUtils.isBlank(request.getPatientContactNo()) ||
                Objects.isNull(request.getPatientGender()) ||
                Objects.isNull(request.getFee()) ||
                Objects.isNull(request.getPatientAge()) ||
                Objects.isNull(request.getAppointmentDate()) ||
                Objects.isNull(request.getAppointmentTime())) {

            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
    }

    private void validateUpdateRequest(UpdateAppointmentRequest request) {
        if(Objects.isNull(request) ||
            Objects.isNull(request.getId()) ||
            StringUtils.isBlank(request.getPatientName()) ||
            StringUtils.isBlank(request.getPatientContactNo()) ||
            Objects.isNull(request.getPatientGender()) ||
            Objects.isNull(request.getPatientAge())){
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
    }
}