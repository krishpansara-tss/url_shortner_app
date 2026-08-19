package com.tssconsultancy.url_sortner_app.dtos.users;

import com.tssconsultancy.url_sortner_app.enums.UserStatus;
import com.tssconsultancy.url_sortner_app.enums.UserTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String name;
    private String email;
    private UserTypes role;
    private UserStatus status;
    private Integer remainingUrlSlots;
    private boolean isVerified;
    private String profilePicturePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
