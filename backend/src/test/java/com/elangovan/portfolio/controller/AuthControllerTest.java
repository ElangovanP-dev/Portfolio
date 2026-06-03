package com.elangovan.portfolio.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the authentication flow: register, login, and protected endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_withValidCredentials_shouldReturnJwt() throws Exception {
        String loginJson = """
                {
                    "email": "admin@elangovan.dev",
                    "password": "Admin@2026"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.type").value("Bearer"))
                .andExpect(jsonPath("$.data.email").value("admin@elangovan.dev"));
    }

    @Test
    void login_withInvalidPassword_shouldReturn401() throws Exception {
        String loginJson = """
                {
                    "email": "admin@elangovan.dev",
                    "password": "wrongpassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_withExistingEmail_shouldReturn400() throws Exception {
        String registerJson = """
                {
                    "name": "Test",
                    "email": "admin@elangovan.dev",
                    "password": "Test@1234"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void protectedEndpoint_withValidJwt_shouldSucceed() throws Exception {
        // 1. Login to get JWT
        String loginJson = """
                {
                    "email": "admin@elangovan.dev",
                    "password": "Admin@2026"
                }
                """;

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        // 2. Extract token from response
        String response = loginResult.getResponse().getContentAsString();
        String token = com.fasterxml.jackson.databind.ObjectMapper
                .class.getDeclaredConstructor().newInstance()
                .readTree(response).get("data").get("token").asText();

        // 3. Create a project using the JWT
        String projectJson = """
                {
                    "title": "Test Project via API",
                    "description": "Created through authenticated endpoint",
                    "techStack": "Java, Spring Boot"
                }
                """;

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Project via API"));
    }
}
