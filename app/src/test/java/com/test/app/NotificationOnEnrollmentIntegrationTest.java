package com.test.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.notifications.api.NotificationsApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The brief's full happy path: publish course -> enroll -> notification produced.
 *
 * <p>The notification assertion goes through {@link NotificationsApi} (the notifications read seam),
 * so the test never touches the notifications entity or repository — exactly how a real consumer
 * would observe the outcome. This test lives entirely outside enrollments-core, underlining that
 * the notifications domain plugged into the existing enrollment event without modifying it.
 */
@SpringBootTest
@AutoConfigureMockMvc
class NotificationOnEnrollmentIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper json;

    @Autowired
    private NotificationsApi notifications;

    @Test
    void enrolling_produces_a_confirmation_notification() throws Exception {
        UUID courseId = createCourse("Event-Driven Java", "Amsterdam", 5);
        publish(courseId);

        String email = "notify-" + UUID.randomUUID() + "@example.com";
        assertThat(notifications.countForEmail(email)).isZero();

        mvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Grace Hopper","email":"%s","courseId":"%s"}
                                """.formatted(email, courseId)))
                .andExpect(status().isCreated());

        // One notification, over the one channel (EMAIL) configured today.
        assertThat(notifications.countForEmail(email)).isEqualTo(1L);
    }

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
                .andExpect(status().isOk());
    }
}
