package com.trainingplatform.enrollments.core.enrollment;

import com.trainingplatform.catalog.api.CourseView;
import com.trainingplatform.catalog.api.CoursesApi;
import com.trainingplatform.common.error.ConflictException;
import com.trainingplatform.common.error.NotFoundException;
import com.trainingplatform.common.event.DomainEventPublisher;
import com.trainingplatform.enrollments.api.event.EnrollmentCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Application service for the enrollment subdomain. Home of the two business rules:
 * <ol>
 *   <li>the course must exist and be <b>published</b> — discovered through {@link CoursesApi}, the
 *       catalog domain's sync seam, never by reading catalog's tables;</li>
 *   <li>the course must not be at <b>capacity</b> — seats taken (owned here) vs. capacity (from the
 *       course view).</li>
 * </ol>
 * On success it publishes {@link EnrollmentCreatedEvent} through the {@link DomainEventPublisher}
 * seam, unaware of who (if anyone) consumes it.
 */
@Service
class EnrollmentService {

    private final EnrollmentRepository enrollments;
    private final CoursesApi courses;
    private final DomainEventPublisher events;
    private final Clock clock;

    EnrollmentService(EnrollmentRepository enrollments,
                      CoursesApi courses,
                      DomainEventPublisher events,
                      Clock clock) {
        this.enrollments = enrollments;
        this.courses = courses;
        this.events = events;
        this.clock = clock;
    }

    @Transactional
    Enrollment enroll(UUID courseId, String visitorName, String visitorEmail) {
        CourseView course = courses.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

        if (!course.published()) {
            throw new ConflictException("Course is not published: " + courseId);
        }

        long seatsTaken = enrollments.countByCourseId(courseId);
        if (seatsTaken >= course.capacity()) {
            throw new ConflictException("Course is at capacity: " + courseId);
        }

        Enrollment enrollment = enrollments.save(
                Enrollment.create(courseId, visitorName, visitorEmail, Instant.now(clock)));

        events.publish(new EnrollmentCreatedEvent(
                enrollment.getId(),
                enrollment.getCourseId(),
                enrollment.getVisitorName(),
                enrollment.getVisitorEmail(),
                Instant.now(clock)));

        return enrollment;
    }

    @Transactional(readOnly = true)
    List<Enrollment> listForCourse(UUID courseId) {
        return enrollments.findByCourseIdOrderByCreatedAtAsc(courseId);
    }
}
