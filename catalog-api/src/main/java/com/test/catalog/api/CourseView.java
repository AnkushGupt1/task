package com.test.catalog.api;

import java.util.UUID;

/**
 * Immutable read model of a course, as seen by other domains.
 *
 * <p>This is the <b>only</b> shape of a course that ever crosses the catalog boundary. The JPA
 * {@code Course} entity stays inside {@code catalog-core}; callers such as {@code enrollments-core}
 * see this DTO and nothing else.
 *
 * @param published whether the course accepts enrollments
 * @param capacity  maximum number of enrollments the course allows
 */
public record CourseView(
        UUID id,
        String title,
        String city,
        int capacity,
        boolean published
) {
}
