package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.entities.Payment;
import com.tssconsultancy.url_sortner_app.enums.PaymentStatus;
import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;
import com.tssconsultancy.url_sortner_app.mapper.PaymentMapper;
import com.tssconsultancy.url_sortner_app.repositories.PaymentRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.IAdminPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements IAdminPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PageResponse<PaymentResponseDto> getAllPayments(PaymentStatus status, Pageable pageable) {
        Page<Payment> paymentPage;

        if (status != null) {
            paymentPage = paymentRepository.findAllByPaymentStatus(status, pageable);
        } else {
            paymentPage = paymentRepository.findAll(pageable);
        }

        List<PaymentResponseDto> content = paymentPage.getContent().stream()
                .map(paymentMapper::toResponse)
                .toList();

        return PageResponse.<PaymentResponseDto>builder()
                .content(content)
                .page(paymentPage.getNumber())
                .size(paymentPage.getSize())
                .totalElements(paymentPage.getTotalElements())
                .totalPages(paymentPage.getTotalPages())
                .last(paymentPage.isLast())
                .build();
    }

    @Override
    public PaymentResponseDto getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentId));

        return paymentMapper.toResponse(payment);
    }

    @Override
    public Map<String, Object> getPaymentStats() {
        List<Payment> allPayments = paymentRepository.findAll();

        long totalPayments = allPayments.size();
        long successCount = paymentRepository.countByPaymentStatus(PaymentStatus.SUCCESS);
        long pendingCount = paymentRepository.countByPaymentStatus(PaymentStatus.PENDING);
        long cancelledCount = paymentRepository.countByPaymentStatus(PaymentStatus.CANCELLED);
        long failedCount = paymentRepository.countByPaymentStatus(PaymentStatus.FAILED);

        double totalRevenue = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.SUCCESS && p.getAmount() != null)
                .mapToDouble(Payment::getAmount)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPayments", totalPayments);
        stats.put("totalRevenue", totalRevenue);
        stats.put("successfulPayments", successCount);
        stats.put("pendingPayments", pendingCount);
        stats.put("cancelledPayments", cancelledCount);
        stats.put("failedPayments", failedCount);

        return stats;
    }
}
