package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.dtos.users.PasswordChangeRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserMeUpdateRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserUsageResponseDto;
import com.tssconsultancy.url_sortner_app.security.UserPrincipal;
import com.tssconsultancy.url_sortner_app.services.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final IUserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Long userId = currentUser.getId();
        return ResponseEntity.ok(userService.getCurrentUser(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateMyProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody UserMeUpdateRequestDto updateDto) {

        Long userId = currentUser.getId();
        return ResponseEntity.ok(userService.updateCurrentUser(userId, updateDto));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody PasswordChangeRequestDto passwordChangeDto) {
        Long userId = currentUser.getId();
        userService.changePassword(userId, passwordChangeDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/usage")
    public ResponseEntity<UserUsageResponseDto> getMyUsage(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Long userId = currentUser.getId();
        return ResponseEntity.ok(userService.getUserUsage(userId));
    }

    @PostMapping("/me/profile-picture")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam("image") MultipartFile image) {

        Long userId = currentUser.getId();
        String imageUrl = userService.uploadProfilePicture(userId, image);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile picture uploaded successfully");
        response.put("imageUrl", imageUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<Map<String, String>> getProfilePicture(@AuthenticationPrincipal UserPrincipal currentUser) {
        Long userId = currentUser.getId();
        String imageUrl = userService.getProfilePicture(userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/profile-picture")
    public ResponseEntity<Void> deleteProfilePicture(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Long userId = currentUser.getId();
        userService.deleteProfilePicture(userId);
        return ResponseEntity.noContent().build();
    }
}
