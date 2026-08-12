package com.tssconsultancy.url_sortner_app.dtos.urls;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class CustomUrlRequestDto {
    private String longUrl;
    private String alias;
}
