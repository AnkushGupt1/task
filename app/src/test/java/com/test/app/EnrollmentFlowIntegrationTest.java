package com.test.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end test of the cross-domain flow through the real HTTP layer and a real (in-memory)
 * database with Flyway migrations applied. Exercises the happy path — create course, publish, enroll,
 * list — and the two rejection rules. The notification side of the flow is asserted separately in
 * {@code NotificationOnEnrollmentIntegrationTest}, added with the notifications domain.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentFlowIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper json;

    @Test
    void publish_then_enroll_then_list_happy_path() throws Exception {
        UUID courseId = createCourse("Java 101", "Berlin", 2);
        publish(courseId);

        enroll(courseId, "Ada Lovelace", "ada@example.com")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.courseId").value(courseId.toString()));

        enroll(courseId, "Alan Turing", "alan@example.com")
                .andExpect(status().isCreated());

        mvc.perform(get("/api/v1/enrollments").param("courseId", courseId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void enrollment_is_rejected_when_course_is_not_published() throws Exception {
        UUID courseId = createCourse("Draft Course", "Munich", 5);
        // deliberately not published

        enroll(courseId, "Grace Hopper", "grace@example.com")
                .andExpect(status().isConflict());
    }

    @Test
    void enrollment_is_rejected_when_course_is_at_capacity() throws Exception {
        UUID courseId = createCourse("Tiny Course", "Hamburg", 1);
        publish(courseId);

        enroll(courseId, "First Visitor", "first@example.com")
                .andExpect(status().isCreated());
        enroll(courseId, "Second Visitor", "second@example.com")
                .andExpect(status().isConflict());
    }

    @Test
    void enrollment_is_rejected_when_course_does_not_exist() throws Exception {
        enroll(UUID.randomUUID(), "Nobody", "nobody@example.com")
                .andExpect(status().isNotFound());
    }

    // --- helpers -------------------------------------------------------------

    private UUID createCourse(String title, String city, int capacity) throws Exception {
        MvcResult result = mvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"%s","city":"%s","capacity":%d}
                                """.formatted(title, city, capacity)))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode body = json.readTree(result.getResponse().getContentAsString());
        return UUID.fromString(body.get("id").asText());
    }

    private void publish(UUID courseId) throws Exception {
        mvc.perform(post("/api/v1/courses/{id}/publish", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.published").value(true));
    }

    private org.springframework.test.web.servlet.ResultActions enroll(UUID courseId, String name, String email)
            throws Exception {
        return mvc.perform(post("/api/v1/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"%s","email":"%s","courseId":"%s"}
                        """.formatted(name, email, courseId)));
    }
}
