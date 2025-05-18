package com.notificationSystem.Notification.System.Dto;

import com.notificationSystem.Notification.System.model.NotificationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    @NotNull(message = "User Id is required")
    private Long userId;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Content is required")
    private String content;

    @NotNull(message = "Notification Type is required")
    private NotificationType type;

    @NotEmpty(message = "Recipient is required")
    private String recipient;
    
}
