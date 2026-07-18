package com.test.catalog.core.course;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Request body for {@code POST /api/v1/courses}. Kept next to the controller that uses it
 * (subdomain-first packaging), not in a shared {@code dto} bucket.
 */
record CreateCourseRequest(
        @NotBlank String title,
        @NotBlank String city,
        @Min(1) int capacity
) {
}
