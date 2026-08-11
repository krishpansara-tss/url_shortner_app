package com.tssconsultancy.url_sortner_app.dtos.urls;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class UrlRequestDto {
    private String longUrl;
    private String customAlias;
}
