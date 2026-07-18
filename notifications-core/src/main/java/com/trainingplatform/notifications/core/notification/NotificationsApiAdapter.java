package com.trainingplatform.notifications.core.notification;

import com.trainingplatform.notifications.api.NotificationsApi;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * In-JVM implementation of the notifications read seam. Keeps the entity and repository private to
 * this module while still letting callers ask "was a notification produced?".
 */
@Component
class NotificationsApiAdapter implements NotificationsApi {

    private final NotificationRepository notifications;

    NotificationsApiAdapter(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @Override
    @Transactional(readOnly = true)
    public long countForEmail(String recipientEmail) {
        return notifications.countByRecipientEmail(recipientEmail);
    }
}
