// src/main/java/com/qabrains/pages/login/LoginPage.java

package com.qabrains.pages.login;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.qabrains.config.AppConfig;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;

/**
 * Page Object Model for the Login Page.
 * URL: https://practice.qabrains.com/ecommerce/login
 *
 * Contains:
 *   - Locators for all page elements
 *   - Getter methods for element access
 *   - Action methods for user interactions
 *   - State query methods for assertions
 *
 * This class does NOT contain any assertions or test logic.
 * Assertions belong in the test class (LoginTests.java).
 */
public class LoginPage {

    // ========================
    // PAGE INSTANCE
    // ========================
    private final Page page;

    // ========================
    // CONSTRUCTOR
    // ========================
    public LoginPage(Page page) {
        this.page = page;
    }

    // ========================
    // LOCATORS — PRIVATE (Encapsulated)
    // ========================

    // Logo / Home Button
    private Locator logoButton() {
        return page.locator(
                "[data-testid='logo'], [data-testid='app-logo'], [data-testid='logo-link'] img, " +
                "header a[aria-label*='home' i] img, header a[href*='ecommerce'] img, .logo"
        ).first();
    }

    // Login Heading
    private Locator loginHeading() {
        return page.locator("[data-testid='login-heading'], h1:has-text('Login'), h2:has-text('Login')").first();
    }

    // Email Label
    private Locator emailLabel() {
        return page.locator("label[for='email'], label:has-text('Email')").first();
    }

    // Email Input Field
    private Locator emailInput() {
        return page.locator(
                "input[data-testid='login-email'], input[data-testid='email-input'], " +
                "input[name='email'], input[type='email'], input#email"
        ).first();
    }

    // Password Label
    private Locator passwordLabel() {
        return page.locator("label[for='password'], label:has-text('Password')").first();
    }

    // Password Input Field
    private Locator passwordInput() {
        return page.locator(
                "input[data-testid='login-password'], input[data-testid='password-input'], " +
                "input[name='password'], input#password"
        ).first();
    }

    // Password Visibility Toggle Button
    private Locator passwordToggleButton() {
        return page.locator(
                "button[data-testid='password-toggle'], [data-testid='toggle-password-visibility'], " +
                "button[aria-label*='password' i], button:near(input[name='password']), button:near(#password)"
        ).first();
    }

    // Login Button
    private Locator loginButton() {
        return page.locator(
                "button[data-testid='login-submit'], button[data-testid='login-button'], " +
                "button[type='submit']:has-text('Login'), button:has-text('Login'), input[type='submit']"
        ).first();
    }

    // Error Message (Generic — for login failures)
    private Locator errorMessage() {
        return page.locator(
                "[data-testid='login-error'], [data-testid='error-message'], [role='alert'], " +
                ".error-message, .alert-danger, [class*='error'], [class*='alert']"
        ).first();
    }

    // ========================
    // URL METHODS
    // ========================

    /**
     * Returns the Login Page URL.
     */
    public String getLoginURL() {
        return AppConfig.LOGIN_URL;
    }

    /**
     * Returns the Home Page URL (post-login redirect target).
     */
    public String getHomeURL() {
        return AppConfig.BASE_URL;
    }

    /**
     * Returns the current page URL.
     */
    public String getCurrentURL() {
        return page.url();
    }

    // ========================
    // NAVIGATION METHODS
    // ========================

    /**
     * Navigates to the Login Page.
     */
    public void navigateToLoginPage() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                page.navigate(
                        AppConfig.LOGIN_URL,
                        new Page.NavigateOptions()
                                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                                .setTimeout((double) AppConfig.DEFAULT_TIMEOUT)
                );
                waitForLoginFormReady();
                System.out.println("  [OK] Navigated to: " + AppConfig.LOGIN_URL);
                return;
            } catch (RuntimeException ex) {
                if (attempt == 3) {
                    throw ex;
                }
                page.waitForTimeout(1200);
            }
        }
    }

    private void waitForLoginFormReady() {
        emailInput().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        passwordInput().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        loginButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    // ========================
    // GETTER METHODS — For assertions in test classes
    // ========================

    /**
     * Returns the Page instance (for Playwright assertions).
     */
    public Page getPage() {
        return this.page;
    }

    /**
     * Returns the Logo/Home button locator.
     */
    public Locator getLogoButton() {
        return logoButton();
    }

    /**
     * Returns the Login Heading locator.
     */
    public Locator getLoginHeading() {
        return loginHeading();
    }

    /**
     * Returns the Email Label locator.
     */
    public Locator getEmailLabel() {
        return emailLabel();
    }

    /**
     * Returns the Email Input locator.
     */
    public Locator getEmailInput() {
        return emailInput();
    }

    /**
     * Returns the Password Label locator.
     */
    public Locator getPasswordLabel() {
        return passwordLabel();
    }

    /**
     * Returns the Password Input locator.
     */
    public Locator getPasswordInput() {
        return passwordInput();
    }

    /**
     * Returns the Password Toggle Button locator.
     */
    public Locator getPasswordToggleButton() {
        return passwordToggleButton();
    }

    /**
     * Returns the Login Button locator.
     */
    public Locator getLoginButton() {
        return loginButton();
    }

    /**
     * Returns the Error Message locator.
     */
    public Locator getErrorMessage() {
        return errorMessage();
    }

    // ========================
    // ACTION METHODS — User interactions
    // ========================

    /**
     * Enters email into the email input field.
     *
     * @param email The email address to enter.
     */
    public void enterEmail(String email) {
        emailInput().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        emailInput().clear();
        emailInput().fill(email);
        System.out.println("  [INPUT] Entered email: " + email);
    }

    /**
     * Enters password into the password input field.
     *
     * @param password The password to enter.
     */
    public void enterPassword(String password) {
        passwordInput().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        passwordInput().clear();
        passwordInput().fill(password);
        System.out.println("  [INPUT] Entered password: " + "*".repeat(password.length()));
    }

    /**
     * Clicks the Login button.
     */
    public void clickLoginButton() {
        loginButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        loginButton().click();
        System.out.println("  [CLICK] Clicked Login button.");
    }

    /**
     * Clicks the Logo/Home button.
     */
    public void clickLogoButton() {
        logoButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        logoButton().click();
        System.out.println("  [CLICK] Clicked Logo/Home button.");
    }

    /**
     * Clicks the Password Visibility Toggle button.
     */
    public void clickPasswordToggle() {
        passwordToggleButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        passwordToggleButton().click();
        System.out.println("  [CLICK] Clicked Password Toggle button.");
    }

    /**
     * Performs a complete login action: enter email, password, and click login.
     *
     * @param email    The email address.
     * @param password The password.
     */
    public void performLogin(String email, String password) {
        System.out.println("  [LOGIN] Performing login...");
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Clears both email and password fields.
     */
    public void clearAllFields() {
        emailInput().clear();
        passwordInput().clear();
        System.out.println("  [CLEAR] Cleared all input fields.");
    }

    // ========================
    // STATE QUERY METHODS — For custom checks
    // ========================

    /**
     * Checks if the Login Heading is visible.
     */
    public boolean isLoginHeadingVisible() {
        return loginHeading().isVisible();
    }

    /**
     * Checks if the Email Input is visible.
     */
    public boolean isEmailInputVisible() {
        return emailInput().isVisible();
    }

    /**
     * Checks if the Password Input is visible.
     */
    public boolean isPasswordInputVisible() {
        return passwordInput().isVisible();
    }

    /**
     * Checks if the Login Button is visible.
     */
    public boolean isLoginButtonVisible() {
        return loginButton().isVisible();
    }

    /**
     * Checks if the Logo Button is visible.
     */
    public boolean isLogoButtonVisible() {
        return logoButton().isVisible();
    }

    /**
     * Checks if the Password Toggle Button is visible.
     */
    public boolean isPasswordToggleVisible() {
        return passwordToggleButton().isVisible();
    }

    /**
     * Checks if an error message is visible.
     */
    public boolean isErrorMessageVisible() {
        return errorMessage().isVisible();
    }

    /**
     * Returns the error message text.
     */
    public String getErrorMessageText() {
        return errorMessage().textContent().trim();
    }

    /**
     * Returns the Login Heading text.
     */
    public String getLoginHeadingText() {
        return loginHeading().textContent().trim();
    }

    /**
     * Returns the Email Label text.
     */
    public String getEmailLabelText() {
        return emailLabel().textContent().trim();
    }

    /**
     * Returns the Password Label text.
     */
    public String getPasswordLabelText() {
        return passwordLabel().textContent().trim();
    }

    /**
     * Returns the Login Button text.
     */
    public String getLoginButtonText() {
        return loginButton().textContent().trim();
    }

    /**
     * Returns the current type attribute of the password input.
     * "password" = masked, "text" = visible.
     */
    public String getPasswordInputType() {
        return passwordInput().getAttribute("type");
    }

    /**
     * Returns the placeholder text of the email input.
     */
    public String getEmailPlaceholder() {
        return emailInput().getAttribute("placeholder");
    }

    /**
     * Returns the placeholder text of the password input.
     */
    public String getPasswordPlaceholder() {
        return passwordInput().getAttribute("placeholder");
    }

    /**
     * Returns the value currently in the email input field.
     */
    public String getEmailInputValue() {
        return emailInput().inputValue();
    }

    /**
     * Returns the value currently in the password input field.
     */
    public String getPasswordInputValue() {
        return passwordInput().inputValue();
    }

    /**
     * Checks if the Login Button is enabled.
     */
    public boolean isLoginButtonEnabled() {
        return loginButton().isEnabled();
    }

    /**
     * Checks if the Email Input is editable.
     */
    public boolean isEmailInputEditable() {
        return emailInput().isEditable();
    }

    /**
     * Checks if the Password Input is editable.
     */
    public boolean isPasswordInputEditable() {
        return passwordInput().isEditable();
    }
}