package com.tssconsultancy.url_sortner_app.schedulers.implementation;

import com.tssconsultancy.url_sortner_app.entities.Url;
import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import com.tssconsultancy.url_sortner_app.repositories.UrlRepository;
import com.tssconsultancy.url_sortner_app.schedulers.interfaces.IScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * URL Expiry Scheduler
 * 
 * Runs daily at 01:00 to check for expired URLs and mark them as INACTIVE.
 * This prevents users from accessing expired short URLs.
 * 
 * Schedule: Every day at 01:00 (1 AM)
 * Cron: "0 0 1 * * *" (second, minute, hour, day, month, dayOfWeek)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UrlExpiryScheduler implements IScheduler {

    private final UrlRepository urlRepository;

    @Override
    @Scheduled(cron = "0 0 1 * * *")  // Runs daily at 1 AM
    public void execute() {
        log.info("[URL EXPIRY] Starting URL expiry scheduler...");
        
        try {
            LocalDateTime now = LocalDateTime.now();
            
            // Find all active URLs that have expired
            List<Url> expiredUrls = urlRepository.findByUrlStatusAndExpiresAtBefore(UrlStatus.ACTIVE, now);
            
            if (expiredUrls.isEmpty()) {
                log.info("[URL EXPIRY] No expired URLs found");
                return;
            }
            
            // Mark all expired URLs as INACTIVE
            for (Url url : expiredUrls) {
                url.setUrlStatus(UrlStatus.INACTIVE);
                url.setUpdatedAt(LocalDateTime.now());
            }
            
            urlRepository.saveAll(expiredUrls);
            log.info("[URL EXPIRY] Successfully marked {} URLs as inactive", expiredUrls.size());
            
        } catch (Exception e) {
            log.error("[URL EXPIRY] Error during URL expiry check: ", e);
            // Don't throw exception - scheduler should continue running
        }
    }

    @Override
    public String getSchedulerName() {
        return "URL Expiry Scheduler";
    }
}
