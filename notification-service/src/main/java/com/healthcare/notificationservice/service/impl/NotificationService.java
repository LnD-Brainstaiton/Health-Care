package com.healthcare.notificationservice.service.impl;

import com.healthcare.kafka.domain.EventWrapper;
import com.healthcare.notificationservice.domain.dto.ReceiverDto;
import com.healthcare.notificationservice.event.NotificationEvent;
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
    public Boolean sendNotification(ReceiverDto receiverDto) {

        emailService.sendHtmlEmail("fariha.bs23@gmail.com","test","Test email","Fariha");
        return false;
    }

    @Override
    public void processDynamicNotification(EventWrapper<NotificationEvent> event) throws IOException {

    }
}
