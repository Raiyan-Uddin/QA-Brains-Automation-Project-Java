// src/main/java/com/qabrains/pages/checkoutinfo/CheckoutInfoPage.java

package com.qabrains.pages.checkoutinfo;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import com.qabrains.config.AppConfig;

/**
 * Page Object Model for the Checkout: Your Information Page.
 * URL: https://practice.qabrains.com/ecommerce/checkout-info
 */
public class CheckoutInfoPage {

    private final Page page;

    public CheckoutInfoPage(Page page) {
        this.page = page;
    }

    // ========================
    // LOCATORS — PRIVATE
    // ========================

    // Heading ("Checkout: Your Information" or similar)
    private Locator heading() {
        return page.locator(
                "[data-testid='checkout-info-heading'], " +
                "h1:has-text('Checkout: Your Information'), h2:has-text('Checkout: Your Information'), h3:has-text('Checkout: Your Information'), " +
                "h1:has-text('Information'), h2:has-text('Information'), h3:has-text('Information')"
        ).first();
    }

    // Email field (may be pre-filled, read-only)
    private Locator emailField() {
        return page.locator(
                "input[data-testid='checkout-email'], " +
                "div.form-group:has(label:has-text('Email')) input, " +
                "input[type='email'], input[name*='email' i], input[placeholder*='email' i]"
        ).first();
    }

    // First name field (placeholder "Ex. John" per spec)
    private Locator firstNameField() {
        return page.locator(
                "input[data-testid='checkout-first-name'], " +
                "div.form-group:has(label:has-text('First Name')) input, " +
                "input[placeholder='Ex. John'], input[name*='firstName'], " +
                "input[name*='first_name'], input[name*='firstname'], " +
                "input[placeholder*='first' i]"
        ).first();
    }

    // Last name field (placeholder "Ex. Doe" per spec)
    private Locator lastNameField() {
        return page.locator(
                "input[data-testid='checkout-last-name'], " +
                "div.form-group:has(label:has-text('Last Name')) input, " +
                "input[placeholder='Ex. Doe'], input[name*='lastName'], " +
                "input[name*='last_name'], input[name*='lastname'], " +
                "input[placeholder*='last' i]"
        ).first();
    }

    // Zip / Postal code field
    private Locator zipField() {
        return page.locator(
                "input[data-testid='checkout-zip'], input[data-testid='checkout-postal'], " +
                "div.form-group:has(label:has-text('Zip')) input, " +
                "div.form-group:has(label:has-text('Postal')) input, " +
                "input[name*='zip' i], input[placeholder*='zip' i], " +
                "input[placeholder*='postal' i], input[name*='postal' i]"
        ).first();
    }

    // Continue button
    private Locator continueButton() {
        return page.locator(
                "button[data-testid='checkout-continue'], " +
                "button:has-text('Continue'), input[type='submit'][value*='Continue'], " +
                "a:has-text('Continue')"
        ).first();
    }

    // Cancel button
    private Locator cancelButton() {
        return page.locator(
                "button[data-testid='checkout-cancel'], button:has-text('Cancel'), a:has-text('Cancel')"
        ).first();
    }

    // Validation error messages
    private Locator validationErrors() {
        return page.locator(
                "[data-testid='checkout-error'], " +
                "[class*='error'], [class*='Error'], [role='alert'], " +
                ".error-message, [class*='invalid'], [class*='Invalid']"
        );
    }

    // ========================
    // NAVIGATION
    // ========================

    public void navigateToCheckoutInfo() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                page.navigate(
                        AppConfig.CHECKOUT_INFO_URL,
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

    public Locator getHeading()           { return heading(); }
    public Locator getEmailField()        { return emailField(); }
    public Locator getFirstNameField()    { return firstNameField(); }
    public Locator getLastNameField()     { return lastNameField(); }
    public Locator getZipField()          { return zipField(); }
    public Locator getContinueButton()    { return continueButton(); }
    public Locator getCancelButton()      { return cancelButton(); }
    public Locator getValidationErrors()  { return validationErrors(); }

    public String getCurrentURL() { return page.url(); }

    // ========================
    // ACTIONS
    // ========================

    public void fillFirstName(String value) {
        firstNameField().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        firstNameField().fill(value);
    }

    public void fillLastName(String value) {
        lastNameField().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        lastNameField().fill(value);
    }

    public void fillZip(String value) {
        zipField().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        zipField().fill(value);
    }

    public void fillForm(String firstName, String lastName, String zip) {
        fillFirstName(firstName);
        fillLastName(lastName);
        fillZip(zip);
        System.out.println("  [FILL] Filled form: firstName='" + firstName + "', lastName='" + lastName + "', zip='" + zip + "'.");
    }

    public void clickContinue() {
        continueButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        continueButton().click();
        System.out.println("  [CLICK] Clicked Continue button.");
    }

    public void clickCancel() {
        cancelButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        cancelButton().click();
        System.out.println("  [CLICK] Clicked Cancel button.");
    }

    public void clearAllFields() {
        try { firstNameField().fill(""); } catch (Exception ignored) {}
        try { lastNameField().fill(""); } catch (Exception ignored) {}
        try { zipField().fill(""); } catch (Exception ignored) {}
        System.out.println("  [CLEAR] Cleared all checkout info fields.");
    }

    // ========================
    // STATE QUERIES
    // ========================

    public boolean isHeadingVisible() {
        try { return heading().isVisible(); }
        catch (Exception e) { return false; }
    }

    public boolean isValidationErrorVisible() {
        try { return validationErrors().first().isVisible(); }
        catch (Exception e) { return false; }
    }
}

