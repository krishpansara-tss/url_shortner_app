package com.tssconsultancy.url_sortner_app.controllers;

import com.tssconsultancy.url_sortner_app.contollers.v1.UserController;
import com.tssconsultancy.url_sortner_app.dtos.users.PasswordChangeRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserMeUpdateRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserUsageResponseDto;
import com.tssconsultancy.url_sortner_app.enums.UserStatus;
import com.tssconsultancy.url_sortner_app.enums.UserTypes;
import com.tssconsultancy.url_sortner_app.services.interfaces.IUserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetMyProfile() throws Exception {
        UserResponseDto responseDto = new UserResponseDto(
                1L, "Test User", "test@example.com",
                UserTypes.USER, UserStatus.ACTIVE, 100, true, null,
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(userService.getCurrentUser(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/users/me")
                        .header("X-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testUpdateMyProfile() throws Exception {
        String jsonContent = "{\"name\":\"Updated Name\",\"email\":\"updated@example.com\",\"profilePicturePath\":\"/pic.png\"}";
        UserResponseDto responseDto = new UserResponseDto(
                1L, "Updated Name", "updated@example.com",
                UserTypes.USER, UserStatus.ACTIVE, 100, true, "/pic.png",
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(userService.updateCurrentUser(eq(1L), any(UserMeUpdateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/users/me")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testChangeMyPassword() throws Exception {
        String jsonContent = "{\"oldPassword\":\"oldSecret123\",\"newPassword\":\"newSecret123\"}";

        doNothing().when(userService).changePassword(eq(1L), any(PasswordChangeRequestDto.class));

        mockMvc.perform(put("/api/v1/users/me/password")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMyUsage() throws Exception {
        UserUsageResponseDto usageDto = UserUsageResponseDto.builder()
                .userId(1L)
                .email("test@example.com")
                .remainingUrlSlots(100)
                .totalUrlsCreated(5L)
                .activeUrlsCount(4L)
                .totalUrlVisits(150L)
                .build();

        when(userService.getUserUsage(1L)).thenReturn(usageDto);

        mockMvc.perform(get("/api/v1/users/me/usage")
                        .header("X-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.totalUrlsCreated").value(5))
                .andExpect(jsonPath("$.activeUrlsCount").value(4))
                .andExpect(jsonPath("$.totalUrlVisits").value(150));
    }
}
