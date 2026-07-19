package com.test.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger UI metadata. springdoc discovers every {@code -core} module's controllers at
 * runtime from Spring MVC's handler mapping, so this composition-root config is the only place the
 * documentation concern lives — no domain module depends on springdoc.
 *
 * <ul>
 *   <li>Swagger UI:  {@code http://localhost:8080/swagger-ui.html}</li>
 *   <li>OpenAPI spec: {@code http://localhost:8080/v3/api-docs}</li>
 * </ul>
 */
@Configuration
class OpenApiConfig {

    @Bean
    OpenAPI trainingPlatformOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Training Platform API")
                .version("v1")
                .description("Catalog (courses) and Enrollments API for the take-home exercise. "
                        + "Publish a course, enroll a visitor, and watch the cross-domain "
                        + "notification fire."));
    }
}
