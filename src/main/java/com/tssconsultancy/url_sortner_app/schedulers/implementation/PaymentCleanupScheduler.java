package com.tssconsultancy.url_sortner_app.schedulers.implementation;

import com.tssconsultancy.url_sortner_app.entities.Payment;
import com.tssconsultancy.url_sortner_app.enums.PaymentStatus;
import com.tssconsultancy.url_sortner_app.repositories.PaymentRepository;
import com.tssconsultancy.url_sortner_app.schedulers.interfaces.IScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Payment Cleanup Scheduler
 * 
 * Runs daily at 02:00 to cancel pending payments that have been pending for more than 24 hours.
 * This ensures that users don't get stuck with pending payments and can retry.
 * 
 * Schedule: Every day at 02:00 (2 AM)
 * Cron: "0 0 2 * * *" (second, minute, hour, day, month, dayOfWeek)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCleanupScheduler implements IScheduler {

    private final PaymentRepository paymentRepository;
    
    // Payment pending timeout: 24 hours
    private static final int PENDING_TIMEOUT_HOURS = 24;

    @Override
    @Scheduled(cron = "0 0 2 * * *")  // Runs daily at 2 AM
    public void execute() {
        log.info("[PAYMENT CLEANUP] Starting payment cleanup scheduler...");
        
        try {
            LocalDateTime thresholdTime = LocalDateTime.now().minusHours(PENDING_TIMEOUT_HOURS);
            
            // Find all pending payments created before the threshold time
            List<Payment> expiredPendingPayments = paymentRepository
                    .findByPaymentStatusAndCreatedAtBefore(PaymentStatus.PENDING, thresholdTime);
            
            if (expiredPendingPayments.isEmpty()) {
                log.info("[PAYMENT CLEANUP] No expired pending payments found");
                return;
            }
            
            // Cancel all expired pending payments
            for (Payment payment : expiredPendingPayments) {
                payment.setPaymentStatus(PaymentStatus.CANCELLED);
            }
            
            paymentRepository.saveAll(expiredPendingPayments);
            log.info("[PAYMENT CLEANUP] Successfully cancelled {} pending payments (older than {} hours)", 
                    expiredPendingPayments.size(), PENDING_TIMEOUT_HOURS);
            
        } catch (Exception e) {
            log.error("[PAYMENT CLEANUP] Error during payment cleanup: ", e);
            // Don't throw exception - scheduler should continue running
        }
    }

    @Override
    public String getSchedulerName() {
        return "Payment Cleanup Scheduler";
    }
}
