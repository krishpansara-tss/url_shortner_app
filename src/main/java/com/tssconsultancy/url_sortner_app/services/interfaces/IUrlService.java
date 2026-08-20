package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.CustomUrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlUpdateRequestDto;
import org.springframework.data.domain.Pageable;

public interface IUrlService {
    UrlResponseDto createShortUrl(UrlRequestDto dto, Long userId);
    UrlResponseDto getUrlByIdAndUserId(Long urlId, Long userId);
//    UrlResponseDto updateUrl(Long urlId, Long userId, UrlUpdateRequestDto dto);
    UrlResponseDto createCustomUrl(CustomUrlRequestDto dto, Long userId);
    PageResponse<UrlResponseDto> getAllUrlByUserId(Long userId, Pageable pageable);

    String resolveShortUrlAndRecordVisit(String shortUrl);
    void deleteUrl(Long urlId, Long userId);

    PaymentResponseDto urlRenew(Long urlId, Long userId);
    void activeUrl(Long urlId, Long userId);
}
