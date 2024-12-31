package com.health.care.appointment.service.domain.common;

import jakarta.ws.rs.sse.OutboundSseEvent;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> implements Serializable {
    private String responseCode;
    private String responseMessage;
    private T data;

    public ApiResponse(T data) {
        this.data = data;
    }


}