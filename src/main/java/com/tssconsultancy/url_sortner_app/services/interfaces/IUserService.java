package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.dtos.users.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto updateUser(Long id, UserUpdateRequestDto updateRequestDto);
    void deleteUser(Long id);

    UserResponseDto getCurrentUser(Long userId);
    UserResponseDto updateCurrentUser(Long userId, UserMeUpdateRequestDto updateDto);
    void changePassword(Long userId, PasswordChangeRequestDto requestDto);
    UserUsageResponseDto getUserUsage(Long userId);

    // Profile Picture Endpoints
    String uploadProfilePicture(Long userId, MultipartFile file);
    String getProfilePicture(Long userId);
    void deleteProfilePicture(Long userId);

    void activateUser(Long id);
}


