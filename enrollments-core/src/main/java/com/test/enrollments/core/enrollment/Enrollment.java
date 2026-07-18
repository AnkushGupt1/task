package com.test.enrollments.core.enrollment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * An enrollment of a visitor into a course.
 *
 * <p>{@code course_id} is stored as a plain UUID with no database foreign key to the catalog's
 * {@code courses} table: the enrollments domain must not couple to catalog's schema (in a real
 * deployment these could be separate databases). The relationship is validated at the application
 * layer via {@code CoursesApi}, not by the database.
 */
@Entity
@Table(name = "enrollments")
class Enrollment {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "course_id", nullable = false, updatable = false)
    private UUID courseId;

    @Column(name = "visitor_name", nullable = false)
    private String visitorName;

    @Column(name = "visitor_email", nullable = false)
    private String visitorEmail;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Enrollment() {
        // for JPA
    }

    private Enrollment(UUID id, UUID courseId, String visitorName, String visitorEmail, Instant createdAt) {
        this.id = id;
        this.courseId = courseId;
        this.visitorName = visitorName;
        this.visitorEmail = visitorEmail;
        this.createdAt = createdAt;
    }

    static Enrollment create(UUID courseId, String visitorName, String visitorEmail, Instant now) {
        return new Enrollment(UUID.randomUUID(), courseId, visitorName, visitorEmail, now);
    }

    UUID getId() {
        return id;
    }

    UUID getCourseId() {
        return courseId;
    }

    String getVisitorName() {
        return visitorName;
    }

    String getVisitorEmail() {
        return visitorEmail;
    }

    Instant getCreatedAt() {
        return createdAt;
    }
}
