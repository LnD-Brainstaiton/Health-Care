package com.healthcare.notificationservice.service.impl;

import com.healthcare.notificationservice.service.interfaces.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    @Override
    public void sendHtmlEmail(String to, String subject, String title, String name) {
        try {
            // Load the HTML template
            String htmlContent = loadEmailTemplate("test.html");

            // Replace placeholders with actual values
            htmlContent = htmlContent.replace("{{title}}", title);
            htmlContent = htmlContent.replace("{{name}}", name);

            // Send email
            sendEmail(to, subject, htmlContent);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

    private String loadEmailTemplate(String templateName) throws IOException {
        // Load the HTML template from resources
        ClassPathResource resource = new ClassPathResource("templates/" + templateName);
        return new String(Files.readAllBytes(Paths.get(resource.getURI())));
    }

    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);  // Set the HTML content

        helper.setFrom("your-email@gmail.com"); // Your email address

        javaMailSender.send(message);

        System.out.println("Email sent successfully!");
    }
}
