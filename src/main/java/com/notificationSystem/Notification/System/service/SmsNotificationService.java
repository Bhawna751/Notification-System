package com.notificationSystem.Notification.System.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsNotificationService {
    public boolean sendSms(String phoneNumber, String message) {

        log.info("Sending SMS to {}", phoneNumber);
        log.info("SMS content: {}", message);

        return true;
    }
}
