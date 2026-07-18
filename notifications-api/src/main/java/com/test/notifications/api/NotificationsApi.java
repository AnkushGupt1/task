package com.test.notifications.api;

/**
 * Small read seam over sent notifications. Exists so callers (today: the integration test) can
 * confirm a notification was produced without reaching into the notifications entity or repository.
 * Mirrors the api/core split used by the other domains.
 */
public interface NotificationsApi {

    /** How many notifications have been sent to the given recipient across all channels. */
    long countForEmail(String recipientEmail);
}
