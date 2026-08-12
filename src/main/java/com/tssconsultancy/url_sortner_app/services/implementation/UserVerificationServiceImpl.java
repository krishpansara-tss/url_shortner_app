package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.exceptions.base.InvalidOperationException;
import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;
import com.tssconsultancy.url_sortner_app.repositories.UserRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.UserVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserVerificationServiceImpl implements UserVerificationService {

    private final UserRepository userRepository;
    private final NotificationProcessor notificationProcessor;

    @Override
    public void sendEmailVerificationOtp(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        notificationProcessor.getProcessor("email").sendOtp(user.getEmail());
    }

    @Override
    public void verifyEmailOtp(Long userId, String otpCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        notificationProcessor.getProcessor("email").verifyOtp(user.getEmail(), otpCode);

        user.setVerified(true);
        userRepository.save(user);
    }

    @Override
    public void resendEmailVerificationOtp(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        if (user.isVerified()) {
            throw new InvalidOperationException("User email is already verified.");
        }

        sendEmailVerificationOtp(user);
    }
}
