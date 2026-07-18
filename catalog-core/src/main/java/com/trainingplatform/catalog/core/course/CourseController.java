package com.trainingplatform.catalog.core.course;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * HTTP surface of the course subdomain. Lives in the same package as its service and entity
 * (package-by-subdomain), so everything about a course is in one place.
 */
@RestController
@RequestMapping("/api/v1/courses")
class CourseController {

    private final CourseService courses;

    CourseController(CourseService courses) {
        this.courses = courses;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CourseResponse create(@Valid @RequestBody CreateCourseRequest request) {
        Course course = courses.create(request.title(), request.city(), request.capacity());
        return CourseResponse.from(course);
    }

    @PostMapping("/{id}/publish")
    CourseResponse publish(@PathVariable UUID id) {
        return CourseResponse.from(courses.publish(id));
    }

    @GetMapping("/{id}")
    ResponseEntity<CourseResponse> getOne(@PathVariable UUID id) {
        return ResponseEntity.ok(CourseResponse.from(courses.getById(id)));
    }
}
