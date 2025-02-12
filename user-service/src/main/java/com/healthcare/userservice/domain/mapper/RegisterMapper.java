package com.healthcare.userservice.domain.mapper;

import com.healthcare.userservice.domain.entity.User;
import com.healthcare.userservice.domain.enums.Role;
import com.healthcare.userservice.domain.response.RegisterResponse;
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
