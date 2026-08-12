package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.services.implementation.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> initiatePayment(@RequestBody PaymentRequestDto dto) {
        PaymentResponseDto response = paymentService.initiatePayment(
                dto.getUserId(),
                dto.getUrlId(),
                dto.getPaymentType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<PaymentResponseDto>> getUserPayments( @RequestParam(defaultValue = "0") Integer page,
                                                                                @RequestParam(defaultValue = "0") Integer size,
                                                                                @RequestParam Long userId)
    {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PaymentResponseDto> payments = paymentService.getUserPayments(userId, pageable);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable("id") Long paymentId,
                                                             @RequestParam Long userId) {
        PaymentResponseDto payment = paymentService.getPaymentById(paymentId, userId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponseDto> processPayment(
            @PathVariable("id") Long paymentId) {

        PaymentResponseDto response = paymentService.processPayment(
                paymentId
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<PaymentResponseDto> cancelPayment(
            @PathVariable("id") Long paymentId,
            @RequestParam Long userId) {

        PaymentResponseDto response = paymentService.cancelPayment(paymentId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<PaymentResponseDto> getPaymentReceipt(
            @PathVariable("id") Long paymentId,
            @RequestParam Long userId) {

        PaymentResponseDto response = paymentService.getPaymentById(paymentId, userId);
        return ResponseEntity.ok(response);
    }
}
