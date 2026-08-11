package com.tssconsultancy.url_sortner_app.utils;

import java.security.SecureRandom;

public class ShortAliasGenerator {
    private static final String BASE62 =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
        "abcdefghijklmnopqrstuvwxyz" +
        "0123456789";

    private static final int ALIAS_LENGTH = 7;

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        StringBuilder alias = new StringBuilder(ALIAS_LENGTH);

        for (int i = 0; i < ALIAS_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(BASE62.length());
            alias.append(BASE62.charAt(randomIndex));
        }

        return alias.toString();
    }
}