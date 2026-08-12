package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.dtos.auth.*;
import com.tssconsultancy.url_sortner_app.dtos.users.UserRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserResponseDto;

public interface IAuthService {
    UserResponseDto register(UserRequestDto requestDto);

    void verifyEmail(VerifyEmailRequestDto requestDto);

    LoginResponseDto login(LoginRequestDto requestDto);

    UserResponseDto getMe(Long userId);

    void logout(Long userId, String token);

    void logoutAll(Long userId);

    void forgotPassword(ForgotPasswordRequestDto requestDto);

    void resetPassword(ResetPasswordRequestDto requestDto);
}
