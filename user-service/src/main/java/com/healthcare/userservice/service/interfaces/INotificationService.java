package com.healthcare.userservice.service.interfaces;

import com.healthcare.userservice.presenter.rest.event.NotificationEvent;

public interface INotificationService {
    Boolean sendNotification(NotificationEvent notificationEvent);
}
