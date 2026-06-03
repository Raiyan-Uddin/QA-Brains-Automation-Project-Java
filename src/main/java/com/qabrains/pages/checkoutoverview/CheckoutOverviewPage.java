// src/main/java/com/qabrains/pages/checkoutoverview/CheckoutOverviewPage.java

package com.qabrains.pages.checkoutoverview;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import com.qabrains.config.AppConfig;

/**
 * Page Object Model for the Checkout Overview Page.
 * URL: https://practice.qabrains.com/ecommerce/checkout-overview
 */
public class CheckoutOverviewPage {

    private final Page page;

    public CheckoutOverviewPage(Page page) {
        this.page = page;
    }

    // ========================
    // LOCATORS — PRIVATE
    // ========================

    // Heading ("Checkout" or "Overview")
    private Locator heading() {
        return page.locator(
                "[data-testid='checkout-overview-heading'], " +
                "h1:has-text('Checkout: Overview'), h2:has-text('Checkout: Overview'), h3:has-text('Checkout: Overview'), " +
                "h1:has-text('Checkout'), h2:has-text('Checkout'), h3:has-text('Checkout'), " +
                "h1:has-text('Overview'), h2:has-text('Overview'), h3:has-text('Overview')"
        ).first();
    }

    // Product items in overview
    private Locator productItems() {
        return page.locator(
                "[data-testid='overview-item'], [data-testid^='overview-item-'], " +
                "[class*='item'], [class*='Item'], [data-testid*='item'], " +
                ".cart-item, [class*='order-item'], [class*='OrderItem'], " +
                "[class*='lineItem'], [class*='product-row']"
        );
    }

    // Product prices displayed (bold)
    private Locator productPrices() {
        return page.locator(
                "[data-testid='overview-price'], " +
                "strong:has-text('$'), b:has-text('$'), " +
                "[class*='price']:has-text('$'), [class*='Price']:has-text('$')"
        );
    }

    // Payment Information section
    private Locator paymentInfoSection() {
        return page.locator("[data-testid='payment-info'], *:has-text('Payment Information'), *:has-text('Payment Info')").first();
    }

    // Shipping Information section
    private Locator shippingInfoSection() {
        return page.locator("[data-testid='shipping-info'], *:has-text('Shipping Information'), *:has-text('Shipping Info')").first();
    }

    // Item Total label
    private Locator itemTotalLabel() {
        return page.locator("[data-testid='item-total'], *:has-text('Item Total'), *:has-text('Subtotal')").first();
    }

    // Tax label
    private Locator taxLabel() {
        return page.locator("[data-testid='tax-total'], *:has-text('Tax')").first();
    }

    // Grand Total / Total label
    private Locator grandTotalLabel() {
        return page.locator("[data-testid='grand-total'], *:has-text('Total')").last();
    }

    // Cancel button
    private Locator cancelButton() {
        return page.locator("button[data-testid='overview-cancel'], button:has-text('Cancel'), a:has-text('Cancel')").first();
    }

    // Finish button
    private Locator finishButton() {
        return page.locator(
                "button[data-testid='overview-finish'], " +
                "button:has-text('Finish'), a:has-text('Finish'), " +
                "button:has-text('Place Order'), button:has-text('Complete Order'), " +
                "button:has-text('Confirm')"
        ).first();
    }

    // ========================
    // NAVIGATION
    // ========================

    public void navigateToCheckoutOverview() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                page.navigate(
                        AppConfig.CHECKOUT_OVERVIEW_URL,
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

    public Locator getHeading()             { return heading(); }
    public Locator getProductItems()        { return productItems(); }
    public Locator getProductPrices()       { return productPrices(); }
    public Locator getPaymentInfoSection()  { return paymentInfoSection(); }
    public Locator getShippingInfoSection() { return shippingInfoSection(); }
    public Locator getItemTotalLabel()      { return itemTotalLabel(); }
    public Locator getTaxLabel()            { return taxLabel(); }
    public Locator getGrandTotalLabel()     { return grandTotalLabel(); }
    public Locator getCancelButton()        { return cancelButton(); }
    public Locator getFinishButton()        { return finishButton(); }

    public String getCurrentURL() { return page.url(); }

    // ========================
    // ACTIONS
    // ========================

    public void clickCancel() {
        cancelButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        cancelButton().click();
        System.out.println("  🖱 Clicked Cancel button on overview.");
    }

    public void clickFinish() {
        finishButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        finishButton().click();
        System.out.println("  🖱 Clicked Finish button on overview.");
    }

    // ========================
    // STATE QUERIES
    // ========================

    public boolean isHeadingVisible() {
        try { return heading().isVisible(); }
        catch (Exception e) { return false; }
    }

    public double extractAmount(String text) {
        String numOnly = text.replaceAll("[^0-9.]", "");
        if (numOnly.isEmpty()) return 0.0;
        try { return Double.parseDouble(numOnly); }
        catch (NumberFormatException e) { return 0.0; }
    }
}

