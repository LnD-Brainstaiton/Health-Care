package com.healthcare.userservice.domain.mapper;

import com.healthcare.userservice.domain.entity.Doctor;
import com.healthcare.userservice.domain.response.DoctorInfoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorInfoResponse toDoctorInfoResponse(Doctor doctor);
}
