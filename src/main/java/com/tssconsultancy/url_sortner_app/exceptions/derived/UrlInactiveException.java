package com.tssconsultancy.url_sortner_app.exceptions.derived;

import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import com.tssconsultancy.url_sortner_app.exceptions.base.InvalidOperationException;

public class UrlInactiveException extends InvalidOperationException {
    public UrlInactiveException(String shortUrl, UrlStatus status) {
        super("Short URL '" + shortUrl + "' is not active. Current status: " + status);
    }
}
