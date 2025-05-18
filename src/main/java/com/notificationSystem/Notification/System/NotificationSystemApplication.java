package com.notificationSystem.Notification.System;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@OpenAPIDefinition(
		info = @Info(
				title = "Notification Service API",
				version = "1.0",
				description = "API for sending Email, SMS, and In-App notifications"
		)
)
@EnableRetry
@SpringBootApplication
public class NotificationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationSystemApplication.class, args);
	}

}
