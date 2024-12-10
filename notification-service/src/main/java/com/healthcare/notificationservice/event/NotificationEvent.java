package com.healthcare.notificationservice.event;

import com.healthcare.notificationservice.domain.dto.ReceiverDto;
import com.healthcare.notificationservice.domain.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class NotificationEvent implements Serializable {
    static final String TYPE = "NOTIFICATION";

    private String notificationCode;
    private NotificationType notificationType;

    private List<ReceiverDto> receiverDtos;

}
