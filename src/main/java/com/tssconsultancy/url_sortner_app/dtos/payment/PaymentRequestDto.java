package com.tssconsultancy.url_sortner_app.dtos.payment;

import com.tssconsultancy.url_sortner_app.enums.PaymentType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class PaymentRequestDto {
    private Long userId;
    private Long urlId;
    private PaymentType paymentType;
}
