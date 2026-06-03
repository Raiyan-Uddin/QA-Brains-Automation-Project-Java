// src/main/java/com/qabrains/pages/home/HomePage.java

package com.qabrains.pages.home;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import com.qabrains.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Model for the Home Page.
 * URL: https://practice.qabrains.com/ecommerce
 *
 * DOM verified via diagnostics on 2026-06-02:
 *  - Cart  : <span role="button" class="text-[20px] sm:text-[28px] cursor-pointer relative"> (in header)
 *  - Badge : <span class="bg-qa-clr ... absolute ...">1</span>  (inside cart span)
 *  - Profile: <button aria-haspopup="menu" ...>
 *  - Dropdown items: <div role="menuitem">
 *  - Products grid: <div class="products grid ...">
 *  - Product card: direct <div> children of .products
 *  - Product name: <a class="... font-oswald ..." href="/ecommerce/product-details?id=N">
 *  - Price: <span class="text-lg font-bold text-black">$49.99</span>
 *  - Add to cart button: text = "Add to cart"  (lowercase 'c')
 *  - Favourite: <span class="absolute ..." role="button"><button ...>
 *  - Sort: <button data-slot="popover-trigger" role="combobox">
 */
public class HomePage {

    private final Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    // ========================
    // LOCATORS — PRIVATE
    // ========================

    // Logo (image tag inside header anchor)
    private Locator logoButton() {
        return page.locator(
                "[data-testid='logo'], [data-testid='app-logo'], [data-testid='logo-link'] img, " +
                "header a[aria-label*='home' i] img, header a[href*='ecommerce'] img"
        ).first();
    }

    // Cart icon = span[role='button'] in header (clicking navigates to /cart)
    private Locator cartButton() {
        return page.locator(
                "[data-testid='cart-button'], [data-testid='header-cart'], " +
                "header [aria-label*='cart' i], header span[role='button']"
        ).first();
    }

    // Cart item-count badge (appears after adding to cart)
    private Locator cartBadge() {
        return page.locator(
                "[data-testid='cart-badge'], [data-testid='cart-count'], " +
                "header [class*='cart-badge'], header [class*='cart-count'], header span[class*='bg-qa-clr'], header .bg-qa-clr"
        ).first();
    }

    // Profile / account button
    private Locator profileButton() {
        return page.locator("[data-testid='profile-menu-trigger'], button[aria-haspopup='menu']").first();
    }

    // Profile dropdown menu container
    private Locator profileDropdownMenu() {
        return page.locator("[data-testid='profile-menu'], [role='menu']").first();
    }

    // Favourites item inside profile dropdown
    private Locator favouritesMenuItem() {
        return page.locator(
                "[data-testid='menu-favourites'], [data-testid='menu-favorites'], " +
                "[role='menuitem']:has-text('Favourites'), [role='menuitem']:has-text('Favorite')"
        ).first();
    }

    // Logout item inside profile dropdown
    private Locator logoutMenuItem() {
        return page.locator(
                "[data-testid='menu-logout'], [role='menuitem']:has-text('Logout'), [role='menuitem']:has-text('Log out')"
        ).first();
    }

    // "Products" heading
    private Locator productsHeading() {
        return page.locator("[data-testid='products-heading'], h1, h2, h3")
                .filter(new Locator.FilterOptions().setHasText("Products"))
                .first();
    }

    // Sort combobox trigger button
    private Locator sortDropdown() {
        return page.locator("[data-testid='sort-dropdown'], button[role='combobox'], button[data-slot='popover-trigger']").first();
    }

    // All product cards = direct <div> children of the .products grid
    private Locator productCards() {
        return page.locator("[data-testid='product-card'], [data-testid^='product-card-'], .products > div");
    }

    // Convenience — first card
    private Locator firstProductCard() {
        return productCards().first();
    }

    // Product name anchor (has class font-oswald, points to product-details)
    private Locator firstProductName() {
        return firstProductCard().locator(
                "[data-testid='product-name'], " +
                "a[class*='font-oswald'][href*='product-details'], " +
                "a[class*='font-semibold'][href*='product-details']"
        ).first();
    }

    // Price span inside the card
    private Locator firstProductPrice() {
        return firstProductCard().locator(
                "[data-testid='product-price'], " +
                "span[class*='font-bold']:has-text('$'), " +
                "div[class*='justify-between'] span:has-text('$'), " +
                "span:has-text('$')"
        ).first();
    }

    // Product image
    private Locator firstProductImage() {
        return firstProductCard().locator("[data-testid='product-image'], img").first();
    }

    // "Add to cart" button (lowercase 'c')
    private Locator firstProductAddToCartButton() {
        return firstProductCard().locator(
                "button[data-testid='add-to-cart'], button[data-action='add-to-cart'], " +
                "button:has-text('Add to cart'), button:has-text('Add to Cart')"
        ).first();
    }

    // Heart / favourite button (inside .absolute span wrapper)
    private Locator firstProductFavouriteButton() {
        return firstProductCard().locator(
                "button[data-testid='favorite-toggle'], button[data-testid='favourite-toggle'], " +
                "span[class*='absolute'] button, span[role='button'] button"
        ).first();
    }

    // Clickable PDP link — first anchor pointing to product-details
    private Locator firstProductClickTarget() {
        return firstProductCard().locator("[data-testid='product-link'], a[href*='product-details']").first();
    }

    // Search input — standalone product search (NOT the sort combobox filter input)
    private Locator searchInput() {
        return page.locator(
                "input[data-testid='product-search'], input[data-testid='search-input'], " +
                "input[type='search']:not([cmdk-input]), " +
                "input[placeholder*='Search' i]:not([cmdk-input]):not([data-slot='command-input']), " +
                "input[placeholder*='search' i]:not([cmdk-input]):not([data-slot='command-input'])"
        ).first();
    }

    // "No products found" message
    private Locator noResultsMessage() {
        return page.locator(
                "[data-testid='no-results'], " +
                "*:has-text('No products found'), *:has-text('no products found'), " +
                "*:has-text('No results'), *:has-text('no results')"
        ).first();
    }

    // Footer
    private Locator footer() {
        return page.locator("[data-testid='app-footer'], footer, [role='contentinfo']").first();
    }

    private Locator firstFooterLink() {
        return page.locator("[data-testid='footer-link'], footer a, [role='contentinfo'] a").first();
    }

    // ========================
    // NAVIGATION
    // ========================

    public void navigateToHomePage() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                page.navigate(
                        AppConfig.HOME_URL,
                        new Page.NavigateOptions()
                                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                                .setTimeout((double) AppConfig.DEFAULT_TIMEOUT)
                );
                waitForHomePageReady();
                System.out.println("  [OK] Navigated to Home Page: " + AppConfig.HOME_URL);
                return;
            } catch (RuntimeException ex) {
                if (attempt == 3) throw ex;
                page.waitForTimeout(1200);
            }
        }
    }

    private void waitForHomePageReady() {
        productsHeading().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout((double) AppConfig.DEFAULT_TIMEOUT)
        );
    }

    // ========================
    // GETTERS
    // ========================

    public Page getPage()                       { return this.page; }
    public Locator getLogoButton()              { return logoButton(); }
    public Locator getCartButton()              { return cartButton(); }
    public Locator getCartBadge()               { return cartBadge(); }
    public Locator getProfileButton()           { return profileButton(); }
    public Locator getProfileDropdownMenu()     { return profileDropdownMenu(); }
    public Locator getFavouritesMenuItem()      { return favouritesMenuItem(); }
    public Locator getLogoutMenuItem()          { return logoutMenuItem(); }
    public Locator getProductsHeading()         { return productsHeading(); }
    public Locator getSortDropdown()            { return sortDropdown(); }
    public Locator getProductCards()            { return productCards(); }
    public Locator getFirstProductCard()        { return firstProductCard(); }
    public Locator getFirstProductName()        { return firstProductName(); }
    public Locator getFirstProductPrice()       { return firstProductPrice(); }
    public Locator getFirstProductImage()       { return firstProductImage(); }
    public Locator getFirstAddToCartButton()    { return firstProductAddToCartButton(); }
    public Locator getFirstFavouriteButton()    { return firstProductFavouriteButton(); }
    public Locator getSearchInput()             { return searchInput(); }
    public Locator getNoResultsMessage()        { return noResultsMessage(); }
    public Locator getFooter()                  { return footer(); }
    public Locator getFirstFooterLink()         { return firstFooterLink(); }
    public String getCurrentURL()               { return page.url(); }

    // ========================
    // ACTIONS
    // ========================

    public void clickLogoButton() {
        logoButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        logoButton().click();
        System.out.println("  [CLICK] Clicked Logo button.");
    }

    /**
     * Clicks the cart icon (span[role='button']).
     * Clicking navigates to /cart.
     */
    public void clickCartButton() {
        cartButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        cartButton().click();
        page.waitForURL("**/cart**",
                new Page.WaitForURLOptions().setTimeout((double) AppConfig.DEFAULT_TIMEOUT));
        System.out.println("  [CLICK] Clicked Cart button.");
    }

    /**
     * Opens or closes the profile dropdown.
     * Waits 800 ms after click to allow Radix animation to complete.
     */
    public void clickProfileButton() {
        profileButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        try {
            profileButton().click();
        } catch (Exception e) {
            profileButton().click(new Locator.ClickOptions().setForce(true));
        }
        page.waitForTimeout(800); // allow Radix dropdown animation
        System.out.println("  [CLICK] Clicked Profile button.");
    }

    public void clickFavouritesMenuItem() {
        favouritesMenuItem().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        favouritesMenuItem().click();
        System.out.println("  [CLICK] Clicked Favourites menu item.");
    }

    public void clickLogoutMenuItem() {
        logoutMenuItem().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        logoutMenuItem().click();
        System.out.println("  [CLICK] Clicked Logout menu item.");
    }

    /**
     * Selects a sort option from the Radix UI Combobox / Popover dropdown.
     * Uses [role='option'] selector confirmed by DOM diagnostic.
     */
    public void selectSortOption(String optionText) {
        Locator dropdown = sortDropdown();
        dropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        // Open the dropdown
        dropdown.click();
        page.waitForTimeout(800);

        // Primary: use [role='option'] — confirmed present via diagnostic
        Locator optionItem = page.locator("[data-testid='sort-option'], [role='option']")
                .filter(new Locator.FilterOptions().setHasText(optionText)).first();

        try {
            optionItem.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
            optionItem.click();
        } catch (Exception e) {
            // Fallback to cmdk-item
            try {
                page.locator("[cmdk-item]")
                        .filter(new Locator.FilterOptions().setHasText(optionText))
                        .first().click();
            } catch (Exception ex) {
                // Last resort: getByText
                page.getByText(optionText).first().click();
            }
        }

        page.waitForTimeout(1500); // allow DOM re-render after sort
        System.out.println("  [SORT] Selected sort option: " + optionText);
    }

    /**
     * Checks if a sort option with the given text exists in the dropdown.
     */
    public boolean sortOptionExists(String optionText) {
        try {
            Locator dropdown = sortDropdown();
            if (!dropdown.isVisible()) return false;

            dropdown.click();
            page.waitForTimeout(800);

            boolean exists = page.locator("[data-testid='sort-option'], [role='option']")
                    .filter(new Locator.FilterOptions().setHasText(optionText))
                    .first().isVisible();

            // Close dropdown
            try { page.keyboard().press("Escape"); } catch (Exception ignored) {}
            page.waitForTimeout(200);
            return exists;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickFirstProductAddToCart() {
        firstProductAddToCartButton().waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        firstProductAddToCartButton().click();
        System.out.println("  [CLICK] Clicked Add to Cart on first product.");
    }

    public void clickFirstProductFavourite() {
        firstProductFavouriteButton().waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        firstProductFavouriteButton().click();
        System.out.println("  [CLICK] Clicked Favourite button on first product.");
    }

    public void clickFirstProduct() {
        firstProductClickTarget().waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        firstProductClickTarget().click();
        System.out.println("  [CLICK] Clicked first product card.");
    }

    /**
     * Types a keyword into the product search input if one exists.
     * Does NOT open the sort combobox (that input filters sort options, not products).
     */
    public void searchForKeyword(String keyword) {
        if (searchInput().isVisible()) {
            searchInput().fill(keyword);
            page.waitForTimeout(600);
            System.out.println("  [SEARCH] Searched for: " + keyword);
        } else {
            System.out.println("  [WARN] No product search input found; keyword '" + keyword + "' could not be applied.");
        }
    }

    public void clearSearch() {
        if (searchInput().isVisible()) {
            searchInput().fill("");
            page.waitForTimeout(500);
            System.out.println("  [CLEAR] Cleared search input.");
        }
    }

    public void scrollToFooter() {
        footer().scrollIntoViewIfNeeded();
        System.out.println("  [SCROLL] Scrolled to footer.");
    }

    // ========================
    // STATE QUERIES
    // ========================

    public boolean isLogoVisible()              { return logoButton().isVisible(); }
    public boolean isCartButtonVisible()        { return cartButton().isVisible(); }
    public boolean isCartBadgeVisible()         { return cartBadge().isVisible(); }
    public boolean isProfileButtonVisible()     { return profileButton().isVisible(); }
    public boolean isProductsHeadingVisible()   { return productsHeading().isVisible(); }
    public boolean isSortDropdownVisible()      { return sortDropdown().isVisible(); }
    public boolean isFooterVisible()            { return footer().isVisible(); }
    public boolean isNoResultsVisible()         { return noResultsMessage().isVisible(); }

    public boolean isProfileDropdownOpen() {
        try {
            return profileDropdownMenu().isVisible()
                    || favouritesMenuItem().isVisible()
                    || logoutMenuItem().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFavouritesMenuItemVisible() {
        try { return favouritesMenuItem().isVisible(); } catch (Exception e) { return false; }
    }

    public boolean isLogoutMenuItemVisible() {
        try { return logoutMenuItem().isVisible(); } catch (Exception e) { return false; }
    }

    public String getProductsHeadingText()  { return productsHeading().textContent().trim(); }
    public String getFirstProductNameText() { return firstProductName().textContent().trim(); }
    public String getFirstProductPriceText(){ return firstProductPrice().textContent().trim(); }
    public int    getProductCardCount()      { return productCards().count(); }

    /**
     * Returns all visible product names using the font-oswald anchor locator.
     */
    public List<String> getAllProductNames() {
        List<String> names = new ArrayList<>();
        int count = productCards().count();
        for (int i = 0; i < count; i++) {
            Locator card = productCards().nth(i);
            Locator nameLoc = card.locator(
                    "[data-testid='product-name'], " +
                    "a[class*='font-oswald'][href*='product-details'], " +
                    "a[class*='font-semibold'][href*='product-details']"
            ).first();
            try {
                if (nameLoc.isVisible()) {
                    String text = nameLoc.textContent().trim();
                    if (!text.isEmpty()) names.add(text);
                }
            } catch (Exception ignored) {}
        }
        return names;
    }

    /**
     * Returns all visible product prices as doubles.
     */
    public List<Double> getAllProductPrices() {
        List<Double> prices = new ArrayList<>();
        int count = productCards().count();
        for (int i = 0; i < count; i++) {
            Locator card = productCards().nth(i);
            Locator priceLoc = card.locator(
                    "[data-testid='product-price'], " +
                    "span[class*='font-bold']:has-text('$'), " +
                    "div[class*='justify-between'] span:has-text('$'), " +
                    "span:has-text('$')"
            ).first();
            try {
                if (priceLoc.isVisible()) {
                    String raw = priceLoc.textContent().trim().replaceAll("[^0-9.]", "");
                    if (!raw.isEmpty()) prices.add(Double.parseDouble(raw));
                }
            } catch (Exception ignored) {}
        }
        return prices;
    }
}

