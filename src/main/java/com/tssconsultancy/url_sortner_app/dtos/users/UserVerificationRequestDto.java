package com.tssconsultancy.url_sortner_app.dtos.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVerificationRequestDto {
    @NotBlank(message = "OTP code is required")
    private String otpCode;
}
