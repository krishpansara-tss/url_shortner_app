package com.tssconsultancy.url_sortner_app.dtos.users;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMeUpdateRequestDto {
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    private String profilePicturePath;
}
