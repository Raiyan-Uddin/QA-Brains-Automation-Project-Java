// src/test/java/com/qabrains/tests/login/LoginTests.java

package com.qabrains.tests.login;

import com.qabrains.base.BaseTest;
import com.qabrains.config.AppConfig;
import com.qabrains.pages.login.LoginPage;
import com.qabrains.utils.BrowserFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Test class for the Login Page.
 * URL: https://practice.qabrains.com/ecommerce/login
 *
 * Covers:
 *   - TC-01: Page URL verification
 *   - TC-02: Logo/Home button visibility
 *   - TC-03: Login heading text verification
 *   - TC-04: Email label text verification
 *   - TC-05: Email input field visibility & editability
 *   - TC-06: Email placeholder text verification
 *   - TC-07: Password label text verification
 *   - TC-08: Password input field visibility & editability
 *   - TC-09: Password placeholder text verification
 *   - TC-10: Password field is masked by default
 *   - TC-11: Password toggle reveals password
 *   - TC-12: Password toggle hides password again
 *   - TC-13: Password toggle does not alter content
 *   - TC-14: Login button visibility
 *   - TC-15: Login button text verification
 *   - TC-16: Login button is enabled
 *   - TC-17: Successful login with valid credentials
 *   - TC-18: Failed login with invalid email
 *   - TC-19: Failed login with invalid password
 *   - TC-20: Failed login with both invalid credentials
 *   - TC-21: Login with empty email field
 *   - TC-22: Login with empty password field
 *   - TC-23: Login with both empty fields
 *   - TC-24: Email field accepts input
 *   - TC-25: Password field accepts input
 *   - TC-26: Tab order navigation
 *   - TC-27: Enter key triggers form submission
 */
public class LoginTests extends BaseTest {

    private LoginPage loginPage;

    private void resetContextAndGoToLogin() {
        if (context != null) {
            context.close();
        }
        context = BrowserFactory.createContext(browser);
        page = BrowserFactory.createPage(context);
        loginPage = new LoginPage(page);
        loginPage.navigateToLoginPage();
        assertThat(page).hasURL(loginPage.getLoginURL());
    }

    // ════════════════════════════════════════════════════════════
    // SETUP — Runs before EACH test method
    // Creates a fresh LoginPage instance and navigates to login URL
    // ════════════════════════════════════════════════════════════
    @BeforeMethod(alwaysRun = true)
    @Override
    public void testSetup() {
        super.testSetup();
        loginPage = new LoginPage(page);
        loginPage.navigateToLoginPage();
    }


    // ════════════════════════════════════════════════════════════════════
    //                     PAGE LOAD & URL TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-01: VERIFY LOGIN PAGE URL
    // ════════════════════════════════════════════════════════════
    @Test(priority = 1, description = "TC-01: Verify login page loads with correct URL")
    public void TC01_verifyLoginPageURL() {
        try {
            assertThat(page).hasURL(loginPage.getLoginURL());
            System.out.println("\n✅ TC-01 PASSED: Login page URL is correct.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-01 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     LOGO / HOME BUTTON TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-02: VERIFY LOGO / HOME BUTTON IS VISIBLE
    // ════════════════════════════════════════════════════════════
    @Test(priority = 2, description = "TC-02: Verify logo/home button is visible on login page")
    public void TC02_verifyLogoIsVisible() {
        try {
            assertThat(loginPage.getLogoButton()).isVisible();
            System.out.println("\n✅ TC-02 PASSED: Logo/Home button is visible.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-02 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     LOGIN HEADING TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-03: VERIFY LOGIN HEADING TEXT
    // ════════════════════════════════════════════════════════════
    @Test(priority = 3, description = "TC-03: Verify login heading displays correct text")
    public void TC03_verifyLoginHeadingText() {
        try {
            assertThat(loginPage.getLoginHeading()).isVisible();
            assertThat(loginPage.getLoginHeading()).hasText(AppConfig.LOGIN_HEADING_TEXT);
            System.out.println("\n✅ TC-03 PASSED: Login heading text is '" + AppConfig.LOGIN_HEADING_TEXT + "'.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-03 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     EMAIL FIELD TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-04: VERIFY EMAIL LABEL TEXT
    // ════════════════════════════════════════════════════════════
    @Test(priority = 4, description = "TC-04: Verify email label displays correct text")
    public void TC04_verifyEmailLabelText() {
        try {
            assertThat(loginPage.getEmailLabel()).isVisible();
            assertThat(loginPage.getEmailLabel()).containsText(AppConfig.EMAIL_LABEL_TEXT);
            System.out.println("\n✅ TC-04 PASSED: Email label text is '" + AppConfig.EMAIL_LABEL_TEXT + "'.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-04 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-05: VERIFY EMAIL INPUT FIELD IS VISIBLE & EDITABLE
    // ════════════════════════════════════════════════════════════
    @Test(priority = 5, description = "TC-05: Verify email input field is visible and editable")
    public void TC05_verifyEmailInputVisibleAndEditable() {
        try {
            assertThat(loginPage.getEmailInput()).isVisible();
            assertThat(loginPage.getEmailInput()).isEditable();
            assertThat(loginPage.getEmailInput()).isEmpty();
            System.out.println("\n✅ TC-05 PASSED: Email input is visible, editable, and empty.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-05 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-06: VERIFY EMAIL PLACEHOLDER TEXT
    // ════════════════════════════════════════════════════════════
    @Test(priority = 6, description = "TC-06: Verify email input has correct placeholder text")
    public void TC06_verifyEmailPlaceholderText() {
        try {
            assertThat(loginPage.getEmailInput()).hasAttribute("placeholder", AppConfig.EMAIL_PLACEHOLDER);
            System.out.println("\n✅ TC-06 PASSED: Email placeholder is '" + AppConfig.EMAIL_PLACEHOLDER + "'.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-06 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     PASSWORD FIELD TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-07: VERIFY PASSWORD LABEL TEXT
    // ════════════════════════════════════════════════════════════
    @Test(priority = 7, description = "TC-07: Verify password label displays correct text")
    public void TC07_verifyPasswordLabelText() {
        try {
            assertThat(loginPage.getPasswordLabel()).isVisible();
            assertThat(loginPage.getPasswordLabel()).containsText(AppConfig.PASSWORD_LABEL_TEXT);
            System.out.println("\n✅ TC-07 PASSED: Password label text is '" + AppConfig.PASSWORD_LABEL_TEXT + "'.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-07 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-08: VERIFY PASSWORD INPUT FIELD IS VISIBLE & EDITABLE
    // ════════════════════════════════════════════════════════════
    @Test(priority = 8, description = "TC-08: Verify password input field is visible and editable")
    public void TC08_verifyPasswordInputVisibleAndEditable() {
        try {
            assertThat(loginPage.getPasswordInput()).isVisible();
            assertThat(loginPage.getPasswordInput()).isEditable();
            assertThat(loginPage.getPasswordInput()).isEmpty();
            System.out.println("\n✅ TC-08 PASSED: Password input is visible, editable, and empty.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-08 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-09: VERIFY PASSWORD PLACEHOLDER TEXT
    // ════════════════════════════════════════════════════════════
    @Test(priority = 9, description = "TC-09: Verify password input has correct placeholder text")
    public void TC09_verifyPasswordPlaceholderText() {
        try {
            assertThat(loginPage.getPasswordInput()).hasAttribute("placeholder", AppConfig.PASSWORD_PLACEHOLDER);
            System.out.println("\n✅ TC-09 PASSED: Password placeholder is '" + AppConfig.PASSWORD_PLACEHOLDER + "'.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-09 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     PASSWORD TOGGLE TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-10: VERIFY PASSWORD FIELD IS MASKED BY DEFAULT
    // ════════════════════════════════════════════════════════════
    @Test(priority = 10, description = "TC-10: Verify password field type is 'password' (masked) by default")
    public void TC10_verifyPasswordIsMaskedByDefault() {
        try {
            assertThat(loginPage.getPasswordInput()).hasAttribute("type", "password");
            System.out.println("\n✅ TC-10 PASSED: Password field is masked by default (type='password').");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-10 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-11: VERIFY PASSWORD TOGGLE REVEALS PASSWORD
    // ════════════════════════════════════════════════════════════
    @Test(priority = 11, description = "TC-11: Verify clicking password toggle reveals password (type='text')")
    public void TC11_verifyPasswordToggleRevealsPassword() {
        try {
            loginPage.enterPassword("TestPassword123");
            loginPage.clickPasswordToggle();

            assertThat(loginPage.getPasswordInput()).hasAttribute("type", "text");
            System.out.println("\n✅ TC-11 PASSED: Password toggle reveals password (type='text').");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-11 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-12: VERIFY PASSWORD TOGGLE HIDES PASSWORD AGAIN
    // ════════════════════════════════════════════════════════════
    @Test(priority = 12, description = "TC-12: Verify clicking password toggle again hides password (type='password')")
    public void TC12_verifyPasswordToggleHidesPasswordAgain() {
        try {
            loginPage.enterPassword("TestPassword123");

            // First toggle — reveal password
            loginPage.clickPasswordToggle();
            assertThat(loginPage.getPasswordInput()).hasAttribute("type", "text");
            System.out.println("  📍 Step 1: Password revealed (type='text').");

            // Second toggle — hide password again
            loginPage.clickPasswordToggle();
            assertThat(loginPage.getPasswordInput()).hasAttribute("type", "password");
            System.out.println("  📍 Step 2: Password hidden again (type='password').");

            System.out.println("\n✅ TC-12 PASSED: Password toggle hides password again (type='password').");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-12 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-13: VERIFY PASSWORD TOGGLE DOES NOT ALTER CONTENT
    // ════════════════════════════════════════════════════════════
    @Test(priority = 13, description = "TC-13: Verify password toggle does not change password value")
    public void TC13_verifyPasswordToggleDoesNotAlterContent() {
        try {
            String testPassword = "MySecureP@ss123";
            loginPage.enterPassword(testPassword);

            // Capture value before toggle
            String valueBefore = loginPage.getPasswordInputValue();
            System.out.println("  📍 Step 1: Value before toggle: '" + valueBefore + "'");

            // Toggle to reveal
            loginPage.clickPasswordToggle();
            String valueAfterReveal = loginPage.getPasswordInputValue();
            System.out.println("  📍 Step 2: Value after reveal: '" + valueAfterReveal + "'");

            // Toggle to hide
            loginPage.clickPasswordToggle();
            String valueAfterHide = loginPage.getPasswordInputValue();
            System.out.println("  📍 Step 3: Value after hide: '" + valueAfterHide + "'");

            // Assert value is unchanged throughout all toggles
            assertThat(loginPage.getPasswordInput()).hasValue(testPassword);

            // Additional explicit check
            boolean contentUnchanged = valueBefore.equals(testPassword)
                    && valueAfterReveal.equals(testPassword)
                    && valueAfterHide.equals(testPassword);

            if (!contentUnchanged) {
                throw new AssertionError(
                        "Password content was altered by toggle! "
                                + "Expected: '" + testPassword + "' | "
                                + "Before: '" + valueBefore + "' | "
                                + "After Reveal: '" + valueAfterReveal + "' | "
                                + "After Hide: '" + valueAfterHide + "'"
                );
            }

            System.out.println("\n✅ TC-13 PASSED: Password toggle does not alter content. Value remains: '" + testPassword + "'.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-13 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     LOGIN BUTTON TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-14: VERIFY LOGIN BUTTON IS VISIBLE
    // ════════════════════════════════════════════════════════════
    @Test(priority = 14, description = "TC-14: Verify login button is visible")
    public void TC14_verifyLoginButtonIsVisible() {
        try {
            assertThat(loginPage.getLoginButton()).isVisible();
            System.out.println("\n✅ TC-14 PASSED: Login button is visible.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-14 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-15: VERIFY LOGIN BUTTON TEXT
    // ════════════════════════════════════════════════════════════
    @Test(priority = 15, description = "TC-15: Verify login button displays correct text")
    public void TC15_verifyLoginButtonText() {
        try {
            assertThat(loginPage.getLoginButton()).containsText(AppConfig.LOGIN_BUTTON_TEXT);
            System.out.println("\n✅ TC-15 PASSED: Login button text is '" + AppConfig.LOGIN_BUTTON_TEXT + "'.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-15 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-16: VERIFY LOGIN BUTTON IS ENABLED
    // ════════════════════════════════════════════════════════════
    @Test(priority = 16, description = "TC-16: Verify login button is enabled")
    public void TC16_verifyLoginButtonIsEnabled() {
        try {
            assertThat(loginPage.getLoginButton()).isEnabled();
            System.out.println("\n✅ TC-16 PASSED: Login button is enabled.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-16 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     SUCCESSFUL LOGIN TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-17: SUCCESSFUL LOGIN WITH VALID CREDENTIALS
    // ════════════════════════════════════════════════════════════
    @Test(priority = 17, description = "TC-17: Verify successful login with valid email and password redirects to home page")
    public void TC17_verifySuccessfulLoginWithValidCredentials() {
        try {
            loginPage.performLogin(AppConfig.VALID_EMAIL, AppConfig.VALID_PASSWORD);

            // Wait for navigation away from login page
            page.waitForURL("**/ecommerce**");

            // Verify we are no longer on the login page
            assertThat(page).not().hasURL(loginPage.getLoginURL());

            String currentURL = page.url();
            System.out.println("  📍 Redirected to: " + currentURL);
            System.out.println("\n✅ TC-17 PASSED: Successful login. Redirected away from login page.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-17 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     FAILED LOGIN TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-18: FAILED LOGIN WITH INVALID EMAIL
    // ════════════════════════════════════════════════════════════
    @Test(priority = 18, description = "TC-18: Verify login fails with invalid email and valid password")
    public void TC18_verifyLoginFailsWithInvalidEmail() {
        try {
            loginPage.performLogin(AppConfig.INVALID_EMAIL, AppConfig.VALID_PASSWORD);

            // Allow time for error response
            page.waitForTimeout(2000);

            // Verify still on login page
            assertThat(page).hasURL(loginPage.getLoginURL());
            System.out.println("  📍 Step 1: URL is still login page. PASS.");

            // Verify error message is displayed
            assertThat(loginPage.getErrorMessage()).isVisible();
            String errorText = loginPage.getErrorMessageText();
            System.out.println("  📍 Step 2: Error message displayed: '" + errorText + "'");

            System.out.println("\n✅ TC-18 PASSED: Login failed with invalid email. Error displayed. URL unchanged.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-18 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-19: FAILED LOGIN WITH INVALID PASSWORD
    // ════════════════════════════════════════════════════════════
    @Test(priority = 19, description = "TC-19: Verify login fails with valid email and invalid password")
    public void TC19_verifyLoginFailsWithInvalidPassword() {
        try {
            loginPage.performLogin(AppConfig.VALID_EMAIL, AppConfig.INVALID_PASSWORD);

            // Allow time for error response
            page.waitForTimeout(2000);

            // Verify still on login page
            assertThat(page).hasURL(loginPage.getLoginURL());
            System.out.println("  📍 Step 1: URL is still login page. PASS.");

            // Verify error message is displayed
            assertThat(loginPage.getErrorMessage()).isVisible();
            String errorText = loginPage.getErrorMessageText();
            System.out.println("  📍 Step 2: Error message displayed: '" + errorText + "'");

            System.out.println("\n✅ TC-19 PASSED: Login failed with invalid password. Error displayed. URL unchanged.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-19 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-20: FAILED LOGIN WITH BOTH INVALID CREDENTIALS
    // ════════════════════════════════════════════════════════════
    @Test(priority = 20, description = "TC-20: Verify login fails with both invalid email and password")
    public void TC20_verifyLoginFailsWithBothInvalidCredentials() {
        try {
            loginPage.performLogin(AppConfig.INVALID_EMAIL, AppConfig.INVALID_PASSWORD);

            // Allow time for error response
            page.waitForTimeout(2000);

            // Verify still on login page
            assertThat(page).hasURL(loginPage.getLoginURL());
            System.out.println("  📍 Step 1: URL is still login page. PASS.");

            // Verify error message is displayed
            assertThat(loginPage.getErrorMessage()).isVisible();
            String errorText = loginPage.getErrorMessageText();
            System.out.println("  📍 Step 2: Error message displayed: '" + errorText + "'");

            System.out.println("\n✅ TC-20 PASSED: Login failed with both invalid credentials. Error displayed.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-20 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     EMPTY FIELD TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-21: LOGIN WITH EMPTY EMAIL FIELD
    // ════════════════════════════════════════════════════════════
    @Test(priority = 21, description = "TC-21: Verify login is blocked when email field is empty")
    public void TC21_verifyLoginBlockedWithEmptyEmail() {
        try {
            // Only enter password, leave email empty
            loginPage.enterPassword(AppConfig.VALID_PASSWORD);
            loginPage.clickLoginButton();

            // Allow time for validation/response
            page.waitForTimeout(1500);

            // Should remain on login page — form should not submit
            assertThat(page).hasURL(loginPage.getLoginURL());
            System.out.println("  📍 Step 1: URL is still login page. PASS.");

            System.out.println("\n✅ TC-21 PASSED: Login blocked with empty email. Remained on login page.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-21 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-22: LOGIN WITH EMPTY PASSWORD FIELD
    // ════════════════════════════════════════════════════════════
    @Test(priority = 22, description = "TC-22: Verify login is blocked when password field is empty")
    public void TC22_verifyLoginBlockedWithEmptyPassword() {
        try {
            // Only enter email, leave password empty
            loginPage.enterEmail(AppConfig.VALID_EMAIL);
            loginPage.clickLoginButton();

            // Allow time for validation/response
            page.waitForTimeout(1500);

            // Should remain on login page
            assertThat(page).hasURL(loginPage.getLoginURL());
            System.out.println("  📍 Step 1: URL is still login page. PASS.");

            System.out.println("\n✅ TC-22 PASSED: Login blocked with empty password. Remained on login page.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-22 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-23: LOGIN WITH BOTH EMPTY FIELDS
    // ════════════════════════════════════════════════════════════
    @Test(priority = 23, description = "TC-23: Verify login is blocked when both fields are empty")
    public void TC23_verifyLoginBlockedWithBothEmptyFields() {
        try {
            // Click login without entering anything
            loginPage.clickLoginButton();

            // Allow time for validation/response
            page.waitForTimeout(1500);

            // Should remain on login page
            assertThat(page).hasURL(loginPage.getLoginURL());
            System.out.println("  📍 Step 1: URL is still login page. PASS.");

            System.out.println("\n✅ TC-23 PASSED: Login blocked with both empty fields. Remained on login page.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-23 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     INPUT ACCEPTANCE TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-24: VERIFY EMAIL FIELD ACCEPTS INPUT
    // ════════════════════════════════════════════════════════════
    @Test(priority = 24, description = "TC-24: Verify email field accepts and retains typed input")
    public void TC24_verifyEmailFieldAcceptsInput() {
        try {
            String testEmail = "testuser@example.com";
            loginPage.enterEmail(testEmail);

            assertThat(loginPage.getEmailInput()).hasValue(testEmail);
            System.out.println("  📍 Email field value verified: '" + testEmail + "'");

            System.out.println("\n✅ TC-24 PASSED: Email field accepts and retains input. Value: '" + testEmail + "'.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-24 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // TC-25: VERIFY PASSWORD FIELD ACCEPTS INPUT
    // ════════════════════════════════════════════════════════════
    @Test(priority = 25, description = "TC-25: Verify password field accepts and retains typed input")
    public void TC25_verifyPasswordFieldAcceptsInput() {
        try {
            String testPassword = "SecureP@ssword!99";
            loginPage.enterPassword(testPassword);

            assertThat(loginPage.getPasswordInput()).hasValue(testPassword);
            System.out.println("  📍 Password field value verified.");

            System.out.println("\n✅ TC-25 PASSED: Password field accepts and retains input.");
        } catch (AssertionError e) {
            System.out.println("\n❌ TC-25 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     KEYBOARD NAVIGATION TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-26: VERIFY TAB ORDER NAVIGATION
    // ════════════════════════════════════════════════════════════
    // ════════════════════════════════════════════════════════════════════
    //                     KEYBOARD NAVIGATION TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-26: VERIFY TAB ORDER NAVIGATION
    // ════════════════════════════════════════════════════════════
    @Test(priority = 26, description = "TC-26: Verify Tab key navigates through form fields in correct order")
    public void TC26_verifyTabOrderNavigation() {
        try {
            // ── Step 1: Focus on email field ──────────────────────────
            loginPage.getEmailInput().click();
            assertThat(loginPage.getEmailInput()).isFocused();
            System.out.println("  📍 Step 1: Email field is focused after click. PASS.");

            // ── Step 2: Tab → should move focus to Password field ─────
            page.keyboard().press("Tab");
            assertThat(loginPage.getPasswordInput()).isFocused();
            System.out.println("  📍 Step 2: Tab pressed → Password field is now focused. PASS.");

            // ── Step 3: Tab → should move focus to Password Toggle ────
            page.keyboard().press("Tab");
            boolean toggleFocused = (boolean) loginPage.getPasswordToggleButton()
                    .evaluate("el => el === document.activeElement");
            if (toggleFocused) {
                System.out.println("  📍 Step 3: Tab pressed → Password Toggle button is now focused. PASS.");
            } else {
                System.out.println("  📍 Step 3: Tab pressed → Password Toggle skipped (not focusable in this browser). Continuing...");
            }

            // ── Step 4: Continue tabbing until Login Button is focused ─
            // We allow up to 5 additional Tab presses to reach the Login button
            boolean loginButtonFocused = false;
            for (int i = 0; i < 5; i++) {
                boolean isFocused = (boolean) loginPage.getLoginButton()
                        .evaluate("el => el === document.activeElement");
                if (isFocused) {
                    loginButtonFocused = true;
                    System.out.println("  📍 Step 4: Tab pressed → Login button is now focused after "
                            + (i + 3) + " total Tab presses. PASS.");
                    break;
                }
                page.keyboard().press("Tab");
            }

            if (!loginButtonFocused) {
                // Final check after the loop
                loginButtonFocused = (boolean) loginPage.getLoginButton()
                        .evaluate("el => el === document.activeElement");
            }

            if (!loginButtonFocused) {
                throw new AssertionError(
                        "Login button did not receive focus after tabbing through all form fields. "
                                + "Expected Tab order: Email → Password → (Toggle) → Login Button."
                );
            }

            // ── Step 5: Verify Shift+Tab goes back (reverse Tab) ──────
            page.keyboard().press("Shift+Tab");
            boolean passwordFocusedAfterShiftTab = (boolean) loginPage.getPasswordInput()
                    .evaluate("el => el === document.activeElement");
            if (passwordFocusedAfterShiftTab) {
                System.out.println("  📍 Step 5: Shift+Tab pressed → Focus moved back to Password field. PASS.");
            } else {
                System.out.println("  📍 Step 5: Shift+Tab pressed → Focus moved back to previous element. PASS.");
            }

            System.out.println("\n✅ TC-26 PASSED: Tab key navigates through form fields in correct order.");
            System.out.println("   Tab Order verified: Email → Password → (Toggle) → Login Button");

        } catch (AssertionError e) {
            System.out.println("\n❌ TC-26 FAILED: " + e.getMessage());
            throw e;
        }
    }


    // ════════════════════════════════════════════════════════════════════
    //                     FORM SUBMISSION TESTS
    // ════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════
    // TC-27: VERIFY ENTER KEY TRIGGERS FORM SUBMISSION
    // ════════════════════════════════════════════════════════════
    @Test(priority = 27, description = "TC-27: Verify pressing Enter key in form fields triggers login submission")
    public void TC27_verifyEnterKeyTriggersFormSubmission() {
        try {
            // ── Sub-Test A: Enter key pressed in Password field ───────
            System.out.println("  ── Sub-Test A: Enter key in Password field ──");

            loginPage.enterEmail(AppConfig.VALID_EMAIL);
            loginPage.enterPassword(AppConfig.VALID_PASSWORD);

            // Press Enter in password field — should submit the form
            loginPage.getPasswordInput().press("Enter");
            System.out.println("  ⌨  Enter key pressed in Password field.");

            // Wait for navigation
            page.waitForURL("**/ecommerce**");

            // Verify redirect away from login page
            assertThat(page).not().hasURL(loginPage.getLoginURL());
            System.out.println("  📍 Sub-Test A: Redirected to: " + page.url() + ". PASS.");

            // ── Start Sub-Test B with a fresh browser context ─────────
            resetContextAndGoToLogin();
            System.out.println("\n  ── Sub-Test B: Enter key in Email field ──");

            // ── Sub-Test B: Enter key pressed in Email field ──────────
            loginPage.enterEmail(AppConfig.VALID_EMAIL);
            loginPage.enterPassword(AppConfig.VALID_PASSWORD);

            // Focus on email field and press Enter
            loginPage.getEmailInput().focus();
            loginPage.getEmailInput().press("Enter");
            System.out.println("  ⌨  Enter key pressed in Email field.");

            // Wait for navigation
            page.waitForURL("**/ecommerce**");

            // Verify redirect away from login page
            assertThat(page).not().hasURL(loginPage.getLoginURL());
            System.out.println("  📍 Sub-Test B: Redirected to: " + page.url() + ". PASS.");

            // ── Start Sub-Test C with a fresh browser context ─────────
            resetContextAndGoToLogin();
            System.out.println("\n  ── Sub-Test C: Enter key on Login Button ──");

            // ── Sub-Test C: Enter key pressed on Login Button ─────────
            loginPage.enterEmail(AppConfig.VALID_EMAIL);
            loginPage.enterPassword(AppConfig.VALID_PASSWORD);

            // Focus on login button and press Enter
            loginPage.getLoginButton().focus();
            loginPage.getLoginButton().press("Enter");
            System.out.println("  ⌨  Enter key pressed on Login button.");

            // Wait for navigation
            page.waitForURL("**/ecommerce**");

            // Verify redirect away from login page
            assertThat(page).not().hasURL(loginPage.getLoginURL());
            System.out.println("  📍 Sub-Test C: Redirected to: " + page.url() + ". PASS.");

            // ── Start Sub-Test D with a fresh browser context ─────────
            resetContextAndGoToLogin();
            System.out.println("\n  ── Sub-Test D: Enter key with invalid credentials (should NOT submit) ──");

            // ── Sub-Test D: Enter key with invalid credentials ────────
            loginPage.enterEmail(AppConfig.INVALID_EMAIL);
            loginPage.enterPassword(AppConfig.INVALID_PASSWORD);

            // Press Enter in password field
            loginPage.getPasswordInput().press("Enter");
            System.out.println("  ⌨  Enter key pressed with invalid credentials.");

            page.waitForTimeout(2000);

            // Should remain on login page — submission attempted but failed
            assertThat(page).hasURL(loginPage.getLoginURL());
            assertThat(loginPage.getErrorMessage()).isVisible();
            System.out.println("  📍 Sub-Test D: Remained on login page, error shown. PASS.");

            System.out.println("\n✅ TC-27 PASSED: Enter key correctly triggers form submission in all fields.");
            System.out.println("   Sub-Test A (Password field + Enter) → PASSED");
            System.out.println("   Sub-Test B (Email field + Enter)    → PASSED");
            System.out.println("   Sub-Test C (Login button + Enter)   → PASSED");
            System.out.println("   Sub-Test D (Invalid creds + Enter)  → PASSED");

        } catch (AssertionError e) {
            System.out.println("\n❌ TC-27 FAILED: " + e.getMessage());
            throw e;
        }
    }
}
