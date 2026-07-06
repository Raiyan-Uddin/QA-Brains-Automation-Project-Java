# ♿ Accessibility Testing - QA Brains E-Commerce

## Overview

Accessibility Testing has been enabled for the QA Brains E-Commerce project to ensure the application is usable by all users, including those with disabilities. This implementation follows **WCAG 2.1 Level AA** standards and includes comprehensive tests for keyboard navigation, screen reader support, and visual accessibility.

---

## What is Accessibility Testing?

Accessibility Testing (also called A11Y testing) verifies that web applications are usable by people with disabilities, including:

- **👁️ Blind users** (Screen readers)
- **🖱️ Motor disabilities users** (Keyboard navigation, voice control)
- **👓 Low vision users** (Color contrast, font size)
- **🎧 Deaf users** (Captions, transcripts)
- **Cognitive disabilities users** (Clear language, consistent navigation)

### WCAG 2.1 Standards

- **Level A**: Basic accessibility
- **Level AA**: Enhanced accessibility (recommended standard)
- **Level AAA**: Enhanced accessibility (highest level)

This project targets **WCAG 2.1 Level AA** compliance.

---

## Files Added

### 1. **Test Cases**: `docs/test cases/accessibility_test_cases.csv`

Contains 20 comprehensive accessibility test cases covering:

| Test Case ID | Title | WCAG Criteria | Type |
|---|---|---|---|
| ACC-001 | Verify page heading hierarchy | 1.3.1 Info and Relationships | Accessibility |
| ACC-002 | Form inputs have labels | 1.3.1, 4.1.3 | Accessibility |
| ACC-003 | Keyboard navigation works | 2.1.1 Keyboard | Accessibility |
| ACC-004 | Images have alt text | 1.1.1 Non-text Content | Accessibility |
| ACC-005 | Color not only indicator | 1.4.1 Use of Color | Accessibility |
| ACC-006 | Links have descriptive text | 2.4.4 Link Purpose | Accessibility |
| ACC-007 | Buttons have clear labels | 4.1.3 Name, Role, Value | Accessibility |
| ACC-008 | Form validation announced | 3.3.3 Error Suggestion | Accessibility |
| ACC-009 | Text contrast meets standards | 1.4.3 Contrast (Minimum) | Accessibility |
| ACC-010 | Font sizes readable | 1.4.4 Resize Text | Accessibility |
| ACC-011 | Semantic HTML structure | 1.3.1 Info and Relationships | Accessibility |
| ACC-012 | Form submittable via keyboard | 2.1.1 Keyboard | Accessibility |
| ACC-013 | Skip navigation link | 2.4.1 Bypass Blocks | Accessibility |
| ACC-014 | Focus management in modals | 2.4.3 Focus Order | Accessibility |
| ACC-015 | Table headers associated | 1.3.1 Info and Relationships | Accessibility |
| ACC-016 | Page language declared | 3.1.1 Language of Page | Accessibility |
| ACC-017 | Page title descriptive | 2.4.2 Page Titled | Accessibility |
| ACC-018 | Target size sufficient | 2.5.5 Target Size | Accessibility |
| ACC-019 | Focus indicators visible | 2.4.7 Focus Visible | Accessibility |
| ACC-020 | Dynamic content announced | 4.1.3 Status Messages | Accessibility |

### 2. **Utility Class**: `src/main/java/com/qabrains/utils/AccessibilityUtil.java`

Helper methods for accessibility testing:

```java
// ♿ ACCESSIBILITY TESTING METHODS

// Verify heading hierarchy (H1 main title, no skipped levels)
AccessibilityUtil.verifyHeadingHierarchy(page);

// Verify form inputs have associated labels
AccessibilityUtil.verifyFormLabelsAssociated(page);

// Verify keyboard navigation works
AccessibilityUtil.verifyKeyboardNavigationWorks(page);

// Verify images have alt text
AccessibilityUtil.verifyImageAltText(page);

// Verify color is not only indicator
AccessibilityUtil.verifyColorNotOnlyIndicator(page);

// Verify links have descriptive text
AccessibilityUtil.verifyLinkDescriptiveText(page);

// Verify buttons have labels
AccessibilityUtil.verifyButtonLabels(page);

// Verify form validation is accessible
AccessibilityUtil.verifyFormValidationAccessibility(page);

// Verify color contrast (WCAG AA 4.5:1)
AccessibilityUtil.verifyContrastRatio(page);

// Verify font size is readable
AccessibilityUtil.verifyFontSizeReadable(page);

// Verify semantic HTML structure
AccessibilityUtil.verifySemanticHTMLStructure(page);

// Verify focus indicators are visible
AccessibilityUtil.verifyFocusIndicatorVisible(page);

// Verify page title is descriptive
AccessibilityUtil.verifyPageTitleDescriptive(page);

// Verify page language is declared
AccessibilityUtil.verifyPageLanguageDeclared(page);

// Verify target size is sufficient
AccessibilityUtil.verifyTargetSize(page);

// Verify aria-live regions for announcements
AccessibilityUtil.verifyAriaLiveRegions(page);
```

### 3. **Test Class**: `src/test/java/com/qabrains/tests/accessibility/AccessibilityTests.java`

Contains 19 test methods covering all accessibility test cases:

- **5 Smoke Tests** (Critical issues)
  - `ACC_001S_smokeVerifyHeadingHierarchy()`
  - `ACC_002S_smokeVerifyFormLabelsAssociated()`
  - `ACC_003S_smokeVerifyKeyboardNavigationWorks()`
  - `ACC_009S_smokeVerifyContrastRatio()`
  - `ACC_019S_smokeVerifyFocusIndicatorVisible()`

- **14 Functional Tests** (Detailed checks)
  - ACC-001 through ACC-020 (excluding smoke tests)

---

## Running Accessibility Tests

### Run All Accessibility Tests

```bash
mvn clean test -Dgroups=accessibility
```

### Run Only Smoke Accessibility Tests

```bash
mvn clean test -Dtest=AccessibilityTests#*S_*
```

### Run Specific Accessibility Test

```bash
mvn clean test -Dtest=AccessibilityTests#ACC_001S_smokeVerifyHeadingHierarchy
```

### Run with Allure Report

```powershell
.\allure\run-suite-with-allure.ps1
```

---

## Example Usage in Your Tests

### Add Accessibility Checks to Existing Tests

```java
import com.qabrains.utils.AccessibilityUtil;

@Test
public void myTest() {
    // Your normal test code
    page.navigate("https://example.com");
    
    // ♿ ACCESSIBILITY TESTING: Verify the page is accessible
    AccessibilityUtil.verifyHeadingHierarchy(page);
    AccessibilityUtil.verifyFormLabelsAssociated(page);
    AccessibilityUtil.verifyKeyboardNavigationWorks(page);
    
    // Continue with your test
}
```

### Create Custom Accessibility Tests

```java
import com.qabrains.base.BaseTest;
import com.qabrains.utils.AccessibilityUtil;
import org.testng.annotations.Test;

public class MyCustomAccessibilityTests extends BaseTest {
    
    /**
     * ♿ ACCESSIBILITY TESTING: Custom check for my specific page
     */
    @Test
    public void customPageAccessibility() {
        // Navigate to page
        page.navigate("https://practice.qabrains.com/ecommerce");
        
        // ♿ ACCESSIBILITY TESTING: Run multiple accessibility checks
        AccessibilityUtil.verifyHeadingHierarchy(page);
        AccessibilityUtil.verifySemanticHTMLStructure(page);
        AccessibilityUtil.verifyLinkDescriptiveText(page);
        AccessibilityUtil.verifyPageLanguageDeclared(page);
        
        System.out.println("✓ Custom page accessibility verified");
    }
}
```

---

## Key Features of Accessibility Tests

### ✅ Keyboard Navigation

Tests that all interactive elements are reachable and usable via keyboard:

```java
// Users can navigate using Tab key
page.keyboard().press("Tab");

// Users can activate buttons with Enter or Space
page.keyboard().press("Enter");

// Users can close modals with Escape
page.keyboard().press("Escape");
```

### ✅ Form Accessibility

Tests that form elements are properly labeled and accessible:

```java
// Input fields have associated labels
<label for="email">Email</label>
<input id="email" type="email">

// Or use aria-label for unlabeled inputs
<input aria-label="Email Address" type="email">
```

### ✅ Screen Reader Support

Tests ARIA attributes for screen reader announcement:

```java
// Invalid form fields are marked
<input aria-invalid="true" aria-describedby="error-msg">
<div id="error-msg" role="alert">Email is required</div>

// Dynamic updates are announced
<div aria-live="polite">Item added to cart</div>

// Buttons have clear labels
<button aria-label="Close menu">X</button>
```

### ✅ Visual Accessibility

Tests color contrast and font sizes:

```java
// WCAG AA requires 4.5:1 contrast ratio for text
// Color is never the only method to convey information
<p style="color: red;">✗ Error</p>  <!-- BAD -->
<p style="color: red;">✗ Error: Invalid email</p>  <!-- GOOD -->
```

### ✅ Semantic HTML

Tests proper use of semantic elements:

```java
<header>Navigation</header>
<nav>Links</nav>
<main>Content</main>
<footer>Footer Info</footer>
```

---

## Common Accessibility Issues Found & Fixed

### Issue 1: Missing Form Labels

**Problem**: Input fields without labels confuse screen reader users

**Solution**: 
```html
<!-- ✓ GOOD - Associated label -->
<label for="email">Email:</label>
<input id="email" type="email">

<!-- ✓ GOOD - aria-label -->
<input aria-label="Email" type="email">
```

### Issue 2: Images Without Alt Text

**Problem**: Screen reader users can't understand images

**Solution**:
```html
<!-- ✓ GOOD - Descriptive alt text -->
<img src="product.jpg" alt="Red wireless headphones">

<!-- ✓ GOOD - Decorative images marked empty -->
<img src="decoration.jpg" alt="">
```

### Issue 3: Poor Color Contrast

**Problem**: Low vision users can't read text

**Solution**: Ensure 4.5:1 contrast ratio
- Black text on white background: ✓ Good
- Light gray text on white: ✗ Bad
- Use contrast checkers to verify

### Issue 4: Keyboard Navigation Blocked

**Problem**: Users with motor disabilities can't navigate

**Solution**:
```javascript
/* ✓ GOOD - Keep focus outline visible */
button:focus {
    outline: 2px solid blue;
}

/* ✗ BAD - Hidden focus outline */
button:focus {
    outline: none;  /* Don't do this! */
}
```

### Issue 5: No Skip Links

**Problem**: Keyboard users must Tab through navigation

**Solution**:
```html
<!-- Add skip link at top of page -->
<a href="#main" class="skip-link">Skip to main content</a>
<header>Navigation</header>
<main id="main">Content</main>
```

---

## WCAG Checklist for Development

Use this checklist when building or updating features:

### 1.1 Text Alternatives
- [ ] All images have `alt` attribute
- [ ] Alt text is descriptive and meaningful
- [ ] Decorative images have `alt=""`

### 2.1 Keyboard Accessible
- [ ] All functionality works with Tab/Enter/Escape keys
- [ ] Focus indicator is visible (not hidden with `outline:none`)
- [ ] Tab order follows logical reading order

### 1.3 Adaptable
- [ ] Page structure uses semantic HTML (header, nav, main, footer)
- [ ] Headings follow hierarchy (H1, then H2, not H1 then H3)
- [ ] Form inputs have labels
- [ ] Lists use proper `<ul>`, `<ol>` elements

### 1.4 Distinguishable
- [ ] Text contrast is 4.5:1 minimum (WCAG AA)
- [ ] Color is not the only way to convey information
- [ ] Text can be resized to 200% without breaking
- [ ] Font size is at least 12-14px

### 3.3 Input Assistance
- [ ] Form errors are clear and identified
- [ ] Invalid form fields marked with `aria-invalid="true"`
- [ ] Error messages linked with `aria-describedby`

### 4.1 Compatible
- [ ] All elements have proper ARIA attributes
- [ ] Buttons are `<button>` elements or have `role="button"`
- [ ] Links are `<a>` elements with clear link text
- [ ] Language declared in `<html lang="en">`

---

## Testing Tools & Resources

### Browser Tools
- **Chrome DevTools**: Lighthouse accessibility audit
- **Firefox**: Accessibility Inspector
- **Edge**: Edge DevTools accessibility checker

### Online Tools
- **WAVE**: https://wave.webaim.org/
- **Axe DevTools**: https://www.deque.com/axe/
- **WCAG Contrast Checker**: https://webaim.org/resources/contrastchecker/

### Assistive Technologies to Test With
- **NVDA** (Free screen reader for Windows)
- **JAWS** (Commercial screen reader)
- **VoiceOver** (Built-in to macOS)
- **Keyboard only navigation** (No mouse)

### Documentation
- **WCAG 2.1**: https://www.w3.org/WAI/WCAG21/quickref/
- **WebAIM**: https://webaim.org/
- **MDN Accessibility**: https://developer.mozilla.org/en-US/docs/Web/Accessibility

---

## Integration with CI/CD

### Add to TestNG Configuration

Edit `src/test/resources/testng.xml` to include accessibility tests:

```xml
<test name="Accessibility Tests">
    <classes>
        <class name="com.qabrains.tests.accessibility.AccessibilityTests"/>
    </classes>
</test>
```

### Run as Part of Test Suite

```bash
# Run all tests including accessibility
mvn clean test

# Generate Allure report
.\allure\run-suite-with-allure.ps1
```

---

## Code Comments - Understanding the Implementation

All accessibility test methods include detailed comments:

```java
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
    // Implementation
}
```

### Comment Legend:
- **♿**: Indicates accessibility testing code
- **WCAG Criteria**: Which WCAG standard is being tested
- **IMPORTANCE**: Why this test matters for users with disabilities
- **SMOKE**: Critical tests that run first
- **FUNCTIONAL**: Detailed feature tests

---

## Troubleshooting

### Test Fails: "Page must contain at least one heading element"

**Problem**: Page doesn't have any `<h1>`, `<h2>`, etc. elements

**Fix**: Add proper heading elements to your page:
```html
<h1>Welcome to Our Store</h1>
<h2>Featured Products</h2>
```

### Test Fails: "Form input must have an associated label"

**Problem**: Input field doesn't have a label

**Fix**: Add label element or aria-label:
```html
<label for="search">Search Products:</label>
<input id="search" type="text">
```

### Test Fails: "Focusable elements must not have outline:none"

**Problem**: CSS removes focus indicators

**Fix**: Remove `outline:none` from your CSS:
```css
/* ✗ Remove this */
button:focus {
    outline: none;
}

/* ✓ Keep focus visible */
button:focus {
    outline: 2px solid blue;
}
```

---

## Next Steps

1. ✅ **Run the accessibility tests**: `mvn clean test`
2. ✅ **Review test results** in Allure report
3. ✅ **Fix any accessibility issues** found
4. ✅ **Add accessibility checks** to other test modules
5. ✅ **Make accessibility part of** your Definition of Done
6. ✅ **Train team** on WCAG guidelines

---

## Support & Questions

For questions about accessibility testing:
1. Review WCAG 2.1 guidelines: https://www.w3.org/WAI/WCAG21/quickref/
2. Check WebAIM resources: https://webaim.org/
3. Consult the AccessibilityUtil.java source code for method documentation
4. Review test methods in AccessibilityTests.java for usage examples

---

## Success Metrics

After implementing accessibility testing:

- ✅ 100% keyboard navigable application
- ✅ All form inputs properly labeled
- ✅ Text contrast meets WCAG AA (4.5:1)
- ✅ All images have descriptive alt text
- ✅ Semantic HTML structure in place
- ✅ Focus indicators visible on interactive elements
- ✅ Screen reader compatible (ARIA attributes)
- ✅ WCAG 2.1 Level AA compliant

---

**Remember**: Accessibility is not a feature, it's a fundamental right. Make sure your application is usable by everyone! ♿✨

