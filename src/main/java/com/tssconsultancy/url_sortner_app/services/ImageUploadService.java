package com.tssconsultancy.url_sortner_app.services;

import com.cloudinary.Cloudinary;
import com.tssconsultancy.url_sortner_app.exceptions.derived.ImageUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final Cloudinary cloudinary;

    // Generic upload with optional folder
    public String uploadToCloudinary(MultipartFile file, String folder) {
        try {
            Map<Object, Object> uploadOptions = Map.of(
                    "resource_type", "auto"
            );
            
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    addFolderToOptions(uploadOptions, folder)
            );

            Object url = result.get("secure_url");
            
            if (url == null) {
                throw new ImageUploadException("Cloudinary did not return URL");
            }

            return url.toString();

        } catch (IOException e) {
            throw new ImageUploadException("Failed to read file: " + e.getMessage());
        } catch (Exception e) {
            throw new ImageUploadException(e.getMessage());
        }
    }

    // Upload image only (backward compatibility)
    public String uploadImage(MultipartFile file) {
        return uploadToCloudinary(file, null);
    }

    // Delete file from Cloudinary
    public void deleteFromCloudinary(String cloudinaryUrl) {
        try {
            String publicId = extractPublicId(cloudinaryUrl);
            if (!publicId.isEmpty()) {
                cloudinary.uploader().destroy(publicId, Map.of());
            }
        } catch (Exception e) {
            // Log error but don't fail
        }
    }

    // Helper: Add folder to upload options
    @SuppressWarnings("unchecked")
    private Map<Object, Object> addFolderToOptions(Map<Object, Object> options, String folder) {
        if (folder != null && !folder.isEmpty()) {
            Map<Object, Object> newOptions = new java.util.HashMap<>(options);
            newOptions.put("folder", folder);
            return newOptions;
        }
        return options;
    }

    // Helper: Extract public ID from Cloudinary URL
    private String extractPublicId(String cloudinaryUrl) {
        try {
            String[] parts = cloudinaryUrl.split("/upload/v[0-9]+/");
            if (parts.length > 1) {
                String path = parts[1];
                return path.substring(0, path.lastIndexOf("."));
            }
        } catch (Exception e) {
            // Return empty string if parsing fails
        }
        return "";
    }
}
