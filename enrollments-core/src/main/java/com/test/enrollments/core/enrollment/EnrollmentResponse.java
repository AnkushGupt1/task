package com.test.enrollments.core.enrollment;

import java.time.Instant;
import java.util.UUID;

record EnrollmentResponse(
        UUID id,
        UUID courseId,
        String name,
        String email,
        Instant createdAt
) {
    static EnrollmentResponse from(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getCourseId(),
                enrollment.getVisitorName(),
                enrollment.getVisitorEmail(),
                enrollment.getCreatedAt());
    }
}
