package com.terreneitors.backendclintec.auth.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terreneitors.backendclintec.auth.application.port.in.LoginUseCase;
import com.terreneitors.backendclintec.auth.application.port.in.RegisterUseCase;
import com.terreneitors.backendclintec.auth.infrastructure.dto.LoginRequest;
import com.terreneitors.backendclintec.auth.infrastructure.dto.RegisterRequest;
import com.terreneitors.backendclintec.auth.infrastructure.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Mock
    private LoginUseCase loginUseCase;

    @Mock
    private RegisterUseCase registerUseCase;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /api/auth/login - Success")
    void login_WithValidCredentials_ReturnsTokenResponse() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
        TokenResponse expectedResponse = new TokenResponse(
                1L,
                "testuser",
                "test@example.com",
                "jwt-token-example",
                List.of("ROLE_USER")
        );

        when(loginUseCase.login(loginRequest.email(), loginRequest.password()))
                .thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.token").value("jwt-token-example"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

        verify(loginUseCase, times(1)).login(loginRequest.email(), loginRequest.password());
    }

    @Test
    @DisplayName("POST /api/auth/login - Invalid Email Format")
    void login_WithInvalidEmail_ReturnsBadRequest() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("invalid-email", "password123");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(loginUseCase, never()).login(anyString(), anyString());
    }

    @Test
    @DisplayName("POST /api/auth/login - Empty Password")
    void login_WithEmptyPassword_ReturnsBadRequest() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(loginUseCase, never()).login(anyString(), anyString());
    }

    @Test
    @DisplayName("POST /api/auth/login - Missing Request Body")
    void login_WithMissingBody_ReturnsBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(loginUseCase, never()).login(anyString(), anyString());
    }

    @Test
    @DisplayName("POST /api/auth/register - Success")
    void register_WithValidData_ReturnsTokenResponseAndCreatedStatus() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest(
                "John Doe",
                "john.doe@example.com",
                "securePassword123"
        );
        TokenResponse expectedResponse = new TokenResponse(
                2L,
                "John Doe",
                "john.doe@example.com",
                "jwt-token-new-user",
                List.of("ROLE_USER")
        );

        when(registerUseCase.register(
                registerRequest.nombreUser(),
                registerRequest.email(),
                registerRequest.password()
        )).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.token").value("jwt-token-new-user"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

        verify(registerUseCase, times(1)).register(
                registerRequest.nombreUser(),
                registerRequest.email(),
                registerRequest.password()
        );
    }

    @Test
    @DisplayName("POST /api/auth/register - Invalid Email Format")
    void register_WithInvalidEmail_ReturnsBadRequest() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest(
                "John Doe",
                "invalid-email",
                "securePassword123"
        );

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(registerUseCase, never()).register(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("POST /api/auth/register - Empty Name")
    void register_WithEmptyName_ReturnsBadRequest() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest(
                "",
                "john.doe@example.com",
                "securePassword123"
        );

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(registerUseCase, never()).register(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("POST /api/auth/register - Weak Password")
    void register_WithWeakPassword_ReturnsBadRequest() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest(
                "John Doe",
                "john.doe@example.com",
                "123"
        );

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(registerUseCase, never()).register(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("POST /api/auth/register - Missing Request Body")
    void register_WithMissingBody_ReturnsBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(registerUseCase, never()).register(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("POST /api/auth/register - Null Fields")
    void register_WithNullFields_ReturnsBadRequest() throws Exception {
        // Given
        String requestBody = "{\"nombreUser\":null,\"email\":null,\"password\":null}";

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(registerUseCase, never()).register(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("POST /api/auth/login - Verify Use Case Interaction")
    void login_VerifyUseCaseInteraction() throws Exception {
        // Given
        String email = "user@test.com";
        String password = "myPassword";
        LoginRequest loginRequest = new LoginRequest(email, password);
        TokenResponse expectedResponse = new TokenResponse(
                1L,
                "user",
                email,
                "token",
                List.of("ROLE_USER")
        );

        when(loginUseCase.login(email, password)).thenReturn(expectedResponse);

        // When
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        // Then
        verify(loginUseCase, times(1)).login(email, password);
        verifyNoMoreInteractions(loginUseCase);
    }

    @Test
    @DisplayName("POST /api/auth/register - Verify Use Case Interaction")
    void register_VerifyUseCaseInteraction() throws Exception {
        // Given
        String nombre = "Test User";
        String email = "newuser@test.com";
        String password = "strongPassword";
        RegisterRequest registerRequest = new RegisterRequest(nombre, email, password);
        TokenResponse expectedResponse = new TokenResponse(
                3L,
                nombre,
                email,
                "new-token",
                List.of("ROLE_USER")
        );

        when(registerUseCase.register(nombre, email, password)).thenReturn(expectedResponse);

        // When
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        // Then
        verify(registerUseCase, times(1)).register(nombre, email, password);
        verifyNoMoreInteractions(registerUseCase);
    }
}