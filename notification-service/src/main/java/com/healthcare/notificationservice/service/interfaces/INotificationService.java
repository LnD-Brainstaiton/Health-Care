package com.healthcare.notificationservice.service.interfaces;

import com.healthcare.notificationservice.domain.dto.ReceiverDto;

public interface INotificationService {
    Boolean sendNotification(ReceiverDto receiverDto);
}
