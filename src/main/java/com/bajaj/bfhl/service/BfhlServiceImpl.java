package com.bajaj.bfhl.service;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import com.bajaj.bfhl.exception.BfhlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for the BFHL data processing pipeline.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Classify each input token as: number (odd/even), alphabet, or special character</li>
 *   <li>Convert alphabets to uppercase</li>
 *   <li>Compute arithmetic sum of all numbers (returned as String)</li>
 *   <li>Build concat_string: alphabets → join → reverse → alternating caps (index 0 = UPPER)</li>
 * </ul>
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    private static final Logger log = LoggerFactory.getLogger(BfhlServiceImpl.class);

    // ── User Constants ────────────────────────────────────────────────────────
    private static final String USER_ID     = "prisha_sharma_03012005";
    private static final String EMAIL       = "prishas0301@gmail.com";
    private static final String ROLL_NUMBER = "0827CS231192";

    // ── Regex ─────────────────────────────────────────────────────────────────
    /** Matches any integer (including negative) */
    private static final String NUMBER_REGEX   = "-?\\d+";
    /** Matches one or more ASCII letters */
    private static final String ALPHABET_REGEX = "[a-zA-Z]+";

    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public BfhlResponse process(BfhlRequest request) {
        log.debug("Processing BFHL request with {} items", request.getData().size());

        List<String> oddNumbers   = new ArrayList<>();
        List<String> evenNumbers  = new ArrayList<>();
        List<String> alphabets    = new ArrayList<>();
        List<String> specialChars = new ArrayList<>();
        long         numericSum   = 0L;

        for (String token : request.getData()) {
            if (token == null || token.isEmpty()) {
                throw new BfhlException("Input list must not contain null or empty strings");
            }

            if (isNumber(token)) {
                long value = parseNumber(token);
                numericSum += value;

                if (value % 2 == 0) {
                    evenNumbers.add(token);   // keep original string
                } else {
                    oddNumbers.add(token);    // keep original string
                }

            } else if (isAlphabet(token)) {
                alphabets.add(token.toUpperCase());   // always uppercase

            } else {
                specialChars.add(token);              // keep as-is
            }
        }

        String concatString = buildConcatString(alphabets);
        log.debug("Classified → odd={}, even={}, alpha={}, special={}, sum={}, concat={}",
                oddNumbers, evenNumbers, alphabets, specialChars, numericSum, concatString);

        return BfhlResponse.builder()
                .isSuccess(true)
                .userId(USER_ID)
                .email(EMAIL)
                .rollNumber(ROLL_NUMBER)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialChars)
                .sum(String.valueOf(numericSum))
                .concatString(concatString)
                .build();
    }

    // ── Classification Helpers ────────────────────────────────────────────────

    private boolean isNumber(String token) {
        return token.matches(NUMBER_REGEX);
    }

    private boolean isAlphabet(String token) {
        return token.matches(ALPHABET_REGEX);
    }

    private long parseNumber(String token) {
        try {
            return Long.parseLong(token);
        } catch (NumberFormatException e) {
            throw new BfhlException("Number out of supported range: " + token);
        }
    }

    // ── concat_string Logic ───────────────────────────────────────────────────

    /**
     * Builds the concat_string from the (already-uppercased) alphabets list.
     *
     * <p>Algorithm:
     * <ol>
     *   <li>Join all alphabet strings</li>
     *   <li>Reverse the combined string</li>
     *   <li>Apply alternating case: index 0 → UPPER, index 1 → lower, …</li>
     * </ol>
     *
     * <p>Example: ["a","b","c","d","e"] → uppercased ["A","B","C","D","E"]
     * → joined "ABCDE" → reversed "EDCBA" → alternating "EdCbA"
     */
    private String buildConcatString(List<String> uppercasedAlphabets) {
        if (uppercasedAlphabets.isEmpty()) {
            return "";
        }

        // Step 1: join
        String joined = String.join("", uppercasedAlphabets);

        // Step 2: reverse
        String reversed = new StringBuilder(joined).reverse().toString();

        // Step 3: alternating caps (index 0 = UPPER)
        StringBuilder result = new StringBuilder(reversed.length());
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            result.append(i % 2 == 0
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c));
        }

        return result.toString();
    }
}
