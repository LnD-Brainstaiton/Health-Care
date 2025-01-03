package com.healthcare.userservice.domain.mapper;

import com.healthcare.userservice.domain.entity.Admin;
import com.healthcare.userservice.domain.response.AdminInfoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminInfoResponse toAdminInfoResponse(Admin admin);
}
