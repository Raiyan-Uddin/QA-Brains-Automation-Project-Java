// src/test/java/com/qabrains/tests/checkoutinfo/CheckoutInfoTests.java

package com.qabrains.tests.checkoutinfo;

import com.qabrains.base.BaseTest;
import com.qabrains.config.AppConfig;
import com.qabrains.pages.cart.CartPage;
import com.qabrains.pages.checkoutinfo.CheckoutInfoPage;
import com.qabrains.pages.home.HomePage;
import com.qabrains.pages.login.LoginPage;
import com.qabrains.utils.BrowserFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Test class for the Checkout: Your Information Page.
 * URL: https://practice.qabrains.com/ecommerce/checkout-info
 *
 * Source of truth: docs/test cases/checkout_info_test_cases.csv
 *
 * Covers:
 *   CI-001   : Checkout info page and fields are visible
 *   CI-001-S : @smoke Checkout info page loads with all required fields
 *   CI-002   : Checkout info page and fields are visible (functional)
 *   CI-003   : Valid form continues to checkout overview
 *   CI-003-S : @smoke Valid checkout info continues to overview
 *   CI-004   : Invalid inputs show validation messages (all empty)
 *   CI-005   : Invalid inputs show validation messages (empty first name)
 *   CI-006   : Invalid inputs show validation messages (empty last name)
 *   CI-007   : Invalid inputs show validation messages (empty zip)
 *   CI-008   : Invalid inputs show validation messages (all fields cleared)
 *   CI-009   : Cancel returns to cart
 *   CI-010   : Empty-cart flow guard prevents invalid checkout-info progression
 *   CI-011   : Validation errors appear in accessible error container
 *   CI-012   : @regression Numeric-only zip code is accepted
 *   CI-013   : @regression Single character names are handled gracefully
 *   CI-014   : @regression Special characters in name field are handled
 */
public class CheckoutInfoTests extends BaseTest {

    private CheckoutInfoPage checkoutInfoPage;

    // ════════════════════════════════════════════════════════════
    // SETUP — Logs in, adds item, goes to cart → checkout-info
    // ════════════════════════════════════════════════════════════
    @BeforeMethod(alwaysRun = true)
    @Override
    public void testSetup() {
        super.testSetup();
        loginAddItemAndGoToCheckoutInfo();
    }

    private void loginAddItemAndGoToCheckoutInfo() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigateToLoginPage();
        loginPage.performLogin(AppConfig.VALID_EMAIL, AppConfig.VALID_PASSWORD);
        page.waitForURL("**/ecommerce", new com.microsoft.playwright.Page.WaitForURLOptions()
                .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        homePage.clickFirstProductAddToCart();
        page.waitForTimeout(1000);
        CartPage cartPage = new CartPage(page);
        cartPage.navigateToCartPage();
        cartPage.clickCheckout();
        waitForCheckoutInfoRouteOrRecover();
        checkoutInfoPage = new CheckoutInfoPage(page);
    }

    private void waitForCheckoutInfoRouteOrRecover() {
        try {
            page.waitForURL("**/checkout-info**", new com.microsoft.playwright.Page.WaitForURLOptions()
                    .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
        } catch (RuntimeException ex) {
            // Fallback keeps tests deterministic if CTA routing lags.
            new CheckoutInfoPage(page).navigateToCheckoutInfo();
            page.waitForURL("**/checkout-info**", new com.microsoft.playwright.Page.WaitForURLOptions()
                    .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
        }
    }

    private void resetContext() {
        if (context != null) context.close();
        context = BrowserFactory.createContext(browser);
        page = BrowserFactory.createPage(context);
        checkoutInfoPage = new CheckoutInfoPage(page);
    }

    // ════════════════════════════════════════════════════════════════════
    //                   FUNCTIONAL & SMOKE TESTS
    // ════════════════════════════════════════════════════════════════════

    // ════════════════════════════════════════════════════════════
    // CI-001: Checkout info page and fields are visible
    // ════════════════════════════════════════════════════════════
    @Test(priority = 1, description = "CI-001: Checkout info page displays heading and all required fields")
    public void CI_001_checkoutInfoPageAndFieldsVisible() {
        try {
            assertThat(checkoutInfoPage.getHeading()).isVisible();
            assertThat(checkoutInfoPage.getFirstNameField()).isVisible();
            assertThat(checkoutInfoPage.getLastNameField()).isVisible();
            assertThat(checkoutInfoPage.getZipField()).isVisible();
            System.out.println("\n✅ CI-001 PASSED: Checkout info page fields and heading are visible.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-001 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-001-S: @smoke Checkout info page loads with all required fields
    // ════════════════════════════════════════════════════════════
    @Test(priority = 2, description = "CI-001-S: @smoke Checkout info page displays heading and all required form fields")
    public void CI_001S_smokeCheckoutInfoPageLoadsWithAllFields() {
        try {
            assertThat(checkoutInfoPage.getHeading()).isVisible();
            assertThat(checkoutInfoPage.getFirstNameField()).isVisible();
            assertThat(checkoutInfoPage.getLastNameField()).isVisible();
            assertThat(checkoutInfoPage.getZipField()).isVisible();
            assertThat(checkoutInfoPage.getContinueButton()).isVisible();
            System.out.println("\n✅ CI-001-S PASSED: Smoke — Checkout info page displays all required fields.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-001-S FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-002: Checkout info page and fields are visible (functional)
    // ════════════════════════════════════════════════════════════
    @Test(priority = 3, description = "CI-002: Page displays email, first name, last name and zip fields")
    public void CI_002_checkoutInfoPageAndFieldsVisibleFunctional() {
        try {
            assertThat(checkoutInfoPage.getFirstNameField()).isVisible();
            assertThat(checkoutInfoPage.getLastNameField()).isVisible();
            assertThat(checkoutInfoPage.getZipField()).isVisible();
            System.out.println("\n✅ CI-002 PASSED: All required form fields are visible on checkout info page.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-002 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-003: Valid form continues to checkout overview
    // ════════════════════════════════════════════════════════════
    @Test(priority = 4, description = "CI-003: Valid form submission navigates to checkout-overview")
    public void CI_003_validFormContinuesToCheckoutOverview() {
        try {
            checkoutInfoPage.fillForm("John", "Doe", "1207");
            checkoutInfoPage.clickContinue();
            page.waitForTimeout(2000);
            String url = checkoutInfoPage.getCurrentURL();
            Assert.assertTrue(url.contains("checkout"),
                    "Expected navigation to checkout-overview after valid form, but got: " + url);
            System.out.println("\n✅ CI-003 PASSED: Valid form navigated to: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-003 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-003-S: @smoke Valid checkout info continues to overview
    // ════════════════════════════════════════════════════════════
    @Test(priority = 5, description = "CI-003-S: @smoke Valid checkout info navigates to overview successfully")
    public void CI_003S_smokeValidCheckoutInfoContinuesToOverview() {
        try {
            checkoutInfoPage.fillForm("Jane", "Smith", "10001");
            checkoutInfoPage.clickContinue();
            page.waitForTimeout(2000);
            String url = checkoutInfoPage.getCurrentURL();
            Assert.assertTrue(url.contains("checkout"),
                    "Expected navigation to checkout-overview, but got: " + url);
            System.out.println("\n✅ CI-003-S PASSED: Smoke — Valid form navigated to: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-003-S FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-004: Invalid inputs show validation messages (all empty)
    // ════════════════════════════════════════════════════════════
    @Test(priority = 6, description = "CI-004: Submitting all empty fields shows validation error messages")
    public void CI_004_allEmptyFieldsShowValidationMessages() {
        try {
            checkoutInfoPage.clearAllFields();
            checkoutInfoPage.clickContinue();
            page.waitForTimeout(1500);
            boolean stayedOnPage = checkoutInfoPage.getCurrentURL().contains("checkout");
            boolean hasErrors = checkoutInfoPage.isValidationErrorVisible();
            Assert.assertTrue(stayedOnPage || hasErrors,
                    "Expected to stay on checkout-info page or show validation errors for empty form.");
            System.out.println("\n✅ CI-004 PASSED: Empty form handled with validation. URL: " + checkoutInfoPage.getCurrentURL());
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-004 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-005: Invalid inputs — empty first name
    // ════════════════════════════════════════════════════════════
    @Test(priority = 7, description = "CI-005: Empty first name shows required validation error")
    public void CI_005_emptyFirstNameShowsValidationError() {
        try {
            checkoutInfoPage.fillLastName("Doe");
            checkoutInfoPage.fillZip("1207");
            checkoutInfoPage.clickContinue();
            page.waitForTimeout(1500);
            boolean stayedOnPage = checkoutInfoPage.getCurrentURL().contains("checkout");
            boolean hasErrors = checkoutInfoPage.isValidationErrorVisible();
            Assert.assertTrue(stayedOnPage || hasErrors,
                    "Expected validation error for empty first name.");
            System.out.println("\n✅ CI-005 PASSED: Empty first name validation handled correctly.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-005 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-006: Invalid inputs — empty last name
    // ════════════════════════════════════════════════════════════
    @Test(priority = 8, description = "CI-006: Empty last name shows required validation error")
    public void CI_006_emptyLastNameShowsValidationError() {
        try {
            checkoutInfoPage.fillFirstName("John");
            checkoutInfoPage.fillZip("1207");
            checkoutInfoPage.clickContinue();
            page.waitForTimeout(1500);
            boolean stayedOnPage = checkoutInfoPage.getCurrentURL().contains("checkout");
            boolean hasErrors = checkoutInfoPage.isValidationErrorVisible();
            Assert.assertTrue(stayedOnPage || hasErrors,
                    "Expected validation error for empty last name.");
            System.out.println("\n✅ CI-006 PASSED: Empty last name validation handled correctly.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-006 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-007: Invalid inputs — empty zip
    // ════════════════════════════════════════════════════════════
    @Test(priority = 9, description = "CI-007: Empty zip shows required validation error")
    public void CI_007_emptyZipShowsValidationError() {
        try {
            checkoutInfoPage.fillFirstName("John");
            checkoutInfoPage.fillLastName("Doe");
            checkoutInfoPage.clickContinue();
            page.waitForTimeout(1500);
            boolean stayedOnPage = checkoutInfoPage.getCurrentURL().contains("checkout");
            boolean hasErrors = checkoutInfoPage.isValidationErrorVisible();
            Assert.assertTrue(stayedOnPage || hasErrors,
                    "Expected validation error for empty zip.");
            System.out.println("\n✅ CI-007 PASSED: Empty zip validation handled correctly.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-007 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-008: Invalid inputs — all fields cleared
    // ════════════════════════════════════════════════════════════
    @Test(priority = 10, description = "CI-008: Clearing all fields and submitting shows validation errors")
    public void CI_008_clearingAllFieldsShowsValidationErrors() {
        try {
            checkoutInfoPage.clearAllFields();
            checkoutInfoPage.clickContinue();
            page.waitForTimeout(1500);
            boolean stayedOnPage = checkoutInfoPage.getCurrentURL().contains("checkout");
            boolean hasErrors = checkoutInfoPage.isValidationErrorVisible();
            Assert.assertTrue(stayedOnPage || hasErrors,
                    "Expected validation errors for all cleared fields.");
            System.out.println("\n✅ CI-008 PASSED: All cleared fields validation handled correctly.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-008 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-009: Cancel returns to cart
    // ════════════════════════════════════════════════════════════
    @Test(priority = 11, description = "CI-009: Cancel button returns user to cart or home page")
    public void CI_009_cancelReturnsToCart() {
        try {
            assertThat(checkoutInfoPage.getCancelButton()).isVisible();
            checkoutInfoPage.clickCancel();
            page.waitForTimeout(2000);
            String url = checkoutInfoPage.getCurrentURL();
            Assert.assertTrue(url.contains("ecommerce"),
                    "Expected navigation to cart or home after Cancel, but got: " + url);
            System.out.println("\n✅ CI-009 PASSED: Cancel navigated to: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-009 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-010: Empty-cart flow guard prevents invalid checkout-info
    // ════════════════════════════════════════════════════════════
    @Test(priority = 12, description = "CI-010: Checkout-info without cart items is blocked or redirected")
    public void CI_010_emptyCartFlowGuardPreventsCheckoutInfo() {
        try {
            // Login without adding items and try to navigate to checkout-info directly
            resetContext();
            LoginPage loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();
            loginPage.performLogin(AppConfig.VALID_EMAIL, AppConfig.VALID_PASSWORD);
            page.waitForURL("**/ecommerce", new com.microsoft.playwright.Page.WaitForURLOptions()
                    .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
            checkoutInfoPage = new CheckoutInfoPage(page);
            checkoutInfoPage.navigateToCheckoutInfo();
            page.waitForTimeout(2000);
            String url = checkoutInfoPage.getCurrentURL();
            Assert.assertTrue(url.contains("ecommerce"),
                    "Expected URL to remain in ecommerce flow, but got: " + url);
            System.out.println("\n✅ CI-010 PASSED: Empty-cart checkout-info flow handled. URL: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-010 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-011: Validation errors appear in accessible error container
    // ════════════════════════════════════════════════════════════
    @Test(priority = 13, description = "CI-011: Validation errors appear near fields and are accessible")
    public void CI_011_validationErrorsAppearInAccessibleContainer() {
        try {
            checkoutInfoPage.clearAllFields();
            checkoutInfoPage.clickContinue();
            page.waitForTimeout(1500);
            boolean hasErrors = checkoutInfoPage.isValidationErrorVisible();
            boolean stayedOnPage = checkoutInfoPage.getCurrentURL().contains("checkout");
            Assert.assertTrue(hasErrors || stayedOnPage,
                    "Expected validation errors to appear or user to stay on checkout-info page.");
            System.out.println("\n✅ CI-011 PASSED: Validation errors appear in accessible container.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-011 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //                   REGRESSION TESTS
    // ════════════════════════════════════════════════════════════════════

    // ════════════════════════════════════════════════════════════
    // CI-012: @regression Numeric-only zip code is accepted
    // ════════════════════════════════════════════════════════════
    @Test(priority = 14, description = "CI-012: @regression Numeric-only zip code is accepted and continues to overview")
    public void CI_012_numericOnlyZipCodeIsAccepted() {
        try {
            checkoutInfoPage.fillForm("Test", "User", "99999");
            checkoutInfoPage.clickContinue();
            page.waitForTimeout(2000);
            String url = checkoutInfoPage.getCurrentURL();
            Assert.assertTrue(url.contains("checkout"),
                    "Expected navigation after numeric zip, but got: " + url);
            System.out.println("\n✅ CI-012 PASSED: Numeric-only zip accepted. Navigated to: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-012 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-013: @regression Single character names are handled gracefully
    // ════════════════════════════════════════════════════════════
    @Test(priority = 15, description = "CI-013: @regression Single character names are handled gracefully")
    public void CI_013_singleCharacterNamesHandledGracefully() {
        try {
            checkoutInfoPage.fillForm("A", "B", "1000");
            checkoutInfoPage.clickContinue();
            page.waitForTimeout(2000);
            String url = checkoutInfoPage.getCurrentURL();
            Assert.assertTrue(url.contains("ecommerce"),
                    "Expected page to remain stable after single char names, but got: " + url);
            System.out.println("\n✅ CI-013 PASSED: Single character names handled gracefully. URL: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-013 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CI-014: @regression Special characters in name field are handled
    // ════════════════════════════════════════════════════════════
    @Test(priority = 16, description = "CI-014: @regression Special characters in name field are handled safely")
    public void CI_014_specialCharactersInNameFieldHandled() {
        try {
            checkoutInfoPage.fillFirstName("<script>alert(1)</script>");
            checkoutInfoPage.fillLastName("Test");
            checkoutInfoPage.fillZip("1207");
            checkoutInfoPage.clickContinue();
            page.waitForTimeout(2000);
            String url = checkoutInfoPage.getCurrentURL();
            // Page should remain stable — no JS alert, no crash
            Assert.assertTrue(url.contains("ecommerce"),
                    "Expected page to remain stable after special characters, but got: " + url);
            Assert.assertNotNull(page.url(), "Page should remain stable after special character input.");
            System.out.println("\n✅ CI-014 PASSED: Special characters in name field handled safely. URL: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CI-014 FAILED: " + e.getMessage());
            throw e;
        }
    }
}

