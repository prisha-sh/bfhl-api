package com.bajaj.bfhl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST Controller exposing the health check endpoint.
 */
@RestController
public class HealthController {

    /**
     * Endpoint to verify service health status.
     * Matches GET /health as specified in the submission form.
     *
     * @return HTTP 200 with status "UP"
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> checkHealth() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
