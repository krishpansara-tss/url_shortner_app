package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigResponseDto;

public interface ISystemConfigService {
    SystemConfigResponseDto getSystemConfiguration();
    SystemConfigResponseDto updateSystemConfiguration(SystemConfigRequestDto dto);
    int getIntConfig(String key, int fallback);
    double getDoubleConfig(String key, double fallback);
}
