// EXECUTION MODE ENFORCEMENT DOCUMENTATION
// ============================================
// 
// OBJECTIVE: Enforce Consistent Execution Mode for Reporting
// 
// PROBLEM SOLVED:
// Previously, execution mode (headless vs headed) was scattered throughout the codebase:
// - AppConfig.HEADLESS was used directly in BrowserFactory
// - No centralized tracking of execution mode
// - Reporting could be inconsistent if AppConfig was changed mid-execution
// - No way to audit which execution mode was used for a particular test run
//
// SOLUTION IMPLEMENTED:
// Created a three-tier execution mode management system:
//
// 1. ExecutionMode (Enum)
//    - Defines possible execution modes: HEADLESS, HEADED
//    - Provides display names and descriptions
//    - Utility methods: fromHeadlessFlag(), isHeadless()
//
// 2. ExecutionContext (Singleton)
//    - Maintains ONE authoritative execution mode per test session
//    - Initialized once at suite start, never changes during execution
//    - Synchronized to prevent race conditions
//    - Provides getInstance().getExecutionMode() access everywhere
//
// 3. Integration Points:
//    - TestListener.onStart() → Initializes ExecutionContext (runs ONCE per suite)
//    - BrowserFactory.launchBrowser() → Uses ExecutionContext, not AppConfig
//    - FailureReporter → Captures execution mode in every failure diagnostic
//    - FailureReportGenerator → Includes execution mode in HTML reports
//
// HOW IT WORKS:
// Step 1: Test suite starts → onStart(ITestContext) is called
// Step 2: TestListener initializes ExecutionContext with mode from AppConfig
// Step 3: ExecutionContext becomes the "source of truth"
// Step 4: All components (browser, reporter, logs) use ExecutionContext
// Step 5: Every failure inherits the same execution mode
// Step 6: Reports consistently show the execution mode used
//
// BENEFITS:
// ✓ Single Source of Truth: One ExecutionContext per session
// ✓ Consistency: All failures report the same execution mode
// ✓ Traceability: Can audit which mode was used for any failed test
// ✓ Auditability: Execution mode appears in logs, reports, and diagnostics
// ✓ Thread-Safety: Synchronized singleton prevents race conditions
// ✓ Non-Intrusive: Doesn't require changes to existing test code
// ✓ Flexibility: Can be extended to support other modes in future
//
// OUTPUT EXAMPLES:
//
// Console Output:
// [EXECUTION MODE] Headless
//   Description: Browser runs without UI - faster execution, suitable for CI/CD
//   Initialized at: 2026-06-02T10:30:45
//
// [FAIL] TEST: testLogin
//   Execution Mode: Headless
//   Reason: Element not found exception
//
// HTML Report Header:
// 🔴 Test Failure Report
// Generated: 2026-06-02 10:30:50.123
// Execution Mode: Headless
//
// Failure Diagnostics Log:
// ================================================================================
// FAILURE REPORT: 2026-06-02T10:30:50.123
// Execution Mode: Headless
// Test Class: LoginTests
// Test Method: testLogin
// Failure Reason: Element not found
// ================================================================================
//
// NEW FILES CREATED:
// 1. ExecutionMode.java - Enum for headless/headed modes
// 2. ExecutionContext.java - Singleton for execution mode management
//
// FILES MODIFIED:
// 1. FailureReporter.java - Added execution mode to diagnostics
// 2. FailureReportGenerator.java - Added execution mode to HTML report
// 3. TestListener.java - Initialize ExecutionContext at suite start
// 4. BrowserFactory.java - Use ExecutionContext instead of AppConfig.HEADLESS
//
// USAGE EXAMPLE IN TEST CODE (No changes needed, but if needed):
// 
//   import com.qabrains.config.ExecutionContext;
//   
//   public void someTest() {
//       // Access execution mode anywhere
//       if (ExecutionContext.getInstance().isHeadless()) {
//           System.out.println("Running in headless mode");
//       }
//   }
//
// TESTING THE IMPLEMENTATION:
// 1. Run tests in headless mode: mvn clean test
// 2. Check console for execution mode initialization message
// 3. Trigger a test failure and check:
//    - Console log includes execution mode
//    - failure-diagnostics.log includes execution mode
//    - failure-report.html includes execution mode in header
// 4. Run tests in headed mode:
//    - Update AppConfig.HEADLESS = false
//    - mvn clean test
//    - All reports should show "Headed" mode
//
// MAINTENANCE NOTES:
// - ExecutionContext should NOT be reset during test execution (only for testing)
// - Changing AppConfig.HEADLESS after initialization has no effect
// - This is intentional to prevent inconsistent reporting
// - To change execution mode, update AppConfig.HEADLESS BEFORE running tests

