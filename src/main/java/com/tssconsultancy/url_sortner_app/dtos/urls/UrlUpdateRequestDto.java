package com.tssconsultancy.url_sortner_app.dtos.urls;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class UrlUpdateRequestDto {
    private Integer additionalVisitsRequested;
    private String paymentTransactionId;
//    private String paymentId;
}
