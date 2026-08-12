package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.constants.SystemConfigConstants;
import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlUpdateRequestDto;
import com.tssconsultancy.url_sortner_app.entities.SystemConfig;
import com.tssconsultancy.url_sortner_app.entities.Url;
import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;
import com.tssconsultancy.url_sortner_app.exceptions.derived.*;
import com.tssconsultancy.url_sortner_app.mapper.UrlMapper;
import com.tssconsultancy.url_sortner_app.repositories.SystemConfigRepository;
import com.tssconsultancy.url_sortner_app.repositories.UrlRepository;
import com.tssconsultancy.url_sortner_app.repositories.UserRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.IUrlService;
import com.tssconsultancy.url_sortner_app.utils.ShortAliasGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlServiceImp implements IUrlService {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final SystemConfigRepository systemConfigRepository;
    private final UrlMapper urlMapper;

    @Override
    @Transactional
    public UrlResponseDto createShortUrl(UrlRequestDto dto, Long userId) {
        if (dto == null || dto.getLongUrl() == null || dto.getLongUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("Long URL must not be blank");
        }

        String longUrl = normalizeUrl(dto.getLongUrl().trim());

        if (urlRepository.existsByLongUrl(longUrl)) {
            throw new LongUrlExistsException(longUrl);
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );
        if (user.getRemainingUrlSlots() != null && user.getRemainingUrlSlots() <= 0) {
            throw new IllegalStateException("User has exceeded their URL creation limit");
        }

        Integer visitLimit = getSystemConfigVisitLimit();

        String shortUrl;
        if (dto.getCustomAlias() != null && !dto.getCustomAlias().trim().isEmpty()) {
            String customAlias = dto.getCustomAlias().trim();
            if (urlRepository.existsByShortUrl(customAlias)) {
                throw new CustomAliasExistsException(customAlias);
            }
            shortUrl = customAlias;
        } else {
            shortUrl = generateUniqueShortAlias();
        }

        Url url = urlMapper.toEntity(dto);
        url.setLongUrl(longUrl);
        url.setShortUrl(shortUrl);
        url.setUser(user);
        url.setUrlStatus(UrlStatus.ACTIVE);
        url.setVisitLimit(visitLimit);
        url.setRemainingVisits(visitLimit);
        url.setTotalVisits(0);

        Url savedUrl = urlRepository.save(url);

        if (user.getRemainingUrlSlots() != null && user.getRemainingUrlSlots() > 0) {
            user.setRemainingUrlSlots(user.getRemainingUrlSlots() - 1);
            userRepository.save(user);
        }

        return urlMapper.toResponse(savedUrl);
    }

    @Override
    public UrlResponseDto getUrlByIdAndUserId(Long urlId, Long userId) {
        Url url = urlRepository.findByUrlIdAndUserUserId(urlId, userId).orElseThrow(
                () -> new ResourceNotFoundException("`URL not found or you do not have permission to access this URL.")
        );

        return urlMapper.toResponse(url);
    }

    @Override
    public UrlResponseDto updateUrl(Long urlId, Long userId, UrlUpdateRequestDto dto) {
        return null;
    }

    @Override
    public UrlResponseDto getUrlById(Long urlId) {
        return null;
    }

    @Override
    public PageResponse<UrlResponseDto> getAllUrlByUserId(Long userId, Pageable pageable) {
        Page<Url> urlPage = urlRepository.findAllByUserUserId(userId, pageable);

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
    public String resolveShortUrlAndRecordVisit(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl).orElseThrow(
                () -> new ShortUrlNotFoundException(shortUrl)
        );

        if(url.getRemainingVisits() <= 0){
            throw new LimitExceededException(shortUrl, url.getVisitLimit());
        }

        url.setRemainingVisits(url.getVisitLimit() - 1);
        url.setLastAccessedAt(LocalDateTime.now());
        url.setTotalVisits(url.getTotalVisits() + 1);

        urlRepository.save(url);

        return url.getLongUrl();
    }

    @Override
    public void deleteUrl(Long urlId, Long userId) {
        Url url = urlRepository.findByUrlIdAndUserUserId(urlId, userId).orElseThrow(
                ()-> new ResourceNotFoundException("`URL not found or you do not have permission to access this URL.")
        );

        url.setUrlStatus(UrlStatus.DELETED);
        url.setDeletedAt(LocalDateTime.now());

        urlRepository.save(url);
    }

    private Integer getSystemConfigVisitLimit() {
        Optional<SystemConfig> configOpt = systemConfigRepository.findByConfigKey(SystemConfigConstants.MAX_VISITS_PER_FREE_URL);
        if (configOpt.isEmpty()) {
            return SystemConfigConstants.FALLBACK_MAX_VISITS_PER_FREE_URL;
        }

        try {
            return Integer.parseInt(configOpt.get().getConfigValue());
        } catch (NumberFormatException e) {
            return SystemConfigConstants.FALLBACK_MAX_VISITS_PER_FREE_URL;
        }
    }


    private String generateUniqueShortAlias() {
        int maxAttempts = 10;
        for (int i = 0; i < maxAttempts; i++) {
            String alias = ShortAliasGenerator.generate();
            if (!urlRepository.existsByShortUrl(alias)) {
                return alias;
            }
        }
        throw new RuntimeException("Failed to generate unique short URL alias after " + maxAttempts + " attempts");
    }

    private String normalizeUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "https://" + url;
        }
        return url;
    }
}
