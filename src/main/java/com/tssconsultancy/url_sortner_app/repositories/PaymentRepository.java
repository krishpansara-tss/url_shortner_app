package com.tssconsultancy.url_sortner_app.repositories;

import com.tssconsultancy.url_sortner_app.entities.Payment;
import com.tssconsultancy.url_sortner_app.enums.PaymentStatus;
import com.tssconsultancy.url_sortner_app.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentIdAndUserUserId(Long paymentId, Long userId);
    Page<Payment> findAllByUserUserId(Long userId, Pageable pageable);
    
    // ========== SCHEDULER METHODS ==========
    
    /**
     * Find all pending payments created before the given time.
     * Used by PaymentCleanupScheduler to cancel stale pending payments.
     */
    List<Payment> findByPaymentStatusAndCreatedAtBefore(PaymentStatus paymentStatus, LocalDateTime createdAt);

    Page<Payment> findAllByPaymentStatus(PaymentStatus status, Pageable pageable);
    Page<Payment> findAllByPaymentType(PaymentType type, Pageable pageable);
    Page<Payment> findAllByPaymentStatusAndPaymentType(PaymentStatus status, PaymentType type, Pageable pageable);

    Long countByPaymentStatus(PaymentStatus status);
    Long countByPaymentType(PaymentType type);
    Long countByPaymentStatusAndPaymentType(PaymentStatus status, PaymentType type);
}

