package com.tssconsultancy.url_sortner_app.services.interfaces;

import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.TokenType;

public interface OtpSystem {
    TokenType getSupportedTokenType();
    void sendOtp(User user, String otpCode);
}
