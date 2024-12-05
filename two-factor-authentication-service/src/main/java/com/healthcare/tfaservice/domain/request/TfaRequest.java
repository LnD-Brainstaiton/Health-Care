package com.healthcare.tfaservice.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TfaRequest implements Serializable {

    @NotBlank(message = "{username.notblank}")
    private String userName;

    private String tfaTypeCode;
}
