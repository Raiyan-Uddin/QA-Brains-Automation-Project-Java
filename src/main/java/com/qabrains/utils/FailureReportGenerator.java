// src/main/java/com/qabrains/utils/FailureReportGenerator.java

package com.qabrains.utils;

import com.qabrains.config.ExecutionContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generates an HTML failure report with all diagnostic information.
 *
 * Creates a comprehensive HTML report that includes:
 *   - Summary statistics (total failures, test classes affected)
 *   - Detailed failure reports with screenshots, page source links, URLs
 *   - Browser information and console logs
 *   - Timestamp and duration information for quick analysis
 *
 * This facilitates quicker root-cause analysis by providing organized,
 * navigable failure diagnostics in a web-friendly format.
 */
public class FailureReportGenerator {

    private static final String FAILURE_REPORTS_DIR = "docs/Test Reports/failures";
    private static final String REPORT_FILE = FAILURE_REPORTS_DIR + "/failure-report.html";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Generates a comprehensive HTML failure report from diagnostic log file.
     * Should be called after test suite completion.
     */
    public static void generateFailureReport() {
        try {
            Path failuresDir = Paths.get(FAILURE_REPORTS_DIR);
            if (!Files.exists(failuresDir)) {
                return; // No failures to report
            }

            Path logFile = Paths.get(FAILURE_REPORTS_DIR, "failure-diagnostics.log");
            List<Map<String, String>> failures = parseFailureDiagnostics(logFile);

            if (failures.isEmpty()) {
                return;
            }

            String htmlContent = buildHtmlReport(failures);
            Path reportPath = Paths.get(REPORT_FILE).toAbsolutePath();
            Files.writeString(reportPath, htmlContent);

            System.out.println("\n✅ Failure report generated: " + reportPath);
        } catch (IOException e) {
            System.out.println("[WARN] Could not generate failure report: " + e.getMessage());
        }
    }

    /**
     * Parses the failure diagnostics log file to extract failure data.
     */
    private static List<Map<String, String>> parseFailureDiagnostics(Path logFile) {
        List<Map<String, String>> failures = new ArrayList<>();

        try {
            if (!Files.exists(logFile)) {
                return failures;
            }

            String content = Files.readString(logFile);
            String[] entries = content.split("=".repeat(80));

            for (String entry : entries) {
                if (entry.trim().isEmpty() || !entry.contains("FAILURE REPORT")) {
                    continue;
                }

                Map<String, String> failure = new HashMap<>();
                String[] lines = entry.split("\n");

                for (String line : lines) {
                    if (line.contains(":")) {
                        String[] parts = line.split(":", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();

                            if (!value.isEmpty()) {
                                failure.put(key, value);
                            }
                        }
                    }
                }

                if (!failure.isEmpty()) {
                    failures.add(failure);
                }
            }
        } catch (IOException e) {
            System.out.println("[WARN] Error parsing failure diagnostics: " + e.getMessage());
        }

        return failures;
    }

    /**
     * Builds the HTML report content.
     */
    private static String buildHtmlReport(List<Map<String, String>> failures) {
        StringBuilder html = new StringBuilder();

        // Calculate statistics
        Set<String> testClasses = failures.stream()
                .map(f -> f.get("Test Class"))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("  <title>Failure Report - QA Brains E-Commerce Tests</title>\n");
        html.append(getStyles());
        html.append("</head>\n");
        html.append("<body>\n");

         // Header
         html.append("  <div class=\"header\">\n");
         html.append("    <h1>🔴 Test Failure Report</h1>\n");
         html.append("    <p class=\"timestamp\">Generated: ").append(LocalDateTime.now().format(TIMESTAMP_FORMAT)).append("</p>\n");
         html.append("    <p class=\"execution-mode\">Execution Mode: <strong>").append(ExecutionContext.getInstance().getExecutionMode().getDisplayName()).append("</strong></p>\n");
         html.append("  </div>\n");

        // Summary
        html.append("  <div class=\"summary\">\n");
        html.append("    <h2>Summary</h2>\n");
        html.append("    <div class=\"stats\">\n");
        html.append("      <div class=\"stat-item\">\n");
        html.append("        <span class=\"stat-label\">Total Failures:</span>\n");
        html.append("        <span class=\"stat-value\">").append(failures.size()).append("</span>\n");
        html.append("      </div>\n");
        html.append("      <div class=\"stat-item\">\n");
        html.append("        <span class=\"stat-label\">Test Classes Affected:</span>\n");
        html.append("        <span class=\"stat-value\">").append(testClasses.size()).append("</span>\n");
        html.append("      </div>\n");
        html.append("    </div>\n");
        html.append("  </div>\n");

        // Detailed Failures
        html.append("  <div class=\"failures\">\n");
        html.append("    <h2>Failure Details</h2>\n");

        for (int i = 0; i < failures.size(); i++) {
            Map<String, String> failure = failures.get(i);
            html.append(buildFailureCard(failure, i + 1));
        }

        html.append("  </div>\n");

        // Footer
        html.append("  <div class=\"footer\">\n");
        html.append("    <p>For more information, check the diagnostic files in: <code>docs/Test Reports/failures</code></p>\n");
        html.append("  </div>\n");

        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();
    }

    /**
     * Builds an individual failure card.
     */
    private static String buildFailureCard(Map<String, String> failure, int index) {
        StringBuilder card = new StringBuilder();

        String testClass = failure.getOrDefault("Test Class", "Unknown");
        String testMethod = failure.getOrDefault("Test Method", "Unknown");
        String url = failure.getOrDefault("URL", "N/A");
        String reason = failure.getOrDefault("Failure Reason", "No reason provided");
        String screenshot = failure.getOrDefault("Screenshot", "");
        String pageSource = failure.getOrDefault("Page Source", "");

        card.append("    <div class=\"failure-card\">\n");
        card.append("      <div class=\"card-header\">\n");
        card.append("        <span class=\"card-number\">Failure #").append(index).append("</span>\n");
        card.append("        <span class=\"card-title\">").append(testClass).append("::").append(testMethod).append("</span>\n");
        card.append("      </div>\n");

        card.append("      <div class=\"card-content\">\n");

        // Failure Reason
        card.append("        <div class=\"detail-row\">\n");
        card.append("          <span class=\"detail-label\">❌ Reason:</span>\n");
        card.append("          <span class=\"detail-value\">").append(escapeHtml(reason)).append("</span>\n");
        card.append("        </div>\n");

        // URL
        card.append("        <div class=\"detail-row\">\n");
        card.append("          <span class=\"detail-label\">🔗 URL:</span>\n");
        card.append("          <span class=\"detail-value\"><a href=\"").append(escapeHtml(url)).append("\" target=\"_blank\">")
                .append(escapeHtml(url)).append("</a></span>\n");
        card.append("        </div>\n");

        // Screenshot
        if (screenshot != null && !screenshot.isEmpty() && !screenshot.contains("failed")) {
            card.append("        <div class=\"detail-row\">\n");
            card.append("          <span class=\"detail-label\">📸 Screenshot:</span>\n");
            card.append("          <span class=\"detail-value\"><a href=\"file:///").append(escapeHtml(screenshot.replace("\\", "/"))).append("\">View Screenshot</a></span>\n");
            card.append("        </div>\n");
        }

        // Page Source
        if (pageSource != null && !pageSource.isEmpty() && !pageSource.contains("failed")) {
            card.append("        <div class=\"detail-row\">\n");
            card.append("          <span class=\"detail-label\">📄 Page Source:</span>\n");
            card.append("          <span class=\"detail-value\"><a href=\"file:///").append(escapeHtml(pageSource.replace("\\", "/"))).append("\">View HTML</a></span>\n");
            card.append("        </div>\n");
        }

        card.append("      </div>\n");
        card.append("    </div>\n");

        return card.toString();
    }

    /**
     * Returns CSS styles for the HTML report.
     */
    private static String getStyles() {
        return "  <style>\n" +
                "    * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
                "    body {\n" +
                "      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);\n" +
                "      padding: 20px;\n" +
                "      min-height: 100vh;\n" +
                "    }\n" +
                "    .header {\n" +
                "      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "      color: white;\n" +
                "      padding: 40px 20px;\n" +
                "      border-radius: 8px;\n" +
                "      margin-bottom: 30px;\n" +
                "      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);\n" +
                "    }\n" +
                 "    .header h1 { font-size: 2.5em; margin-bottom: 10px; }\n" +
                 "    .header .timestamp { opacity: 0.9; font-size: 0.95em; }\n" +
                 "    .header .execution-mode { opacity: 0.9; font-size: 0.95em; margin-top: 5px; }\n" +
                "    .summary {\n" +
                "      background: white;\n" +
                "      padding: 30px;\n" +
                "      border-radius: 8px;\n" +
                "      margin-bottom: 30px;\n" +
                "      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);\n" +
                "    }\n" +
                "    .summary h2 { margin-bottom: 20px; color: #333; }\n" +
                "    .stats {\n" +
                "      display: flex;\n" +
                "      gap: 30px;\n" +
                "      flex-wrap: wrap;\n" +
                "    }\n" +
                "    .stat-item {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 15px;\n" +
                "      background: #f8f9fa;\n" +
                "      padding: 15px 25px;\n" +
                "      border-radius: 6px;\n" +
                "      border-left: 4px solid #667eea;\n" +
                "    }\n" +
                "    .stat-label { color: #666; font-weight: 500; }\n" +
                "    .stat-value { font-size: 1.8em; font-weight: bold; color: #667eea; }\n" +
                "    .failures { margin-bottom: 30px; }\n" +
                "    .failures h2 { margin-bottom: 20px; color: #333; }\n" +
                "    .failure-card {\n" +
                "      background: white;\n" +
                "      border-left: 5px solid #ff6b6b;\n" +
                "      border-radius: 6px;\n" +
                "      margin-bottom: 20px;\n" +
                "      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);\n" +
                "      overflow: hidden;\n" +
                "    }\n" +
                "    .card-header {\n" +
                "      background: #f8f9fa;\n" +
                "      padding: 15px 20px;\n" +
                "      display: flex;\n" +
                "      gap: 15px;\n" +
                "      align-items: center;\n" +
                "      border-bottom: 1px solid #e9ecef;\n" +
                "    }\n" +
                "    .card-number { background: #ff6b6b; color: white; padding: 5px 10px; border-radius: 4px; font-weight: bold; }\n" +
                "    .card-title { font-weight: 600; color: #333; font-size: 1.05em; }\n" +
                "    .card-content { padding: 20px; }\n" +
                "    .detail-row {\n" +
                "      display: flex;\n" +
                "      margin-bottom: 15px;\n" +
                "      flex-wrap: wrap;\n" +
                "      gap: 10px;\n" +
                "    }\n" +
                "    .detail-label {\n" +
                "      font-weight: 600;\n" +
                "      color: #555;\n" +
                "      min-width: 120px;\n" +
                "    }\n" +
                "    .detail-value {\n" +
                "      color: #666;\n" +
                "      word-break: break-all;\n" +
                "      flex: 1;\n" +
                "    }\n" +
                "    .detail-value a {\n" +
                "      color: #667eea;\n" +
                "      text-decoration: none;\n" +
                "      font-weight: 500;\n" +
                "      word-break: break-all;\n" +
                "    }\n" +
                "    .detail-value a:hover { text-decoration: underline; }\n" +
                "    .footer {\n" +
                "      background: white;\n" +
                "      padding: 20px;\n" +
                "      border-radius: 6px;\n" +
                "      text-align: center;\n" +
                "      color: #666;\n" +
                "      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);\n" +
                "    }\n" +
                "    .footer code {\n" +
                "      background: #f0f0f0;\n" +
                "      padding: 2px 6px;\n" +
                "      border-radius: 3px;\n" +
                "      font-family: monospace;\n" +
                "    }\n" +
                "    @media (max-width: 768px) {\n" +
                "      .header h1 { font-size: 1.8em; }\n" +
                "      .stats { flex-direction: column; }\n" +
                "      .stat-item { flex: 1; }\n" +
                "    }\n" +
                "  </style>\n";
    }

    /**
     * Escapes HTML special characters.
     */
    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}

