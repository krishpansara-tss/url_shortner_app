package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.dtos.users.PasswordChangeRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserMeUpdateRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserUpdateRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.users.UserUsageResponseDto;
import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import com.tssconsultancy.url_sortner_app.enums.UserStatus;
import com.tssconsultancy.url_sortner_app.exceptions.base.InvalidOperationException;
import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;
import com.tssconsultancy.url_sortner_app.exceptions.derived.EmailAlreadyExistsException;
import com.tssconsultancy.url_sortner_app.mapper.UserMapper;
import com.tssconsultancy.url_sortner_app.repositories.UrlRepository;
import com.tssconsultancy.url_sortner_app.repositories.UserRepository;
import com.tssconsultancy.url_sortner_app.services.ImageUploadService;
import com.tssconsultancy.url_sortner_app.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements IUserService {

    private final UserRepository userRepository;
    private final UrlRepository urlRepository;
    private final UserMapper userMapper;
    private final ImageUploadService imageUploadService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDto> getAllUsers()
    {
        List<UserResponseDto> userResponses = new ArrayList<>();

        for (User user : userRepository.findAll())
        {
            if (user.getStatus() == UserStatus.ACTIVE)
            {
                userResponses.add(userMapper.toDto(user));
            }
        }

        return userResponses;
    }

    @Override
    public UserResponseDto getUserById(Long id)
    {
        User user = findActiveUserById(id);

        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id,UserUpdateRequestDto updateRequestDto)
    {
        User user = findActiveUserById(id);

        updateEmailIfProvided(user, updateRequestDto.getEmail());

        if (updateRequestDto.getName() != null)
        {
            user.setName(updateRequestDto.getName());
        }

        if (updateRequestDto.getPassword() != null)
        {
            user.setHashedPassword(passwordEncoder.encode(updateRequestDto.getPassword()));
        }

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = findActiveUserById(id);

        user.setStatus(UserStatus.INACTIVE);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public void activateUser(Long id)
    {
        User user = findActiveUserById(id);

        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }

    @Override
    public UserResponseDto getCurrentUser(Long userId)
    {
        User user = findActiveUserById(userId);

        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateCurrentUser(Long userId,UserMeUpdateRequestDto updateDto)
    {
        User user = findActiveUserById(userId);

        updateEmailIfProvided(user, updateDto.getEmail());

        if (updateDto.getName() != null)
        {
            user.setName(updateDto.getName());
        }

        if (updateDto.getProfilePicturePath() != null)
        {
            user.setProfilePicturePath(updateDto.getProfilePicturePath());
        }

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    @Override
    public void changePassword(Long userId,PasswordChangeRequestDto requestDto)
    {
        User user = findActiveUserById(userId);

        if (!passwordEncoder.matches(requestDto.getOldPassword(),user.getHashedPassword()))
        {
            throw new InvalidOperationException("Old password is incorrect");
        }

        user.setHashedPassword(passwordEncoder.encode(requestDto.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    public UserUsageResponseDto getUserUsage(Long userId) {
        User user = findActiveUserById(userId);

        long totalUrls = urlRepository.countByUser(user);

        long activeUrls = urlRepository.countByUserAndUrlStatus(
                user,
                UrlStatus.ACTIVE
        );

        long totalVisits = urlRepository.sumTotalVisitsByUser(user);

        return UserUsageResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .remainingUrlSlots(user.getRemainingUrlSlots())
                .totalUrlsCreated(totalUrls)
                .activeUrlsCount(activeUrls)
                .totalUrlVisits(totalVisits)
                .build();
    }

    private User findActiveUserById(Long userId) {
        if (userId == null) {
            throw new InvalidOperationException(
                    "User ID is required"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + userId
                        )
                );

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResourceNotFoundException(
                    "User not found with id " + userId
            );
        }

        return user;
    }

    private void updateEmailIfProvided(
            User user,
            String newEmail
    ) {
        if (newEmail == null || newEmail.equals(user.getEmail())) {
            return;
        }

        if (userRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException(newEmail);
        }

        user.setEmail(newEmail);
    }

    // ========== PROFILE PICTURE ENDPOINTS ==========

    @Override
    public String uploadProfilePicture(
            Long userId,
            MultipartFile file
    ) {
        User user = findActiveUserById(userId);

        String imageUrl = imageUploadService.uploadToCloudinary(
                file,
                "profile_pictures"
        );

        user.setProfilePicturePath(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    @Override
    public String getProfilePicture(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + userId
                        )
                );

        if (user.getProfilePicturePath() == null
                || user.getProfilePicturePath().isEmpty()) {
            throw new ResourceNotFoundException(
                    "Profile picture not found for user: " + userId
            );
        }

        return user.getProfilePicturePath();
    }

    @Override
    public void deleteProfilePicture(Long userId) {
        User user = findActiveUserById(userId);

        if (user.getProfilePicturePath() == null
                || user.getProfilePicturePath().isEmpty()) {
            throw new ResourceNotFoundException(
                    "Profile picture not found for user: " + userId
            );
        }

        imageUploadService.deleteFromCloudinary(user.getProfilePicturePath());

        user.setProfilePicturePath(null);
        userRepository.save(user);
    }
}