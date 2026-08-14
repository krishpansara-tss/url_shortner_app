package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.enums.PaymentStatus;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface IAdminPaymentService {
    PageResponse<PaymentResponseDto> getAllPayments(PaymentStatus status, Pageable pageable);
    PaymentResponseDto getPaymentById(Long paymentId);
    Map<String, Object> getPaymentStats();
}
