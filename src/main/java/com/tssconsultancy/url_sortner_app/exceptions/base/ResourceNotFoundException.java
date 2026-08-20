package com.tssconsultancy.url_sortner_app.exceptions.base;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
