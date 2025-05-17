package com.notificationSystem.Notification.System.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private String recipient;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private boolean sent;
    private int retryCount;
    private String failureReason;

}
