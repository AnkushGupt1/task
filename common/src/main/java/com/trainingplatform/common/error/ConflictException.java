package com.trainingplatform.common.error;

/**
 * A request conflicts with the current state of a resource — e.g. enrolling in a course that is
 * not published or already at capacity. Mapped to HTTP 409 by the app's exception handler.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
