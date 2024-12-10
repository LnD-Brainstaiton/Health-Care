package com.health_care.user_service.domain.mapper;

import com.health_care.user_service.domain.entity.Admin;
import com.health_care.user_service.domain.response.AdminInfoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminInfoResponse toAdminInfoResponse(Admin admin);
}
