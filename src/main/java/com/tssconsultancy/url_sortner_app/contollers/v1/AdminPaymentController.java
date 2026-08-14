package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.enums.PaymentStatus;
import com.tssconsultancy.url_sortner_app.services.interfaces.IAdminPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final IAdminPaymentService adminPaymentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<PaymentResponseDto>> getAllPayments(
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PaymentResponseDto> payments = adminPaymentService.getAllPayments(status, pageable);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPaymentStats() {
        Map<String, Object> stats = adminPaymentService.getPaymentStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable("id") Long paymentId) {
        PaymentResponseDto payment = adminPaymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }
}
