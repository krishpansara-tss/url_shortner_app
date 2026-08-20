package com.tssconsultancy.url_sortner_app.services;

import com.cloudinary.Cloudinary;
import com.tssconsultancy.url_sortner_app.exceptions.derived.ImageUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private static final long MIN_FILE_SIZE_BYTES = 1024;
    private static final long MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final Pattern VERSION_SEGMENT = Pattern.compile("v\\d+");

    private final Cloudinary cloudinary;

    public String uploadToCloudinary(MultipartFile file, String folder) {
        validateImage(file);

        try {
            Map<String, Object> uploadOptions = Map.of(
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
        } catch (ImageUploadException e) {
            throw e;
        } catch (Exception e) {
            throw new ImageUploadException(e.getMessage());
        }
    }

    public void deleteFromCloudinary(String cloudinaryUrl) {
        try {
            String publicId = extractPublicId(cloudinaryUrl);
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception e) {
        }
    }

    private Map<String, Object> addFolderToOptions(Map<String, Object> options, String folder) {
        if (folder != null && !folder.isBlank()) {
            Map<String, Object> newOptions = new java.util.HashMap<>(options);
            newOptions.put("folder", folder);
            return newOptions;
        }
        return options;
    }

    private String extractPublicId(String cloudinaryUrl) {
        if (cloudinaryUrl == null || cloudinaryUrl.isBlank()) {
            throw new ImageUploadException("Cloudinary URL is required");
        }

        int uploadIndex = cloudinaryUrl.indexOf("/upload/");
        if (uploadIndex < 0) {
            throw new ImageUploadException("Invalid Cloudinary URL");
        }

        String path = cloudinaryUrl.substring(uploadIndex + "/upload/".length());
        String[] pathSegments = path.split("/");
        for (int i = 0; i < pathSegments.length; i++) {
            if (VERSION_SEGMENT.matcher(pathSegments[i]).matches()) {
                path = String.join("/", java.util.Arrays.copyOfRange(pathSegments, i + 1, pathSegments.length));
                break;
            }
        }

        int extensionIndex = path.lastIndexOf('.');
        return extensionIndex > 0 ? path.substring(0, extensionIndex) : path;
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ImageUploadException("Image file must not be empty");
        }
        if (file.getSize() < MIN_FILE_SIZE_BYTES) {
            throw new ImageUploadException("Image file must be at least 1 KiB");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new ImageUploadException("Image file must not exceed 5 MiB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new ImageUploadException("Image file must have an allowed extension");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ImageUploadException("Allowed image extensions are: jpg, jpeg, png, gif, webp");
        }

        try {
            byte[] content = file.getBytes();
            if (!matchesImageSignature(content, extension)) {
                throw new ImageUploadException("File content does not match its image extension");
            }
        } catch (IOException e) {
            throw new ImageUploadException("Failed to read file: " + e.getMessage());
        }
    }

    private boolean matchesImageSignature(byte[] content, String extension) {
        if ("jpg".equals(extension) || "jpeg".equals(extension)) {
            return content.length >= 3 && (content[0] & 0xFF) == 0xFF
                    && (content[1] & 0xFF) == 0xD8 && (content[2] & 0xFF) == 0xFF;
        }
        if ("png".equals(extension)) {
            return content.length >= 8 && (content[0] & 0xFF) == 0x89
                    && content[1] == 0x50 && content[2] == 0x4E && content[3] == 0x47
                    && content[4] == 0x0D && content[5] == 0x0A && content[6] == 0x1A && content[7] == 0x0A;
        }
        if ("gif".equals(extension)) {
            return content.length >= 6 && (new String(content, 0, 6).equals("GIF87a")
                    || new String(content, 0, 6).equals("GIF89a"));
        }
        return "webp".equals(extension) && content.length >= 12
                && new String(content, 0, 4).equals("RIFF")
                && new String(content, 8, 4).equals("WEBP");
    }
}
