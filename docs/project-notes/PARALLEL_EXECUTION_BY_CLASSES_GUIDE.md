# Parallel Execution by Classes (Beginner Guide)

This project now runs tests in parallel **by class**.

## What changed

### File: `src/test/resources/testng.xml`

- `parallel="classes"`
  - Means: TestNG can run different test classes at the same time.
- `thread-count="3"`
  - Means: at most 3 classes run in parallel.

## Why this is safe in your framework

### 1) Per-class browser
In `BaseTest`:
- `@BeforeClass` launches browser for that class.
- Each class owns its own browser object.

### 2) Per-method context
In `BaseTest`:
- `@BeforeMethod` creates a fresh `BrowserContext` and `Page`.
- `@AfterMethod` closes that context.
- This isolates cookies/session/storage between tests.

### 3) Shared failure log is now thread-safe
In `FailureReporter`:
- We added `LOG_WRITE_LOCK` and synchronized append.
- Prevents two failing threads from writing mixed lines in the same log file.

## Beginner-friendly tuning

In `testng.xml`, you can tune speed vs stability:

- Stable and safer: `thread-count="2"`
- Balanced (current): `thread-count="3"`
- Aggressive (faster, more load): `thread-count="4"` or higher

Change only one step at a time and observe flakiness.

## How to run

```bash
mvn clean test
```

## How to confirm parallel execution

Watch console output:
- Different classes will print setup/start messages interleaved.
- Total runtime should usually reduce compared to serial class execution.

## If you see flaky behavior

1. Reduce thread count from `3` to `2`.
2. Keep each test independent (no shared static mutable state).
3. Keep API data/test accounts isolated when possible.
4. Re-run a failing class alone to confirm whether issue is data timing.

## Summary

- Yes, parallel by classes is implemented.
- It is beginner-readable in `testng.xml` comments.
- It keeps existing behavior and adds safer shared-log writing for parallel failures.

