package com.health_care.user_service.domain.mapper;

import com.health_care.user_service.domain.entity.Patient;
import com.health_care.user_service.domain.response.PatientInfoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientInfoResponse toPatientInfoResponse(Patient patient);
}
