package com.notificationSystem.Notification.System.repository;

import com.notificationSystem.Notification.System.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository <Notification,Long>{
    List<Notification> findByUserId(Long userId);
}
