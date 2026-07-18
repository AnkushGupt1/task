package com.trainingplatform.notifications.api;

/**
 * The extension point for delivery channels — the answer to "add SMS by adding a class, not editing
 * one".
 *
 * <p>The notifications domain sends through every {@code NotificationChannel} bean it can find. To
 * add a second channel (SMS next to email) you write one new class implementing this interface and
 * annotate it {@code @Component}; no existing class changes. This is a classic open/closed SPI.
 */
public interface NotificationChannel {

    /** Deliver the message over this channel. */
    void send(NotificationMessage message);

    /** Stable identifier of this channel (e.g. {@code "EMAIL"}), recorded with each sent notification. */
    String channelName();
}
