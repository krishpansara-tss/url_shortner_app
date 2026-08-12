package com.tssconsultancy.url_sortner_app.exceptions.derived;

import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;

public class UserEmailNotFoundException extends ResourceNotFoundException {
    public UserEmailNotFoundException(String email) {
        super("User doesn't exists having email: " + email);
    }
}
