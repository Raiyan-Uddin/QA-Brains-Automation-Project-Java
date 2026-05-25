// src/main/java/com/qabrains/config/AppConfig.java

package com.qabrains.config;

/**
 * Centralized application configuration.
 * All URLs, credentials, timeouts, and global settings live here.
 * When adding new modules (Home, Cart, etc.), add their URLs here.
 */
public final class AppConfig {

    // ========================
    // PRIVATE CONSTRUCTOR (Utility Class)
    // ========================
    private AppConfig() {
        throw new UnsupportedOperationException("AppConfig is a utility class and cannot be instantiated.");
    }

    // ========================
    // BASE URL
    // ========================
    public static final String BASE_URL = "https://practice.qabrains.com/ecommerce";

    // ========================
    // PAGE URLs
    // ========================
    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String HOME_URL = BASE_URL;
    // Add new page URLs here as you add modules:
    // public static final String CART_URL = BASE_URL + "/cart";
    // public static final String PRODUCT_DETAILS_URL = BASE_URL + "/product-details";
    // public static final String CHECKOUT_INFO_URL = BASE_URL + "/checkout-info";
    // public static final String CHECKOUT_OVERVIEW_URL = BASE_URL + "/checkout-overview";
    // public static final String CHECKOUT_COMPLETE_URL = BASE_URL + "/checkout-complete";

    // ========================
    // TEST CREDENTIALS
    // ========================
    public static final String VALID_EMAIL = "test@qabrains.com";
    public static final String VALID_PASSWORD = "Password123";
    public static final String INVALID_EMAIL = "invalid@wrong.com";
    public static final String INVALID_PASSWORD = "wrongpassword";
    public static final String EMPTY_STRING = "";

    // ========================
    // BROWSER SETTINGS
    // ========================
    public static final String BROWSER_TYPE = "chromium";  // Only Chromium browser
    public static final boolean HEADLESS = false;          // Set true for CI/CD
    public static final int SLOW_MO = 500;                 // Milliseconds between actions
    public static final int DEFAULT_TIMEOUT = 30000;       // 30 seconds
    public static final int VIEWPORT_WIDTH = 1920;
    public static final int VIEWPORT_HEIGHT = 1080;

    // ========================
    // LOGIN PAGE EXPECTED VALUES
    // ========================
    public static final String LOGIN_HEADING_TEXT = "Login";
    public static final String EMAIL_LABEL_TEXT = "Email";
    public static final String PASSWORD_LABEL_TEXT = "Password";
    public static final String LOGIN_BUTTON_TEXT = "Login";
    public static final String EMAIL_PLACEHOLDER = "eg. user@user.com";
    public static final String PASSWORD_PLACEHOLDER = "*******";
}