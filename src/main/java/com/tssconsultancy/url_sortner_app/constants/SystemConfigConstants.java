package com.tssconsultancy.url_sortner_app.constants;

public final class SystemConfigConstants {

    private SystemConfigConstants() {
    }

    // System Configuration Keys
    public static final String MAX_VISITS_PER_FREE_URL = "MAX_VISITS_PER_FREE_URL";
    public static final String RENEWAL_FEE = "RENEWAL_FEE";
    public static final String RENEWAL_VISITS_GRANTED = "RENEWAL_VISITS_GRANTED";
    public static final String FREE_URL_QUOTA_PER_USER = "FREE_URL_QUOTA_PER_USER";
    public static final String PRICE_PER_ADDITIONAL_SLOT = "PRICE_PER_ADDITIONAL_SLOT";
    public static final String PRICE_QR_CODE = "PRICE_QR_CODE";
    public static final String PRICE_CUSTOM_ALIAS = "PRICE_CUSTOM_ALIAS";

    // Fallback Default Values
    public static final int FALLBACK_MAX_VISITS_PER_FREE_URL = 105;
    public static final double FALLBACK_RENEWAL_FEE = 50;
    public static final int FALLBACK_RENEWAL_VISITS_GRANTED = 500;
    public static final int FALLBACK_FREE_URL_QUOTA_PER_USER = 10;
    public static final double FALLBACK_PRICE_PER_ADDITIONAL_SLOT = 20;
    public static final double FALLBACK_PRICE_QR_CODE = 25.0;
    public static final double FALLBACK_PRICE_CUSTOM_ALIAS = 100.0;
}
