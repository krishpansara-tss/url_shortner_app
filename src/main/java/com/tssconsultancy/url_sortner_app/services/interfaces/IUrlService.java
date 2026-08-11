package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.dtos.urls.UrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;

public interface IUrlService {
    UrlResponseDto createShortUrl(UrlRequestDto dto);
}
