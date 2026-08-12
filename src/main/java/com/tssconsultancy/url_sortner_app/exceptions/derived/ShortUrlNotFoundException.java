package com.tssconsultancy.url_sortner_app.exceptions.derived;

import com.tssconsultancy.url_sortner_app.exceptions.base.ResourceNotFoundException;

public class ShortUrlNotFoundException extends ResourceNotFoundException {
    public ShortUrlNotFoundException(String url) {
        super("Short Url doesn't exists having : " + url);
    }
}
