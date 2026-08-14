package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.entities.Url;
import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import com.tssconsultancy.url_sortner_app.exceptions.base.InvalidOperationException;
import com.tssconsultancy.url_sortner_app.exceptions.derived.UrlNotFoundException;
import com.tssconsultancy.url_sortner_app.mapper.UrlMapper;
import com.tssconsultancy.url_sortner_app.repositories.UrlRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.IAdminUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUrlServiceImpl implements IAdminUrlService {

    private final UrlRepository urlRepository;
    private final UrlMapper urlMapper;

    @Override
    public PageResponse<UrlResponseDto> getAllUrls(Pageable pageable) {
        Page<Url> urlPage = urlRepository.findAll(pageable);

        List<UrlResponseDto> content = urlPage
                .getContent()
                .stream()
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
    public UrlResponseDto getUrlById(Long urlId) {
        Url url = urlRepository.findById(urlId).orElseThrow(
                () -> new UrlNotFoundException(urlId)
        );
        return urlMapper.toResponse(url);
    }

    @Override
    public void disableUrl(Long urlId) {
        Url url = urlRepository.findById(urlId).orElseThrow(
                () -> new UrlNotFoundException(urlId)
        );

        url.setUrlStatus(UrlStatus.DISABLED);

        urlRepository.save(url);
    }

    @Override
    public void activeUrl(Long urlId) {
        Url url = urlRepository.findById(urlId).orElseThrow(
                () -> new UrlNotFoundException(urlId)
        );

        if(LocalDateTime.now().isAfter(url.getExpiresAt())){
            throw new InvalidOperationException("You can't enable the expired url.");
        }

        url.setUrlStatus(UrlStatus.ACTIVE);

        urlRepository.save(url);
    }
}
