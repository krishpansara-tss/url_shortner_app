package com.tssconsultancy.url_sortner_app.mapper;

import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigResponseDto;
import com.tssconsultancy.url_sortner_app.entities.SystemConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SystemConfigMapper {
    SystemConfig toEntity(SystemConfigRequestDto dto);

    SystemConfigResponseDto toResponse(SystemConfig entity);
}

