# Failure Reporting Quick Reference Guide

## 🎯 What's New

Enhanced failure reporting automatically captures critical diagnostics when tests fail:
- 📸 **Screenshots** - Full page capture at failure moment
- 📄 **Page Source** - Complete HTML content
- 🔗 **URLs** - Current page URL for context
- 📜 **Console Logs** - JavaScript errors and debug messages
- 🌐 **Browser Info** - User agent, viewport, page title
- 📊 **Formatted Reports** - HTML dashboard for quick analysis

## 📂 Where to Find Failure Data

When tests run, failure artifacts are stored in:

```
docs/Test Reports/failures/
├── failure-report.html          ← Open this in browser (easiest!)
├── failure-diagnostics.log      ← Raw diagnostic data
├── screenshots/                 ← PNG images
└── page-source/                 ← HTML files
```

## 🚀 Quick Start (Post-Test Execution)

### 1. View All Failures at Once
```bash
# Open this file in your browser:
docs/Test Reports/failures/failure-report.html
```

### 2. Check Individual Artifacts
- Click screenshot links to view failure state
- Click page source links to inspect HTML
- Review URLs to understand navigation context

### 3. Analyze Console Logs
- Check for JavaScript errors
- Look for network errors
- Search for debug messages you added

## 🔍 Root-Cause Analysis Examples

### Example 1: Login Button Not Working
1. Open `failure-report.html`
2. Find the failed login test
3. **View Screenshot** → See if button is visible
4. **View Page Source** → Check button HTML
5. **Check Console Logs** → Look for JS errors

### Example 2: Page Didn't Navigate
1. Open failure report
2. Check **URL** field
3. Expected: home page URL → Got: login page URL
4. Helps identify navigation issues

### Example 3: Element Not Found
1. View **Page Source** of failed test
2. Search for the element in HTML
3. Check CSS selectors
4. Compare with expected DOM structure

## 📋 File Naming Convention

All diagnostic files are timestamped for easy correlation:

```
LoginTests-testMethod_2026-06-02_10-30-45-123.png
└─ Class     └─ Method      └─ Timestamp (YYYY-MM-DD_HH-mm-ss-ms)
```

**Multiple runs?** All failures are accumulated with different timestamps.

## 💡 Console Output Example

After each test failure, you'll see:

```
[FAIL] TEST: LGN_008_successfulLoginRedirectsToHome
  Reason: Expected URL not found

  📍 FAILURE DIAGNOSTICS:
     URL: https://practice.qabrains.com/ecommerce/login
     Screenshot: C:\...\screenshots\LoginTests-testMethod_2026-06-02_10-30-45-123.png
     Page Source: C:\...\page-source\LoginTests-testMethod_2026-06-02_10-30-45-123.html
     Console Logs (1):
       - [error] Login endpoint returned 500

  📂 Diagnostic files stored in: C:\...\docs\Test Reports\failures
```

## 🎨 HTML Failure Report Features

### Dashboard Overview
- **Total Failures**: Quick count
- **Affected Test Classes**: Which tests failed

### Per-Failure Card
- ❌ **Failure Reason**: Why it failed
- 🔗 **URL**: Where it failed
- 📸 **Screenshot Link**: Visual state
- 📄 **Page Source Link**: HTML structure
- 🌐 **Browser Info**: Environment details

## ⚙️ How It Works (Technical)

```
Test Fails
    ↓
TestListener detects failure
    ↓
FailureReporter captures:
  • Screenshot (SetPath + SetFullPage)
  • Page Source (page.content())
  • URL (page.url())
  • Console Logs (from onConsoleMessage listeners)
  • Browser Info (navigator.userAgent, viewport, etc.)
    ↓
Data written to files:
  • Screenshots → PNG files
  • Page Source → HTML files
  • Metadata → failure-diagnostics.log
    ↓
Suite completion
    ↓
FailureReportGenerator creates HTML report
    ↓
Open report.html for analysis
```

## 🔧 Automatic vs Manual

### Automatic (Default)
```java
@Test
public void testLogin() {
    loginPage.performLogin("user", "pass");
    // If fails → diagnostics captured automatically
}
```

### Manual (Optional)
```java
@Test
public void customTest() {
    try {
        // test code
    } catch (Exception e) {
        captureFailureDiagnostics("testName", e.getMessage());
        throw e;
    }
}
```

## 🗂️ Storage Estimates

| Item | Size | Count |
|------|------|-------|
| Screenshot | 100-500 KB | 1 per failure |
| Page Source | 50-200 KB | 1 per failure |
| Log Entry | 1-5 KB | 1 per failure |
| HTML Report | 50-200 KB | 1 total |

**Example**: 10 failures ≈ 2-10 MB total

## 🔗 File Access in Windows

### From File Explorer
```
Right-click screenshot → Open with → Paint/Image Viewer
Right-click page source → Open with → VS Code/Notepad
Double-click failure-report.html → Opens in default browser
```

### From PowerShell
```powershell
# Open failure report
Start-Process "docs/Test Reports/failures/failure-report.html"

# List recent failures
Get-ChildItem "docs/Test Reports/failures/screenshots" -Newest 5

# Open screenshot directly
Start-Process "docs/Test Reports/failures/screenshots/LoginTests-test_2026-06-02_*.png"
```

## 📌 Troubleshooting

| Problem | Cause | Solution |
|---------|-------|----------|
| No HTML report | No failures in run | Script only generates on failures |
| Missing screenshots | Page closed at failure | Check test setup/teardown |
| Empty console logs | No events captured | Check page wasn't null |
| Links not working | Path format | Use backslash (\\) in paths |

## ✅ Best Practices

1. **Check HTML report first** - Best overview of all failures
2. **Cross-reference data** - Compare screenshot + source + console
3. **Use timestamps** - Correlate with test execution time
4. **Clean old reports** - Monthly cleanup recommended
5. **Archive failures** - Keep important ones for regression analysis

## 🎓 Learning Resources

- Detailed docs: `docs/FAILURE_REPORTING.md`
- Playwright screenshots: `src/main/java/com/qabrains/utils/FailureReporter.java`
- Report generator: `src/main/java/com/qabrains/utils/FailureReportGenerator.java`

## 📞 Questions?

Check these files for implementation details:
- **TestListener.java** - Where failure diagnostics are captured
- **BaseTest.java** - Console log setup and capture methods
- **FailureReporter.java** - Core diagnostic capture logic
- **FailureReportGenerator.java** - HTML report generation

---

**Summary**: Run tests → Check failures 📸 → Open HTML report 📊 → Analyze quickly! 🎯

