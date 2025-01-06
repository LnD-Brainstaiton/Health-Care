package com.health_care.gateway.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class CurrentUserContext implements Serializable {
    private String userIdentity;
}
