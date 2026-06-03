// src/main/java/com/qabrains/base/BaseTest.java

package com.qabrains.base;

import com.microsoft.playwright.*;
import com.qabrains.utils.BrowserFactory;
import com.qabrains.utils.FailureReporter;
import com.qabrains.utils.TestListener;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * BaseTest is the parent class for all test classes.
 *
 * Beginner flow (high level):
 * 1) BeforeSuite  -> print framework start message
 * 2) BeforeClass  -> create Playwright and launch browser (once per class)
 * 3) BeforeMethod -> create fresh context/page (before every test)
 * 4) Test method  -> your actual test steps/assertions
 * 5) AfterMethod  -> close context (after every test)
 * 6) AfterClass   -> close browser and Playwright (once per class)
 * 7) AfterSuite   -> print framework end message
 */
@Listeners(TestListener.class)
public class BaseTest {

    // Core Playwright objects used by child test classes.
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    // Failure/debug data for current test execution.
    protected List<String> consoleLogs = new ArrayList<>();
    private FailureReporter.FailureDiagnostics lastFailureDiagnostics;

    /**
     * Captures a full-page screenshot.
     *
     * This helper is safe to call only when page exists and is still open.
     */
    public String captureFailureScreenshot(String testMethodName) {
        if (page == null || page.isClosed()) {
            return null;
        }

        try {
            // Ensure screenshot target folder exists.
            Path screenshotDir = Paths.get("docs", "Test Reports", "screenshots");
            Files.createDirectories(screenshotDir);

            // Build safe, timestamped file name.
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String className = this.getClass().getSimpleName().replaceAll("[^a-zA-Z0-9._-]", "_");
            String methodName = testMethodName == null ? "unknown_test" : testMethodName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String fileName = className + "-" + methodName + "-" + timestamp + ".png";

            // Take screenshot and return absolute path.
            Path outputPath = screenshotDir.resolve(fileName).toAbsolutePath();
            page.screenshot(new Page.ScreenshotOptions().setPath(outputPath).setFullPage(true));
            return outputPath.toString();
        } catch (PlaywrightException | IOException e) {
            System.out.println("[WARN] Could not capture failure screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Captures full diagnostics (URL, screenshot, page source, logs, browser info).
     */
    public FailureReporter.FailureDiagnostics captureFailureDiagnostics(String testMethodName, String failureReason) {
        String className = this.getClass().getSimpleName();

        FailureReporter.FailureDiagnostics diagnostics = FailureReporter.captureFailureDiagnostics(
                page,
                className,
                testMethodName,
                failureReason
        );

        // Keep last diagnostics in memory for optional later usage.
        this.lastFailureDiagnostics = diagnostics;
        return diagnostics;
    }

    /**
     * Returns last diagnostics captured by this test instance.
     */
    public FailureReporter.FailureDiagnostics getLastFailureDiagnostics() {
        return lastFailureDiagnostics;
    }

    /**
     * Returns a safe copy of collected console logs.
     */
    public List<String> getConsoleLogs() {
        return new ArrayList<>(consoleLogs);
    }

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        System.out.println("\n[SETUP] Initializing Playwright framework...");
    }

    @BeforeClass(alwaysRun = true)
    public void classSetup() {
        System.out.println("\n[BROWSER] Setting up browser for test class: " + this.getClass().getSimpleName());

        // Create runtime and launch browser once per test class.
        playwright = BrowserFactory.createPlaywright();
        browser = BrowserFactory.launchBrowser(playwright);
    }

    @BeforeMethod(alwaysRun = true)
    public void testSetup() {
        // Every test gets a clean browser context and fresh page.
        context = BrowserFactory.createContext(browser);
        page = BrowserFactory.createPage(context);

        // Ensure previous test logs do not leak into current test.
        consoleLogs.clear();

        // Capture browser console messages during this test.
        page.onConsoleMessage(msg -> {
            String logEntry = "[" + msg.type() + "] " + msg.text();
            consoleLogs.add(logEntry);
        });

        System.out.println("[PAGE] New page created for test.");
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown() {
        // Close context to clean cookies/session/storage created by this test.
        if (context != null) {
            context.close();
            System.out.println("[CLEANUP] Browser context closed.");
        }
    }

    @AfterClass(alwaysRun = true)
    public void classTeardown() {
        // Close browser and Playwright once all tests in this class finish.
        if (browser != null) {
            browser.close();
            System.out.println("[BROWSER] Browser closed for class: " + this.getClass().getSimpleName());
        }

        if (playwright != null) {
            playwright.close();
            System.out.println("[SETUP] Playwright closed.");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        System.out.println("\n[DONE] Playwright framework shutdown complete.");
    }
}