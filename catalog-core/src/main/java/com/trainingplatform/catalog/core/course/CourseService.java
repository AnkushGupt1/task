package com.trainingplatform.catalog.core.course;

import com.trainingplatform.catalog.api.event.CoursePublishedEvent;
import com.trainingplatform.common.error.NotFoundException;
import com.trainingplatform.common.event.DomainEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

/**
 * Application service for the course subdomain. Owns the transaction boundary and the one
 * cross-domain reaction: publishing {@link CoursePublishedEvent} through the {@link DomainEventPublisher}
 * seam when (and only when) a course actually transitions to published.
 */
@Service
class CourseService {

    private final CourseRepository courses;
    private final DomainEventPublisher events;
    private final Clock clock;

    CourseService(CourseRepository courses, DomainEventPublisher events, Clock clock) {
        this.courses = courses;
        this.events = events;
        this.clock = clock;
    }

    @Transactional
    Course create(String title, String city, int capacity) {
        Course course = Course.create(title, city, capacity, Instant.now(clock));
        return courses.save(course);
    }

    @Transactional
    Course publish(UUID courseId) {
        Course course = courses.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

        if (course.publish()) {
            courses.save(course);
            events.publish(new CoursePublishedEvent(
                    course.getId(),
                    course.getTitle(),
                    course.getCity(),
                    course.getCapacity(),
                    Instant.now(clock)));
        }
        return course;
    }

    @Transactional(readOnly = true)
    Course getById(UUID courseId) {
        return courses.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));
    }
}
