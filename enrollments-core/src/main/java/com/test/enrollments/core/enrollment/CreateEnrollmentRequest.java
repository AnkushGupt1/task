package com.test.enrollments.core.enrollment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request body for {@code POST /api/v1/enrollments}.
 */
record CreateEnrollmentRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull UUID courseId
) {
}
