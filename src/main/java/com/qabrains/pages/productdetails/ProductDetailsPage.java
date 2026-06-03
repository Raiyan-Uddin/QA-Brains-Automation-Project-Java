// src/main/java/com/qabrains/pages/productdetails/ProductDetailsPage.java

package com.qabrains.pages.productdetails;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import com.qabrains.config.AppConfig;

import java.util.regex.Pattern;

/**
 * Page Object Model for the Product Details Page.
 * URL: https://practice.qabrains.com/ecommerce/product-details?id={id}
 *
 * Contains:
 *   - Locators for all page elements
 *   - Getter methods for element access
 *   - Action methods for user interactions
 *   - State query methods for assertions
 *
 * This class does NOT contain any assertions or test logic.
 * Assertions belong in the test class (ProductDetailsTests.java).
 */
public class ProductDetailsPage {

    // ========================
    // PAGE INSTANCE
    // ========================
    private final Page page;

    // ========================
    // CONSTRUCTOR
    // ========================
    public ProductDetailsPage(Page page) {
        this.page = page;
    }

    // ========================
    // LOCATORS — PRIVATE (Encapsulated)
    // ========================

    // Back button (top-left of content area)
    private Locator backButton() {
        return page.locator(
                "button[data-testid='pdp-back'], button[data-testid='back-button'], " +
                "button:has-text('Back'), a:has-text('Back'), " +
                "[class*='back'], [aria-label*='back' i], " +
                "button:has-text('<'), a:has-text('<')"
        ).first();
    }

    // Product image
    private Locator productImage() {
        return page.locator(
                "[data-testid='product-image'], " +
                // Prefer non-logo image elements inside content area first.
                "main img:not([alt='logo']), main img:not([src*='qa-logo']), " +
                "[class*='product'] img:not([alt='logo']), section img:not([alt='logo']), " +
                "main img, [class*='product-image'] img, [class*='ProductDetail'] img, img"
        ).first();
    }

    // Favourite / heart toggle button
    private Locator favouriteButton() {
        return page.locator(
                "button[data-testid='favorite-toggle'], button[data-testid='favourite-toggle'], " +
                "main button[aria-label*='favourite' i], main button[aria-label*='favorite' i], " +
                "main button[aria-pressed], main button:has(svg[class*='heart']), " +
                "main button:has([data-testid*='heart']), main button[class*='favourite'], main button[class*='favorite'], " +
                "main button[data-testid*='favourite'], main button[data-testid*='favorite'], " +
                // Fallback: likely PDP icon button but avoid menu/dialog triggers.
                "main button:has(svg):not([aria-haspopup='menu']):not([aria-haspopup='dialog']):not([data-testid='cart-button'])"
        ).first();
    }

    // Product name heading
    private Locator productName() {
        return page.locator(
                "[data-testid='product-name'], " +
                "h1, h2, [class*='product-name'], [class*='product-title'], " +
                "[class*='ProductName'], [class*='ProductTitle']"
        ).first();
    }

    // Quantity minus (-) button
    private Locator quantityMinusButton() {
        return page.locator(
                "button[data-testid='quantity-minus'], " +
                "button[aria-label*='decrease' i], button[aria-label*='Decrease' i], " +
                "button[data-testid*='minus'], button[data-testid*='decrease'], " +
                "button:has-text('-'), button:has-text('−'), button:has-text('–'), " +
                "button[class*='minus'], button[class*='decrease'], " +
                "main div:has(p:has-text('Quantity')) button:first-child"
        ).first();
    }

    // Quantity number display (read-only)
    private Locator quantityDisplay() {
        return page.locator(
                "[data-testid='quantity-value'], [data-testid='quantity-display'], " +
                "[class*='quantity']:not(button), [class*='qty']:not(button), " +
                "input[type='number'], " +
                "span[class*='qty'], span[class*='quantity'], [data-testid*='quantity-display'], " +
                "div[class*='quantity'], div[class*='qty'], p[class*='quantity'], p[class*='qty'], " +
                "main div:has(button:has-text('+')) span, " +
                "main div:has(p:has-text('Quantity')) span.border"
        ).first();
    }

    // Quantity plus (+) button
    private Locator quantityPlusButton() {
        return page.locator(
                "button[data-testid='quantity-plus'], " +
                "button[aria-label*='increase' i], button[aria-label*='Increase' i], " +
                "button[data-testid*='plus'], button[data-testid*='increase'], " +
                "button:has-text('+'), button[class*='plus'], button[class*='increase']"
        ).first();
    }

    // Product price
    private Locator productPrice() {
        return page.locator(
                "[data-testid='product-price'], " +
                "main p:has-text('$'), main span:has-text('$'), main div:has-text('$'), " +
                "[class*='price']:not(button), [class*='Price']:not(button), " +
                "span:has-text('$'), p:has-text('$'), div[class*='price'], div[class*='Price'], " +
                "[data-testid*='price'], span[class*='amount'], div:has(span:has-text('$'))"
        ).first();
    }

    // "Add to Cart" button
    private Locator addToCartButton() {
        return page.locator(
                "button[data-testid='add-to-cart'], button[data-action='add-to-cart'], " +
                "button:has-text('Add to Cart'), button:has-text('Add to cart')"
        ).first();
    }

    // Cart button in header (same pattern as homepage)
    private Locator cartButton() {
        return page.locator(
                "[data-testid='cart-button'], [data-testid='header-cart'], " +
                "a[href*='/cart'], button[aria-label*='cart' i], a[aria-label*='cart' i], " +
                "header span[role='button']:has(svg):not(:has-text('test@'))"
        ).first();
    }

    // Cart badge in header
    private Locator cartBadge() {
        return page.locator(
                "[data-testid='cart-badge'], [data-testid='cart-count'], " +
                "[class*='cart-count'], [class*='cart-badge'], [data-testid*='cart-count'], " +
                "header [class*='badge'], header span[class*='count'], " +
                "header .badge, [aria-label*='cart'] span"
        ).first();
    }

    // "Product not found" / guard message for invalid IDs
    private Locator notFoundMessage() {
        return page.locator(
                "[data-testid='product-not-found'], " +
                "*:has-text('not found'), *:has-text('Not Found'), " +
                "*:has-text('Product not found'), *:has-text('Page not found')"
        ).first();
    }

    // ========================
    // NAVIGATION METHODS
    // ========================

    /**
     * Navigates to the Product Details page for the given product ID.
     *
     * @param productId The numeric product ID (e.g., 1, 2, -1, 0).
     */
    public void navigateToProductDetails(int productId) {
        String url = AppConfig.PRODUCT_DETAILS_URL + "?id=" + productId;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                page.navigate(
                        url,
                        new Page.NavigateOptions()
                                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                                .setTimeout((double) AppConfig.DEFAULT_TIMEOUT)
                );
                if (productId > 0) {
                    waitForProductDetailsReady();
                } else {
                    page.waitForTimeout(2000); // for invalid IDs, just wait briefly
                }
                System.out.println("  📍 Navigated to Product Details: " + url);
                return;
            } catch (RuntimeException ex) {
                if (attempt == 3) throw ex;
                page.waitForTimeout(1200);
            }
        }
    }

    /**
     * Waits until the product name heading is visible.
     */
    private void waitForProductDetailsReady() {
        productName().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout((double) AppConfig.DEFAULT_TIMEOUT)
        );
    }

    // ========================
    // GETTER METHODS — For assertions in test classes
    // ========================

    public Page getPage()                  { return this.page; }
    public Locator getBackButton()         { return backButton(); }
    public Locator getProductImage()       { return productImage(); }
    public Locator getFavouriteButton()    { return favouriteButton(); }
    public Locator getProductName()        { return productName(); }
    public Locator getQuantityMinus()      { return quantityMinusButton(); }
    public Locator getQuantityDisplay()    { return quantityDisplay(); }
    public Locator getQuantityPlus()       { return quantityPlusButton(); }
    public Locator getProductPrice()       { return productPrice(); }
    public Locator getAddToCartButton()    { return addToCartButton(); }
    public Locator getCartButton()         { return cartButton(); }
    public Locator getCartBadge()          { return cartBadge(); }
    public Locator getNotFoundMessage()    { return notFoundMessage(); }

    public String getCurrentURL() { return page.url(); }

    // ========================
    // ACTION METHODS — User interactions
    // ========================

    /**
     * Clicks the Back button.
     */
    public void clickBackButton() {
        backButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        backButton().click();
        System.out.println("  🖱 Clicked Back button.");
    }

    /**
     * Clicks the Favourite toggle button.
     */
    public void clickFavouriteButton() {
        favouriteButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        favouriteButton().click();
        System.out.println("  🖱 Clicked Favourite button.");
    }

    /**
     * Clicks the quantity minus (–) button.
     */
    public void clickMinusButton() {
        quantityMinusButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        quantityMinusButton().click();
        System.out.println("  🖱 Clicked Minus (-) button.");
    }

    /**
     * Clicks the quantity plus (+) button.
     */
    public void clickPlusButton() {
        quantityPlusButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        quantityPlusButton().click();
        System.out.println("  🖱 Clicked Plus (+) button.");
    }

    /**
     * Clicks the "Add to Cart" button.
     */
    public void clickAddToCartButton() {
        addToCartButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        addToCartButton().click();
        System.out.println("  🖱 Clicked Add to Cart button.");
    }

    /**
     * Clicks the cart button in the header.
     */
    public void clickCartButton() {
        cartButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        cartButton().click();
        System.out.println("  🖱 Clicked Cart button in header.");
    }

    // ========================
    // STATE QUERY METHODS — For custom checks
    // ========================

    public boolean isBackButtonVisible()      { return backButton().isVisible(); }
    public boolean isProductImageVisible()    { return productImage().isVisible(); }
    public boolean isFavouriteButtonVisible() { return favouriteButton().isVisible(); }
    public boolean isProductNameVisible()     { return productName().isVisible(); }
    public boolean isQuantityMinusVisible()   { return quantityMinusButton().isVisible(); }
    public boolean isQuantityDisplayVisible() { return quantityDisplay().isVisible(); }
    public boolean isQuantityPlusVisible()    { return quantityPlusButton().isVisible(); }
    public boolean isProductPriceVisible()    { return productPrice().isVisible(); }
    public boolean isAddToCartButtonVisible() { return addToCartButton().isVisible(); }
    public boolean isCartButtonVisible()      { return cartButton().isVisible(); }
    public boolean isCartBadgeVisible()       { return cartBadge().isVisible(); }

    /**
     * Returns true if the minus button is disabled (qty = 1).
     */
    public boolean isMinusButtonDisabled() {
        try {
            boolean notEnabled = !quantityMinusButton().isEnabled();
            String ariaDisabled = quantityMinusButton().getAttribute("aria-disabled");
            boolean ariaTrue = "true".equals(ariaDisabled);
            return notEnabled || ariaTrue;
        } catch (Exception e) {
            return false;
        }
    }

    public String getProductNameText()       { return productName().textContent().trim(); }
    public String getProductPriceText()      { return productPrice().textContent().trim(); }
    public String getQuantityDisplayText() {
        try {
            if (quantityDisplay().count() > 0) {
                String text = quantityDisplay().first().textContent();
                if (text != null && !text.trim().isEmpty()) {
                    return text.trim();
                }
            }
        } catch (Exception ignored) {
        }

        // Fallback: detect a numeric token quickly without waiting for long timeouts.
        try {
            Locator numericSpans = page.locator("main span").filter(
                    new Locator.FilterOptions().setHasText(Pattern.compile("\\d+"))
            );
            int spanCount = numericSpans.count();
            for (int i = 0; i < spanCount; i++) {
                String candidate = numericSpans.nth(i).textContent();
                if (candidate != null && !candidate.trim().isEmpty()) {
                    return candidate.trim();
                }
            }
        } catch (Exception ignored) {
        }
        return "";
    }
    public String getProductImageAlt()       { return productImage().getAttribute("alt"); }

    /**
     * Checks whether the page is in a guarded / not-found state (for invalid product IDs).
     */
    public boolean isNotFoundStateVisible() {
        try {
            return notFoundMessage().isVisible();
        } catch (Exception e) {
            // If no "not found" message, check if the product name is absent
            try {
                return !productName().isVisible();
            } catch (Exception ex) {
                return true;
            }
        }
    }
}

