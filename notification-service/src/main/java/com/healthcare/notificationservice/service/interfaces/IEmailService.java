package com.healthcare.notificationservice.service.interfaces;

import com.healthcare.notificationservice.presenter.rest.event.NotificationEvent;

public interface IEmailService {
    void sendHtmlEmail(NotificationEvent notificationEvent);
}
