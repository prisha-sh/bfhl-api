package com.bajaj.bfhl.controller;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import com.bajaj.bfhl.service.BfhlService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller exposing the BFHL data processing endpoint.
 *
 * <p>Routes:
 * <ul>
 *   <li>POST /bfhl — Process mixed data array and return classified results</li>
 * </ul>
 */
@RestController
@RequestMapping("/bfhl")
public class BfhlController {

    private static final Logger log = LoggerFactory.getLogger(BfhlController.class);

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * Accepts a JSON payload with a "data" array of mixed strings,
     * classifies each element, and returns enriched metadata.
     *
     * @param request validated request body containing the data list
     * @return HTTP 200 with a {@link BfhlResponse} on success
     */
    @PostMapping
    public ResponseEntity<BfhlResponse> process(@Valid @RequestBody BfhlRequest request) {
        log.info("POST /bfhl — received {} items", request.getData().size());
        BfhlResponse response = bfhlService.process(request);
        return ResponseEntity.ok(response);
    }
}
