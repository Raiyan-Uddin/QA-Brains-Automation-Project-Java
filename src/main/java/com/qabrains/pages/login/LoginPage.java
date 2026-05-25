// src/main/java/com/qabrains/pages/login/LoginPage.java

package com.qabrains.pages.login;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.qabrains.config.AppConfig;

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
        return page.locator("a[href*='ecommerce'] img, header a img, .logo, a[aria-label*='home'] img").first();
    }

    // Login Heading
    private Locator loginHeading() {
        return page.locator("h1, h2, h3").filter(new Locator.FilterOptions().setHasText("Login")).first();
    }

    // Email Label
    private Locator emailLabel() {
        return page.locator("label").filter(new Locator.FilterOptions().setHasText("Email")).first();
    }

    // Email Input Field
    private Locator emailInput() {
        return page.locator("input#email:visible, input[name='email']:visible, input[type='email']:visible").first();
    }

    // Password Label
    private Locator passwordLabel() {
        return page.locator("label").filter(new Locator.FilterOptions().setHasText("Password")).first();
    }

    // Password Input Field
    private Locator passwordInput() {
        return page.locator("input#password:visible, input[name='password']:visible").first();
    }

    // Password Visibility Toggle Button
    private Locator passwordToggleButton() {
        return page.locator("button:near(input[name='password']), button:near(#password), [class*='toggle'], [class*='eye'], [aria-label*='password' i]").first();
    }

    // Login Button
    private Locator loginButton() {
        return page.locator("button[type='submit'], button:has-text('Login'), input[type='submit']").first();
    }

    // Error Message (Generic — for login failures)
    private Locator errorMessage() {
        return page.locator("[class*='error'], [class*='alert'], [role='alert'], .error-message, .alert-danger").first();
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
        page.navigate(AppConfig.LOGIN_URL);
        page.waitForLoadState();
        System.out.println("  📍 Navigated to: " + AppConfig.LOGIN_URL);
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
        emailInput().clear();
        emailInput().fill(email);
        System.out.println("  📧 Entered email: " + email);
    }

    /**
     * Enters password into the password input field.
     *
     * @param password The password to enter.
     */
    public void enterPassword(String password) {
        passwordInput().clear();
        passwordInput().fill(password);
        System.out.println("  🔑 Entered password: " + "*".repeat(password.length()));
    }

    /**
     * Clicks the Login button.
     */
    public void clickLoginButton() {
        loginButton().click();
        System.out.println("  🖱 Clicked Login button.");
    }

    /**
     * Clicks the Logo/Home button.
     */
    public void clickLogoButton() {
        logoButton().click();
        System.out.println("  🖱 Clicked Logo/Home button.");
    }

    /**
     * Clicks the Password Visibility Toggle button.
     */
    public void clickPasswordToggle() {
        passwordToggleButton().click();
        System.out.println("  🖱 Clicked Password Toggle button.");
    }

    /**
     * Performs a complete login action: enter email, password, and click login.
     *
     * @param email    The email address.
     * @param password The password.
     */
    public void performLogin(String email, String password) {
        System.out.println("  🔐 Performing login...");
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
        System.out.println("  🧹 Cleared all input fields.");
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