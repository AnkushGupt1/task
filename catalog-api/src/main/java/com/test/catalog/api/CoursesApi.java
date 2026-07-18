package com.test.catalog.api;

import java.util.Optional;
import java.util.UUID;

/**
 * The synchronous seam into the catalog domain.
 *
 * <p>{@code enrollments-core} checks course state <b>only</b> through this interface — never by
 * touching catalog's services, entities, or tables. The single implementation today is an in-JVM
 * adapter over catalog's repository ({@code catalog-core}). Because the contract is expressed purely
 * in terms of {@link CourseView} (a plain DTO) and returns {@link Optional} rather than throwing,
 * the implementation could later become an HTTP client with <b>no change to the calling code</b>.
 */
public interface CoursesApi {

    /**
     * Look up a course by id.
     *
     * @return the course view, or {@link Optional#empty()} if no such course exists
     */
    Optional<CourseView> findById(UUID courseId);
}
