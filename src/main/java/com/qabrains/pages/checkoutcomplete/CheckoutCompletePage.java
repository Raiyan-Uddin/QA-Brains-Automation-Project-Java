// src/main/java/com/qabrains/pages/checkoutcomplete/CheckoutCompletePage.java

package com.qabrains.pages.checkoutcomplete;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import com.qabrains.config.AppConfig;

/**
 * Page Object Model for the Checkout Complete Page.
 * URL: https://practice.qabrains.com/ecommerce/checkout-complete
 */
public class CheckoutCompletePage {

    private final Page page;

    public CheckoutCompletePage(Page page) {
        this.page = page;
    }

    // ========================
    // LOCATORS — PRIVATE
    // ========================

    // Heading ("Checkout Complete!" or similar)
    private Locator heading() {
        return page.locator(
                "[data-testid='checkout-complete-heading'], " +
                "h1:has-text('Checkout: Complete'), h2:has-text('Checkout: Complete'), h3:has-text('Checkout: Complete'), " +
                "h1:has-text('Checkout Complete'), h2:has-text('Checkout Complete'), h3:has-text('Checkout Complete'), " +
                "h1:has-text('Complete'), h2:has-text('Complete'), h3:has-text('Complete')"
        ).first();
    }

    // "Thank you for your order" message
    private Locator thankYouMessage() {
        return page.locator(
                "[data-testid='thank-you-message'], " +
                "*:has-text('Thank you for your order'), *:has-text('Thank You for your order'), " +
                "*:has-text('Thank you'), *:has-text('Thank You')"
        ).first();
    }

    // Dispatch / shipping confirmation message
    private Locator dispatchMessage() {
        return page.locator(
                "[data-testid='dispatch-message'], " +
                "*:has-text('dispatched'), *:has-text('shipped'), " +
                "*:has-text('Your order has been'), *:has-text('will be delivered'), " +
                "*:has-text('shipping'), *:has-text('delivery')"
        ).first();
    }

    // Success icon or image
    private Locator successIcon() {
        return page.locator(
                "[data-testid='checkout-success-icon'], " +
                "img[alt*='success' i], img[alt*='complete' i], img[alt*='check' i], " +
                "svg[class*='success'], svg[class*='check'], [class*='success-icon'], " +
                "[class*='check-icon'], img, svg"
        ).first();
    }

    // Continue Shopping button
    private Locator continueShoppingButton() {
        return page.locator(
                "button[data-testid='continue-shopping'], " +
                "button:has-text('Continue Shopping'), a:has-text('Continue Shopping'), " +
                "button:has-text('Continue shopping'), a:has-text('Continue shopping'), " +
                "button:has-text('Back to Home'), a:has-text('Back to Home'), " +
                "button:has-text('Home'), a:has-text('Home')"
        ).first();
    }

    // ========================
    // NAVIGATION
    // ========================

    public void navigateToCheckoutComplete() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                page.navigate(
                        AppConfig.CHECKOUT_COMPLETE_URL,
                        new Page.NavigateOptions()
                                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                                .setTimeout((double) AppConfig.DEFAULT_TIMEOUT)
                );
                page.waitForTimeout(2000);
                return;
            } catch (RuntimeException ex) {
                if (attempt == 3) throw ex;
                page.waitForTimeout(1200);
            }
        }
    }

    // ========================
    // GETTERS
    // ========================

    public Locator getHeading()                  { return heading(); }
    public Locator getThankYouMessage()          { return thankYouMessage(); }
    public Locator getDispatchMessage()          { return dispatchMessage(); }
    public Locator getSuccessIcon()              { return successIcon(); }
    public Locator getContinueShoppingButton()   { return continueShoppingButton(); }

    public String getCurrentURL() { return page.url(); }

    // ========================
    // ACTIONS
    // ========================

    public void clickContinueShopping() {
        continueShoppingButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        continueShoppingButton().click();
        System.out.println("  🖱 Clicked Continue Shopping on checkout-complete.");
    }

    // ========================
    // STATE QUERIES
    // ========================

    public boolean isHeadingVisible() {
        try { return heading().isVisible(); }
        catch (Exception e) { return false; }
    }

    public boolean isThankYouMessageVisible() {
        try { return thankYouMessage().isVisible(); }
        catch (Exception e) { return false; }
    }

    public boolean isSuccessIconVisible() {
        try { return successIcon().isVisible(); }
        catch (Exception e) { return false; }
    }
}

