package com.bajaj.bfhl.exception;

/**
 * Custom application exception for BFHL API.
 * Thrown when business logic validation fails during request processing.
 */
public class BfhlException extends RuntimeException {

    private final int statusCode;

    public BfhlException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public BfhlException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
