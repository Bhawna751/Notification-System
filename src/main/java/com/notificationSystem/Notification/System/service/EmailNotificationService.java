package com.notificationSystem.Notification.System.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotificationService {
    public boolean sendEmail(String recipient, String subject, String content) {
        log.info("Sending email to {} with subject: {}", recipient, subject);
        log.info("Email content: {}", content);

        return true;
    }
}
