package com.healthcare.kafka.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventWrapper<T> implements Serializable {

    private String eventId;

    private String correlationId;

    private String userContext;

    private LocalDateTime eventDate;

    private T data;
}