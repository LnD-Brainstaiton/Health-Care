package com.health_care.user_service.domain.mapper;

import com.health_care.user_service.domain.entity.User;
import com.health_care.user_service.domain.enums.Role;
import com.health_care.user_service.domain.response.RegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    @Mapping(source = "userType", target = "userType", qualifiedByName = "mapRoleToString")
    RegisterResponse toRegisterResponse(User user);
    @Named("mapRoleToString")
    default String mapRoleToString(Role role) {
        return role != null ? role.name() : "UNKNOWN";
    }
}
