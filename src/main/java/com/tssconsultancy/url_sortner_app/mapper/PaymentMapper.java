package com.tssconsultancy.url_sortner_app.mapper;

import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.CustomUrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.entities.Payment;
import com.tssconsultancy.url_sortner_app.entities.Url;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "url.urlId", target = "urlId")
    PaymentResponseDto toResponse(Payment payment);
}
