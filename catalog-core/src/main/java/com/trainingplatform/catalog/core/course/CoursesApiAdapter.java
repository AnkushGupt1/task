package com.trainingplatform.catalog.core.course;

import com.trainingplatform.catalog.api.CourseView;
import com.trainingplatform.catalog.api.CoursesApi;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * In-JVM implementation of the catalog sync seam ({@link CoursesApi}).
 *
 * <p>This is the ONLY class that bridges catalog's internals (the {@link CourseRepository} and the
 * {@link Course} entity) to the outside world's view of a course ({@link CourseView}). Callers such
 * as {@code enrollments-core} bind to the {@link CoursesApi} interface, so replacing this adapter
 * with an HTTP-client implementation later requires no change on the calling side.
 */
@Component
class CoursesApiAdapter implements CoursesApi {

    private final CourseRepository courses;

    CoursesApiAdapter(CourseRepository courses) {
        this.courses = courses;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseView> findById(UUID courseId) {
        return courses.findById(courseId).map(this::toView);
    }

    private CourseView toView(Course course) {
        return new CourseView(
                course.getId(),
                course.getTitle(),
                course.getCity(),
                course.getCapacity(),
                course.isPublished());
    }
}
