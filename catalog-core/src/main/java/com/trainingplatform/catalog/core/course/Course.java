package com.trainingplatform.catalog.core.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * The course aggregate. Package-private on purpose: this JPA entity never leaves catalog-core.
 * Every value that crosses a module boundary does so as {@code CourseView} (a DTO), not as this class.
 */
@Entity
@Table(name = "courses")
class Course {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private CourseStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Course() {
        // for JPA
    }

    private Course(UUID id, String title, String city, int capacity, CourseStatus status, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.city = city;
        this.capacity = capacity;
        this.status = status;
        this.createdAt = createdAt;
    }

    static Course create(String title, String city, int capacity, Instant now) {
        return new Course(UUID.randomUUID(), title, city, capacity, CourseStatus.DRAFT, now);
    }

    /**
     * Marks the course as published. Idempotent: publishing an already-published course is a no-op
     * and returns {@code false} so the caller can avoid emitting a duplicate event.
     *
     * @return {@code true} if this call transitioned the course from draft to published
     */
    boolean publish() {
        if (status == CourseStatus.PUBLISHED) {
            return false;
        }
        this.status = CourseStatus.PUBLISHED;
        return true;
    }

    boolean isPublished() {
        return status == CourseStatus.PUBLISHED;
    }

    UUID getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getCity() {
        return city;
    }

    int getCapacity() {
        return capacity;
    }
}
