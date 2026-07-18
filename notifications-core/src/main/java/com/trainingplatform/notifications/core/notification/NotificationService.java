package com.trainingplatform.notifications.core.notification;

import com.trainingplatform.notifications.api.NotificationChannel;
import com.trainingplatform.notifications.api.NotificationMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

/**
 * Sends a notification over every configured {@link NotificationChannel} and records each send.
 *
 * <p>The channel list is injected by Spring. Adding a channel (e.g. SMS) is purely additive: a new
 * {@code @Component} implementing {@link NotificationChannel} appears in this list automatically —
 * this class does not change.
 */
@Service
class NotificationService {

    private final List<NotificationChannel> channels;
    private final NotificationRepository notifications;
    private final Clock clock;

    NotificationService(List<NotificationChannel> channels,
                        NotificationRepository notifications,
                        Clock clock) {
        this.channels = channels;
        this.notifications = notifications;
        this.clock = clock;
    }

    @Transactional
    void send(NotificationMessage message) {
        Instant now = Instant.now(clock);
        for (NotificationChannel channel : channels) {
            channel.send(message);
            notifications.save(Notification.sent(message, channel.channelName(), now));
        }
    }
}
