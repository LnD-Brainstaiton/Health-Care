package com.healthcare.userservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverDto implements Serializable {

    private String receiverPhone;

    private String receiverEmail;

    private Map<String, String> templateData;
}
