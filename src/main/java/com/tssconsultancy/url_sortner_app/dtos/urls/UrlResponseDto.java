package com.tssconsultancy.url_sortner_app.dtos.urls;

import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public class UrlResponseDto {
    private Long urlId;
    private String shortUrl;
    private String longUrl;
    private UrlStatus urlStatus;
    private Integer visitLimit;
    private Integer remainingVisits;
    private Integer totalVisits;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime lastAccessedAt;
}
