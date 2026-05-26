package com.bajaj.bfhl.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request DTO for POST /bfhl endpoint.
 * Accepts a list of mixed strings (numbers, alphabets, special characters).
 */
public class BfhlRequest {

    @NotNull(message = "'data' field must not be null")
    @NotEmpty(message = "'data' field must not be empty")
    private List<String> data;

    public BfhlRequest() {}

    public BfhlRequest(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
