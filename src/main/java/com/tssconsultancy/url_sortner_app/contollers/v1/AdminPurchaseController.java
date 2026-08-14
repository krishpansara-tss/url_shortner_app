package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.enums.PaymentType;
import com.tssconsultancy.url_sortner_app.services.interfaces.IAdminPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/purchases")
@RequiredArgsConstructor
public class AdminPurchaseController {

    private final IAdminPurchaseService adminPurchaseService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<PaymentResponseDto>> getAllPurchases(
            @RequestParam(required = false) PaymentType type,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PaymentResponseDto> purchases = adminPurchaseService.getAllPurchases(type, pageable);
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPurchaseStats() {
        Map<String, Object> stats = adminPurchaseService.getPurchaseStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponseDto> getPurchaseById(@PathVariable("id") Long purchaseId) {
        PaymentResponseDto purchase = adminPurchaseService.getPurchaseById(purchaseId);
        return ResponseEntity.ok(purchase);
    }
}
