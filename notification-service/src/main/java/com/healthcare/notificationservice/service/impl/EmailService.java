package com.healthcare.notificationservice.service.impl;

import com.healthcare.notificationservice.common.exceptions.RecordNotFoundException;
import com.healthcare.notificationservice.domain.dto.ReceiverDto;
import com.healthcare.notificationservice.domain.entity.DynamicNotificationTemplate;
import com.healthcare.notificationservice.domain.enums.ResponseMessage;
import com.healthcare.notificationservice.presenter.rest.event.NotificationEvent;
import com.healthcare.notificationservice.repository.DynamicNotificationTemplateRepository;
import com.healthcare.notificationservice.service.interfaces.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    DynamicNotificationTemplateRepository dynamicNotificationTemplateRepository;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async
    @Override
    public void sendHtmlEmail(NotificationEvent notificationEvent) {

        ReceiverDto receiverDto = notificationEvent.getReceiverDto();

        DynamicNotificationTemplate dynamicNotificationTemplate = dynamicNotificationTemplateRepository.findByNotificationCode(notificationEvent.getNotificationCode());

        if (ObjectUtils.isEmpty(dynamicNotificationTemplate)) {
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);
        }

        try {
            String htmlContent = loadEmailTemplate(dynamicNotificationTemplate.getTemplateName().concat(".html"));
            htmlContent = mapTheValue(htmlContent, receiverDto.getTemplateData());

            sendEmail(receiverDto.getReceiverEmail(), dynamicNotificationTemplate.getTemplateSubject(), htmlContent);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }

    }

    private String loadEmailTemplate(String templateName) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/" + templateName);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String mapTheValue(String htmlContent, Map<String, String> templateData) {
        for (Map.Entry<String, String> entry : templateData.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue();
            htmlContent = htmlContent.replace(placeholder, value);
        }
        return htmlContent;
    }

    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        helper.setFrom(senderEmail);

        javaMailSender.send(message);

    }
}
