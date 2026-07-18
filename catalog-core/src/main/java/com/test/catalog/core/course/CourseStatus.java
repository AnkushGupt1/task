package com.test.catalog.core.course;

/**
 * Lifecycle of a course. A course is created as {@link #DRAFT} and accepts enrollments only once
 * it becomes {@link #PUBLISHED}. Internal to catalog-core — other domains observe published-ness
 * through {@code CourseView.published()}, never this enum.
 */
enum CourseStatus {
    DRAFT,
    PUBLISHED
}
