package com.test.common.error;

/**
 * A requested resource does not exist. Mapped to HTTP 404 by the app's exception handler.
 * Lives in {@code common} (JDK-only) so every domain throws the same semantic error without
 * coupling its service layer to a web framework.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
