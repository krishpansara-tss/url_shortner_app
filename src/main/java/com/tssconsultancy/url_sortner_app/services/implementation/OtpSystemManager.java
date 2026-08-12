package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.TokenType;
import com.tssconsultancy.url_sortner_app.services.interfaces.OtpSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OtpSystemManager {

    private final List<OtpSystem> otpSystems;

    public void sendOtp(TokenType tokenType, User user, String otpCode) {
        for (OtpSystem system : otpSystems) {
            if (system.getSupportedTokenType() == tokenType) {
                system.sendOtp(user, otpCode);
                return;
            }
        }
        throw new IllegalStateException("No OTP system configured for " + tokenType);
    }
}
