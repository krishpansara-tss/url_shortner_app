package com.tssconsultancy.url_sortner_app.exceptions.derived;

import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;

public class ImageUploadException extends ResourceNotFoundException {
    public ImageUploadException(String message) {
        super("Image upload failed: " + message);
    }
}
