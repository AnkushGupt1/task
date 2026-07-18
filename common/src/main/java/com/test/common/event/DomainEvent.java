package com.test.common.event;

/**
 * Marker for anything that can travel across a domain boundary as a notification.
 *
 * <p>Concrete events are immutable {@code record}s that live in the <b>publisher's</b> {@code -api}
 * module (e.g. {@code CoursePublishedEvent} in {@code catalog-api}). Consumers depend on that
 * {@code -api} module to receive them, never on the publisher's {@code -core}.
 */
public interface DomainEvent {
}
