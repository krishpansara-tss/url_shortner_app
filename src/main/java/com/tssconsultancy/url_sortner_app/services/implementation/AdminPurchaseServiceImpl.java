package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.entities.Payment;
import com.tssconsultancy.url_sortner_app.enums.PaymentStatus;
import com.tssconsultancy.url_sortner_app.enums.PaymentType;
import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;
import com.tssconsultancy.url_sortner_app.mapper.PaymentMapper;
import com.tssconsultancy.url_sortner_app.repositories.PaymentRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.IAdminPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminPurchaseServiceImpl implements IAdminPurchaseService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PageResponse<PaymentResponseDto> getAllPurchases(PaymentType type, Pageable pageable) {
        Page<Payment> purchasePage;

        if (type != null) {
            purchasePage = paymentRepository.findAllByPaymentStatusAndPaymentType(PaymentStatus.SUCCESS, type, pageable);
        } else {
            purchasePage = paymentRepository.findAllByPaymentStatus(PaymentStatus.SUCCESS, pageable);
        }

        List<PaymentResponseDto> content = purchasePage.getContent().stream()
                .map(paymentMapper::toResponse)
                .toList();

        return PageResponse.<PaymentResponseDto>builder()
                .content(content)
                .page(purchasePage.getNumber())
                .size(purchasePage.getSize())
                .totalElements(purchasePage.getTotalElements())
                .totalPages(purchasePage.getTotalPages())
                .last(purchasePage.isLast())
                .build();
    }

    @Override
    public PaymentResponseDto getPurchaseById(Long purchaseId) {
        Payment purchase = paymentRepository.findById(purchaseId)
                .filter(p -> p.getPaymentStatus() == PaymentStatus.SUCCESS)
                .orElseThrow(() -> new ResourceNotFoundException("Completed purchase record not found with ID: " + purchaseId));

        return paymentMapper.toResponse(purchase);
    }

    @Override
    public Map<String, Object> getPurchaseStats() {
        List<Payment> successfulPurchases = paymentRepository.findAll().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.SUCCESS)
                .toList();

        long totalPurchases = successfulPurchases.size();
        double totalRevenue = successfulPurchases.stream()
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        Map<PaymentType, Long> countByType = new HashMap<>();
        Map<PaymentType, Double> revenueByType = new HashMap<>();

        for (PaymentType type : PaymentType.values()) {
            List<Payment> byType = successfulPurchases.stream()
                    .filter(p -> p.getPaymentType() == type)
                    .toList();

            countByType.put(type, (long) byType.size());
            revenueByType.put(type, byType.stream().mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0).sum());
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPurchases", totalPurchases);
        stats.put("totalRevenue", totalRevenue);
        stats.put("countByType", countByType);
        stats.put("revenueByType", revenueByType);

        return stats;
    }
}
