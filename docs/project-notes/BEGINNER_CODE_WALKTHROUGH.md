# Beginner Code Walkthrough

This guide explains the most important framework files in simple language.

> Goal: help you understand "what each line block is doing" without changing runtime behavior.

---

## 1) `src/main/java/com/qabrains/base/BaseTest.java`

### Lines 1-18: Imports
- Bring Playwright classes, utility classes, TestNG annotations, and Java helpers.

### Lines 20-31: Class-level comment
- Explains the lifecycle order (`BeforeSuite` -> `BeforeClass` -> `BeforeMethod` -> test -> teardown).

### Lines 33-40: Shared objects
- `playwright`, `browser`, `context`, `page` are shared so child test classes can use them.

### Lines 42-73: `captureFailureScreenshot(...)`
- Early return if page is not available.
- Creates screenshot folder if missing.
- Builds safe file name with class + method + timestamp.
- Takes full-page screenshot and returns path.

### Lines 75-90: `captureFailureDiagnostics(...)`
- Calls `FailureReporter` to collect full failure data (URL, screenshot, page source, logs, browser info).
- Saves last diagnostics object in memory.

### Lines 92-104: Getter helpers
- `getLastFailureDiagnostics()` returns last captured failure bundle.
- `getConsoleLogs()` returns copy of logs (safe copy, avoids direct mutation).

### Lines 106-109: `@BeforeSuite`
- Prints framework startup message once per suite.

### Lines 111-119: `@BeforeClass`
- Creates Playwright and launches browser once per test class.

### Lines 121-136: `@BeforeMethod`
- Creates fresh context/page for each test.
- Clears previous logs.
- Subscribes to browser console and stores messages.

### Lines 138-145: `@AfterMethod`
- Closes context to isolate test data between methods.

### Lines 147-159: `@AfterClass`
- Closes browser and Playwright once class finishes.

### Lines 161-164: `@AfterSuite`
- Prints suite shutdown message.

---

## 2) `src/main/java/com/qabrains/utils/BrowserFactory.java`

### Lines 1-7: Imports
- Pulls Playwright classes + config.

### Lines 9-16: Class comment
- This class centralizes browser object creation.

### Lines 18-20: `createPlaywright()`
- Returns a Playwright runtime instance.

### Lines 22-40: `launchBrowser(...)`
- Reads execution mode from `ExecutionContext` (single source of truth).
- Builds launch options (`headless`, `slowMo`).
- Launches Chromium and returns `Browser`.

### Lines 42-51: `createContext(...)`
- Creates new browser context with configured viewport.

### Lines 53-64: `createPage(...)`
- Opens a new page and applies default timeout.

---

## 3) `src/main/java/com/qabrains/utils/TestListener.java`

### Lines 1-11: Imports
- Brings TestNG listener types + framework classes.

### Lines 13-20: Class comment
- This class listens to test lifecycle events and logs/report failures.

### Lines 24-29: `onTestStart(...)`
- Prints test start banner and description.

### Lines 31-39: `onTestSuccess(...)`
- Prints pass banner and test duration.

### Lines 41-89: `onTestFailure(...)`
- Prints fail banner + reason.
- If test extends `BaseTest`, it captures diagnostics.
- Prints URL, screenshot path, page source path, console logs, browser info.

### Lines 91-96: `onTestSkipped(...)`
- Prints skip banner.

### Lines 98-107: `onStart(...)`
- Initializes `ExecutionContext` once using `AppConfig.HEADLESS`.
- Prints suite start banner.

### Lines 109-123: `onFinish(...)`
- Prints suite end summary (passed/failed/skipped).
- Generates HTML failure report only when failures exist.

---

## 4) `src/main/java/com/qabrains/config/ExecutionContext.java`

### Lines 1-5: Setup
- Package + `LocalDateTime` import.

### Lines 7-22: Class comment
- Describes the singleton role: keep one execution mode for full run.

### Lines 25-29: Core fields
- `INSTANCE`: singleton object.
- `executionMode`, `executionStartTime`, `initialized` track state.

### Lines 31-44: Constructor + getter
- Private constructor prevents outside creation.
- `getInstance()` returns shared singleton.

### Lines 46-68: `initialize(...)`
- Synchronized (thread-safe).
- Ignores repeated initialization.
- Saves mode/start time and prints execution mode details.

### Lines 70-83: `getExecutionMode()`
- Returns selected mode.
- If not initialized, auto-initializes from `AppConfig.HEADLESS`.

### Lines 85-92: `isHeadless()`
- Convenience boolean helper.

### Lines 94-110: State getters
- Returns start time + initialized status.

### Lines 112-121: `reset()`
- Clears state (useful for testing only).

### Lines 123-129: `toString()`
- String representation for debugging.

---

## Quick Learning Path

1. Read `BaseTest.java` first (core lifecycle).
2. Read `BrowserFactory.java` second (object creation).
3. Read `TestListener.java` third (event reporting).
4. Read `ExecutionContext.java` last (execution mode consistency).

---

## Important Promise

All edits were comment/readability-focused and did not intentionally change test execution logic.

