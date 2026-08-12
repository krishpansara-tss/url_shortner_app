package com.tssconsultancy.url_sortner_app.exceptions.derived;

import com.tssconsultancy.url_sortner_app.exceptions.base.DuplicateResourceException;

public class CustomAliasExistsException extends DuplicateResourceException {
    public CustomAliasExistsException(String alias) {
        super("The custom alias already exists: " + alias);
    }
}
