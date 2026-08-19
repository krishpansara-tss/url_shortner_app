package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import org.springframework.data.domain.Pageable;

public interface IAdminUrlService {
    PageResponse<UrlResponseDto> getAllUrls(Pageable pageable);
    UrlResponseDto getUrlById(Long urlId);
    UrlResponseDto updateUrlStatus(Long urlId, UrlStatus status);

}
