package com.trainingplatform.enrollments.core.enrollment;

import com.trainingplatform.catalog.api.event.CoursePublishedEvent;
import com.trainingplatform.common.event.DomainEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The one cross-domain reaction required by the brief: when a course is published, the enrollments
 * domain becomes aware of it. Here we simply "warm" by logging the payload — the effect is
 * deliberately trivial; the point is the <b>mechanism</b>.
 *
 * <p>Note what this class does NOT do: it does not call catalog, and it does not use Spring's
 * {@code @EventListener}. It implements the transport-agnostic {@link DomainEventHandler} seam. The
 * event arrives from catalog-api; the app module's dispatcher routes it here. Move the transport to
 * a broker tomorrow and this class is untouched.
 */
@Component
class CoursePublishedReaction implements DomainEventHandler<CoursePublishedEvent> {

    private static final Logger log = LoggerFactory.getLogger(CoursePublishedReaction.class);

    @Override
    public void handle(CoursePublishedEvent event) {
        log.info("Course published — warming enrollment capacity for course {} ('{}') with {} seats in {}",
                event.courseId(), event.title(), event.capacity(), event.city());
    }

    @Override
    public Class<CoursePublishedEvent> eventType() {
        return CoursePublishedEvent.class;
    }
}
