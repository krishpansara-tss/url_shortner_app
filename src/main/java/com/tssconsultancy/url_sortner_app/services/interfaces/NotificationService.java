package com.tssconsultancy.url_sortner_app.services.interfaces;

public interface NotificationService {
    void sendMessage(String receiver, String message);

    void verifyOtp(String email, String otp);

    void sendOtp(String email);
}
