package com.test.enrollments.core.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    /** Current seat count for a course — the left-hand side of the capacity rule. */
    long countByCourseId(UUID courseId);

    List<Enrollment> findByCourseIdOrderByCreatedAtAsc(UUID courseId);
}
