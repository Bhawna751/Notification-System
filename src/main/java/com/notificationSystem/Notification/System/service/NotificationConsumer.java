package com.notificationSystem.Notification.System.service;

import com.notificationSystem.Notification.System.NotificationException;
import com.notificationSystem.Notification.System.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;
    private final EmailNotificationService emailNotificationService;
    private final SmsNotificationService smsNotificationService;
    private final InAppNotificationService inAppNotificationService;
    private final NotificationProducer notificationProducer;

    private static final int MAX_RETRY_ATTEMPTS = 3;
    @Retryable(
            value = {NotificationException.class},
            maxAttempts = MAX_RETRY_ATTEMPTS,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void processEmailNotification(Notification notification) {
        log.info("Processing email notification: {}", notification);

        boolean success = emailNotificationService.sendEmail(
                notification.getRecipient(),
                notification.getTitle(),
                notification.getContent()
        );

        if (success) {
            notificationService.updateNotificationStatus(notification.getId(), true, null);
            log.info("Email notification sent successfully: {}", notification.getId());
        } else {
            throw new NotificationException("Failed to send email notification");
        }
    }
    @Retryable(
            value = {NotificationException.class},
            maxAttempts = MAX_RETRY_ATTEMPTS,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void processSmsNotification(Notification notification) {
        log.info("Processing SMS notification: {}", notification);

        boolean success = smsNotificationService.sendSms(
                notification.getRecipient(),
                notification.getContent()
        );

        if (success) {
            notificationService.updateNotificationStatus(notification.getId(), true, null);
            log.info("SMS notification sent successfully: {}", notification.getId());
        } else {
            throw new NotificationException("Failed to send SMS notification");
        }
    }
    @Retryable(
            value = {NotificationException.class},
            maxAttempts = MAX_RETRY_ATTEMPTS,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void processInAppNotification(Notification notification) {
        log.info("Processing in-app notification: {}", notification);

        boolean success = inAppNotificationService.sendInAppNotification(
                notification.getUserId(),
                notification.getTitle(),
                notification.getContent()
        );

        if (success) {
            notificationService.updateNotificationStatus(notification.getId(), true, null);
            log.info("In-app notification sent successfully: {}", notification.getId());
        } else {
            throw new NotificationException("Failed to send in-app notification");
        }
    }
    @Recover
    public void recoverEmailNotification(NotificationException e, Notification notification) {
        handleNotificationFailure(notification, e);
    }
    @Recover
    public void recoverSmsNotification(NotificationException e, Notification notification) {
        handleNotificationFailure(notification, e);
    }
    @Recover
    public void recoverInAppNotification(NotificationException e, Notification notification) {
        handleNotificationFailure(notification, e);
    }
    private void handleNotificationFailure(Notification notification, Exception e) {
        log.error("Failed to process notification after retries: {}, Error: {}", notification.getId(), e.getMessage());
        notificationService.incrementRetryCount(notification.getId());

        if (notification.getRetryCount() >= MAX_RETRY_ATTEMPTS) {
            notificationService.updateNotificationStatus(notification.getId(), false, e.getMessage());
            notificationProducer.sendNotificationToDeadLetterQueue(notification);
            log.info("Notification sent to dead letter queue: {}", notification.getId());
        } else {
            notificationProducer.sendNotification(notification);
            log.info("Notification requeued for another attempt: {}", notification.getId());
        }
    }
}