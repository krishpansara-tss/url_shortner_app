package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.dtos.users.PasswordChangeRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserMeUpdateRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserUpdateRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserUsageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    UserResponseDto createUser(UserRequestDto requestDto);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto updateUser(Long id, UserUpdateRequestDto updateRequestDto);
    void deleteUser(Long id);
    void verifyUserEmail(Long userId, String otpCode);
    void resendEmailVerificationOtp(Long userId);

    UserResponseDto getCurrentUser(Long userId);
    UserResponseDto getUserByEmail(String email);
    UserResponseDto updateCurrentUser(Long userId, UserMeUpdateRequestDto updateDto);
    void changePassword(Long userId, PasswordChangeRequestDto requestDto);
    UserUsageResponseDto getUserUsage(Long userId);

    // Profile Picture Endpoints
    String uploadProfilePicture(Long userId, MultipartFile file);
    String getProfilePicture(Long userId);
    void deleteProfilePicture(Long userId);
}


