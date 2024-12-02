package com.health_care.user_service.domain.mapper;

import com.health_care.user_service.domain.entity.User;
import com.health_care.user_service.domain.response.RegisterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    RegisterResponse toRegisterResponse(User user);
}
