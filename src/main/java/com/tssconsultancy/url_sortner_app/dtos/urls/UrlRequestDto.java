package com.tssconsultancy.url_sortner_app.dtos.urls;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;

@RequiredArgsConstructor
@Data
public class UrlRequestDto {
    @NotBlank(message = "Long URL cannot be blank")
    @URL(message = "Please provide a valid URL (e.g., http://example.com)")
    private String longUrl;
}
