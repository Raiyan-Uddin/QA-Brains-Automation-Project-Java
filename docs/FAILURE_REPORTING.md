// FAILURE REPORTING DOCUMENTATION
// Enhanced failure reporting for QA Brains E-Commerce Test Automation

## Overview

The failure reporting system automatically captures essential diagnostics when test failures occur, enabling quick root-cause analysis. This includes:

- ✅ Screenshots (full-page capture)
- ✅ Page source (HTML content)
- ✅ Current URL
- ✅ Browser console logs
- ✅ Browser information
- ✅ Structured failure log
- ✅ HTML failure report

## Architecture

### Core Components

1. **FailureReporter.java**
   - Central utility for capturing and storing failure diagnostics
   - Captures: screenshots, page source, URL, console logs, browser info
   - Stores data in organized directory structure: `docs/Test Reports/failures/`
   - Creates structured log file: `failure-diagnostics.log`

2. **FailureReportGenerator.java**
   - Generates comprehensive HTML failure report
   - Parses failure diagnostics log
   - Creates navigable, styled HTML report
   - Published to: `docs/Test Reports/failures/failure-report.html`

3. **BaseTest.java (Enhanced)**
   - Added `captureFailureDiagnostics()` method for comprehensive diagnostics
   - Added `getConsoleLogs()` method to retrieve captured console logs
   - Console log capture on page creation (onConsoleMessage listener)
   - Stores last failure diagnostics for programmatic access

4. **TestListener.java (Enhanced)**
   - Calls FailureReporter on test failure
   - Displays diagnostics in console output
   - Generates HTML report at suite completion
   - Shows file paths to diagnostic artifacts

## Directory Structure

```
docs/Test Reports/failures/
├── failure-diagnostics.log          # Centralized log of all failures
├── failure-report.html              # Comprehensive HTML report
├── screenshots/                     # Full-page screenshots
│   ├── LoginTests-testMethod_timestamp.png
│   └── ...
└── page-source/                     # HTML page source files
    ├── LoginTests-testMethod_timestamp.html
    └── ...
```

## Usage

### Automatic Failure Capture (Default)

```java
public class LoginTests extends BaseTest {
    @Test
    public void testLogin() {
        // Test code
        // On failure: automatically captures all diagnostics
    }
}
```

**What happens:**
1. Test fails
2. TestListener detects failure
3. FailureReporter captures:
   - Screenshot at `docs/Test Reports/failures/screenshots/`
   - Page source at `docs/Test Reports/failures/page-source/`
   - URL and browser info
   - Console logs (captured from test setup)
4. Failure logged to `failure-diagnostics.log`
5. On suite completion, HTML report generated

### Accessing Failure Diagnostics in Tests

```java
@Test
public void testWithDiagnostics() {
    try {
        // Test code
    } catch (AssertionError e) {
        // Manually capture specific diagnostics if needed
        FailureReporter.FailureDiagnostics diag = captureFailureDiagnostics(
            "testMethod",
            e.getMessage()
        );
        
        // Access captured data
        String url = diag.getUrl();
        String screenshot = diag.getScreenshotPath();
        List<String> logs = diag.getConsoleLogs();
        
        throw e;
    }
}
```

### Accessing Console Logs (During Test Execution)

```java
@Test
public void testWithConsoleLogs() {
    page.evaluate("() => console.log('Debug info')");
    
    // Retrieve logs captured during test
    List<String> logs = getConsoleLogs();
    for (String log : logs) {
        System.out.println(log);
    }
}
```

## Console Output Example

When a test fails, you'll see output like:

```
[FAIL] TEST: loginFailure
  Reason: Expected to remain on login page...

  📍 FAILURE DIAGNOSTICS:
     URL: https://practice.qabrains.com/ecommerce/login
     Screenshot: D:\...\LoginTests-loginFailure_2026-06-02_10-30-45-123.png
     Page Source: D:\...\LoginTests-loginFailure_2026-06-02_10-30-45-123.html
     Console Logs (2):
       - [log] Form validation error
       - [error] Network request failed

  📂 Diagnostic files stored in: D:\...\docs\Test Reports\failures
```

## HTML Failure Report Features

The generated `failure-report.html` includes:

- **Summary Statistics**
  - Total number of failures
  - Number of test classes affected

- **Detailed Failure Cards** (for each failure)
  - Test class and method name
  - Failure reason with full stack trace
  - Current URL (clickable link)
  - Links to screenshot and page source files
  - Browser information (user agent, viewport)
  - Captured console logs

- **Styling**
  - Clean, professional design
  - Color-coded sections
  - Responsive layout
  - File links for direct access

## File Size Considerations

- **Screenshots**: ~100-500 KB per screenshot (PNG format)
- **Page Source**: ~50-200 KB per file (HTML format)
- **Log File**: ~1-5 KB per failure entry
- **Total per test run**: ~5-20 MB (depending on number and complexity of failures)

## Root-Cause Analysis Workflow

1. **Run Tests**: Execute test suite
   ```bash
   mvn clean test
   ```

2. **Review Console Output**: Quickly see failure reasons and diagnostic locations

3. **Open HTML Report**: Browse `docs/Test Reports/failures/failure-report.html`

4. **Investigate Failures**:
   - View screenshots to see page state at failure
   - Check HTML source to inspect DOM structure
   - Review console logs for JavaScript errors
   - Click URLs to understand application state

5. **Root Cause Analysis**:
   - Compare screenshots with expected UI
   - Analyze HTML structure for missing/incorrect elements
   - Check console logs for errors
   - Verify URL transitions

## Best Practices

1. **Check Failure Reports First**
   - Always start with the HTML report for visual context

2. **Use URL as Context**
   - Current URL shows exactly where test failed

3. **Correlate Data Sources**
   - Cross-reference screenshot, page source, and console logs
   - Helps pinpoint UI issues, DOM problems, or JS errors

4. **File Organization**
   - Diagnostic files are automatically date-timestamped
   - Easy to correlate with test execution times

5. **Clean Up Old Reports** (Optional)
   - Run cleanup script periodically to remove old diagnostics
   - Prevents directory bloat

## Troubleshooting

### Missing Screenshots
- **Cause**: Page was null or closed at failure time
- **Solution**: Ensure page is properly created in BeforeMethod

### Missing Console Logs
- **Cause**: Console events not captured during test
- **Solution**: Logs are captured from BeforeMethod onwards; check test timeline

### HTML Report Not Generated
- **Cause**: No failures in test run, or error writing file
- **Solution**: Check file permissions in `docs/Test Reports/failures/` directory

### Screenshots Too Large
- **Cause**: Full-page screenshots include large scrollable content
- **Solution**: This is expected; increase disk space if needed

## Integration with CI/CD

Add this to your CI/CD pipeline to preserve failure reports:

```bash
# Collect failure artifacts
artifacts:
  paths:
    - "docs/Test Reports/failures/"
  expire_in: 30 days
```

## Performance Impact

- Minimal CPU overhead (<1% per test)
- I/O operations: ~100-300ms per failure capture
- Storage: ~100-500 KB per failed test
- No impact on passing tests

## Future Enhancements

Potential improvements:
- [ ] Video recording of failed tests (requires FFmpeg)
- [ ] Network request logging
- [ ] Performance metrics capture
- [ ] Database state snapshot
- [ ] Email report delivery
- [ ] Dashboard integration

