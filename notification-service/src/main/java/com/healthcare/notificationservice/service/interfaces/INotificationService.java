package com.healthcare.notificationservice.service.interfaces;

import com.healthcare.kafka.domain.EventWrapper;
import com.healthcare.notificationservice.domain.dto.ReceiverDto;
import com.healthcare.notificationservice.event.NotificationEvent;

import java.io.IOException;

public interface INotificationService {
    Boolean sendNotification(ReceiverDto receiverDto);

    void processDynamicNotification(EventWrapper<NotificationEvent> event) throws IOException;
}
