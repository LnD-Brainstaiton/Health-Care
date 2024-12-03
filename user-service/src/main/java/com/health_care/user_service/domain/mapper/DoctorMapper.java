package com.health_care.user_service.domain.mapper;

import com.health_care.user_service.domain.entity.Doctor;
import com.health_care.user_service.domain.response.DoctorInfoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorInfoResponse toDoctorInfoResponse(Doctor doctor);
}
