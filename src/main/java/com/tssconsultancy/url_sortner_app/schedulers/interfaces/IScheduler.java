package com.tssconsultancy.url_sortner_app.schedulers.interfaces;

/**
 * Base interface for all scheduled tasks.
 * 
 * This interface follows the Open/Closed Principle:
 * - Open for extension: New schedulers can implement this interface
 * - Closed for modification: Core scheduler logic remains unchanged
 * 
 * Usage: Implement this interface to create new schedulers (OTP cleanup, URL expiry, payment cleanup, etc.)
 */
public interface IScheduler {
    
    /**
     * Execute the scheduled task.
     * Implementation should contain the business logic for the specific scheduler.
     */
    void execute();
    
    /**
     * Get the name of the scheduler for logging purposes.
     */
    String getSchedulerName();
}
