package com.notificationSystem.Notification.System.Dto;

import com.notificationSystem.Notification.System.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private NotificationType type;
    private String recipient;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private boolean sent;
}
