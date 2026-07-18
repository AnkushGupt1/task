package com.trainingplatform.notifications.core.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface NotificationRepository extends JpaRepository<Notification, UUID> {

    long countByRecipientEmail(String recipientEmail);
}
