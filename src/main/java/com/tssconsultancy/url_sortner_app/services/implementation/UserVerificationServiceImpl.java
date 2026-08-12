package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.entities.VarificationToken;
import com.tssconsultancy.url_sortner_app.enums.TokenType;
import com.tssconsultancy.url_sortner_app.exceptions.InvalidRequestException;
import com.tssconsultancy.url_sortner_app.exceptions.ResourceNotFoundException;
import com.tssconsultancy.url_sortner_app.repositories.UserRepository;
import com.tssconsultancy.url_sortner_app.repositories.VerificationTokenRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.UserVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserVerificationServiceImpl implements UserVerificationService {

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_MINUTES = 10;

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final OtpSystemManager otpSystemManager;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void sendEmailVerificationOtp(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        String otpCode = generateNumericOtp(OTP_LENGTH);
        String hashedToken = passwordEncoder.encode(otpCode);

        VarificationToken token = new VarificationToken(
                hashedToken,
                TokenType.EMAIL_VERIFICATION,
                user,
                LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES)
        );
        verificationTokenRepository.save(token);
        otpSystemManager.sendOtp(TokenType.EMAIL_VERIFICATION, user, otpCode);
    }

    @Override
    public void verifyEmailOtp(Long userId, String otpCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        VarificationToken verificationToken = verificationTokenRepository
                .findFirstByUserAndTokenTypeAndUsedFalseOrderByCreatedAtDesc(user, TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new InvalidRequestException("No valid verification token found. Please request a new OTP."));

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Verification code has expired. Please request a new OTP.");
        }

        if (!passwordEncoder.matches(otpCode, verificationToken.getHashedToken())) {
            throw new InvalidRequestException("Invalid verification code.");
        }

        verificationToken.setUsed(true);
        verificationToken.setUsedAt(LocalDateTime.now());
        verificationTokenRepository.save(verificationToken);

        user.setVerified(true);
        userRepository.save(user);
    }

    @Override
    public void resendEmailVerificationOtp(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        if (user.isVerified()) {
            throw new InvalidRequestException("User email is already verified.");
        }

        sendEmailVerificationOtp(user);
    }

    private String generateNumericOtp(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
