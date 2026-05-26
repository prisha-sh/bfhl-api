package com.bajaj.bfhl.service;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;

/**
 * Service contract for the BFHL data processing pipeline.
 */
public interface BfhlService {

    /**
     * Processes the incoming request data and returns a structured response.
     *
     * @param request the validated request containing the mixed data list
     * @return a fully populated {@link BfhlResponse}
     */
    BfhlResponse process(BfhlRequest request);
}
