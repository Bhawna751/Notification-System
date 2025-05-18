package com.notificationSystem.Notification.System.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InAppNotificationService {
    public boolean sendInAppNotification(Long userId, String title, String message) {
        log.info("Sending in-app notification to user {}", userId);
        log.info("Title: {}, Message: {}", title, message);
        return true;
    }
}
