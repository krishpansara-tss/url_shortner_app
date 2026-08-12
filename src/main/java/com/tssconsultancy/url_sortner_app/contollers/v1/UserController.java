package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.dtos.users.PasswordChangeRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserMeUpdateRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserUpdateRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserUsageResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserVerificationRequestDto;
import com.tssconsultancy.url_sortner_app.exceptions.InvalidRequestException;
import com.tssconsultancy.url_sortner_app.services.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final IUserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.createUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile(
            @RequestHeader(value = "X-User-Id", required = false) Long userIdHeader) {
        Long userId = requireUserId(userIdHeader);
        return ResponseEntity.ok(userService.getCurrentUser(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateMyProfile(
            @RequestHeader(value = "X-User-Id", required = false) Long userIdHeader,
            @Valid @RequestBody UserMeUpdateRequestDto updateDto) {
        Long userId = requireUserId(userIdHeader);
        return ResponseEntity.ok(userService.updateCurrentUser(userId, updateDto));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(
            @RequestHeader(value = "X-User-Id", required = false) Long userIdHeader,
            @Valid @RequestBody PasswordChangeRequestDto passwordChangeDto) {
        Long userId = requireUserId(userIdHeader);
        userService.changePassword(userId, passwordChangeDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/usage")
    public ResponseEntity<UserUsageResponseDto> getMyUsage(
            @RequestHeader(value = "X-User-Id", required = false) Long userIdHeader) {
        Long userId = requireUserId(userIdHeader);
        return ResponseEntity.ok(userService.getUserUsage(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDto updateRequestDto) {
        return ResponseEntity.ok(userService.updateUser(id, updateRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/verify-email")
    public ResponseEntity<Void> verifyEmail(@PathVariable Long id,
                                            @Valid @RequestBody UserVerificationRequestDto requestDto) {
        userService.verifyUserEmail(id, requestDto.getOtpCode());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/resend-email-verification")
    public ResponseEntity<Void> resendEmailVerification(@PathVariable Long id) {
        userService.resendEmailVerificationOtp(id);
        return ResponseEntity.noContent().build();
    }

    private Long requireUserId(Long userIdHeader) {
        if (userIdHeader == null) {
            throw new InvalidRequestException("Missing required header 'X-User-Id'. Please pass 'X-User-Id: <id>' in Postman headers.");
        }
        return userIdHeader;
    }
}
