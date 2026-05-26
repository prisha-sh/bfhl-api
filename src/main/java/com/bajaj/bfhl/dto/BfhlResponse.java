package com.bajaj.bfhl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response DTO for POST /bfhl endpoint.
 * Contains classified data along with user identity and derived fields.
 */
@JsonIgnoreProperties(value = {"success"})
public class BfhlResponse {

    @JsonProperty("is_success")
    private boolean isSuccess;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("roll_number")
    private String rollNumber;

    @JsonProperty("odd_numbers")
    private List<String> oddNumbers;

    @JsonProperty("even_numbers")
    private List<String> evenNumbers;

    @JsonProperty("alphabets")
    private List<String> alphabets;

    @JsonProperty("special_characters")
    private List<String> specialCharacters;

    @JsonProperty("sum")
    private String sum;

    @JsonProperty("concat_string")
    private String concatString;

    // ── Private constructor — use Builder ────────────────────────────────────
    private BfhlResponse() {}

    // ── Getters ───────────────────────────────────────────────────────────────

    public boolean isSuccess() { return isSuccess; }
    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getRollNumber() { return rollNumber; }
    public List<String> getOddNumbers() { return oddNumbers; }
    public List<String> getEvenNumbers() { return evenNumbers; }
    public List<String> getAlphabets() { return alphabets; }
    public List<String> getSpecialCharacters() { return specialCharacters; }
    public String getSum() { return sum; }
    public String getConcatString() { return concatString; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final BfhlResponse response = new BfhlResponse();

        public Builder isSuccess(boolean isSuccess) {
            response.isSuccess = isSuccess;
            return this;
        }
        public Builder userId(String userId) {
            response.userId = userId;
            return this;
        }
        public Builder email(String email) {
            response.email = email;
            return this;
        }
        public Builder rollNumber(String rollNumber) {
            response.rollNumber = rollNumber;
            return this;
        }
        public Builder oddNumbers(List<String> oddNumbers) {
            response.oddNumbers = oddNumbers;
            return this;
        }
        public Builder evenNumbers(List<String> evenNumbers) {
            response.evenNumbers = evenNumbers;
            return this;
        }
        public Builder alphabets(List<String> alphabets) {
            response.alphabets = alphabets;
            return this;
        }
        public Builder specialCharacters(List<String> specialCharacters) {
            response.specialCharacters = specialCharacters;
            return this;
        }
        public Builder sum(String sum) {
            response.sum = sum;
            return this;
        }
        public Builder concatString(String concatString) {
            response.concatString = concatString;
            return this;
        }
        public BfhlResponse build() {
            return response;
        }
    }
}
