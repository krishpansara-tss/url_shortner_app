package com.tssconsultancy.url_sortner_app.dtos.auth;

import com.tssconsultancy.url_sortner_app.enums.UserTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String token;
    private Long userId;
    private String name;
    private String email;
    private boolean verified;
    private UserTypes role;
}
