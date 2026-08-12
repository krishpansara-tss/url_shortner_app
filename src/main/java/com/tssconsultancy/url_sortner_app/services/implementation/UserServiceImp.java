package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.dtos.users.UserRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserUpdateRequestDto;
import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.UserStatus;
import com.tssconsultancy.url_sortner_app.enums.UserTypes;
import com.tssconsultancy.url_sortner_app.exceptions.DuplicateResourceException;
import com.tssconsultancy.url_sortner_app.exceptions.ResourceNotFoundException;
import com.tssconsultancy.url_sortner_app.mappers.UserMapper;
import com.tssconsultancy.url_sortner_app.repositories.UserRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.IUserService;
import com.tssconsultancy.url_sortner_app.services.interfaces.UserVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserVerificationService userVerificationService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Email is already registered");
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

        User saved = userRepository.save(user);
        userVerificationService.sendEmailVerificationOtp(saved);
        return userMapper.toDto(saved);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<UserResponseDto> userResponses = new java.util.ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (user.getStatus() == UserStatus.ACTIVE) {
                userResponses.add(userMapper.toDto(user));
            }
        }
        return userResponses;
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }

        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto updateRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }

        if (updateRequestDto.getEmail() != null && !updateRequestDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateRequestDto.getEmail())) {
                throw new DuplicateResourceException("Email is already registered");
            }
            user.setEmail(updateRequestDto.getEmail());
        }
        if (updateRequestDto.getName() != null) {
            user.setName(updateRequestDto.getName());
        }
        if (updateRequestDto.getPassword() != null) {
            user.setHashedPassword(passwordEncoder.encode(updateRequestDto.getPassword()));
        }

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }

        user.setStatus(UserStatus.INACTIVE);
        user.setDeletedAt(java.time.LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void verifyUserEmail(Long userId, String otpCode) {
        userVerificationService.verifyEmailOtp(userId, otpCode);
    }

    @Override
    public void resendEmailVerificationOtp(Long userId) {
        userVerificationService.resendEmailVerificationOtp(userId);
    }
}
