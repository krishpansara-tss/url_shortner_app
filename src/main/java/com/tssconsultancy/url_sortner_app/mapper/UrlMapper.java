package com.tssconsultancy.url_sortner_app.mapper;

import com.tssconsultancy.url_sortner_app.dtos.urls.UrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.entities.Url;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UrlMapper {
    Url toEntity(UrlRequestDto dto);

    UrlResponseDto toResponse(Url url);
}
