package com.healthcare.notificationservice.service.impl;

import com.healthcare.kafka.domain.EventWrapper;
import com.healthcare.notificationservice.domain.enums.NotificationType;
import com.healthcare.notificationservice.presenter.rest.event.NotificationEvent;
import com.healthcare.notificationservice.service.interfaces.IEmailService;
import com.healthcare.notificationservice.service.interfaces.INotificationService;
import com.healthcare.notificationservice.service.interfaces.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    ISmsService smsService;

    @Autowired
    IEmailService emailService;

    @Override
    public Boolean sendNotification(NotificationEvent notificationEvent) {
        if (NotificationType.SMS.equals(notificationEvent.getNotificationType())) {
            smsService.sendSms(notificationEvent.getReceiverDto().getReceiverPhone());
        } else if (NotificationType.EMAIL.equals(notificationEvent.getNotificationType())) {
            emailService.sendHtmlEmail(notificationEvent);
        }
        return true;
    }

    @Override
    public void processDynamicNotification(EventWrapper<NotificationEvent> event) throws IOException {
        NotificationEvent notificationEvent = event.getData();
        if (NotificationType.SMS.equals(notificationEvent.getNotificationType())) {
            smsService.sendSms(notificationEvent.getReceiverDto().getReceiverPhone());
        } else if (NotificationType.EMAIL.equals(notificationEvent.getNotificationType())) {
            emailService.sendHtmlEmail(notificationEvent);
        }
    }
}
