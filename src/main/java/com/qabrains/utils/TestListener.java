// src/main/java/com/qabrains/utils/TestListener.java

package com.qabrains.utils;

import com.qabrains.base.BaseTest;
import com.qabrains.config.AppConfig;
import com.qabrains.config.ExecutionContext;
import com.qabrains.config.ExecutionMode;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Global TestNG listener.
 *
 * Beginner note:
 * - ITestListener methods run per suite/test events.
 * - IExecutionListener methods run once per full execution.
 * - We build one final latest HTML report at the end of execution.
 */
public class TestListener implements ITestListener, IExecutionListener {

    // Shared list to collect module summaries during one full run.
    private static final List<FullSuiteHtmlReportGenerator.ModuleSnapshot> MODULE_SUMMARY = new ArrayList<>();
    private static final List<FullSuiteHtmlReportGenerator.TestCaseSnapshot> TEST_CASE_SUMMARY = new ArrayList<>();
    private static final Object MODULE_SUMMARY_LOCK = new Object();

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("[START] TEST: " + result.getMethod().getMethodName());
        System.out.println("  Description: " + result.getMethod().getDescription());
        System.out.println("=".repeat(70));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("[PASS] TEST: " + result.getMethod().getMethodName());

        long duration = result.getEndMillis() - result.getStartMillis();
        System.out.println("  Duration: " + duration + " ms");
        System.out.println("-".repeat(70));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("[FAIL] TEST: " + result.getMethod().getMethodName());
        System.out.println("  Execution Mode: " + ExecutionContext.getInstance().getExecutionMode().getDisplayName());

        Throwable throwable = result.getThrowable();
        String failureReason = throwable == null ? "Unknown failure" : throwable.getMessage();
        System.out.println("  Reason: " + failureReason);

        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTest baseTest) {
            FailureReporter.FailureDiagnostics diagnostics = baseTest.captureFailureDiagnostics(
                    result.getMethod().getMethodName(),
                    failureReason
            );

            if (diagnostics.isPageAvailable()) {
                System.out.println("\n  📍 FAILURE DIAGNOSTICS:");
                System.out.println("     URL: " + diagnostics.getUrl());

                if (diagnostics.getScreenshotPath() != null && !diagnostics.getScreenshotPath().isEmpty()) {
                    System.out.println("     Screenshot: " + diagnostics.getScreenshotPath());
                }

                if (diagnostics.getPageSourcePath() != null && !diagnostics.getPageSourcePath().isEmpty()) {
                    System.out.println("     Page Source: " + diagnostics.getPageSourcePath());
                }

                java.util.List<String> consoleLogs = baseTest.getConsoleLogs();
                if (!consoleLogs.isEmpty()) {
                    System.out.println("     Console Logs (" + consoleLogs.size() + "):");
                    for (String log : consoleLogs) {
                        System.out.println("       - " + log);
                    }
                }

                if (diagnostics.getBrowserInfo() != null && !diagnostics.getBrowserInfo().isEmpty()) {
                    System.out.println("     Browser Info: " + diagnostics.getBrowserInfo());
                }

                System.out.println("\n  📂 Diagnostic files stored in: " + FailureReporter.getFailuresDirectoryPath());
            } else {
                System.out.println("  Note: Page not available at time of failure (page not created or already closed)");
            }
        }

        System.out.println("-".repeat(70));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("[SKIP] TEST: " + result.getMethod().getMethodName());
        System.out.println("-".repeat(70));
    }

    @Override
    public void onStart(ITestContext context) {
        ExecutionMode executionMode = ExecutionMode.fromHeadlessFlag(AppConfig.HEADLESS);
        ExecutionContext.getInstance().initialize(executionMode);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("[SUITE START] " + context.getName());
        System.out.println("=".repeat(70));
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("[SUITE END] " + context.getName());
        System.out.println("  Passed: " + context.getPassedTests().size()
                + "  Failed: " + context.getFailedTests().size()
                + "  Skipped: " + context.getSkippedTests().size());
        System.out.println("=".repeat(70));

        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        int total = passed + failed + skipped;

        double durationSeconds = calculateDurationSeconds(context);

        synchronized (MODULE_SUMMARY_LOCK) {
            MODULE_SUMMARY.add(new FullSuiteHtmlReportGenerator.ModuleSnapshot(
                    context.getName(),
                    total,
                    passed,
                    failed,
                    skipped,
                    durationSeconds
            ));

            collectTestCaseSnapshots(context, "PASSED", context.getPassedTests().getAllResults());
            collectTestCaseSnapshots(context, "FAILED", context.getFailedTests().getAllResults());
            collectTestCaseSnapshots(context, "SKIPPED", context.getSkippedTests().getAllResults());
        }

        // Keep detailed failure diagnostics report generation for failed suites.
        if (context.getFailedTests().size() > 0) {
            FailureReportGenerator.generateFailureReport();
        }
    }

    @Override
    public void onExecutionStart() {
        synchronized (MODULE_SUMMARY_LOCK) {
            MODULE_SUMMARY.clear();
            TEST_CASE_SUMMARY.clear();
        }
    }

    @Override
    public void onExecutionFinish() {
        List<FullSuiteHtmlReportGenerator.ModuleSnapshot> snapshotCopy;
        List<FullSuiteHtmlReportGenerator.TestCaseSnapshot> testCaseSnapshotCopy;
        synchronized (MODULE_SUMMARY_LOCK) {
            snapshotCopy = new ArrayList<>(MODULE_SUMMARY);
            testCaseSnapshotCopy = new ArrayList<>(TEST_CASE_SUMMARY);
        }

        FullSuiteHtmlReportGenerator.generateLatestReportFromModules(
                snapshotCopy,
                testCaseSnapshotCopy,
                "Generated from TestNG execution summary at end of run"
        );
    }

    private void collectTestCaseSnapshots(ITestContext context, String status, java.util.Set<ITestResult> results) {
        for (ITestResult result : results) {
            TEST_CASE_SUMMARY.add(new FullSuiteHtmlReportGenerator.TestCaseSnapshot(
                    extractModuleName(result, context),
                    resolveTestCaseName(result),
                    result.getMethod().getMethodName(),
                    status,
                    Math.max(0, result.getEndMillis() - result.getStartMillis()) / 1000.0,
                    resolveMessage(result)
            ));
        }
    }

    private String extractModuleName(ITestResult result, ITestContext context) {
        if (result.getTestClass() != null && result.getTestClass().getRealClass() != null) {
            String className = result.getTestClass().getRealClass().getSimpleName();
            return className.endsWith("Tests")
                    ? className.substring(0, className.length() - "Tests".length())
                    : className;
        }
        return context.getName();
    }

    private String resolveTestCaseName(ITestResult result) {
        String description = result.getMethod().getDescription();
        if (description != null && !description.isBlank()) {
            return description.trim();
        }
        return result.getMethod().getMethodName();
    }

    private String resolveMessage(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable == null || throwable.getMessage() == null || throwable.getMessage().isBlank()) {
            return "";
        }
        String message = throwable.getMessage().replace("\n", " ").replace("\r", " ").trim();
        return message.length() > 220 ? message.substring(0, 220) + "..." : message;
    }

    private double calculateDurationSeconds(ITestContext context) {
        long totalMillis = 0L;

        for (ITestResult result : context.getPassedTests().getAllResults()) {
            totalMillis += Math.max(0, result.getEndMillis() - result.getStartMillis());
        }
        for (ITestResult result : context.getFailedTests().getAllResults()) {
            totalMillis += Math.max(0, result.getEndMillis() - result.getStartMillis());
        }
        for (ITestResult result : context.getSkippedTests().getAllResults()) {
            totalMillis += Math.max(0, result.getEndMillis() - result.getStartMillis());
        }

        return totalMillis / 1000.0;
    }
}