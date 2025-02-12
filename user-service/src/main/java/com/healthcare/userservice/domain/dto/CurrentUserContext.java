package com.healthcare.userservice.domain.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserContext implements Serializable {
    private String userIdentity;
}

