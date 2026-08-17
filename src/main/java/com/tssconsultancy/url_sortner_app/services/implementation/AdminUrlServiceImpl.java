package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.entities.Url;
import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import com.tssconsultancy.url_sortner_app.exceptions.derived.UrlNotFoundException;
import com.tssconsultancy.url_sortner_app.mapper.UrlMapper;
import com.tssconsultancy.url_sortner_app.repositories.UrlRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.IAdminUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUrlServiceImpl implements IAdminUrlService {

    private final UrlRepository urlRepository;
    private final UrlMapper urlMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UrlResponseDto> getAllUrls(Pageable pageable) {
        Page<Url> urlPage;

        urlPage = urlRepository.findAll(pageable);

        List<UrlResponseDto> content = urlPage.getContent().stream()
                .map(urlMapper::toResponse)
                .toList();

        return PageResponse.<UrlResponseDto>builder()
                .content(content)
                .page(urlPage.getNumber())
                .size(urlPage.getSize())
                .totalElements(urlPage.getTotalElements())
                .totalPages(urlPage.getTotalPages())
                .last(urlPage.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UrlResponseDto getUrlById(Long urlId) {
        Url url = urlRepository.findById(urlId)
                .orElseThrow(() -> new UrlNotFoundException(urlId));

        return urlMapper.toResponse(url);
    }

    @Override
    @Transactional
    public UrlResponseDto updateUrlStatus(Long urlId, UrlStatus status) {
        Url url = urlRepository.findById(urlId)
                .orElseThrow(() -> new UrlNotFoundException(urlId));

        url.setUrlStatus(status);
        Url savedUrl = urlRepository.save(url);
        return urlMapper.toResponse(savedUrl);
    }
}
