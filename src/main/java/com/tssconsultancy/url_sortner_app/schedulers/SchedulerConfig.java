package com.tssconsultancy.url_sortner_app.schedulers;

import com.tssconsultancy.url_sortner_app.schedulers.interfaces.IScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.List;

/**
 * Scheduler Configuration
 * 
 * This configuration enables Spring's scheduled task support using @Scheduled annotations.
 * 
 * All schedulers implementing IScheduler will be automatically discovered and executed
 * according to their cron expressions.
 * 
 * Scheduler Schedule:
 * - 00:00 (Midnight): OtpCleanupScheduler - Delete expired OTPs
 * - 01:00 (1 AM): UrlExpiryScheduler - Mark expired URLs as inactive
 * - 02:00 (2 AM): PaymentCleanupScheduler - Cancel stale pending payments
 * 
 * To add a new scheduler:
 * 1. Create a new class implementing IScheduler
 * 2. Annotate with @Component
 * 3. Implement the @Scheduled(cron = "...") on execute() method
 * 4. The scheduler will be automatically picked up by Spring
 */
@Slf4j
@Configuration
@EnableScheduling
@Component
@RequiredArgsConstructor
public class SchedulerConfig {

    private final List<IScheduler> schedulers;

    /**
     * Log all registered schedulers on application startup.
     * This helps verify that all schedulers are properly configured.
     */
    @PostConstruct
    public void init() {
        log.info("[SCHEDULER CONFIG] SchedulerConfig initialized with {} schedulers", schedulers.size());
        schedulers.forEach(s -> log.info("[SCHEDULER CONFIG] Registered scheduler: {}", s.getSchedulerName()));
    }

    /**
     * Public method to manually trigger a scheduler by name (for testing/debugging).
     * Usage: schedulerConfig.executeScheduler("OTP Cleanup Scheduler");
     */
    public void executeScheduler(String schedulerName) {
        log.info("[SCHEDULER MANUAL EXEC] Triggering scheduler: {}", schedulerName);
        
        IScheduler scheduler = schedulers.stream()
                .filter(s -> s.getSchedulerName().equalsIgnoreCase(schedulerName))
                .findFirst()
                .orElse(null);
        
        if (scheduler != null) {
            try {
                scheduler.execute();
                log.info("[SCHEDULER MANUAL EXEC] Scheduler '{}' executed successfully", schedulerName);
            } catch (Exception e) {
                log.error("[SCHEDULER MANUAL EXEC] Error executing scheduler '{}': ", schedulerName, e);
            }
        } else {
            log.warn("[SCHEDULER MANUAL EXEC] Scheduler '{}' not found", schedulerName);
        }
    }
}
