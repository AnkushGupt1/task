package com.test.app.web;

/**
 * Minimal error body. The brief lists "pretty JSON errors" as a non-goal, so this stays intentionally
 * plain: a status code and a human-readable message.
 */
record ApiError(int status, String message) {
}
