package com.trainingplatform.catalog.core.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Persistence for {@link Course}. Package-private and never referenced outside catalog-core.
 */
interface CourseRepository extends JpaRepository<Course, UUID> {
}
