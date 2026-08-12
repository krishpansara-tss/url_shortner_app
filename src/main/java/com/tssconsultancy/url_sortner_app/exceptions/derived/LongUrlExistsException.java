package com.tssconsultancy.url_sortner_app.exceptions.derived;

import com.tssconsultancy.url_sortner_app.exceptions.base.DuplicateResourceException;

public class LongUrlExistsException extends DuplicateResourceException {
    public LongUrlExistsException(String url) {
        super("The long URL already exists: " + url);
    }
}
