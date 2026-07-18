package com.trainingplatform.notifications.core.notification.channel;

import com.trainingplatform.notifications.api.NotificationChannel;
import com.trainingplatform.notifications.api.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The only channel shipped today. "Delivery" is a log line — the brief says the mechanism is not the
 * point. A second channel (e.g. {@code SmsNotificationChannel}) is added by dropping a sibling class
 * next to this one; nothing else changes.
 */
@Component
class EmailNotificationChannel implements NotificationChannel {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationChannel.class);

    @Override
    public void send(NotificationMessage message) {
        log.info("[EMAIL] to={} ({}) :: {}",
                message.recipientEmail(), message.recipientName(), message.text());
    }

    @Override
    public String channelName() {
        return "EMAIL";
    }
}
