package com.trainingplatform.notifications.core.notification;

import com.trainingplatform.common.event.DomainEventHandler;
import com.trainingplatform.enrollments.api.event.EnrollmentCreatedEvent;
import com.trainingplatform.notifications.api.NotificationMessage;
import org.springframework.stereotype.Component;

/**
 * The entire seam by which notifications attaches to the rest of the system: it subscribes to
 * {@link EnrollmentCreatedEvent} (from enrollments-api) through the {@link DomainEventHandler} seam
 * (from common) and turns each event into a confirmation notification.
 *
 * <p>Because enrollments-core already publishes this event for anyone who cares, wiring this listener
 * required <b>no change to enrollments-core</b>.
 */
@Component
class EnrollmentCreatedListener implements DomainEventHandler<EnrollmentCreatedEvent> {

    private final NotificationService notifications;

    EnrollmentCreatedListener(NotificationService notifications) {
        this.notifications = notifications;
    }

    @Override
    public void handle(EnrollmentCreatedEvent event) {
        notifications.send(new NotificationMessage(
                event.enrollmentId(),
                event.courseId(),
                event.visitorName(),
                event.visitorEmail(),
                "Hi %s, your enrollment is confirmed.".formatted(event.visitorName())));
    }

    @Override
    public Class<EnrollmentCreatedEvent> eventType() {
        return EnrollmentCreatedEvent.class;
    }
}
