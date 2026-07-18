package com.trainingplatform.notifications.core.notification;

import com.trainingplatform.notifications.api.NotificationMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Record of one notification that was sent, over one channel. Package-private; never leaves
 * notifications-core.
 */
@Entity
@Table(name = "notifications")
class Notification {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "enrollment_id", nullable = false, updatable = false)
    private UUID enrollmentId;

    @Column(name = "course_id", nullable = false, updatable = false)
    private UUID courseId;

    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(nullable = false, length = 32)
    private String channel;

    @Column(nullable = false, length = 1000)
    private String body;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Notification() {
        // for JPA
    }

    private Notification(UUID id, UUID enrollmentId, UUID courseId, String recipientEmail,
                         String channel, String body, Instant createdAt) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.courseId = courseId;
        this.recipientEmail = recipientEmail;
        this.channel = channel;
        this.body = body;
        this.createdAt = createdAt;
    }

    static Notification sent(NotificationMessage message, String channel, Instant now) {
        return new Notification(
                UUID.randomUUID(),
                message.enrollmentId(),
                message.courseId(),
                message.recipientEmail(),
                channel,
                message.text(),
                now);
    }
}
