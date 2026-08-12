package com.tssconsultancy.url_sortner_app.exceptions.derived;

import com.tssconsultancy.url_sortner_app.exceptions.base.UrlException;

public class LimitExceededException extends UrlException {
    public LimitExceededException(String shortUrl, Integer visitLimit) {
        super("The short URL " + shortUrl + " has reached its maximum visit limit of " + visitLimit);
    }
}
