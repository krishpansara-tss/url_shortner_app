package com.tssconsultancy.url_sortner_app.dtos.payment;

import com.tssconsultancy.url_sortner_app.enums.PaymentStatus;
import com.tssconsultancy.url_sortner_app.enums.PaymentType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public class PaymentResponseDto {
    private Long paymentId;
    private String transactionId;
    private Double amount;

    private PaymentType paymentType;
    private PaymentStatus paymentStatus;

    private Long userId;
    private Long urlId;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
