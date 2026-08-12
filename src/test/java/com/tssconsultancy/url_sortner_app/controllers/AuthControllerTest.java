package com.tssconsultancy.url_sortner_app.controllers;

import com.tssconsultancy.url_sortner_app.contollers.v1.AuthController;
import com.tssconsultancy.url_sortner_app.dtos.auth.*;
import com.tssconsultancy.url_sortner_app.dtos.users.UserRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserResponseDto;
import com.tssconsultancy.url_sortner_app.enums.UserStatus;
import com.tssconsultancy.url_sortner_app.enums.UserTypes;
import com.tssconsultancy.url_sortner_app.services.interfaces.IAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IAuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testRegister() throws Exception {
        String jsonContent = "{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"password\":\"secret123\"}";
        UserResponseDto responseDto = new UserResponseDto(
                1L, "John Doe", "john@example.com",
                UserTypes.USER, UserStatus.ACTIVE, 100, false, null,
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(authService.register(any(UserRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testVerifyEmail() throws Exception {
        String jsonContent = "{\"email\":\"john@example.com\",\"otpCode\":\"123456\"}";

        doNothing().when(authService).verifyEmail(any(VerifyEmailRequestDto.class));

        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email verified successfully"));
    }

    @Test
    void testLogin() throws Exception {
        String jsonContent = "{\"email\":\"john@example.com\",\"password\":\"secret123\"}";
        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .token("Bearer_token_123")
                .userId(1L)
                .name("John Doe")
                .email("john@example.com")
                .verified(true)
                .role(UserTypes.USER)
                .build();

        when(authService.login(any(LoginRequestDto.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("Bearer_token_123"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetMe() throws Exception {
        UserResponseDto responseDto = new UserResponseDto(
                1L, "John Doe", "john@example.com",
                UserTypes.USER, UserStatus.ACTIVE, 100, true, null,
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(authService.getMe(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("X-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testLogout() throws Exception {
        doNothing().when(authService).logout(eq(1L), any());

        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("X-User-Id", 1L)
                        .header("Authorization", "Bearer_123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully"));
    }

    @Test
    void testLogoutAll() throws Exception {
        doNothing().when(authService).logoutAll(1L);

        mockMvc.perform(post("/api/v1/auth/logout-all")
                        .header("X-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out from all devices successfully"));
    }

    @Test
    void testForgotPassword() throws Exception {
        String jsonContent = "{\"email\":\"john@example.com\"}";

        doNothing().when(authService).forgotPassword(any(ForgotPasswordRequestDto.class));

        mockMvc.perform(post("/api/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password reset OTP sent to your email"));
    }

    @Test
    void testResetPassword() throws Exception {
        String jsonContent = "{\"email\":\"john@example.com\",\"otpCode\":\"123456\",\"newPassword\":\"newPassword123\"}";

        doNothing().when(authService).resetPassword(any(ResetPasswordRequestDto.class));

        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password reset successfully"));
    }
}
