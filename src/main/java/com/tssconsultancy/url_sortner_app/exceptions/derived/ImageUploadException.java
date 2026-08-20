package com.tssconsultancy.url_sortner_app.exceptions.derived;

import com.tssconsultancy.url_sortner_app.exceptions.base.InvalidOperationException;

public class ImageUploadException extends InvalidOperationException {
    public ImageUploadException(String message) {
        super("Image upload failed: " + message);
    }
}
