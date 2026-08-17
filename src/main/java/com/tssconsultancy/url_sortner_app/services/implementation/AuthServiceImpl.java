package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.dtos.auth.*;
import com.tssconsultancy.url_sortner_app.dtos.users.UserRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserResponseDto;
import com.tssconsultancy.url_sortner_app.entities.TokenBlacklist;
import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.UserStatus;
import com.tssconsultancy.url_sortner_app.enums.UserTypes;
import com.tssconsultancy.url_sortner_app.exceptions.base.InvalidOperationException;
import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;
import com.tssconsultancy.url_sortner_app.exceptions.derived.EmailAlreadyExistsException;
import com.tssconsultancy.url_sortner_app.mappers.UserMapper;
import com.tssconsultancy.url_sortner_app.repositories.TokenBlacklistRepository;
import com.tssconsultancy.url_sortner_app.repositories.UserRepository;
import com.tssconsultancy.url_sortner_app.security.JwtTokenProvider;
import com.tssconsultancy.url_sortner_app.services.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final UserMapper userMapper;
    private final NotificationProcessor notificationProcessor;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto register(UserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistsException(requestDto.getEmail());
        }

        User user = new User(
                requestDto.getName(),
                requestDto.getEmail(),
                passwordEncoder.encode(requestDto.getPassword())
        );
        user.setRole(UserTypes.USER);
        user.setStatus(UserStatus.ACTIVE);
        user.setRemainingUrlSlots(100);
        user.setVerified(false);

        User savedUser = userRepository.save(user);

        notificationProcessor.getProcessor("email").sendOtp(savedUser.getEmail());
        String generatedToken = jwtTokenProvider.generateToken(user);

        return LoginResponseDto.builder()
                .token(generatedToken)
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .verified(user.isVerified())
                .role(user.getRole())
                .build();
    }

    @Override
    public void verifyEmail(VerifyEmailRequestDto requestDto) {
        User user = findUserByEmail(requestDto.getEmail());

        notificationProcessor.getProcessor("email").verifyOtp(requestDto.getEmail(), requestDto.getOtpCode());

        user.setVerified(true);
        userRepository.save(user);
    }

    @Override
    public void resendVerificationOtp(String email) {
        User user = findUserByEmail(email);

        if (user.isVerified()) {
            throw new InvalidOperationException("User email is already verified.");
        }

        notificationProcessor.getProcessor("email").sendOtp(user.getEmail());
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = findUserByEmail(requestDto.getEmail());

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getHashedPassword())) {
            throw new InvalidOperationException("Invalid email or password");
        }

        if (!user.isVerified()) {
            throw new InvalidOperationException(
                    "Email is not verified yet. Please verify your email or resend the verification OTP."
            );
        }

        String generatedToken = jwtTokenProvider.generateToken(user);

        return LoginResponseDto.builder()
                .token(generatedToken)
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .verified(user.isVerified())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserResponseDto getMe(Long userId) {
        User user = findUserById(userId);
        return userMapper.toDto(user);
    }

    @Override
    public void logout(Long userId, String token) {
        User user = findUserById(userId);

        if (token != null && !token.isBlank()) {
            String cleanToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();

            TokenBlacklist blacklist = new TokenBlacklist();
            blacklist.setTokenHash(cleanToken);
            blacklist.setUser(user);
            blacklist.setBlacklistedAt(LocalDateTime.now());
            blacklist.setExpiresAt(LocalDateTime.now().plusDays(1));
            tokenBlacklistRepository.save(blacklist);
        }
    }

    @Override
    @Transactional
    public void logoutAll(Long userId) {
        User user = findUserById(userId);
        tokenBlacklistRepository.deleteByUser(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequestDto requestDto) {
        User user = findUserByEmail(requestDto.getEmail());
        notificationProcessor.getProcessor("email").sendOtp(user.getEmail());
    }

    @Override
    public void resetPassword(ResetPasswordRequestDto requestDto) {
        User user = findUserByEmail(requestDto.getEmail());

        notificationProcessor.getProcessor("email").verifyOtp(requestDto.getEmail(), requestDto.getOtpCode());

        user.setHashedPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);
    }

    private User findUserById(Long userId) {
        if (userId == null) {
            throw new InvalidOperationException("User ID is required");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        return user;
    }

    private User findUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidOperationException("Email is required");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResourceNotFoundException("User not found with email " + email);
        }
        return user;
    }
}
