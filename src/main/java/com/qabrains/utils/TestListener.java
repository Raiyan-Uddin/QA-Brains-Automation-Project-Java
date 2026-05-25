// src/main/java/com/qabrains/utils/TestListener.java

package com.qabrains.utils;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;

/**
 * TestNG Listener for global test event handling.
 * Provides formatted console output for test lifecycle events.
 * Automatically applied via testng.xml or @Listeners annotation.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("▶ STARTING TEST: " + result.getMethod().getMethodName());
        System.out.println("  Description: " + result.getMethod().getDescription());
        System.out.println("═".repeat(70));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("✅ TEST PASSED: " + result.getMethod().getMethodName());
        long duration = result.getEndMillis() - result.getStartMillis();
        System.out.println("  Duration: " + duration + " ms");
        System.out.println("─".repeat(70));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("❌ TEST FAILED: " + result.getMethod().getMethodName());
        System.out.println("  Reason: " + result.getThrowable().getMessage());
        System.out.println("─".repeat(70));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("⏭ TEST SKIPPED: " + result.getMethod().getMethodName());
        System.out.println("─".repeat(70));
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n" + "╔" + "═".repeat(68) + "╗");
        System.out.println("║  🚀 TEST SUITE STARTED: " + context.getName()
                + " ".repeat(Math.max(0, 43 - context.getName().length())) + "║");
        System.out.println("╚" + "═".repeat(68) + "╝");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n" + "╔" + "═".repeat(68) + "╗");
        System.out.println("║  🏁 TEST SUITE FINISHED: " + context.getName()
                + " ".repeat(Math.max(0, 42 - context.getName().length())) + "║");
        System.out.println("║  ✅ Passed: " + context.getPassedTests().size()
                + "  ❌ Failed: " + context.getFailedTests().size()
                + "  ⏭ Skipped: " + context.getSkippedTests().size()
                + " ".repeat(30) + "║");
        System.out.println("╚" + "═".repeat(68) + "╝");
    }
}