package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.enums.PaymentType;
import org.springframework.data.domain.Pageable;

public interface IPaymentService {
    PaymentResponseDto initiatePayment(Long userId, Long urlId, PaymentType paymentType);
    PaymentResponseDto cancelPayment(Long paymentId, Long userId);
    PaymentResponseDto processPayment(Long paymentId);
    PaymentResponseDto getPaymentById(Long paymentId, Long userId);
    PageResponse<PaymentResponseDto> getUserPayments(Long userId, Pageable pageable);
}
