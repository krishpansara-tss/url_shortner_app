package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigUserResponseDto;

public interface ISystemConfigService {
    SystemConfigResponseDto getSystemConfiguration();
    SystemConfigUserResponseDto getSystemUserConfiguration();
    SystemConfigResponseDto updateSystemConfiguration(SystemConfigRequestDto dto);
    int getIntConfig(String key, int fallback);
    double getDoubleConfig(String key, double fallback);
}
