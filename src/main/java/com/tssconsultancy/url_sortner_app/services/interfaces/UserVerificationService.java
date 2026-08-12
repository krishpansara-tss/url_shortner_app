package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.entities.User;

public interface UserVerificationService {
    void sendEmailVerificationOtp(User user);
    void verifyEmailOtp(Long userId, String otpCode);
    void resendEmailVerificationOtp(Long userId);
}
