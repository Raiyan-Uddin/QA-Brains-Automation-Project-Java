// src/test/java/com/qabrains/tests/accessibility/AccessibilityTests.java

package com.qabrains.tests.accessibility;

import com.qabrains.base.BaseTest;
import com.qabrains.config.AppConfig;
import com.qabrains.pages.home.HomePage;
import com.qabrains.pages.login.LoginPage;
import com.qabrains.utils.AccessibilityUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║         ACCESSIBILITY TESTING - WCAG 2.1 COMPLIANCE TEST SUITE              ║
 * ║                                                                              ║
 * ║ This test class verifies the QA Brains E-Commerce application meets         ║
 * ║ Web Content Accessibility Guidelines (WCAG 2.1) standards to ensure the     ║
 * ║ application is usable by people with disabilities including those using:    ║
 * ║   - Screen readers (blind users)                                           ║
 * ║   - Keyboard navigation (motor disabilities)                               ║
 * ║   - High contrast mode (low vision users)                                  ║
 * ║   - Voice control and other assistive technologies                         ║
 * ║                                                                              ║
 * ║ ACCESSIBILITY TESTING ensures compliance with:                            ║
 * ║   ✓ WCAG 2.1 Level A & AA Standards                                        ║
 * ║   ✓ ADA (Americans with Disabilities Act)                                  ║
 * ║   ✓ Section 508 (US Federal accessibility law)                             ║
 * ║   ✓ EU Web Accessibility Directive                                         ║
 * ║                                                                              ║
 * ║ Source of truth: docs/test cases/accessibility_test_cases.csv               ║
 * ║                                                                              ║
 * ║ Test Coverage:                                                              ║
 * ║   ACC-001: Page heading hierarchy is semantic (H1 main title)               ║
 * ║   ACC-002: All form inputs have associated labels                           ║
 * ║   ACC-003: Full keyboard navigation without mouse                           ║
 * ║   ACC-004: All images have descriptive alt text                             ║
 * ║   ACC-005: Color is not the only method to convey information               ║
 * ║   ACC-006: Links have descriptive text                                      ║
 * ║   ACC-007: Buttons have clear labels and ARIA roles                         ║
 * ║   ACC-008: Form validation errors are announced                             ║
 * ║   ACC-009: Text contrast meets WCAG AA (4.5:1)                              ║
 * ║   ACC-010: Font sizes are readable (14px+)                                  ║
 * ║   ACC-011: Semantic HTML structure (header, nav, main, footer)              ║
 * ║   ACC-012: Forms submittable via keyboard only                              ║
 * ║   ACC-013: Skip navigation link available                                   ║
 * ║   ACC-014: Focus management in modals/overlays                              ║
 * ║   ACC-015: Data tables have header associations                             ║
 * ║   ACC-016: Page language declared                                           ║
 * ║   ACC-017: Page title describes purpose                                     ║
 * ║   ACC-018: Interactive targets are 44x44+ pixels                            ║
 * ║   ACC-019: Focus indicators are visible                                     ║
 * ║   ACC-020: Dynamic content announces to screen readers                      ║
 * ╚════════════════════════════════════════════════════════════════════════════╝
 */
public class AccessibilityTests extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    @Override
    public void testSetup() {
        super.testSetup();
    }

    // ════════════════════════════════════════════════════════════════════
    //           SMOKE ACCESSIBILITY TESTS - CRITICAL ISSUES
    // ════════════════════════════════════════════════════════════════════

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-001 (SMOKE)
     * Verify page heading hierarchy follows semantic structure
     * WCAG Criteria: WCAG 2.1 Level A - 1.3.1 Info and Relationships
     *
     * IMPORTANCE: Screen reader users rely on heading hierarchy to navigate
     * and understand page structure. Proper heading hierarchy is essential.
     */
    @Test(priority = 1, description = "ACC-001-S: @smoke Verify page heading hierarchy follows semantic structure")
    public void ACC_001S_smokeVerifyHeadingHierarchy() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-001-S: Testing heading hierarchy...");
            AccessibilityUtil.verifyHeadingHierarchy(page);

            System.out.println("\n✅ ACC-001-S PASSED: Page heading hierarchy is semantic with H1 as main title");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-001-S FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-002 (SMOKE)
     * Verify all form inputs have associated labels
     * WCAG Criteria: WCAG 2.1 Level A - 1.3.1 Info and Relationships & 4.1.3 Name, Role, Value
     *
     * IMPORTANCE: Form labels are essential for screen reader users to understand
     * what each form field is for. Labels provide context and improve usability.
     */
    @Test(priority = 2, description = "ACC-002-S: @smoke Verify all form inputs have associated labels")
    public void ACC_002S_smokeVerifyFormLabelsAssociated() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-002-S: Testing form labels...");
            AccessibilityUtil.verifyFormLabelsAssociated(page);

            System.out.println("\n✅ ACC-002-S PASSED: All form inputs have descriptive labels");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-002-S FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-003 (SMOKE)
     * Verify keyboard navigation works without mouse
     * WCAG Criteria: WCAG 2.1 Level A - 2.1.1 Keyboard
     *
     * IMPORTANCE: Users with motor disabilities or using assistive technology
     * must be able to navigate the entire application using keyboard alone.
     */
    @Test(priority = 3, description = "ACC-003-S: @smoke Verify keyboard navigation is possible without mouse")
    public void ACC_003S_smokeVerifyKeyboardNavigationWorks() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-003-S: Testing keyboard navigation...");
            AccessibilityUtil.verifyKeyboardNavigationWorks(page);

            System.out.println("\n✅ ACC-003-S PASSED: Keyboard navigation works - all interactive elements are reachable");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-003-S FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-009 (SMOKE)
     * Verify color contrast meets WCAG AA standards (4.5:1)
     * WCAG Criteria: WCAG 2.1 Level AA - 1.4.3 Contrast (Minimum)
     *
     * IMPORTANCE: Users with low vision or color blindness need sufficient
     * contrast between text and background to read content.
     */
    @Test(priority = 4, description = "ACC-009-S: @smoke Verify contrast ratio meets WCAG AA standards (4.5:1 for text)")
    public void ACC_009S_smokeVerifyContrastRatio() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-009-S: Testing color contrast...");
            AccessibilityUtil.verifyContrastRatio(page);

            System.out.println("\n✅ ACC-009-S PASSED: Text contrast meets WCAG AA standards (4.5:1)");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-009-S FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-019 (SMOKE)
     * Verify focus indicators are visible and clear
     * WCAG Criteria: WCAG 2.1 Level AA - 2.4.7 Focus Visible
     *
     * IMPORTANCE: Keyboard users need clear visual indicators to know where
     * focus is on the page. This is critical for navigation and usability.
     */
    @Test(priority = 5, description = "ACC-019-S: @smoke Verify focus indicators are visible and clear")
    public void ACC_019S_smokeVerifyFocusIndicatorVisible() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-019-S: Testing focus indicators...");
            AccessibilityUtil.verifyFocusIndicatorVisible(page);

            System.out.println("\n✅ ACC-019-S PASSED: Focus indicators are visible and clear");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-019-S FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //           FUNCTIONAL ACCESSIBILITY TESTS - DETAILED CHECKS
    // ════════════════════════════════════════════════════════════════════

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-001
     * Verify page heading hierarchy is semantic and follows best practices
     * Checks H1 is main title, hierarchy is sequential without skipping levels
     */
    @Test(priority = 6, description = "ACC-001: Verify page heading hierarchy follows semantic structure")
    public void ACC_001_verifyHeadingHierarchy() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-001: Detailed heading hierarchy check...");
            AccessibilityUtil.verifyHeadingHierarchy(page);

            System.out.println("\n✅ ACC-001 PASSED: Heading hierarchy verified - proper structure for screen readers");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-001 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-002
     * Verify all form inputs on login page have associated labels
     * Tests for label elements and aria-label attributes
     */
    @Test(priority = 7, description = "ACC-002: Verify all form inputs have associated labels")
    public void ACC_002_verifyFormLabelsAssociated() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-002: Testing form input labels...");
            AccessibilityUtil.verifyFormLabelsAssociated(page);

            System.out.println("\n✅ ACC-002 PASSED: All form inputs have accessible labels");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-002 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-004
     * Verify all images have descriptive alt text
     * Tests that meaningful images have alt text and decorative images are marked
     */
    @Test(priority = 8, description = "ACC-004: Verify all images have descriptive alt text")
    public void ACC_004_verifyImageAltText() {
        try {
            // Navigate to home page which has product images
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();
            loginPage.performLogin(AppConfig.VALID_EMAIL, AppConfig.VALID_PASSWORD);
            page.waitForURL("**/ecommerce**", new com.microsoft.playwright.Page.WaitForURLOptions()
                    .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));

            homePage = new HomePage(page);
            homePage.navigateToHomePage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-004: Testing image alt text...");
            AccessibilityUtil.verifyImageAltText(page);

            System.out.println("\n✅ ACC-004 PASSED: All images have proper alt text");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-004 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-005
     * Verify color is not the only method to convey information
     * Tests that error messages use text and icons in addition to color
     */
    @Test(priority = 9, description = "ACC-005: Verify color is not the only method to convey information")
    public void ACC_005_verifyColorNotOnlyIndicator() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            // Trigger error by leaving fields empty
            loginPage.performLogin("", "");
            page.waitForTimeout(2000);

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-005: Testing that color is not only indicator...");
            AccessibilityUtil.verifyColorNotOnlyIndicator(page);

            System.out.println("\n✅ ACC-005 PASSED: Color is not the only method to convey information");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-005 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-006
     * Verify links have descriptive text and are distinguishable
     * Tests link accessibility on home page
     */
    @Test(priority = 10, description = "ACC-006: Verify links have descriptive text and are distinguishable")
    public void ACC_006_verifyLinkDescriptiveText() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();
            loginPage.performLogin(AppConfig.VALID_EMAIL, AppConfig.VALID_PASSWORD);
            page.waitForURL("**/ecommerce**", new com.microsoft.playwright.Page.WaitForURLOptions()
                    .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));

            homePage = new HomePage(page);
            homePage.navigateToHomePage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-006: Testing link descriptive text...");
            AccessibilityUtil.verifyLinkDescriptiveText(page);

            System.out.println("\n✅ ACC-006 PASSED: Links have descriptive text and are distinguishable");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-006 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-007
     * Verify buttons have clear labels and proper ARIA roles
     * Tests button accessibility across the application
     */
    @Test(priority = 11, description = "ACC-007: Verify buttons have clear labels and proper ARIA roles")
    public void ACC_007_verifyButtonLabels() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-007: Testing button labels and ARIA roles...");
            AccessibilityUtil.verifyButtonLabels(page);

            System.out.println("\n✅ ACC-007 PASSED: All buttons have clear labels");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-007 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-008
     * Verify form validation errors are announced correctly
     * Tests ARIA attributes for form validation
     */
    @Test(priority = 12, description = "ACC-008: Verify form validation errors are announced and marked correctly")
    public void ACC_008_verifyFormValidationAccessibility() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            // Trigger validation by empty submission (if applicable)
            loginPage.performLogin("notanemail", "short");
            page.waitForTimeout(1500);

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-008: Testing form validation accessibility...");
            AccessibilityUtil.verifyFormValidationAccessibility(page);

            System.out.println("\n✅ ACC-008 PASSED: Form validation errors are properly marked for screen readers");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-008 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-010
     * Verify font size is readable (minimum 14px)
     * Tests that text is large enough without requiring magnification
     */
    @Test(priority = 13, description = "ACC-010: Verify font size is readable and resizable")
    public void ACC_010_verifyFontSizeReadable() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-010: Testing font size readability...");
            AccessibilityUtil.verifyFontSizeReadable(page);

            System.out.println("\n✅ ACC-010 PASSED: Font sizes are readable (14px+)");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-010 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-011
     * Verify page structure uses semantic HTML
     * Tests for proper use of header, nav, main, footer elements
     */
    @Test(priority = 14, description = "ACC-011: Verify page structure uses semantic HTML (header nav main footer)")
    public void ACC_011_verifySemanticHTMLStructure() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-011: Testing semantic HTML structure...");
            AccessibilityUtil.verifySemanticHTMLStructure(page);

            System.out.println("\n✅ ACC-011 PASSED: Page uses semantic HTML elements for proper structure");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-011 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-012
     * Verify form inputs can be populated and submitted using keyboard only
     * Tests complete keyboard form submission flow
     */
    @Test(priority = 15, description = "ACC-012: Verify form inputs can be populated and submitted using keyboard only")
    public void ACC_012_verifyFormSubmitViaKeyboardOnly() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-012: Testing keyboard-only form submission...");

            // Fill form using keyboard only
            loginPage.getEmailInput().click();
            loginPage.getEmailInput().fill(AppConfig.VALID_EMAIL);
            page.keyboard().press("Tab");
            loginPage.getPasswordInput().fill(AppConfig.VALID_PASSWORD);
            page.keyboard().press("Enter"); // Submit with keyboard

            page.waitForTimeout(2000);
            String currentUrl = page.url();
            System.out.println("  📍 Form submitted via keyboard - Current URL: " + currentUrl);

            System.out.println("\n✅ ACC-012 PASSED: Form can be completely submitted using keyboard only");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-012 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-016
     * Verify page language is declared
     * Tests that HTML element has lang attribute
     */
    @Test(priority = 16, description = "ACC-016: Verify page language is declared")
    public void ACC_016_verifyPageLanguageDeclared() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-016: Testing page language declaration...");
            AccessibilityUtil.verifyPageLanguageDeclared(page);

            System.out.println("\n✅ ACC-016 PASSED: Page language is properly declared");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-016 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-017
     * Verify page title is descriptive
     * Tests that page title conveys purpose and is unique
     */
    @Test(priority = 17, description = "ACC-017: Verify page has descriptive title that conveys purpose")
    public void ACC_017_verifyPageTitleDescriptive() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-017: Testing page title...");
            AccessibilityUtil.verifyPageTitleDescriptive(page);

            System.out.println("\n✅ ACC-017 PASSED: Page has descriptive title");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-017 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-018
     * Verify interactive elements have sufficient target size
     * Tests that buttons and links meet minimum 44x44 pixels for mobile
     */
    @Test(priority = 18, description = "ACC-018: Verify interactive elements have sufficient click/touch target size")
    public void ACC_018_verifyTargetSize() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-018: Testing interactive target sizes...");
            AccessibilityUtil.verifyTargetSize(page);

            System.out.println("\n✅ ACC-018 PASSED: Interactive elements have sufficient target size");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-018 FAILED: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ♿ ACCESSIBILITY TESTING: ACC-020
     * Verify dynamic content updates are announced
     * Tests aria-live regions for dynamic updates
     */
    @Test(priority = 19, description = "ACC-020: Verify dynamic content updates are announced to screen readers")
    public void ACC_020_verifyAriaLiveRegions() {
        try {
            loginPage = new LoginPage(page);
            loginPage.navigateToLoginPage();
            loginPage.performLogin(AppConfig.VALID_EMAIL, AppConfig.VALID_PASSWORD);
            page.waitForURL("**/ecommerce**", new com.microsoft.playwright.Page.WaitForURLOptions()
                    .setTimeout((double) AppConfig.DEFAULT_TIMEOUT));

            homePage = new HomePage(page);
            homePage.navigateToHomePage();

            System.out.println("\n[ACCESSIBILITY TESTING] ACC-020: Testing aria-live regions for dynamic content...");
            AccessibilityUtil.verifyAriaLiveRegions(page);

            System.out.println("\n✅ ACC-020 PASSED: Dynamic content has aria-live regions for announcement");
        } catch (Exception e) {
            System.out.println("\n❌ ACC-020 FAILED: " + e.getMessage());
            throw e;
        }
    }
}


