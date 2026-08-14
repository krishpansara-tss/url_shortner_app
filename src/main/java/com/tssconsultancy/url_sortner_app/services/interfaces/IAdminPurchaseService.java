package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.enums.PaymentType;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface IAdminPurchaseService {
    PageResponse<PaymentResponseDto> getAllPurchases(PaymentType type, Pageable pageable);
    PaymentResponseDto getPurchaseById(Long purchaseId);
    Map<String, Object> getPurchaseStats();
}
