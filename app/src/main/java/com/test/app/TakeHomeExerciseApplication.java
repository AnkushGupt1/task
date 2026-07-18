package com.test.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The single entry point. It scans the whole {@code com.test} tree so each domain's
 * components, entities, and repositories — which live in their own {@code -core} modules — are
 * discovered and wired together on one port.
 */
@SpringBootApplication(scanBasePackages = "com.test")
@EntityScan("com.test")
@EnableJpaRepositories("com.test")
public class TakeHomeExerciseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TakeHomeExerciseApplication.class, args);
    }
}
