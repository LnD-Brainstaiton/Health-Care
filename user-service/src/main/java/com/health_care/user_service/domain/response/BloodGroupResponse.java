package com.health_care.user_service.domain.response;

import com.health_care.user_service.domain.enums.BloodGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BloodGroupResponse {
    private List<String> bloodGroups;
}
