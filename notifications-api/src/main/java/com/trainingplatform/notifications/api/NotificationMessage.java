package com.trainingplatform.notifications.api;

import java.util.UUID;

/**
 * Channel-agnostic description of a notification to send. Built by the notifications domain from an
 * enrollment event and handed to each {@link NotificationChannel}. Immutable DTO.
 */
public record NotificationMessage(
        UUID enrollmentId,
        UUID courseId,
        String recipientName,
        String recipientEmail,
        String text
) {
}
