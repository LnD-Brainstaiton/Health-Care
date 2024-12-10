package com.healthcare.notificationservice.service.impl;

import com.healthcare.notificationservice.service.interfaces.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SmsService implements ISmsService {


    public void sendSms(String phoneNumber) {
    }
}
