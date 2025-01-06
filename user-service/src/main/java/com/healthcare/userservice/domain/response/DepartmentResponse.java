package com.healthcare.userservice.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DepartmentResponse {
    private List<String> departments;
}
