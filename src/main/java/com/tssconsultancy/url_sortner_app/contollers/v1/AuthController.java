package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.dtos.ResendVerificationOtpRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.auth.ForgotPasswordRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.auth.LoginRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.auth.LoginResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.auth.ResetPasswordRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.auth.VerifyEmailRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.MessageResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserRequestDto;
import com.tssconsultancy.url_sortner_app.exceptions.base.InvalidOperationException;
import com.tssconsultancy.url_sortner_app.security.UserPrincipal;
import com.tssconsultancy.url_sortner_app.services.interfaces.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@Valid @RequestBody UserRequestDto requestDto) {
        LoginResponseDto response = authService.register(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<MessageResponseDto> verifyEmail(@Valid @RequestBody VerifyEmailRequestDto requestDto) {
        authService.verifyEmail(requestDto);

        return ResponseEntity.ok(
                new MessageResponseDto(
                        "Email verified successfully"
                )
        );
    }

    @PostMapping("/resend-verification-otp")
    public ResponseEntity<MessageResponseDto> resendVerificationOtp(
            @Valid @RequestBody ResendVerificationOtpRequestDto requestDto
    ) {
        authService.resendVerificationOtp(requestDto.getEmail());

        return ResponseEntity.ok(
                new MessageResponseDto(
                        "Verification OTP sent successfully"
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto response = authService.login(requestDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDto> logout(@AuthenticationPrincipal UserPrincipal currentUser,
            @RequestHeader(value = "Authorization",required = false) String token)
    {
        if (currentUser == null) {
            throw new InvalidOperationException("User is not authenticated.");
        }
        if (token == null || token.isBlank()) {
            throw new InvalidOperationException("Authorization token is missing.");
        }

        Long userId = currentUser.getId();

        authService.logout(userId, token);

        return ResponseEntity.ok(
                new MessageResponseDto(
                        "Logged out successfully"
                )
        );
    }

    @PostMapping("/logout-all")
    public ResponseEntity<MessageResponseDto> logoutAll(
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {
        Long userId = currentUser != null
                ? currentUser.getId()
                : null;

        authService.logoutAll(userId);

        return ResponseEntity.ok(
                new MessageResponseDto(
                        "Logged out from all devices successfully"
                )
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponseDto> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDto requestDto
    ) {
        authService.forgotPassword(requestDto);

        return ResponseEntity.ok(
                new MessageResponseDto(
                        "Password reset OTP sent to your email"
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDto> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDto requestDto
    ) {
        authService.resetPassword(requestDto);

        return ResponseEntity.ok(
                new MessageResponseDto(
                        "Password reset successfully"
                )
        );
    }
}