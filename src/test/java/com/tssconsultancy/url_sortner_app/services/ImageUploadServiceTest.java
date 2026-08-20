package com.tssconsultancy.url_sortner_app.services;

import com.cloudinary.Cloudinary;
import com.tssconsultancy.url_sortner_app.exceptions.derived.ImageUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ImageUploadServiceTest {

    @Mock
    private Cloudinary cloudinary;

    private ImageUploadService imageUploadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageUploadService = new ImageUploadService(cloudinary);
    }

    @Test
    void rejectsEmptyFile() {
        assertRejected(new MockMultipartFile("image", "image.png", "image/png", new byte[0]));
    }

    @Test
    void rejectsFileBelowMinimumSize() {
        assertRejected(new MockMultipartFile("image", "image.png", "image/png", pngBytes(1023)));
    }

    @Test
    void rejectsFileAboveMaximumSize() {
        assertRejected(new MockMultipartFile("image", "image.png", "image/png", pngBytes(5 * 1024 * 1024 + 1)));
    }

    @Test
    void rejectsDisallowedExtension() {
        assertRejected(new MockMultipartFile("image", "image.txt", "text/plain", pngBytes(1024)));
    }

    @Test
    void rejectsContentThatDoesNotMatchExtension() {
        assertRejected(new MockMultipartFile("image", "image.png", "image/png", new byte[1024]));
    }

    private void assertRejected(MockMultipartFile file) {
        assertThrows(ImageUploadException.class, () -> imageUploadService.uploadToCloudinary(file, "profile_pictures"));
        verify(cloudinary, never()).uploader();
    }

    private byte[] pngBytes(int size) {
        byte[] bytes = new byte[size];
        bytes[0] = (byte) 0x89;
        bytes[1] = 0x50;
        bytes[2] = 0x4E;
        bytes[3] = 0x47;
        bytes[4] = 0x0D;
        bytes[5] = 0x0A;
        bytes[6] = 0x1A;
        bytes[7] = 0x0A;
        return bytes;
    }
}