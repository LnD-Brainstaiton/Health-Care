package com.healthcare.notificationservice.service.interfaces;

public interface IEmailService {
    void sendHtmlEmail(String to, String subject, String title, String name);
}
