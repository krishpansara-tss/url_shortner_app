package com.tssconsultancy.url_sortner_app.dtos.urls;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;

@RequiredArgsConstructor
@Data
public class CustomUrlRequestDto {
    @NotBlank(message = "Long URL cannot be blank")
    @URL(message = "Please provide a valid URL (e.g., http://example.com)")
    private String longUrl;

    @NotBlank(message = "Alias cannot be blank")
    @Size(min = 3, max = 50, message = "Alias must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\-_]+$", message = "Alias can only contain letters, numbers, hyphens (-), and underscores (_)")
    private String alias;
}
