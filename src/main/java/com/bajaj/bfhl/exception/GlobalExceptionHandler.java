package com.bajaj.bfhl.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the BFHL API.
 * Intercepts all exceptions and returns consistent, structured error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ─── Custom BFHL Exception ────────────────────────────────────────────────

    @ExceptionHandler(BfhlException.class)
    public ResponseEntity<ErrorResponse> handleBfhlException(BfhlException ex) {
        log.error("BfhlException: {}", ex.getMessage());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErrorResponse(false, ex.getMessage()));
    }

    // ─── Bean Validation Failures ─────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        log.error("Validation failed: {}", ex.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse(false, "Validation failed", fieldErrors));
    }

    // ─── Malformed JSON Body ──────────────────────────────────────────────────

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex) {
        log.error("Malformed JSON request: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(false, "Malformed JSON or unreadable request body"));
    }

    // ─── Wrong Content-Type ───────────────────────────────────────────────────

    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(
            org.springframework.web.HttpMediaTypeNotSupportedException ex) {
        log.error("Unsupported media type: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErrorResponse(false, "Content-Type must be application/json"));
    }

    // ─── Method Not Allowed & Route Not Found ──────────────────────────────────

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            org.springframework.web.HttpRequestMethodNotSupportedException ex) {
        log.error("Method not supported: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse(false, "Method not allowed: " + ex.getMessage()));
    }

    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(
            org.springframework.web.servlet.NoHandlerFoundException ex) {
        log.error("No handler found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(false, "Resource not found: " + ex.getRequestURL()));
    }

    // ─── Catch-All ────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(false, "An unexpected error occurred: " + ex.getMessage()));
    }

    // ─── Response Types ───────────────────────────────────────────────────────

    public record ErrorResponse(
            @JsonProperty("is_success") boolean isSuccess,
            @JsonProperty("message")    String message
    ) {}

    public record ValidationErrorResponse(
            @JsonProperty("is_success") boolean isSuccess,
            @JsonProperty("message")    String message,
            @JsonProperty("errors")     Map<String, String> errors
    ) {}
}
