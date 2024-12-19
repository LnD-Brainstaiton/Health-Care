package com.healthcare.userservice.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BloodGroupResponse {
    private List<String> bloodGroups;
}