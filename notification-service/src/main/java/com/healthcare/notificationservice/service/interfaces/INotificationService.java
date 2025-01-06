package com.healthcare.notificationservice.service.interfaces;

import com.healthcare.kafka.domain.EventWrapper;
import com.healthcare.notificationservice.presenter.rest.event.NotificationEvent;

import java.io.IOException;

public interface INotificationService {
    Boolean sendNotification(NotificationEvent notificationEvent);

    void processDynamicNotification(EventWrapper<NotificationEvent> event) throws IOException;
}
