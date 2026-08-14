package com.tssconsultancy.url_sortner_app.repositories;

import com.tssconsultancy.url_sortner_app.entities.Payment;
import com.tssconsultancy.url_sortner_app.enums.PaymentStatus;
import com.tssconsultancy.url_sortner_app.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentIdAndUserUserId(Long paymentId, Long userId);
    Page<Payment> findAllByUserUserId(Long userId, Pageable pageable);

    Page<Payment> findAllByPaymentStatus(PaymentStatus status, Pageable pageable);
    Page<Payment> findAllByPaymentType(PaymentType type, Pageable pageable);
    Page<Payment> findAllByPaymentStatusAndPaymentType(PaymentStatus status, PaymentType type, Pageable pageable);

    Long countByPaymentStatus(PaymentStatus status);
    Long countByPaymentType(PaymentType type);
    Long countByPaymentStatusAndPaymentType(PaymentStatus status, PaymentType type);
}

