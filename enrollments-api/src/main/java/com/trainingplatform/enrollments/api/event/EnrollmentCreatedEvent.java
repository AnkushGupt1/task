package com.trainingplatform.enrollments.api.event;

import com.trainingplatform.common.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * Emitted by the enrollments domain whenever an enrollment is successfully created.
 *
 * <p>Enrollments-core publishes this unconditionally — it does not know or care who listens. That is
 * precisely what lets the notifications domain be bolted on later with <b>zero edits to
 * enrollments-core</b>: notifications simply subscribes to this already-published event.
 */
public record EnrollmentCreatedEvent(
        UUID enrollmentId,
        UUID courseId,
        String visitorName,
        String visitorEmail,
        Instant occurredAt
) implements DomainEvent {
}
