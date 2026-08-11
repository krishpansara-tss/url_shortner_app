package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.constants.SystemConfigConstants;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.entities.SystemConfig;
import com.tssconsultancy.url_sortner_app.entities.Url;
import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;
import com.tssconsultancy.url_sortner_app.exceptions.base.UnauthorizedException;
import com.tssconsultancy.url_sortner_app.exceptions.derived.CustomAliasExistsException;
import com.tssconsultancy.url_sortner_app.exceptions.derived.LongUrlExistsException;
import com.tssconsultancy.url_sortner_app.mapper.UrlMapper;
import com.tssconsultancy.url_sortner_app.repositories.SystemConfigRepository;
import com.tssconsultancy.url_sortner_app.repositories.UrlRepository;
import com.tssconsultancy.url_sortner_app.repositories.UserRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.IUrlService;
import com.tssconsultancy.url_sortner_app.utils.ShortAliasGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UrlResponseDto createShortUrl(UrlRequestDto dto) {
        if (dto == null || dto.getLongUrl() == null || dto.getLongUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("Long URL must not be blank");
        }

        String longUrl = normalizeUrl(dto.getLongUrl().trim());

        if (urlRepository.existsByLongUrl(longUrl)) {
            throw new LongUrlExistsException(longUrl);
        }

        User user = getAuthenticatedUser();
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

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated");
        }

        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private Integer getSystemConfigVisitLimit() {
        Optional<SystemConfig> configOpt = systemConfigRepository.findByConfigKey(SystemConfigConstants.DEFAULT_VISIT_LIMIT_KEY);
        if(configOpt.isEmpty()) {
            return SystemConfigConstants.FALLBACK_DEFAULT_VISIT_LIMIT;
        }

        try {
            return Integer.parseInt(configOpt.get().getConfigValue());
        } catch (NumberFormatException e) {
            return SystemConfigConstants.FALLBACK_DEFAULT_VISIT_LIMIT;
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
