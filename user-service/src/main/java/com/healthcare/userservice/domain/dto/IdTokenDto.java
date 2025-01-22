package com.healthcare.userservice.domain.dto;


import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@Builder(toBuilder = true)
public class IdTokenDto implements Serializable {

    private String userType;

    private String mobileNumber;

    private String userId;

    private String email;
}
