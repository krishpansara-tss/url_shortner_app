package com.tssconsultancy.url_sortner_app.exceptions.derived;

import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;

public class UrlNotFoundException extends ResourceNotFoundException {
    public UrlNotFoundException(Long urlId) {
        super("Url doesn't exists having ID: " + urlId);
    }
}
