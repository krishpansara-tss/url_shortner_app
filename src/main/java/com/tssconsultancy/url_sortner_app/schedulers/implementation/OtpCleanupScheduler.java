package com.tssconsultancy.url_sortner_app.schedulers.implementation;

import com.tssconsultancy.url_sortner_app.repositories.OTPRepository;
import com.tssconsultancy.url_sortner_app.schedulers.interfaces.IScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * OTP Cleanup Scheduler
 * 
 * Runs daily at midnight (00:00) to delete expired and used OTPs.
 * This keeps the database clean and removes sensitive data that's no longer needed.
 * 
 * Schedule: Every day at 00:00 (midnight)
 * Cron: "0 0 0 * * *" (second, minute, hour, day, month, dayOfWeek)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OtpCleanupScheduler implements IScheduler {

    private final OTPRepository otpRepository;

    @Override
    @Scheduled(cron = "0 0 0 * * *")  // Runs daily at midnight
    public void execute() {
        log.info("[OTP CLEANUP] Starting OTP cleanup scheduler...");
        
        try {
            LocalDateTime now = LocalDateTime.now();
            
            // Delete all expired OTPs (expiryTime < current time)
            long expiredCount = otpRepository.deleteByExpiryTimeBefore(now);
            log.info("[OTP CLEANUP] Deleted {} expired OTPs", expiredCount);
            
            // Delete all used OTPs (optional - can also keep for audit trail)
            long usedCount = otpRepository.deleteByUsedTrue();
            log.info("[OTP CLEANUP] Deleted {} used OTPs", usedCount);
            
            log.info("[OTP CLEANUP] OTP cleanup completed successfully. Total deleted: {}", 
                    expiredCount + usedCount);
            
        } catch (Exception e) {
            log.error("[OTP CLEANUP] Error during OTP cleanup: ", e);
            // Don't throw exception - scheduler should continue running
        }
    }

    @Override
    public String getSchedulerName() {
        return "OTP Cleanup Scheduler";
    }
}
