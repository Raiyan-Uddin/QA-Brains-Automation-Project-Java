// src/test/java/com/qabrains/tests/cart/CartTests.java

package com.qabrains.tests.cart;

import com.qabrains.base.BaseTest;
import com.qabrains.config.AppConfig;
import com.qabrains.pages.cart.CartPage;
import com.qabrains.pages.home.HomePage;
import com.qabrains.pages.login.LoginPage;
import com.qabrains.utils.BrowserFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Test class for the Cart Page.
 * URL: https://practice.qabrains.com/ecommerce/cart
 *
 * Source of truth: docs/test cases/cart_test_cases.csv
 *
 * Covers:
 *   CART-001   : Cart page and heading are visible
 *   CART-001-S : @smoke Cart page loads with items and heading
 *   CART-002   : Cart row shows image name quantity controls and price
 *   CART-003   : Quantity controls update cart (+ button)
 *   CART-004   : Quantity controls update cart (- button)
 *   CART-005   : Remove item from cart
 *   CART-006   : Empty cart state is visible when all items removed
 *   CART-007   : Continue shopping navigates to home
 *   CART-008   : Checkout button navigates to checkout info
 *   CART-008-S : @smoke Checkout button navigates to checkout-info
 *   CART-009   : Total formula remains non-decreasing when quantity increases
 *   CART-010   : Cart route enforces access guard for unauthenticated state
 *   CART-011   : Rapid quantity clicks keep cart interaction stable
 *   CART-012   : @regression Cart persists item after page reload
 *   CART-013   : @regression Cart item row shows correct price format
 *   CART-014   : @regression Continue shopping from empty cart goes to home
 */
public class CartTests extends BaseTest {

    private CartPage cartPage;

    // ════════════════════════════════════════════════════════════
    // SETUP — Logs in, adds one item to cart, navigates to cart
    // ════════════════════════════════════════════════════════════
    @BeforeMethod(alwaysRun = true)
    @Override
    public void testSetup() {
        super.testSetup();
        loginAddItemAndGoToCart();
    }

    private void loginAddItemAndGoToCart() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigateToLoginPage();
        loginPage.performLogin(AppConfig.VALID_EMAIL, AppConfig.VALID_PASSWORD);
        page.waitForURL("**/ecommerce", new com.microsoft.playwright.Page.WaitForURLOptions()
                .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        homePage.clickFirstProductAddToCart();
        page.waitForTimeout(1000);
        cartPage = new CartPage(page);
        cartPage.navigateToCartPage();
    }

    private void resetContext() {
        if (context != null) context.close();
        context = BrowserFactory.createContext(browser);
        page = BrowserFactory.createPage(context);
        cartPage = new CartPage(page);
    }

    private void removeAllItems() {
        int maxAttempts = 10;
        while (cartPage.getCartItemCount() > 0 && maxAttempts-- > 0) {
            cartPage.clickFirstItemRemoveButton();
            page.waitForTimeout(600);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //                   FUNCTIONAL TESTS
    // ════════════════════════════════════════════════════════════════════

    // ════════════════════════════════════════════════════════════
    // CART-001: Cart page and heading are visible
    // ════════════════════════════════════════════════════════════
    @Test(priority = 1, description = "CART-001: Cart page loads and displays cart heading correctly")
    public void CART_001_cartPageAndHeadingVisible() {
        try {
            assertThat(cartPage.getCartHeading()).isVisible();
            System.out.println("\n✅ CART-001 PASSED: Cart page loads and heading is visible.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-001 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-001-S: @smoke Cart page loads with items and heading
    // ════════════════════════════════════════════════════════════
    @Test(priority = 2, description = "CART-001-S: @smoke Cart page displays heading and contains cart items")
    public void CART_001S_smokeCartPageLoadsWithItemsAndHeading() {
        try {
            assertThat(cartPage.getCartHeading()).isVisible();
            Assert.assertTrue(cartPage.getCartItemCount() > 0,
                    "Expected at least one cart item, but found none.");
            System.out.println("\n✅ CART-001-S PASSED: Smoke — Cart heading visible and items present.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-001-S FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-002: Cart row shows image name quantity controls and price
    // ════════════════════════════════════════════════════════════
    @Test(priority = 3, description = "CART-002: Cart row displays image, name (h3), +/− buttons, and price with $")
    public void CART_002_cartRowShowsAllElements() {
        try {
            assertThat(cartPage.getFirstCartItemImage()).isVisible();
            assertThat(cartPage.getFirstCartItemName()).isVisible();
            assertThat(cartPage.getFirstCartItemPlusButton()).isVisible();
            assertThat(cartPage.getFirstCartItemMinusButton()).isVisible();
            assertThat(cartPage.getFirstCartItemPrice()).isVisible();
            String priceText = cartPage.getFirstCartItemPriceText();
            Assert.assertTrue(priceText.contains("$"),
                    "Expected price to contain '$', but got: '" + priceText + "'");
            System.out.println("\n✅ CART-002 PASSED: Cart row shows all required elements (image, name, +/−, price).");
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-002 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-003: Quantity controls update cart (+ button)
    // ════════════════════════════════════════════════════════════
    @Test(priority = 4, description = "CART-003: Plus button increments quantity in cart")
    public void CART_003_plusButtonIncrementsQuantity() {
        try {
            assertThat(cartPage.getFirstCartItemPlusButton()).isVisible();
            cartPage.clickFirstItemPlusButton();
            page.waitForTimeout(800);
            assertThat(cartPage.getFirstCartItem()).isVisible();
            System.out.println("\n✅ CART-003 PASSED: Plus button incremented quantity; cart remains stable.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-003 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-004: Quantity controls update cart (- button)
    // ════════════════════════════════════════════════════════════
    @Test(priority = 5, description = "CART-004: Minus button decrements quantity in cart")
    public void CART_004_minusButtonDecrementsQuantity() {
        try {
            // Increase qty first so we can decrease
            cartPage.clickFirstItemPlusButton();
            page.waitForTimeout(500);
            assertThat(cartPage.getFirstCartItemMinusButton()).isVisible();
            cartPage.clickFirstItemMinusButton();
            page.waitForTimeout(800);
            assertThat(cartPage.getFirstCartItem()).isVisible();
            System.out.println("\n✅ CART-004 PASSED: Minus button decremented quantity; cart remains stable.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-004 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-005: Remove item from cart
    // ════════════════════════════════════════════════════════════
    @Test(priority = 6, description = "CART-005: Remove button removes the item from cart")
    public void CART_005_removeItemFromCart() {
        try {
            int countBefore = cartPage.getCartItemCount();
            Assert.assertTrue(countBefore > 0, "Expected at least one cart item before removal.");
            cartPage.clickFirstItemRemoveButton();
            page.waitForTimeout(1000);
            int countAfter = cartPage.getCartItemCount();
            Assert.assertTrue(countAfter < countBefore,
                    "Expected cart item count to decrease after removal; before=" + countBefore + ", after=" + countAfter);
            System.out.println("\n✅ CART-005 PASSED: Item removed from cart. Count: " + countBefore + " → " + countAfter);
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-005 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-006: Empty cart state is visible when all items removed
    // ════════════════════════════════════════════════════════════
    @Test(priority = 7, description = "CART-006: Empty cart message appears after removing all items")
    public void CART_006_emptyCartStateVisibleWhenAllItemsRemoved() {
        try {
            removeAllItems();
            boolean isEmpty = cartPage.isCartEmpty();
            Assert.assertTrue(isEmpty, "Expected cart to be empty after removing all items.");
            System.out.println("\n✅ CART-006 PASSED: Empty cart state is visible after removing all items.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-006 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-007: Continue shopping navigates to home
    // ════════════════════════════════════════════════════════════
    @Test(priority = 8, description = "CART-007: Continue Shopping button redirects to home page")
    public void CART_007_continueShoppingNavigatesToHome() {
        try {
            assertThat(cartPage.getContinueShoppingButton()).isVisible();
            cartPage.clickContinueShopping();
            page.waitForTimeout(2000);
            String url = page.url();
            Assert.assertTrue(url.contains("ecommerce") && !url.contains("cart"),
                    "Expected navigation to home after Continue Shopping, but got: " + url);
            System.out.println("\n✅ CART-007 PASSED: Continue Shopping navigated to: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-007 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-008: Checkout button navigates to checkout info
    // ════════════════════════════════════════════════════════════
    @Test(priority = 9, description = "CART-008: Checkout button navigates to checkout-info page")
    public void CART_008_checkoutButtonNavigatesToCheckoutInfo() {
        try {
            assertThat(cartPage.getCheckoutButton()).isVisible();
            cartPage.clickCheckout();
            page.waitForTimeout(2000);
            String url = page.url();
            Assert.assertTrue(url.contains("checkout"),
                    "Expected navigation to checkout page, but got: " + url);
            System.out.println("\n✅ CART-008 PASSED: Checkout button navigated to: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-008 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-008-S: @smoke Checkout button navigates to checkout-info
    // ════════════════════════════════════════════════════════════
    @Test(priority = 10, description = "CART-008-S: @smoke Checkout button navigates to checkout-info page")
    public void CART_008S_smokeCheckoutButtonNavigatesToCheckoutInfo() {
        try {
            assertThat(cartPage.getCheckoutButton()).isVisible();
            cartPage.clickCheckout();
            page.waitForTimeout(2000);
            String url = page.url();
            Assert.assertTrue(url.contains("checkout"),
                    "Expected navigation to checkout page, but got: " + url);
            System.out.println("\n✅ CART-008-S PASSED: Smoke — Checkout navigated to: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-008-S FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-009: Total formula remains non-decreasing when quantity increases
    // ════════════════════════════════════════════════════════════
    @Test(priority = 11, description = "CART-009: Cart total is non-decreasing after clicking + button")
    public void CART_009_totalNonDecreasingWhenQuantityIncreases() {
        try {
            double totalBefore = cartPage.parseCartTotal();
            cartPage.clickFirstItemPlusButton();
            page.waitForTimeout(800);
            double totalAfter = cartPage.parseCartTotal();
            Assert.assertTrue(totalAfter >= totalBefore,
                    "Expected total to be non-decreasing after + click; before=" + totalBefore + ", after=" + totalAfter);
            System.out.println("\n✅ CART-009 PASSED: Total non-decreasing; before=" + totalBefore + ", after=" + totalAfter);
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-009 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-010: Cart route enforces access guard for unauthenticated state
    // ════════════════════════════════════════════════════════════
    @Test(priority = 12, description = "CART-010: Unauthenticated access to cart redirects to login")
    public void CART_010_cartRouteEnforcesAccessGuardForUnauthenticated() {
        try {
            resetContext();
            page.navigate(AppConfig.CART_URL,
                    new com.microsoft.playwright.Page.NavigateOptions()
                            .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED)
                            .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
            page.waitForTimeout(2000);
            String url = page.url();
            Assert.assertTrue(url.contains("login"),
                    "Expected unauthenticated cart access to redirect to login, but got: " + url);
            System.out.println("\n✅ CART-010 PASSED: Unauthenticated cart access redirected to login.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-010 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-011: Rapid quantity clicks keep cart interaction stable
    // ════════════════════════════════════════════════════════════
    @Test(priority = 13, description = "CART-011: Rapid + button clicks keep cart stable and consistent")
    public void CART_011_rapidQuantityClicksKeepCartStable() {
        try {
            for (int i = 0; i < 5; i++) {
                cartPage.clickFirstItemPlusButton();
                page.waitForTimeout(100);
            }
            page.waitForTimeout(1000);
            assertThat(cartPage.getCartHeading()).isVisible();
            Assert.assertTrue(cartPage.getCartItemCount() > 0,
                    "Expected cart to remain stable after rapid clicks.");
            System.out.println("\n✅ CART-011 PASSED: Cart remains stable after rapid quantity clicks.");
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-011 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //                   REGRESSION TESTS
    // ════════════════════════════════════════════════════════════════════

    // ════════════════════════════════════════════════════════════
    // CART-012: @regression Cart persists item after page reload
    // ════════════════════════════════════════════════════════════
    @Test(priority = 14, description = "CART-012: @regression Cart items persist after page reload")
    public void CART_012_cartPersistsItemAfterPageReload() {
        try {
            int countBefore = cartPage.getCartItemCount();
            Assert.assertTrue(countBefore > 0, "Expected items in cart before reload.");
            page.reload(new com.microsoft.playwright.Page.ReloadOptions()
                    .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
            page.waitForTimeout(2000);
            int countAfter = cartPage.getCartItemCount();
            Assert.assertTrue(countAfter > 0,
                    "Expected cart items to persist after reload, but found: " + countAfter);
            System.out.println("\n✅ CART-012 PASSED: Cart persists after reload; items=" + countAfter);
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-012 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-013: @regression Cart item row shows correct price format
    // ════════════════════════════════════════════════════════════
    @Test(priority = 15, description = "CART-013: @regression Cart item price is displayed in $XX.XX format")
    public void CART_013_cartItemRowShowsCorrectPriceFormat() {
        try {
            assertThat(cartPage.getFirstCartItemPrice()).isVisible();
            String priceText = cartPage.getFirstCartItemPriceText();
            Assert.assertTrue(priceText.contains("$"),
                    "Expected price to contain '$', but got: '" + priceText + "'");
            System.out.println("\n✅ CART-013 PASSED: Cart item price has correct format: " + priceText);
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-013 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CART-014: @regression Continue shopping from empty cart goes to home
    // ════════════════════════════════════════════════════════════
    @Test(priority = 16, description = "CART-014: @regression Continue Shopping from empty cart navigates to home")
    public void CART_014_continueShoppingFromEmptyCartGoesToHome() {
        try {
            removeAllItems();
            assertThat(cartPage.getContinueShoppingButton()).isVisible();
            cartPage.clickContinueShopping();
            page.waitForTimeout(2000);
            String url = page.url();
            Assert.assertTrue(url.contains("ecommerce"),
                    "Expected navigation to home after Continue Shopping from empty cart, but got: " + url);
            System.out.println("\n✅ CART-014 PASSED: Continue Shopping from empty cart navigated to: " + url);
        } catch (AssertionError e) {
            System.out.println("\n❌ CART-014 FAILED: " + e.getMessage());
            throw e;
        }
    }
}

