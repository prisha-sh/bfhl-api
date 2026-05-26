package com.bajaj.bfhl.controller;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import com.bajaj.bfhl.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc slice tests for {@link BfhlController}.
 * Loads only the web layer; the service layer is mocked.
 */
@WebMvcTest({BfhlController.class, HealthController.class})
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BfhlService bfhlService;

    // ── Successful Request ────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /bfhl with valid input returns 200 and correct response structure")
    void shouldReturn200OnValidInput() throws Exception {
        // Arrange
        BfhlResponse mockResponse = BfhlResponse.builder()
                .isSuccess(true)
                .userId("prisha_sharma_03012005")
                .email("prishas0301@gmail.com")
                .rollNumber("0827CS231192")
                .oddNumbers(List.of("1"))
                .evenNumbers(List.of("334", "4"))
                .alphabets(List.of("A", "R"))
                .specialCharacters(List.of("$"))
                .sum("339")
                .concatString("Ra")
                .build();

        when(bfhlService.process(any(BfhlRequest.class))).thenReturn(mockResponse);

        String requestBody = """
                {
                    "data": ["a", "1", "334", "4", "R", "$"]
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").value("prisha_sharma_03012005"))
                .andExpect(jsonPath("$.email").value("prishas0301@gmail.com"))
                .andExpect(jsonPath("$.roll_number").value("0827CS231192"))
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.even_numbers").isArray())
                .andExpect(jsonPath("$.alphabets").isArray())
                .andExpect(jsonPath("$.special_characters[0]").value("$"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"));
    }

    // ── Validation Failures ───────────────────────────────────────────────────

    @Test
    @DisplayName("POST /bfhl with missing 'data' field returns 400")
    void shouldReturn400WhenDataFieldMissing() throws Exception {
        String requestBody = """
                {
                    "wrongField": ["a", "1"]
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl with empty 'data' array returns 400")
    void shouldReturn400WhenDataArrayIsEmpty() throws Exception {
        String requestBody = """
                {
                    "data": []
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl with null 'data' value returns 400")
    void shouldReturn400WhenDataIsNull() throws Exception {
        String requestBody = """
                {
                    "data": null
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl with malformed JSON returns 400")
    void shouldReturn400WhenJsonIsMalformed() throws Exception {
        String badJson = "{ not valid json }";

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // ── Content-Type ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /bfhl without Content-Type header returns 415 Unsupported Media Type")
    void shouldReturn415WithoutContentType() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .content("{\"data\":[\"a\"]}"))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    // ── GET Endpoints ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /bfhl returns 200 and standard operation code")
    void shouldReturn200OnGetBfhl() throws Exception {
        mockMvc.perform(get("/bfhl"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.operation_code").value(1));
    }

    @Test
    @DisplayName("GET /health returns 200 and UP status")
    void shouldReturn200OnGetHealth() throws Exception {
        mockMvc.perform(get("/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
