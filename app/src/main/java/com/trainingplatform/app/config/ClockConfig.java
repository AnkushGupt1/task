package com.trainingplatform.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Provides a single {@link Clock} so domain services stamp timestamps through an injectable
 * dependency instead of calling {@code Instant.now()} statically — which keeps time-based behaviour
 * testable.
 */
@Configuration
class ClockConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
