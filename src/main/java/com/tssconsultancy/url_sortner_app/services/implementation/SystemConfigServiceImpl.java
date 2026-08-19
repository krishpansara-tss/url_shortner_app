package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.constants.SystemConfigConstants;
import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigUserResponseDto;
import com.tssconsultancy.url_sortner_app.entities.SystemConfig;
import com.tssconsultancy.url_sortner_app.repositories.SystemConfigRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.ISystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements ISystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    @Override
    @Transactional(readOnly = true)
    public SystemConfigResponseDto getSystemConfiguration() {
        SystemConfigResponseDto response = new SystemConfigResponseDto();
        response.setMaxVisitsPerFreeUrl(getIntConfig(SystemConfigConstants.MAX_VISITS_PER_FREE_URL, SystemConfigConstants.FALLBACK_MAX_VISITS_PER_FREE_URL));
        response.setRenewalFee(getDoubleConfig(SystemConfigConstants.RENEWAL_FEE, SystemConfigConstants.FALLBACK_RENEWAL_FEE));
        response.setRenewalVisitsGranted(getIntConfig(SystemConfigConstants.RENEWAL_VISITS_GRANTED, SystemConfigConstants.FALLBACK_RENEWAL_VISITS_GRANTED));
        response.setFreeUrlQuotaPerUser(getIntConfig(SystemConfigConstants.FREE_URL_QUOTA_PER_USER, SystemConfigConstants.FALLBACK_FREE_URL_QUOTA_PER_USER));
        response.setPricePerAdditionalSlot(getDoubleConfig(SystemConfigConstants.PRICE_PER_ADDITIONAL_SLOT, SystemConfigConstants.FALLBACK_PRICE_PER_ADDITIONAL_SLOT));
        return response;
    }

    @Override
    public SystemConfigUserResponseDto getSystemUserConfiguration() {
        SystemConfigUserResponseDto response = new SystemConfigUserResponseDto();
        response.setMaxVisitsPerFreeUrl(getIntConfig(SystemConfigConstants.MAX_VISITS_PER_FREE_URL, SystemConfigConstants.FALLBACK_MAX_VISITS_PER_FREE_URL));
        response.setRenewalFee(getDoubleConfig(SystemConfigConstants.RENEWAL_FEE, SystemConfigConstants.FALLBACK_RENEWAL_FEE));
        response.setRenewalVisitsGranted(getIntConfig(SystemConfigConstants.RENEWAL_VISITS_GRANTED, SystemConfigConstants.FALLBACK_RENEWAL_VISITS_GRANTED));
        response.setFreeUrlQuotaPerUser(getIntConfig(SystemConfigConstants.FREE_URL_QUOTA_PER_USER, SystemConfigConstants.FALLBACK_FREE_URL_QUOTA_PER_USER));
        response.setPricePerAdditionalSlot(getDoubleConfig(SystemConfigConstants.PRICE_PER_ADDITIONAL_SLOT, SystemConfigConstants.FALLBACK_PRICE_PER_ADDITIONAL_SLOT));
        return response;
    }

    @Override
    @Transactional
    public SystemConfigResponseDto updateSystemConfiguration(SystemConfigRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("System configuration update payload cannot be null");
        }

        validateConfigurationDto(dto);

        if (dto.getMaxVisitsPerFreeUrl() != null) {
            updateOrSaveConfig(SystemConfigConstants.MAX_VISITS_PER_FREE_URL, dto.getMaxVisitsPerFreeUrl(), "Maximum visits allowed for free short URLs");
        }
        if (dto.getRenewalFee() != null) {
            updateOrSaveConfig(SystemConfigConstants.RENEWAL_FEE, dto.getRenewalFee(), "Fee charged for short URL renewal");
        }
        if (dto.getRenewalVisitsGranted() != null) {
            updateOrSaveConfig(SystemConfigConstants.RENEWAL_VISITS_GRANTED, dto.getRenewalVisitsGranted(), "Number of visits granted per renewal");
        }
        if (dto.getFreeUrlQuotaPerUser() != null) {
            updateOrSaveConfig(SystemConfigConstants.FREE_URL_QUOTA_PER_USER, dto.getFreeUrlQuotaPerUser(), "Free URL quota per user");
        }
        if (dto.getPricePerAdditionalSlot() != null) {
            updateOrSaveConfig(SystemConfigConstants.PRICE_PER_ADDITIONAL_SLOT, dto.getPricePerAdditionalSlot(), "Price per additional URL slot");
        }

        return getSystemConfiguration();
    }

    @Override
    @Transactional(readOnly = true)
    public int getIntConfig(String key, int fallback) {
        Optional<SystemConfig> configOpt = systemConfigRepository.findByConfigKey(key);
        if (configOpt.isPresent()) {
            try {
                return Integer.parseInt(configOpt.get().getConfigValue());
            } catch (NumberFormatException e) {
                return fallback;
            }
        }
        return fallback;
    }

    @Override
    @Transactional(readOnly = true)
    public double getDoubleConfig(String key, double fallback) {
        Optional<SystemConfig> configOpt = systemConfigRepository.findByConfigKey(key);
        if (configOpt.isPresent()) {
            try {
                return Double.parseDouble(configOpt.get().getConfigValue());
            } catch (NumberFormatException e) {
                return fallback;
            }
        }
        return fallback;
    }

    private void updateOrSaveConfig(String key, Object value, String description) {
        SystemConfig config = systemConfigRepository.findByConfigKey(key)
                .orElseGet(() -> {
                    SystemConfig newConfig = new SystemConfig();
                    newConfig.setConfigKey(key);
                    return newConfig;
                });

        config.setConfigValue(String.valueOf(value));
        config.setDescription(description);
        systemConfigRepository.save(config);
    }

    private void validateConfigurationDto(SystemConfigRequestDto dto) {
        if (dto.getMaxVisitsPerFreeUrl() != null && dto.getMaxVisitsPerFreeUrl() < 1) {
            throw new IllegalArgumentException("Max visits per free URL must be at least 1");
        }
        if (dto.getRenewalFee() != null && dto.getRenewalFee() < 0) {
            throw new IllegalArgumentException("Renewal fee cannot be negative");
        }
        if (dto.getRenewalVisitsGranted() != null && dto.getRenewalVisitsGranted() < 1) {
            throw new IllegalArgumentException("Renewal visits granted must be at least 1");
        }
        if (dto.getFreeUrlQuotaPerUser() != null && dto.getFreeUrlQuotaPerUser() < 0) {
            throw new IllegalArgumentException("Free URL quota per user cannot be negative");
        }
        if (dto.getPricePerAdditionalSlot() != null && dto.getPricePerAdditionalSlot() < 0) {
            throw new IllegalArgumentException("Price per additional slot cannot be negative");
        }
    }
}

