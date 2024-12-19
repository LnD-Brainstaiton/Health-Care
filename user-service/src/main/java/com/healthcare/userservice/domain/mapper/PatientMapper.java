package com.healthcare.userservice.domain.mapper;

import com.healthcare.userservice.domain.entity.Patient;
import com.healthcare.userservice.domain.response.PatientInfoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientInfoResponse toPatientInfoResponse(Patient patient);
}
