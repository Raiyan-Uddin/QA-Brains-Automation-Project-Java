# Execution Mode Tracking - Quick Reference

## What Changed?
Execution mode is now **centrally managed** to ensure consistent reporting across all failure diagnostics, logs, and HTML reports.

## Key Components

### ExecutionMode (Enum)
```java
ExecutionMode.HEADLESS   // Browser without UI (default)
ExecutionMode.HEADED     // Browser with UI visible

// Useful methods:
ExecutionMode mode = ExecutionMode.fromHeadlessFlag(true);  // → HEADLESS
boolean isHeadless = ExecutionMode.HEADLESS.isHeadless();    // → true
String name = ExecutionMode.HEADLESS.getDisplayName();       // → "Headless"
```

### ExecutionContext (Singleton)
```java
// Get the current execution mode (anywhere in your code)
ExecutionMode currentMode = ExecutionContext.getInstance().getExecutionMode();

// Check if running headless
if (ExecutionContext.getInstance().isHeadless()) {
    // Headless-specific logic
}

// Get when the test session started
LocalDateTime startTime = ExecutionContext.getInstance().getExecutionStartTime();
```

## How It Works

| Step | Component | Action |
|------|-----------|--------|
| 1 | TestListener | Initializes ExecutionContext at suite start |
| 2 | ExecutionContext | Locks mode for entire session |
| 3 | BrowserFactory | Reads mode from ExecutionContext when launching browser |
| 4 | FailureReporter | Captures mode in every failure diagnostic |
| 5 | FailureReportGenerator | Includes mode in HTML report header |
| 6 | Console Output | Shows mode in all test lifecycle events |

## Where Execution Mode Appears

### ✅ Console Output
```
[EXECUTION MODE] Headless
  Description: Browser runs without UI - faster execution, suitable for CI/CD
  Initialized at: 2026-06-02T10:30:45

[FAIL] TEST: testLogin
  Execution Mode: Headless
  Reason: Element not found
```

### ✅ Failure Diagnostics Log
**File**: `docs/Test Reports/failures/failure-diagnostics.log`
```
FAILURE REPORT: 2026-06-02T10:30:50.123
Execution Mode: Headless
Test Class: LoginTests
Test Method: testLogin
```

### ✅ HTML Report
**File**: `docs/Test Reports/failures/failure-report.html`
```
🔴 Test Failure Report
Generated: 2026-06-02 10:30:50.123
Execution Mode: Headless
```

## Changing Execution Mode

**Step 1**: Edit `AppConfig.java`
```java
public static final boolean HEADLESS = false;  // Change to true/false
```

**Step 2**: Run tests
```bash
mvn clean test
```

**Step 3**: Execution mode automatically initializes
- All reports will reflect the new mode
- Mode stays consistent throughout the run

## Best Practices

✅ **DO**:
- Access execution mode via `ExecutionContext.getInstance()`
- Trust the execution mode that appears in failure reports
- Use execution mode to make test decisions when needed

❌ **DON'T**:
- Directly access `AppConfig.HEADLESS` in test code
- Try to change execution mode during test run
- Call `reset()` on ExecutionContext (only for testing)

## Common Use Cases

### Use headless for CI/CD
```java
// AppConfig.java
public static final boolean HEADLESS = true;  // Fast, suitable for pipelines
```

### Use headed for debugging
```java
// AppConfig.java
public static final boolean HEADLESS = false;  // Slow UI visible, good for debugging
```

### Conditional logic based on mode
```java
if (ExecutionContext.getInstance().isHeadless()) {
    // Use faster timeouts
    page.setDefaultTimeout(10000);
} else {
    // Use slower timeouts
    page.setDefaultTimeout(30000);
}
```

## FAQ

**Q: Does my test code need changes?**
A: No! The system works transparently.

**Q: Can I see the execution mode in reports?**
A: Yes! Check the HTML report header or failure-diagnostics.log file.

**Q: What if I forget to change AppConfig?**
A: Tests will run with the current AppConfig.HEADLESS value, and reports will accurately reflect it.

**Q: Can execution mode change during test run?**
A: No! It's locked at initialization to ensure consistency.

**Q: How do I verify the mode is correct?**
A: Check console output at suite start: `[EXECUTION MODE] Headless`

**Q: What's the benefit?**
A: All failures are tagged with the execution mode used, making it easy to audit and reproduce issues.

## Troubleshooting

| Problem | Check |
|---------|-------|
| Mode not in reports | Ensure TestListener is initialized |
| Mode shows "HEADLESS" in headed runs | Restart tests after changing AppConfig |
| "Already initialized" warning | Normal and harmless; context is locked |
| Compilation error with ExecutionMode | verify file in `com.qabrains.config` |

## Code References

- **ExecutionMode.java**: `src/main/java/com/qabrains/config/ExecutionMode.java`
- **ExecutionContext.java**: `src/main/java/com/qabrains/config/ExecutionContext.java`
- **Updated files**: FailureReporter, FailureReportGenerator, TestListener, BrowserFactory

## Next Steps

1. Run a test suite: `mvn clean test`
2. Check the console for execution mode initialization
3. Trigger a test failure and verify mode appears in:
   - Console output
   - `docs/Test Reports/failures/failure-diagnostics.log`
   - `docs/Test Reports/failures/failure-report.html`
4. Optionally change AppConfig.HEADLESS and re-run
5. Verify all reports accurately reflect the new mode

