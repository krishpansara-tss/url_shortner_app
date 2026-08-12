package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.TokenType;
import com.tssconsultancy.url_sortner_app.services.interfaces.OtpSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailOtpSystem implements OtpSystem {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:no-reply@example.com}")
    private String fromAddress;

    @Override
    public TokenType getSupportedTokenType() {
        return TokenType.EMAIL_VERIFICATION;
    }

    @Override
    public void sendOtp(User user, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(user.getEmail());
        message.setSubject("Email Verification Code");
        message.setText("Your verification code is: " + otpCode + "\nThis code expires in 10 minutes.");
        mailSender.send(message);
    }
}
