package com.trainingplatform.catalog.core.course;

import java.util.UUID;

/**
 * Response body for the course endpoints. Maps from the internal {@link Course} entity so the
 * entity itself is never serialized over the wire.
 */
record CourseResponse(
        UUID id,
        String title,
        String city,
        int capacity,
        boolean published
) {
    static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getCity(),
                course.getCapacity(),
                course.isPublished());
    }
}
