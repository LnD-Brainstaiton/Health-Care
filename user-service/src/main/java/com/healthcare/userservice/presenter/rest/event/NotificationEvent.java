package com.healthcare.userservice.presenter.rest.event;


import com.healthcare.userservice.domain.dto.ReceiverDto;
import com.healthcare.userservice.domain.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class NotificationEvent implements Serializable {

    private String notificationCode;

    private NotificationType notificationType;

    private ReceiverDto receiverDto;

}
