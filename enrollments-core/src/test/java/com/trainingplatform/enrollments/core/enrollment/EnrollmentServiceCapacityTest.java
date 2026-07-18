package com.trainingplatform.enrollments.core.enrollment;

import com.trainingplatform.catalog.api.CourseView;
import com.trainingplatform.catalog.api.CoursesApi;
import com.trainingplatform.common.error.ConflictException;
import com.trainingplatform.common.error.NotFoundException;
import com.trainingplatform.common.event.DomainEvent;
import com.trainingplatform.common.event.DomainEventPublisher;
import com.trainingplatform.enrollments.api.event.EnrollmentCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the two enrollment business rules, in isolation from the web and database layers.
 * The capacity rule is exactly the kind of logic the brief says unit tests should cover, and where
 * they "earn their keep": all collaborators are mocked so each rule is pinned down precisely.
 */
class EnrollmentServiceCapacityTest {

    private static final UUID COURSE_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private EnrollmentRepository repository;
    private CoursesApi courses;
    private DomainEventPublisher events;
    private EnrollmentService service;

    @BeforeEach
    void setUp() {
        repository = mock(EnrollmentRepository.class);
        courses = mock(CoursesApi.class);
        events = mock(DomainEventPublisher.class);
        Clock clock = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
        service = new EnrollmentService(repository, courses, events, clock);
        when(repository.save(any(Enrollment.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void enrolls_when_seats_are_available() {
        givenCourse(publishedWithCapacity(2));
        when(repository.countByCourseId(COURSE_ID)).thenReturn(1L);

        Enrollment result = service.enroll(COURSE_ID, "Ada", "ada@example.com");

        assertThat(result.getCourseId()).isEqualTo(COURSE_ID);
        verify(repository).save(any(Enrollment.class));
        verify(events).publish(any(EnrollmentCreatedEvent.class));
    }

    @Test
    void rejects_when_course_is_at_capacity() {
        givenCourse(publishedWithCapacity(2));
        when(repository.countByCourseId(COURSE_ID)).thenReturn(2L); // seats taken == capacity

        assertThatThrownBy(() -> service.enroll(COURSE_ID, "Ada", "ada@example.com"))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("at capacity");

        verify(repository, never()).save(any());
        verify(events, never()).publish(any(DomainEvent.class));
    }

    @Test
    void rejects_when_course_is_not_published() {
        givenCourse(new CourseView(COURSE_ID, "Java 101", "Berlin", 10, false));

        assertThatThrownBy(() -> service.enroll(COURSE_ID, "Ada", "ada@example.com"))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("not published");

        verify(repository, never()).save(any());
        verify(events, never()).publish(any(DomainEvent.class));
    }

    @Test
    void rejects_when_course_does_not_exist() {
        when(courses.findById(COURSE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.enroll(COURSE_ID, "Ada", "ada@example.com"))
                .isInstanceOf(NotFoundException.class);

        verify(repository, never()).save(any());
        verify(events, never()).publish(any(DomainEvent.class));
    }

    private void givenCourse(CourseView view) {
        when(courses.findById(COURSE_ID)).thenReturn(Optional.of(view));
    }

    private static CourseView publishedWithCapacity(int capacity) {
        return new CourseView(COURSE_ID, "Java 101", "Berlin", capacity, true);
    }
}
