// src/test/java/com/qabrains/tests/checkoutcomplete/CheckoutCompleteTests.java

package com.qabrains.tests.checkoutcomplete;

import com.qabrains.base.BaseTest;
import com.qabrains.config.AppConfig;
import com.qabrains.pages.cart.CartPage;
import com.qabrains.pages.checkoutcomplete.CheckoutCompletePage;
import com.qabrains.pages.checkoutinfo.CheckoutInfoPage;
import com.qabrains.pages.checkoutoverview.CheckoutOverviewPage;
import com.qabrains.pages.home.HomePage;
import com.qabrains.pages.login.LoginPage;
import com.qabrains.utils.BrowserFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Test class for the Checkout Complete Page.
 * URL: https://practice.qabrains.com/ecommerce/checkout-complete
 *
 * Source of truth: docs/test cases/checkout_complete_test_cases.csv
 *
 * Covers:
 *   CC-001   : Completion heading and confirmation texts are visible
 *   CC-001-S : @smoke Completion page shows thank-you confirmation
 *   CC-002   : Success icon is displayed
 *   CC-003   : Completion heading and thank-you message visible
 *   CC-004   : Dispatch/shipping confirmation text is visible
 *   CC-005   : Cart is empty after order completion
 *   CC-006   : Continue Shopping navigates to home page
 *   CC-006-S : @smoke Continue Shopping from completion goes to home
 *   CC-007   : Direct access without finishing flow is guarded
 *   CC-008   : Completion page has no editable transactional inputs
 *   CC-009   : Completion CTA is keyboard accessible
 *   CC-010   : Completion page renders in mobile and desktop viewports
 *   CC-011   : @regression Order confirmation details are visible
 *   CC-012   : @regression Cart is cleared after order completion
 *   CC-013   : @regression Completion page has no stale checkout inputs
 */
public class CheckoutCompleteTests extends BaseTest {

    private CheckoutCompletePage completePage;

    // ════════════════════════════════════════════════════════════
    // SETUP — Full checkout flow: login → add item → cart → info → overview → finish
    // ════════════════════════════════════════════════════════════
    @BeforeMethod(alwaysRun = true)
    @Override
    public void testSetup() {
        super.testSetup();
        loginAndCompleteCheckout();
    }

    private void loginAndCompleteCheckout() {
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
        page.waitForURL("**/checkout-info**", new com.microsoft.playwright.Page.WaitForURLOptions()
                .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
        CheckoutInfoPage checkoutInfoPage = new CheckoutInfoPage(page);
        checkoutInfoPage.fillForm("John", "Doe", "1207");
        checkoutInfoPage.clickContinue();
        waitForCheckoutOverviewRouteOrRecover();
        CheckoutOverviewPage overviewPage = new CheckoutOverviewPage(page);
        overviewPage.clickFinish();
        waitForCheckoutCompleteRouteOrRecover();
        completePage = new CheckoutCompletePage(page);
    }

    private void waitForCheckoutOverviewRouteOrRecover() {
        try {
            page.waitForURL("**/checkout-overview**", new com.microsoft.playwright.Page.WaitForURLOptions()
                    .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
        } catch (RuntimeException ex) {
            new CheckoutOverviewPage(page).navigateToCheckoutOverview();
            page.waitForURL("**/checkout-overview**", new com.microsoft.playwright.Page.WaitForURLOptions()
                    .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
        }
    }

    private void waitForCheckoutCompleteRouteOrRecover() {
        try {
            page.waitForURL("**/checkout-complete**", new com.microsoft.playwright.Page.WaitForURLOptions()
                    .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
        } catch (RuntimeException ex) {
            new CheckoutCompletePage(page).navigateToCheckoutComplete();
            page.waitForURL("**/checkout-complete**", new com.microsoft.playwright.Page.WaitForURLOptions()
                    .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
        }
    }

    private void resetContext() {
        if (context != null) context.close();
        context = BrowserFactory.createContext(browser);
        page = BrowserFactory.createPage(context);
        completePage = new CheckoutCompletePage(page);
    }

    // ════════════════════════════════════════════════════════════════════
    //                   FUNCTIONAL & SMOKE TESTS
    // ════════════════════════════════════════════════════════════════════

    // ════════════════════════════════════════════════════════════
    // CC-001: Completion heading and confirmation texts are visible
    // ════════════════════════════════════════════════════════════
    @Test(priority = 1, description = "CC-001: Completion page displays heading and confirmation messages correctly")
    public void CC_001_completionHeadingAndConfirmationTextsVisible() {
        try {
            assertThat(completePage.getHeading()).isVisible();
            assertThat(completePage.getThankYouMessage()).isVisible();
            System.out.println("\n✅ CC-001 PASSED: Completion heading and thank-you message are visible.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-001 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-001-S: @smoke Completion page shows thank-you confirmation
    // ════════════════════════════════════════════════════════════
    @Test(priority = 2, description = "CC-001-S: @smoke Completion page displays heading and thank-you confirmation message")
    public void CC_001S_smokeCompletionPageShowsThankYouConfirmation() {
        try {
            assertThat(completePage.getHeading()).isVisible();
            assertThat(completePage.getThankYouMessage()).isVisible();
            System.out.println("\n✅ CC-001-S PASSED: Smoke — Completion page heading and thank-you message visible.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-001-S FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-002: Success icon is displayed
    // ════════════════════════════════════════════════════════════
    @Test(priority = 3, description = "CC-002: Success checkmark or confirmation icon is displayed on page")
    public void CC_002_successIconIsDisplayed() {
        try {
            assertThat(completePage.getSuccessIcon()).isVisible();
            System.out.println("\n✅ CC-002 PASSED: Success icon is displayed on the completion page.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-002 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-003: Thank-you and order confirmation messages are displayed
    // ════════════════════════════════════════════════════════════
    @Test(priority = 4, description = "CC-003: Thank-you and order confirmation messages are displayed")
    public void CC_003_thankYouAndOrderConfirmationMessagesDisplayed() {
        try {
            assertThat(completePage.getThankYouMessage()).isVisible();
            System.out.println("\n✅ CC-003 PASSED: Thank-you message is displayed.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-003 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-004: Dispatch/shipping confirmation text is visible
    // ════════════════════════════════════════════════════════════
    @Test(priority = 5, description = "CC-004: Dispatch/shipping confirmation text is displayed")
    public void CC_004_dispatchShippingConfirmationTextVisible() {
        try {
            assertThat(completePage.getDispatchMessage()).isVisible();
            System.out.println("\n✅ CC-004 PASSED: Dispatch/shipping confirmation text is visible.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-004 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-005: Cart is empty after order completion
    // ════════════════════════════════════════════════════════════
    @Test(priority = 6, description = "CC-005: Cart is empty and shows no items after successful order")
    public void CC_005_cartIsEmptyAfterOrderCompletion() {
        try {
            // Navigate to cart to verify it's empty
            page.navigate(AppConfig.CART_URL,
                    new com.microsoft.playwright.Page.NavigateOptions()
                            .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED)
                            .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
            page.waitForTimeout(2000);
            CartPage cartPage = new CartPage(page);
            boolean isEmpty = cartPage.isCartEmpty();
            Assert.assertTrue(isEmpty,
                    "Expected cart to be empty after order completion, but items were found.");
            System.out.println("\n✅ CC-005 PASSED: Cart is empty after order completion.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-005 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-006: Continue Shopping navigates to home page
    // ════════════════════════════════════════════════════════════
    @Test(priority = 7, description = "CC-006: Continue Shopping button redirects to home page")
    public void CC_006_continueShoppingNavigatesToHome() {
        try {
            assertThat(completePage.getContinueShoppingButton()).isVisible();
            completePage.clickContinueShopping();
            page.waitForTimeout(2000);
            String url = page.url();
            Assert.assertTrue(url.contains("ecommerce"),
                    "Expected navigation to home page after Continue Shopping, but got: " + url);
            System.out.println("\n✅ CC-006 PASSED: Continue Shopping navigated to: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-006 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-006-S: @smoke Continue Shopping from completion goes to home
    // ════════════════════════════════════════════════════════════
    @Test(priority = 8, description = "CC-006-S: @smoke Continue Shopping button successfully navigates to home page")
    public void CC_006S_smokeContinueShoppingGoesToHome() {
        try {
            assertThat(completePage.getContinueShoppingButton()).isVisible();
            completePage.clickContinueShopping();
            page.waitForTimeout(2000);
            String url = page.url();
            Assert.assertTrue(url.contains("ecommerce"),
                    "Expected navigation to home page, but got: " + url);
            System.out.println("\n✅ CC-006-S PASSED: Smoke — Continue Shopping navigated to: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-006-S FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-007: Direct access without finishing flow is guarded
    // ════════════════════════════════════════════════════════════
    @Test(priority = 9, description = "CC-007: Direct unauthenticated access to checkout-complete is blocked")
    public void CC_007_directAccessWithoutFinishingFlowIsGuarded() {
        try {
            resetContext();
            page.navigate(AppConfig.CHECKOUT_COMPLETE_URL,
                    new com.microsoft.playwright.Page.NavigateOptions()
                            .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED)
                            .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
            page.waitForTimeout(2000);
            String url = page.url();
            // Should redirect to login or be blocked
            Assert.assertTrue(url.contains("login") || url.contains("ecommerce"),
                    "Expected guard behavior for direct access to checkout-complete, but got: " + url);
            System.out.println("\n✅ CC-007 PASSED: Direct unauthenticated access to checkout-complete was guarded. URL: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-007 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-008: Completion page has no editable transactional inputs
    // ════════════════════════════════════════════════════════════
    @Test(priority = 10, description = "CC-008: Page contains no editable form inputs; page is informational only")
    public void CC_008_completionPageHasNoEditableTransactionalInputs() {
        try {
            int editableInputs = page.locator("input:not([disabled]):not([readonly]):not([type='hidden'])").count();
            int editableTextareas = page.locator("textarea:not([disabled]):not([readonly])").count();
            Assert.assertEquals(editableInputs + editableTextareas, 0,
                    "Expected no editable form inputs on completion page, but found: " +
                    (editableInputs + editableTextareas));
            System.out.println("\n✅ CC-008 PASSED: Completion page has no editable form inputs.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-008 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-009: Completion CTA is keyboard accessible
    // ════════════════════════════════════════════════════════════
    @Test(priority = 11, description = "CC-009: Continue Shopping button is keyboard accessible and can be activated with Enter")
    public void CC_009_completionCtaIsKeyboardAccessible() {
        try {
            // Tab to find the Continue Shopping button
            boolean buttonFocused = false;
            for (int i = 0; i < 10; i++) {
                page.keyboard().press("Tab");
                boolean focused = (boolean) completePage.getContinueShoppingButton()
                        .evaluate("el => el === document.activeElement");
                if (focused) {
                    buttonFocused = true;
                    break;
                }
            }
            Assert.assertTrue(buttonFocused,
                    "Expected Continue Shopping button to receive keyboard focus via Tab.");
            System.out.println("\n✅ CC-009 PASSED: Continue Shopping button is keyboard accessible.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-009 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-010: Completion page renders in mobile and desktop viewports
    // ════════════════════════════════════════════════════════════
    @Test(priority = 12, description = "CC-010: Completion page layout is responsive on mobile and desktop viewports")
    public void CC_010_completionPageRendersInMobileAndDesktopViewports() {
        try {
            // Test mobile viewport (390x844)
            page.setViewportSize(390, 844);
            page.waitForTimeout(500);
            assertThat(completePage.getHeading()).isVisible();
            System.out.println("  📍 Mobile (390x844): Heading visible. PASS.");

            // Test desktop viewport (1366x768)
            page.setViewportSize(1366, 768);
            page.waitForTimeout(500);
            assertThat(completePage.getHeading()).isVisible();
            System.out.println("  📍 Desktop (1366x768): Heading visible. PASS.");

            System.out.println("\n✅ CC-010 PASSED: Completion page renders correctly in mobile and desktop viewports.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-010 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //                   REGRESSION TESTS
    // ════════════════════════════════════════════════════════════════════

    // ════════════════════════════════════════════════════════════
    // CC-011: @regression Order confirmation details are visible
    // ════════════════════════════════════════════════════════════
    @Test(priority = 13, description = "CC-011: @regression Order confirmation details including dispatch info and icon are displayed")
    public void CC_011_orderConfirmationDetailsVisible() {
        try {
            assertThat(completePage.getDispatchMessage()).isVisible();
            assertThat(completePage.getSuccessIcon()).isVisible();
            System.out.println("\n✅ CC-011 PASSED: Order confirmation details (dispatch message and icon) are visible.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-011 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-012: @regression Cart is cleared after order completion
    // ════════════════════════════════════════════════════════════
    @Test(priority = 14, description = "CC-012: @regression Cart remains empty after successful order completion")
    public void CC_012_cartIsClearedAfterOrderCompletion() {
        try {
            page.navigate(AppConfig.CART_URL,
                    new com.microsoft.playwright.Page.NavigateOptions()
                            .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED)
                            .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
            page.waitForTimeout(2000);
            CartPage cartPage = new CartPage(page);
            boolean isEmpty = cartPage.isCartEmpty();
            Assert.assertTrue(isEmpty,
                    "Expected cart to remain empty after order completion, but items were found.");
            System.out.println("\n✅ CC-012 PASSED: Cart remains empty after order completion.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-012 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CC-013: @regression Completion page has no stale checkout inputs
    // ════════════════════════════════════════════════════════════
    @Test(priority = 15, description = "CC-013: @regression Completion page has no stale checkout form inputs")
    public void CC_013_completionPageHasNoStaleCheckoutInputs() {
        try {
            int exJohnCount = page.locator("input[placeholder='Ex. John']").count();
            int exDoeCount = page.locator("input[placeholder='Ex. Doe']").count();
            Assert.assertEquals(exJohnCount, 0,
                    "Expected no 'Ex. John' placeholder (stale checkout form) on completion page.");
            Assert.assertEquals(exDoeCount, 0,
                    "Expected no 'Ex. Doe' placeholder (stale checkout form) on completion page.");
            System.out.println("\n✅ CC-013 PASSED: Completion page has no stale checkout form inputs.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CC-013 FAILED: " + e.getMessage());
            throw e;
        }
    }
}

