package com.notificationSystem.Notification.System.service;

import com.notificationSystem.Notification.System.Dto.NotificationRequest;
import com.notificationSystem.Notification.System.Dto.NotificationResponse;
import com.notificationSystem.Notification.System.model.Notification;
import com.notificationSystem.Notification.System.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmailNotificationService emailService;
    private final SmsNotificationService smsService;
    private final InAppNotificationService inAppService;
    public NotificationResponse sendNotification(NotificationRequest request) {
        log.info("Creating notification: {}", request);
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .recipient(request.getRecipient())
                .createdAt(LocalDateTime.now())
                .sent(false)
                .retryCount(0)
                .build();

        notification = notificationRepository.save(notification);

        // Process notification based on type
        boolean success = false;
        try {
            switch (notification.getType()) {
                case EMAIL:
                    success = emailService.sendEmail(
                            notification.getRecipient(),
                            notification.getTitle(),
                            notification.getContent()
                    );
                    break;
                case SMS:
                    success = smsService.sendSms(
                            notification.getRecipient(),
                            notification.getContent()
                    );
                    break;
                case IN_APP:
                    success = inAppService.sendInAppNotification(
                            notification.getUserId(),
                            notification.getTitle(),
                            notification.getContent()
                    );
                    break;
                default:
                    log.error("Unsupported notification type: {}", notification.getType());
            }

            // Update notification status
            notification.setSent(success);
            if (success) {
                notification.setSentAt(LocalDateTime.now());
                log.info("Notification sent successfully: {}", notification.getId());
            } else {
                notification.setFailureReason("Failed to send notification");
                log.error("Failed to send notification: {}", notification.getId());
            }

        } catch (Exception e) {
            log.error("Error sending notification: {}", e.getMessage());
            notification.setSent(false);
            notification.setFailureReason(e.getMessage());
        }

        // Save updated status
        notification = notificationRepository.save(notification);

        return mapToResponse(notification);
    }

    public List<NotificationResponse> getUserNotifications(Long userId) {
        log.info("Retrieving notifications for user: {}", userId);

        List<Notification> notifications = notificationRepository.findByUserId(userId);

        return notifications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .type(notification.getType())
                .recipient(notification.getRecipient())
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .sent(notification.isSent())
                .build();
    }

    public Notification updateNotificationStatus(Long notificationId, boolean sent, String failureReason) {
        log.info("Updating notification status: id={}, sent={}, failureReason={}",
                notificationId, sent, failureReason);

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        notification.setSent(sent);
        if (sent) {
            notification.setSentAt(LocalDateTime.now());
        }

        if (failureReason != null) {
            notification.setFailureReason(failureReason);
        }

        return notificationRepository.save(notification);
    }

    public Notification incrementRetryCount(Long notificationId) {
        log.info("Incrementing retry count for notification: {}", notificationId);

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        notification.setRetryCount(notification.getRetryCount() + 1);

        return notificationRepository.save(notification);
    }
}
