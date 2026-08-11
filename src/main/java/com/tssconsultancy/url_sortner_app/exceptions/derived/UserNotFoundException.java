package com.tssconsultancy.url_sortner_app.exceptions.derived;

import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(Long userId) {
        super("User doesn't exists having ID: " + userId);
    }
}
