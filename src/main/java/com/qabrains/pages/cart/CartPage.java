// src/main/java/com/qabrains/pages/cart/CartPage.java

package com.qabrains.pages.cart;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import com.qabrains.config.AppConfig;

/**
 * Page Object Model for the Cart Page.
 * URL: https://practice.qabrains.com/ecommerce/cart
 *
 * DOM verified via diagnostic on 2026-06-02:
 *  - Cart list container : <div class="cart-list space-y-4">
 *  - Cart item row       : <div class="flex items-center justify-between border border-gray-200 p-4 pl-8">
 *  - Product name        : <h3 class="font-bold font-oswald text-lg">
 *  - Product image       : <img class="w-20 h-20"> inside div.img-wrapper
 *  - Remove button       : <button class="text-red-500 ...">Remove</button> (opens confirm dialog)
 *  - Minus button        : <button class="text-lg px-2">-</button> (may open dialog at qty=1)
 *  - Plus button         : <button class="text-lg px-2">+</button>
 *  - Quantity span       : <span class="border rounded px-3 py-1 text-gray-500">1</span>
 *  - Price per item      : <p class="font-bold font-oswald text-lg">$49.99</p>  (Price column)
 *  - Total per item      : <p class="font-bold text-lg font-oswald">$49.99</p>  (Total column)
 *  - Continue Shopping   : <button>Continue Shopping</button>
 *  - Checkout            : <button class="...bg-qa-clr...">Checkout</button>
 */
public class CartPage {

    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    // ========================
    // LOCATORS — PRIVATE
    // ========================

    // Cart heading ("Your Cart")
    private Locator cartHeading() {
        return page.locator("[data-testid='cart-heading'], h1, h2, h3")
                .filter(new Locator.FilterOptions().setHasText("Cart"))
                .first();
    }

    // All cart item rows — confirmed selector via diagnostic
    private Locator cartItems() {
        return page.locator("[data-testid='cart-item'], [data-testid^='cart-item-'], .cart-list > div");
    }

    // First cart item row
    private Locator firstCartItem() {
        return cartItems().first();
    }

    // Product name h3 in first item
    private Locator firstCartItemName() {
        return firstCartItem().locator("[data-testid='cart-item-name'], h3").first();
    }

    // Product image in first item
    private Locator firstCartItemImage() {
        return firstCartItem().locator("[data-testid='cart-item-image'], img").first();
    }

    // Plus (+) button in first item — no dialog, direct increment
    private Locator firstCartItemPlusButton() {
        return firstCartItem().locator("button[data-testid='cart-plus'], button:has-text('+')").first();
    }

    // Minus (−) button in first item — may trigger dialog at qty=1
    private Locator firstCartItemMinusButton() {
        return firstCartItem().locator("button[data-testid='cart-minus'], button:has-text('-')").first();
    }

    // Remove button in first item — opens confirmation dialog
    private Locator firstCartItemRemoveButton() {
        return firstCartItem().locator("button[data-testid='cart-remove'], button:has-text('Remove'), button.text-red-500").first();
    }

    // Price per item (Price column, not Total column)
    private Locator firstCartItemPrice() {
        // The Price column is the third w-[20%] div; its paragraph has font-bold text
        return firstCartItem().locator("[data-testid='cart-item-price'], p[class*='font-bold']:has-text('$')").first();
    }

    // Quantity display span
    private Locator quantityDisplay() {
        return firstCartItem().locator("[data-testid='cart-qty'], span[class*='border']").first();
    }

    // Confirmation dialog — Radix Dialog with role='dialog'
    private Locator confirmDialog() {
        return page.locator("[data-testid='remove-dialog'], [role='dialog']").first();
    }

    // Confirm (destructive) button inside the removal dialog — text "Remove"
    private Locator confirmRemoveButton() {
        return page.locator("[data-testid='confirm-remove'], [role='dialog'] button:has-text('Remove')").first();
    }

    // Cancel/Close button inside the dialog — text "Close"
    private Locator cancelDialogButton() {
        return page.locator("[data-testid='cancel-remove'], [role='dialog'] button:has-text('Close')").first();
    }

    // Empty cart message
    private Locator emptyCartMessage() {
        return page.locator(
                "*:has-text('Your cart is empty'), *:has-text('Cart is empty'), " +
                "*:has-text('empty cart'), *:has-text('No items'), " +
                "*:has-text('no items')"
        ).first();
    }

    // Continue Shopping button
    private Locator continueShoppingButton() {
        return page.locator("[data-testid='continue-shopping'], button:has-text('Continue Shopping'), a:has-text('Continue Shopping')").first();
    }

    // Checkout button
    private Locator checkoutButton() {
        return page.locator("[data-testid='checkout-button'], button:has-text('Checkout'), a:has-text('Checkout')").first();
    }

    // Cart total (last price-like value on page)
    private Locator cartTotal() {
        return page.locator("[data-testid='cart-total'], p[class*='font-bold']:has-text('$'), span[class*='font-bold']:has-text('$')").last();
    }

    // ========================
    // NAVIGATION
    // ========================

    public void navigateToCartPage() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                page.navigate(
                        AppConfig.CART_URL,
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

    public Locator getCartHeading()              { return cartHeading(); }
    public Locator getCartItems()                { return cartItems(); }
    public Locator getFirstCartItem()            { return firstCartItem(); }
    public Locator getFirstCartItemName()        { return firstCartItemName(); }
    public Locator getFirstCartItemImage()       { return firstCartItemImage(); }
    public Locator getFirstCartItemPlusButton()  { return firstCartItemPlusButton(); }
    public Locator getFirstCartItemMinusButton() { return firstCartItemMinusButton(); }
    public Locator getFirstCartItemRemoveButton(){ return firstCartItemRemoveButton(); }
    public Locator getFirstCartItemPrice()       { return firstCartItemPrice(); }
    public Locator getQuantityDisplay()          { return quantityDisplay(); }
    public Locator getEmptyCartMessage()         { return emptyCartMessage(); }
    public Locator getContinueShoppingButton()   { return continueShoppingButton(); }
    public Locator getCheckoutButton()           { return checkoutButton(); }
    public Locator getCartTotal()                { return cartTotal(); }

    public String getCurrentURL() { return page.url(); }

    // ========================
    // ACTIONS
    // ========================

    public void clickFirstItemPlusButton() {
        firstCartItemPlusButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        firstCartItemPlusButton().click();
        page.waitForTimeout(500);
        System.out.println("  [CLICK] Clicked + button on first cart item.");
    }

    /**
     * Clicks the minus button. If it opens a confirmation dialog (qty reaches 0/1),
     * automatically cancels the dialog to keep the item.
     */
    public void clickFirstItemMinusButton() {
        firstCartItemMinusButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        firstCartItemMinusButton().click();
        page.waitForTimeout(800);
        // Cancel dialog if it opened (we just want to decrement, not remove)
        try {
            if (confirmDialog().isVisible()) {
                cancelDialogButton().click();
                page.waitForTimeout(300);
            }
        } catch (Exception ignored) {}
        System.out.println("  [CLICK] Clicked - button on first cart item.");
    }

    /**
     * Clicks Remove on first item. Handles the Radix confirmation dialog.
     * Dialog has: Button[0]="Close", Button[1]="Remove", Button[2]="Close (X)"
     */
    public void clickFirstItemRemoveButton() {
        int countBefore = getCartItemCount();
        firstCartItemRemoveButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        firstCartItemRemoveButton().click();
        page.waitForTimeout(600); // allow dialog animation to begin

        // Confirm the removal dialog and force the click to avoid overlay edge-cases.
        try {
            confirmRemoveButton().waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE).setTimeout(4000));
            confirmRemoveButton().click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            System.out.println("  [WARN] Confirm dialog not found: " + e.getMessage().substring(0, Math.min(60, e.getMessage().length())));
        }

        // Poll briefly for the cart row count to update after confirmation.
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 5000) {
            if (getCartItemCount() < countBefore) {
                break;
            }
            page.waitForTimeout(200);
        }

        System.out.println("  [CLICK] Clicked Remove on first cart item.");
    }

    public void clickContinueShopping() {
        continueShoppingButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        continueShoppingButton().click();
        System.out.println("  [CLICK] Clicked Continue Shopping.");
    }

    public void clickCheckout() {
        checkoutButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        checkoutButton().click();
        System.out.println("  [CLICK] Clicked Checkout.");
    }

    // ========================
    // STATE QUERIES
    // ========================

    public int getCartItemCount() { return cartItems().count(); }

    public boolean isCartEmpty() {
        return getCartItemCount() == 0 || isEmptyCartMessageVisible();
    }

    public boolean isEmptyCartMessageVisible() {
        try { return emptyCartMessage().isVisible(); }
        catch (Exception e) { return false; }
    }

    public String getFirstCartItemPriceText() {
        return firstCartItemPrice().textContent().trim();
    }

    public String getQuantityDisplayText() {
        try { return quantityDisplay().textContent().trim(); }
        catch (Exception e) { return ""; }
    }

    public String getCartTotalText() {
        try { return cartTotal().textContent().trim(); }
        catch (Exception e) { return ""; }
    }

    public double parseCartTotal() {
        String text = getCartTotalText();
        String numOnly = text.replaceAll("[^0-9.]", "");
        if (numOnly.isEmpty()) return 0.0;
        try { return Double.parseDouble(numOnly); }
        catch (NumberFormatException e) { return 0.0; }
    }
}

