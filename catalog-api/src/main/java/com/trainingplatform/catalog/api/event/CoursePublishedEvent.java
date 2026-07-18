package com.trainingplatform.catalog.api.event;

import com.trainingplatform.common.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * Emitted by the catalog domain when a course becomes published.
 *
 * <p>Lives in the <b>publisher's</b> {@code -api} module so any domain can subscribe by depending on
 * {@code catalog-api} — without depending on {@code catalog-core}. The payload is deliberately
 * self-contained (id, title, city, capacity) so a consumer never needs a follow-up call to react.
 */
public record CoursePublishedEvent(
        UUID courseId,
        String title,
        String city,
        int capacity,
        Instant occurredAt
) implements DomainEvent {
}
