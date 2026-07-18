package com.trainingplatform.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The single entry point. It scans the whole {@code com.trainingplatform} tree so each domain's
 * components, entities, and repositories — which live in their own {@code -core} modules — are
 * discovered and wired together on one port.
 */
@SpringBootApplication(scanBasePackages = "com.trainingplatform")
@EntityScan("com.trainingplatform")
@EnableJpaRepositories("com.trainingplatform")
public class TrainingPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingPlatformApplication.class, args);
    }
}
