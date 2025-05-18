package com.notificationSystem.Notification.System.service;

import com.notificationSystem.Notification.System.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationProducer {
    public void sendNotification(Notification notification) {
        log.info("Sending notification to queue: {}", notification);
    }

    public void sendNotificationToDeadLetterQueue(Notification notification) {
        log.info("Sending notification to dead letter queue: {}", notification);
    }
}
