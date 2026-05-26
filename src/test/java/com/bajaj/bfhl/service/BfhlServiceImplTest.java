package com.bajaj.bfhl.service;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import com.bajaj.bfhl.exception.BfhlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link BfhlServiceImpl}.
 * No Spring context — pure POJO tests for fast feedback.
 */
class BfhlServiceImplTest {

    private BfhlServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BfhlServiceImpl();
    }

    // ── Identity Fields ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Response always contains correct user identity fields")
    void shouldReturnCorrectIdentityFields() {
        BfhlRequest request = new BfhlRequest(List.of("1"));
        BfhlResponse response = service.process(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getUserId()).isEqualTo("prisha_sharma_03012005");
        assertThat(response.getEmail()).isEqualTo("prishas0301@gmail.com");
        assertThat(response.getRollNumber()).isEqualTo("0827CS231192");
    }

    // ── Classification ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Mixed input: correctly classifies numbers, alphabets, and special chars")
    void shouldClassifyMixedInput() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("a", "1", "334", "4", "R", "$")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getOddNumbers()).containsExactly("1");
        assertThat(response.getEvenNumbers()).containsExactlyInAnyOrder("334", "4");
        assertThat(response.getAlphabets()).containsExactlyInAnyOrder("A", "R");
        assertThat(response.getSpecialCharacters()).containsExactly("$");
    }

    @Test
    @DisplayName("All-number input: no alphabets or special chars")
    void shouldHandleAllNumbers() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("2", "3", "10", "7")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getEvenNumbers()).containsExactlyInAnyOrder("2", "10");
        assertThat(response.getOddNumbers()).containsExactlyInAnyOrder("3", "7");
        assertThat(response.getAlphabets()).isEmpty();
        assertThat(response.getSpecialCharacters()).isEmpty();
    }

    @Test
    @DisplayName("All-alphabet input: no numbers or special chars")
    void shouldHandleAllAlphabets() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("a", "B", "c")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getAlphabets()).containsExactly("A", "B", "C");
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getEvenNumbers()).isEmpty();
        assertThat(response.getSpecialCharacters()).isEmpty();
    }

    @Test
    @DisplayName("Special characters only")
    void shouldHandleSpecialCharsOnly() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("$", "@", "#", "!")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getSpecialCharacters()).containsExactly("$", "@", "#", "!");
        assertThat(response.getAlphabets()).isEmpty();
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getEvenNumbers()).isEmpty();
    }

    @Test
    @DisplayName("Single item — a number")
    void shouldHandleSingleNumberInput() {
        BfhlRequest request = new BfhlRequest(List.of("5"));
        BfhlResponse response = service.process(request);

        assertThat(response.getOddNumbers()).containsExactly("5");
        assertThat(response.getSum()).isEqualTo("5");
    }

    // ── Alphabets Uppercase Conversion ────────────────────────────────────────

    @Test
    @DisplayName("Alphabets are always returned in uppercase")
    void alphabetsShouldBeUppercase() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("z", "m", "A", "q")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getAlphabets()).allMatch(s -> s.equals(s.toUpperCase()));
        assertThat(response.getAlphabets()).containsExactly("Z", "M", "A", "Q");
    }

    // ── Sum ───────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Sum of numeric tokens is returned as a string")
    void shouldComputeSumAsString() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("1", "334", "4")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getSum()).isEqualTo("339");
    }

    @Test
    @DisplayName("Sum is '0' when no numbers present")
    void sumShouldBeZeroWithNoNumbers() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("a", "$")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getSum()).isEqualTo("0");
    }

    @Test
    @DisplayName("Numbers remain as strings in response, not converted to integers")
    void numbersShouldRemainAsStrings() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("100", "201")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getEvenNumbers()).containsExactly("100");
        assertThat(response.getOddNumbers()).containsExactly("201");
    }

    // ── concat_string ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("concat_string: 5 chars → reversed → alternating caps")
    void concatStringShouldReverseAndAlternateCaps() {
        // alphabets: ["a","b","c","d","e"] → uppercased → joined "ABCDE"
        // reversed "EDCBA" → alternating "EdCbA"
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("a", "b", "c", "d", "e")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getConcatString()).isEqualTo("EdCbA");
    }

    @Test
    @DisplayName("concat_string: mixed input [a,1,334,4,R,$] → alphabets=[A,R] → reversed → alternating")
    void concatStringWithMixedInput() {
        // alphabets after uppercase: ["A","R"]
        // joined: "AR" → reversed: "RA" → alternating: "Ra"
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("a", "1", "334", "4", "R", "$")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getConcatString()).isEqualTo("Ra");
    }

    @Test
    @DisplayName("concat_string: single alphabet → itself (uppercase)")
    void concatStringSingleAlphabet() {
        BfhlRequest request = new BfhlRequest(List.of("x"));
        BfhlResponse response = service.process(request);

        assertThat(response.getConcatString()).isEqualTo("X");
    }

    @Test
    @DisplayName("concat_string: empty when no alphabets present")
    void concatStringShouldBeEmptyWhenNoAlphabets() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("1", "2", "$")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("concat_string: two chars → reversed → alternating caps")
    void concatStringTwoChars() {
        // ["a","z"] → uppercased ["A","Z"] → joined "AZ" → reversed "ZA" → alternating "Za"
        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "z"));
        BfhlResponse response = service.process(request);

        assertThat(response.getConcatString()).isEqualTo("Za");
    }

    // ── Edge Cases ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Negative numbers are classified correctly")
    void shouldHandleNegativeNumbers() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("-3", "-4", "5")
        );

        BfhlResponse response = service.process(request);

        assertThat(response.getOddNumbers()).containsExactlyInAnyOrder("-3", "5");
        assertThat(response.getEvenNumbers()).containsExactly("-4");
        assertThat(response.getSum()).isEqualTo("-2"); // -3 + -4 + 5 = -2
    }

    @Test
    @DisplayName("Throws BfhlException for null token inside data list")
    void shouldThrowExceptionForNullToken() {
        List<String> dataWithNull = new java.util.ArrayList<>();
        dataWithNull.add("a");
        dataWithNull.add(null);

        BfhlRequest request = new BfhlRequest(dataWithNull);

        assertThatThrownBy(() -> service.process(request))
                .isInstanceOf(BfhlException.class)
                .hasMessageContaining("null or empty");
    }

    @Test
    @DisplayName("Zero is classified as even")
    void zeroIsEven() {
        BfhlRequest request = new BfhlRequest(List.of("0"));
        BfhlResponse response = service.process(request);

        assertThat(response.getEvenNumbers()).containsExactly("0");
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getSum()).isEqualTo("0");
    }
}
