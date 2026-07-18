package com.trainingplatform.enrollments.core.enrollment;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enrollments")
class EnrollmentController {

    private final EnrollmentService enrollments;

    EnrollmentController(EnrollmentService enrollments) {
        this.enrollments = enrollments;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EnrollmentResponse enroll(@Valid @RequestBody CreateEnrollmentRequest request) {
        Enrollment enrollment = enrollments.enroll(request.courseId(), request.name(), request.email());
        return EnrollmentResponse.from(enrollment);
    }

    @GetMapping
    List<EnrollmentResponse> listForCourse(@RequestParam UUID courseId) {
        return enrollments.listForCourse(courseId).stream()
                .map(EnrollmentResponse::from)
                .toList();
    }
}
