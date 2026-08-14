package com.tssconsultancy.url_sortner_app.repositories;

import com.tssconsultancy.url_sortner_app.entities.OTPModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OTPRepository extends JpaRepository<OTPModel, Long> {
    OTPModel findTopByEmailAndUsedFalseOrderByCreatedAtDesc(String email);

    long countByEmailAndCreatedAtAfter(String email, LocalDateTime after);
    
    // ========== SCHEDULER METHODS ==========
    
    /**
     * Delete all OTPs where expiryTime is before the given time.
     * Used by OtpCleanupScheduler to remove expired OTPs.
     */
    @Modifying
    @Query("DELETE FROM OTPModel o WHERE o.expiryTime < :expiryTime")
    long deleteByExpiryTimeBefore(@Param("expiryTime") LocalDateTime expiryTime);
    
    /**
     * Delete all used OTPs (optional - for audit trail compliance).
     */
    @Modifying
    @Query("DELETE FROM OTPModel o WHERE o.used = true")
    long deleteByUsedTrue();
}
