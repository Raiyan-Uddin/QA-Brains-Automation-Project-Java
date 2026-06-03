// src/main/java/com/qabrains/utils/FailureReporter.java

package com.qabrains.utils;

import com.microsoft.playwright.Page;
import com.qabrains.config.ExecutionContext;
import com.qabrains.config.ExecutionMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Central utility for capturing and storing comprehensive failure diagnostics.
 *
 * Parallel-run safety note:
 * - In class-parallel execution, multiple test threads can fail at the same time.
 * - This class uses LOG_WRITE_LOCK to avoid mixed/corrupted writes in one shared log file.
 */
public class FailureReporter {

    private static final String FAILURE_REPORTS_DIR = "docs/Test Reports/failures";
    private static final String SCREENSHOTS_DIR = FAILURE_REPORTS_DIR + "/screenshots";
    private static final String PAGE_SOURCE_DIR = FAILURE_REPORTS_DIR + "/page-source";
    private static final String LOG_FILE = FAILURE_REPORTS_DIR + "/failure-diagnostics.log";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");

    // Single lock object for safe append to shared diagnostics log.
    private static final Object LOG_WRITE_LOCK = new Object();

    /**
     * Captures all failure diagnostics for a failed test.
     */
    public static FailureDiagnostics captureFailureDiagnostics(
            Page page,
            String testClassName,
            String testMethodName,
            String failureReason) {

        FailureDiagnostics diagnostics = new FailureDiagnostics();
        diagnostics.setTestClass(testClassName);
        diagnostics.setTestMethod(testMethodName);
        diagnostics.setFailureReason(failureReason);
        diagnostics.setTimestamp(LocalDateTime.now());

        // Set the authoritative execution mode (headless/headed) used by this run.
        ExecutionContext context = ExecutionContext.getInstance();
        diagnostics.setExecutionMode(context.getExecutionMode());

        if (page == null || page.isClosed()) {
            diagnostics.setPageAvailable(false);
            return diagnostics;
        }

        diagnostics.setPageAvailable(true);

        try {
            // Ensure diagnostics directories exist.
            Files.createDirectories(Paths.get(SCREENSHOTS_DIR));
            Files.createDirectories(Paths.get(PAGE_SOURCE_DIR));
            Files.createDirectories(Paths.get(FAILURE_REPORTS_DIR));

            diagnostics.setUrl(captureUrl(page));
            diagnostics.setScreenshotPath(captureScreenshot(page, testClassName, testMethodName));
            diagnostics.setPageSourcePath(capturePageSource(page, testClassName, testMethodName));
            diagnostics.setConsoleLogs(captureConsoleLogs(page));
            diagnostics.setBrowserInfo(captureBrowserInfo(page));

            logFailureDiagnostics(diagnostics);

        } catch (Exception e) {
            System.out.println("[WARN] Error capturing failure diagnostics: " + e.getMessage());
        }

        return diagnostics;
    }

    private static String captureUrl(Page page) {
        try {
            return page.url();
        } catch (Exception e) {
            return "Unable to capture URL: " + e.getMessage();
        }
    }

    private static String captureScreenshot(Page page, String testClassName, String testMethodName) {
        try {
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String fileName = sanitizeFileName(testClassName) + "-" + sanitizeFileName(testMethodName) + "_" + timestamp + ".png";
            Path outputPath = Paths.get(SCREENSHOTS_DIR, fileName).toAbsolutePath();

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(outputPath)
                    .setFullPage(true));

            return outputPath.toString();
        } catch (Exception e) {
            return "Screenshot capture failed: " + e.getMessage();
        }
    }

    private static String capturePageSource(Page page, String testClassName, String testMethodName) {
        try {
            String content = page.content();
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String fileName = sanitizeFileName(testClassName) + "-" + sanitizeFileName(testMethodName) + "_" + timestamp + ".html";
            Path outputPath = Paths.get(PAGE_SOURCE_DIR, fileName).toAbsolutePath();

            Files.writeString(outputPath, content);
            return outputPath.toString();
        } catch (Exception e) {
            return "Page source capture failed: " + e.getMessage();
        }
    }

    private static List<String> captureConsoleLogs(Page page) {
        List<String> logs = new ArrayList<>();
        try {
            // Console capture is collected in BaseTest via page.onConsoleMessage(...).
            logs.add("Console logs require setup in BaseTest.beforeMethod()");
        } catch (Exception e) {
            logs.add("Console log capture failed: " + e.getMessage());
        }
        return logs;
    }

    private static String captureBrowserInfo(Page page) {
        try {
            return page.evaluate("() => ({" +
                    "  userAgent: navigator.userAgent," +
                    "  viewport: { width: window.innerWidth, height: window.innerHeight }," +
                    "  title: document.title" +
                    "})").toString();
        } catch (Exception e) {
            return "Browser info capture failed: " + e.getMessage();
        }
    }

    /**
     * Logs failure diagnostics to shared log file.
     * Uses lock to prevent overlapping writes during parallel execution.
     */
    private static void logFailureDiagnostics(FailureDiagnostics diagnostics) {
        try {
            StringBuilder logEntry = new StringBuilder();
            logEntry.append("\n").append("=".repeat(80)).append("\n");
            logEntry.append("FAILURE REPORT: ").append(diagnostics.getTimestamp()).append("\n");
            logEntry.append("Execution Mode: ").append(diagnostics.getExecutionMode().getDisplayName()).append("\n");
            logEntry.append("Test Class: ").append(diagnostics.getTestClass()).append("\n");
            logEntry.append("Test Method: ").append(diagnostics.getTestMethod()).append("\n");
            logEntry.append("Failure Reason: ").append(diagnostics.getFailureReason()).append("\n");

            if (diagnostics.isPageAvailable()) {
                logEntry.append("URL: ").append(diagnostics.getUrl()).append("\n");
                logEntry.append("Screenshot: ").append(diagnostics.getScreenshotPath()).append("\n");
                logEntry.append("Page Source: ").append(diagnostics.getPageSourcePath()).append("\n");
                logEntry.append("Browser Info: ").append(diagnostics.getBrowserInfo()).append("\n");

                if (!diagnostics.getConsoleLogs().isEmpty()) {
                    logEntry.append("Console Logs: ").append("\n");
                    for (String log : diagnostics.getConsoleLogs()) {
                        logEntry.append("  - ").append(log).append("\n");
                    }
                }
            } else {
                logEntry.append("Note: Page not available at time of failure.\n");
            }
            logEntry.append("=".repeat(80)).append("\n");

            Path logPath = Paths.get(LOG_FILE).toAbsolutePath();

            synchronized (LOG_WRITE_LOCK) {
                Files.write(logPath, logEntry.toString().getBytes(),
                        java.nio.file.StandardOpenOption.CREATE,
                        java.nio.file.StandardOpenOption.APPEND);
            }

        } catch (IOException e) {
            System.out.println("[WARN] Could not log failure diagnostics: " + e.getMessage());
        }
    }

    /**
     * Sanitizes a file name by removing invalid characters and limiting length.
     */
    private static String sanitizeFileName(String name) {
        String safeName = (name != null ? name : "unknown").replaceAll("[^a-zA-Z0-9._-]", "_");
        return safeName.substring(0, Math.min(100, safeName.length()));
    }

    public static String getFailuresDirectoryPath() {
        return Paths.get(FAILURE_REPORTS_DIR).toAbsolutePath().toString();
    }

    /**
     * Data object used to carry failure diagnostics between layers.
     */
    public static class FailureDiagnostics {
        private String testClass;
        private String testMethod;
        private String failureReason;
        private LocalDateTime timestamp;
        private ExecutionMode executionMode;
        private String url;
        private String screenshotPath;
        private String pageSourcePath;
        private List<String> consoleLogs;
        private String browserInfo;
        private boolean pageAvailable;

        public String getTestClass() { return testClass; }
        public void setTestClass(String testClass) { this.testClass = testClass; }

        public String getTestMethod() { return testMethod; }
        public void setTestMethod(String testMethod) { this.testMethod = testMethod; }

        public String getFailureReason() { return failureReason; }
        public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

        public ExecutionMode getExecutionMode() { return executionMode != null ? executionMode : ExecutionMode.HEADLESS; }
        public void setExecutionMode(ExecutionMode executionMode) { this.executionMode = executionMode; }

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public String getScreenshotPath() { return screenshotPath; }
        public void setScreenshotPath(String screenshotPath) { this.screenshotPath = screenshotPath; }

        public String getPageSourcePath() { return pageSourcePath; }
        public void setPageSourcePath(String pageSourcePath) { this.pageSourcePath = pageSourcePath; }

        public List<String> getConsoleLogs() { return consoleLogs != null ? consoleLogs : new ArrayList<>(); }
        public void setConsoleLogs(List<String> consoleLogs) { this.consoleLogs = consoleLogs; }

        public String getBrowserInfo() { return browserInfo; }
        public void setBrowserInfo(String browserInfo) { this.browserInfo = browserInfo; }

        public boolean isPageAvailable() { return pageAvailable; }
        public void setPageAvailable(boolean pageAvailable) { this.pageAvailable = pageAvailable; }

        @Override
        public String toString() {
            return "FailureDiagnostics{" +
                    "testClass='" + testClass + '\'' +
                    ", testMethod='" + testMethod + '\'' +
                    ", url='" + url + '\'' +
                    ", screenshot='" + screenshotPath + '\'' +
                    ", pageSource='" + pageSourcePath + '\'' +
                    '}';
        }
    }
}
