// src/main/java/com/qabrains/utils/FullSuiteHtmlReportGenerator.java

package com.qabrains.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds a single "latest" HTML report.
 *
 * Priority order:
 * 1) Use in-memory TestNG summary data (most reliable at execution end).
 * 2) Fallback to surefire XML files when in-memory data is not available.
 */
public final class FullSuiteHtmlReportGenerator {

    private static final Path SUREFIRE_DIR = Paths.get("target", "surefire-reports");
    private static final Path OUTPUT_DIR = Paths.get("docs", "Test Reports");
    private static final Path OUTPUT_FILE = OUTPUT_DIR.resolve("Test-Report-Latest.html");
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private FullSuiteHtmlReportGenerator() {
        // Utility class: prevent direct creation.
    }

    /**
     * Preferred path: generate latest report from TestNG run-time summary.
     */
    public static void generateLatestReportFromModules(List<ModuleSnapshot> moduleSnapshots, String sourceNote) {
        try {
            List<ModuleResult> modules = new ArrayList<>();
            for (ModuleSnapshot snapshot : moduleSnapshots) {
                modules.add(new ModuleResult(
                        snapshot.moduleName(),
                        snapshot.tests(),
                        snapshot.passed(),
                        snapshot.failed(),
                        snapshot.skipped(),
                        snapshot.durationSeconds()
                ));
            }

            if (modules.isEmpty()) {
                // Fallback if no in-memory data was captured.
                generateLatestReportFromSurefireXml();
                return;
            }

            writeReport(modules, sourceNote == null || sourceNote.isBlank()
                    ? "Generated from TestNG execution summary"
                    : sourceNote);
        } catch (Exception e) {
            System.out.println("[WARN] Could not generate latest suite report from TestNG summary: " + e.getMessage());
        }
    }

    /**
     * Preferred path with detailed test-case level rows grouped by module.
     */
    public static void generateLatestReportFromModules(
            List<ModuleSnapshot> moduleSnapshots,
            List<TestCaseSnapshot> testCaseSnapshots,
            String sourceNote
    ) {
        try {
            List<ModuleResult> modules = new ArrayList<>();
            for (ModuleSnapshot snapshot : moduleSnapshots) {
                modules.add(new ModuleResult(
                        snapshot.moduleName(),
                        snapshot.tests(),
                        snapshot.passed(),
                        snapshot.failed(),
                        snapshot.skipped(),
                        snapshot.durationSeconds()
                ));
            }

            List<TestCaseResult> testCases = new ArrayList<>();
            for (TestCaseSnapshot snapshot : testCaseSnapshots) {
                testCases.add(new TestCaseResult(
                        snapshot.moduleName(),
                        snapshot.testCaseName(),
                        snapshot.methodName(),
                        snapshot.status(),
                        snapshot.durationSeconds(),
                        snapshot.message()
                ));
            }

            if (modules.isEmpty()) {
                generateLatestReportFromSurefireXml();
                return;
            }

            writeReport(
                    modules,
                    testCases,
                    sourceNote == null || sourceNote.isBlank()
                            ? "Generated from TestNG execution summary"
                            : sourceNote
            );
        } catch (Exception e) {
            System.out.println("[WARN] Could not generate latest suite report with test cases: " + e.getMessage());
        }
    }

    /**
     * Fallback path: generate latest report by reading surefire XML files.
     */
    public static void generateLatestReportFromSurefireXml() {
        try {
            List<ModuleResult> modules = readModuleResultsFromSurefire();
            if (modules.isEmpty()) {
                System.out.println("[WARN] No surefire XML files found to generate latest report.");
                return;
            }

            writeReport(modules, new ArrayList<>(), "Generated from target/surefire-reports XML files");
        } catch (Exception e) {
            System.out.println("[WARN] Could not generate latest suite report from surefire XML: " + e.getMessage());
        }
    }

    private static void writeReport(List<ModuleResult> modules, String sourceNote) throws IOException {
        writeReport(modules, new ArrayList<>(), sourceNote);
    }

    private static void writeReport(List<ModuleResult> modules, List<TestCaseResult> testCases, String sourceNote) throws IOException {
        Files.createDirectories(OUTPUT_DIR);
        String html = buildHtml(modules, testCases, sourceNote);
        Files.writeString(OUTPUT_FILE, html, StandardCharsets.UTF_8);
        System.out.println("[REPORT] Latest suite report updated: " + OUTPUT_FILE.toAbsolutePath());
    }

    private static List<ModuleResult> readModuleResultsFromSurefire() throws IOException {
        List<ModuleResult> results = new ArrayList<>();

        if (!Files.exists(SUREFIRE_DIR)) {
            return results;
        }

        try (var stream = Files.list(SUREFIRE_DIR)) {
            stream
                    .filter(path -> path.getFileName().toString().startsWith("TEST-"))
                    .filter(path -> path.getFileName().toString().endsWith(".xml"))
                    // Skip aggregate file to avoid double counting class-level files.
                    .filter(path -> !path.getFileName().toString().equals("TEST-TestSuite.xml"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .forEach(path -> parseSurefireFile(path, results));
        }

        return results;
    }

    private static void parseSurefireFile(Path xmlPath, List<ModuleResult> results) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlPath.toFile());
            Element suite = document.getDocumentElement();

            String rawName = suite.getAttribute("name");
            int tests = parseInt(suite.getAttribute("tests"));
            int failures = parseInt(suite.getAttribute("failures"));
            int errors = parseInt(suite.getAttribute("errors"));
            int skipped = parseInt(suite.getAttribute("skipped"));
            double duration = parseDouble(suite.getAttribute("time"));

            String moduleName = moduleNameFromSuite(rawName);
            int passed = Math.max(0, tests - failures - errors - skipped);

            results.add(new ModuleResult(moduleName, tests, passed, failures + errors, skipped, duration));
        } catch (Exception e) {
            System.out.println("[WARN] Could not parse report file " + xmlPath.getFileName() + ": " + e.getMessage());
        }
    }

    private static String moduleNameFromSuite(String rawSuiteName) {
        if (rawSuiteName == null || rawSuiteName.isBlank()) {
            return "Unknown Module";
        }

        String simpleName = rawSuiteName.contains(".")
                ? rawSuiteName.substring(rawSuiteName.lastIndexOf('.') + 1)
                : rawSuiteName;

        if (simpleName.endsWith("Tests")) {
            simpleName = simpleName.substring(0, simpleName.length() - "Tests".length());
        }

        return simpleName;
    }

    private static String buildHtml(List<ModuleResult> modules, List<TestCaseResult> testCases, String sourceNote) {
        int totalTests = modules.stream().mapToInt(m -> m.tests).sum();
        int totalPassed = modules.stream().mapToInt(m -> m.passed).sum();
        int totalFailed = modules.stream().mapToInt(m -> m.failed).sum();
        int totalSkipped = modules.stream().mapToInt(m -> m.skipped).sum();
        double totalDuration = modules.stream().mapToDouble(m -> m.durationSeconds).sum();

        double passRate = totalTests == 0 ? 0 : (totalPassed * 100.0 / totalTests);

        List<TestCaseResult> sortedTests = sortTestCases(testCases);
        Map<String, List<TestCaseResult>> byModule = sortedTests.stream()
                .collect(Collectors.groupingBy(TestCaseResult::moduleName, LinkedHashMap::new, Collectors.toList()));

        int failedHigh = 0;
        int failedMedium = 0;
        int failedLow = 0;
        for (TestCaseResult testCase : sortedTests) {
            if (!"FAILED".equalsIgnoreCase(testCase.status)) {
                continue;
            }
            String lower = (testCase.testCaseName == null ? "" : testCase.testCaseName).toLowerCase();
            if (lower.contains("@smoke")) {
                failedHigh++;
            } else if (lower.contains("@regression")) {
                failedMedium++;
            } else {
                failedLow++;
            }
        }

        String qualityGateClass = passRate >= 90.0 ? "gate-pass" : "gate-fail";
        String qualityGateLabel = passRate >= 90.0 ? "QUALITY GATE PASSED" : "QUALITY GATE FAILED";
        String qualityGateLine = "Pass rate " + String.format("%.2f", passRate) + "% " + (passRate >= 90.0 ? ">=" : "<") + " threshold 90%";

        String passedModuleSection = buildModuleCountTable(byModule, true);
        String failedModuleSection = buildModuleCountTable(byModule, false);
        String moduleOverviewSection = buildModuleOverviewSection(byModule);

        return "<!doctype html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "  <meta charset=\"utf-8\" />\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n"
                + "  <title>Overall Test Report</title>\n"
                + "  <style>\n"
                + "    :root { --bg:#0b1220; --panel:#121b2f; --panel2:#18243d; --text:#dce7ff; --muted:#98a7cc; --ok:#2ecc71; --bad:#ff5f56; --warn:#f5c451; --skip:#7f8ea3; --border:#243659; }\n"
                + "    * { box-sizing:border-box; }\n"
                + "    body { margin:0; font-family:Segoe UI,Arial,sans-serif; background:radial-gradient(circle at top right,#1a2f52 0%,var(--bg) 45%); color:var(--text); }\n"
                + "    .container { width:min(1280px,95vw); margin:24px auto 40px; }\n"
                + "    .header { background:linear-gradient(145deg,var(--panel),var(--panel2)); border:1px solid var(--border); border-radius:14px; padding:18px; margin-bottom:16px; }\n"
                + "    .header h1 { margin:0 0 8px; font-size:24px; }\n"
                + "    .meta { color:var(--muted); font-size:13px; }\n"
                + "    .stats { display:grid; grid-template-columns:repeat(7,minmax(110px,1fr)); gap:10px; margin:16px 0 10px; }\n"
                + "    .card { background:var(--panel); border:1px solid var(--border); border-radius:12px; padding:12px; }\n"
                + "    .card .k { color:var(--muted); font-size:12px; }\n"
                + "    .card .v { font-size:22px; font-weight:700; margin-top:4px; }\n"
                + "    .card .v.priority-v { font-size:15px; line-height:1.3; }\n"
                + "    .badge { font-size:11px; padding:4px 8px; border-radius:999px; font-weight:700; display:inline-block; }\n"
                + "    .badge.passed { background:rgba(46,204,113,0.15); color:var(--ok); }\n"
                + "    .badge.failed { background:rgba(255,95,86,0.16); color:var(--bad); }\n"
                + "    .badge.skipped { background:rgba(127,142,163,0.2); color:var(--skip); }\n"
                + "    .badge.gate-pass { background:rgba(46,204,113,0.2); color:var(--ok); font-size:13px; padding:6px 14px; }\n"
                + "    .badge.gate-fail { background:rgba(255,95,86,0.2); color:var(--bad); font-size:13px; padding:6px 14px; }\n"
                + "    .quality-gate { display:flex; align-items:center; gap:12px; padding:12px 16px; border-radius:10px; margin:8px 0 14px; border:1px solid var(--border); background:var(--panel2); }\n"
                + "    .section-title { font-size:16px; font-weight:600; margin:18px 0 8px; color:var(--text); }\n"
                + "    .split-grid { display:grid; grid-template-columns:repeat(2,minmax(260px,1fr)); gap:12px; }\n"
                + "    .split-box { background:var(--panel); border:1px solid var(--border); border-radius:12px; padding:10px 12px; }\n"
                + "    .split-box h4 { margin:4px 0 8px; font-size:14px; }\n"
                + "    .split-table { width:100%; border-collapse:collapse; min-width:100%; }\n"
                + "    .split-table th,.split-table td { padding:8px 10px; text-align:left; border-bottom:1px solid var(--border); font-size:12px; }\n"
                + "    .split-table th { color:var(--muted); background:#101a2b; }\n"
                + "    .module { background:var(--panel); border:1px solid var(--border); border-radius:12px; margin-top:12px; overflow:hidden; }\n"
                + "    .module h3 { margin:0; padding:12px 14px; background:#0f1a2c; border-bottom:1px solid var(--border); font-size:15px; display:flex; justify-content:space-between; align-items:center; }\n"
                + "    .module h3 span { color:var(--muted); font-size:12px; }\n"
                + "    .module-summary { display:flex; justify-content:space-between; align-items:center; gap:12px; padding:12px 14px; border-bottom:1px solid var(--border); background:#121f35; }\n"
                + "    .summary-grid { display:grid; grid-template-columns:repeat(4,minmax(90px,1fr)); gap:10px; width:100%; }\n"
                + "    .summary-grid .label { display:block; color:var(--muted); font-size:11px; margin-bottom:4px; }\n"
                + "    .summary-grid .value { display:block; font-size:14px; font-weight:700; }\n"
                + "    .summary-grid .value.ok { color:var(--ok); }\n"
                + "    .summary-grid .value.bad { color:var(--bad); }\n"
                + "    .summary-grid .value.skip { color:var(--skip); }\n"
                + "    .details-btn { border:1px solid var(--border); background:#0f1a2c; color:var(--text); border-radius:8px; padding:8px 12px; font-size:12px; font-weight:700; cursor:pointer; white-space:nowrap; }\n"
                + "    .details-btn:hover { background:#172846; }\n"
                + "    .module-details[hidden] { display:none; }\n"
                + "    .table-wrap { overflow:auto; }\n"
                + "    table { width:100%; border-collapse:collapse; min-width:900px; }\n"
                + "    th,td { text-align:left; padding:10px; border-bottom:1px solid var(--border); vertical-align:top; font-size:13px; }\n"
                + "    th { color:var(--muted); background:#101a2b; }\n"
                + "    .footer { color:var(--muted); font-size:12px; margin-top:14px; }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <div class=\"container\">\n"
                + "    <div class=\"header\">\n"
                + "      <h1>Overall Automation Report</h1>\n"
                + "      <div class=\"meta\">Generated: " + LocalDateTime.now().format(TS) + " | " + escapeHtml(sourceNote) + "</div>\n"
                + "    </div>\n"
                + "    <div class=\"stats\">\n"
                + "      <div class=\"card\"><div class=\"k\">Total</div><div class=\"v\">" + totalTests + "</div></div>\n"
                + "      <div class=\"card\"><div class=\"k\">Passed</div><div class=\"v\" style=\"color:var(--ok)\">" + totalPassed + "</div></div>\n"
                + "      <div class=\"card\"><div class=\"k\">Failed</div><div class=\"v\" style=\"color:var(--bad)\">" + totalFailed + "</div></div>\n"
                + "      <div class=\"card\"><div class=\"k\">Priority (Failed)</div><div class=\"v priority-v\">High: " + failedHigh + "<br/>Medium: " + failedMedium + "<br/>Low: " + failedLow + "</div></div>\n"
                + "      <div class=\"card\"><div class=\"k\">Skipped</div><div class=\"v\" style=\"color:var(--skip)\">" + totalSkipped + "</div></div>\n"
                + "      <div class=\"card\"><div class=\"k\">Duration</div><div class=\"v\">" + String.format("%.1fs", totalDuration) + "</div></div>\n"
                + "      <div class=\"card\"><div class=\"k\">Pass Rate</div><div class=\"v\">" + String.format("%.2f%%", passRate) + "</div></div>\n"
                + "    </div>\n"
                + "    <div class=\"quality-gate\">\n"
                + "      <span class=\"badge " + qualityGateClass + "\">" + qualityGateLabel + "</span>\n"
                + "      <span style=\"color:var(--muted);font-size:13px\">" + qualityGateLine + "</span>\n"
                + "    </div>\n"
                + "    <div class=\"section-title\">Module-wise Passed / Failed Split</div>\n"
                + "    <div class=\"split-grid\">\n"
                + "      <div class=\"split-box\"><h4>Passed Test Cases (Module-wise)</h4>" + passedModuleSection + "</div>\n"
                + "      <div class=\"split-box\"><h4>Failed Test Cases (Module-wise)</h4>" + failedModuleSection + "</div>\n"
                + "    </div>\n"
                + "    <div class=\"section-title\">Module Wise Test Result Overview</div>\n"
                + moduleOverviewSection
                + "    <div class=\"footer\">Total Duration: " + String.format("%.2f", totalDuration) + " s</div>\n"
                + "  </div>\n"
                + "  <script>\n"
                + "    document.querySelectorAll('.details-btn').forEach((button) => {\n"
                + "      button.addEventListener('click', () => {\n"
                + "        const targetId = button.getAttribute('data-target');\n"
                + "        const section = targetId ? document.getElementById(targetId) : null;\n"
                + "        if (!section) return;\n"
                + "        const isHidden = section.hasAttribute('hidden');\n"
                + "        if (isHidden) { section.removeAttribute('hidden'); button.textContent = 'Hide Details'; }\n"
                + "        else { section.setAttribute('hidden', ''); button.textContent = 'Details'; }\n"
                + "      });\n"
                + "    });\n"
                + "  </script>\n"
                + "</body>\n"
                + "</html>\n";
    }

    private static String buildModuleCountTable(Map<String, List<TestCaseResult>> byModule, boolean passed) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class=\"split-table\"><thead><tr><th>Module</th><th>Count</th></tr></thead><tbody>");

        for (String module : MODULE_ORDER) {
            List<TestCaseResult> tests = byModule.getOrDefault(module, List.of());
            long count = tests.stream().filter(t -> passed == "PASSED".equalsIgnoreCase(t.status)).count();
            sb.append("<tr><td>")
                    .append(escapeHtml(toDisplayModuleName(module)))
                    .append("</td><td")
                    .append(passed ? " style=\"color:var(--ok)\"" : " style=\"color:var(--bad)\"")
                    .append(">")
                    .append(count)
                    .append("</td></tr>");
        }

        sb.append("</tbody></table>");
        return sb.toString();
    }

    private static String buildModuleOverviewSection(Map<String, List<TestCaseResult>> byModule) {
        StringBuilder section = new StringBuilder();
        int moduleIndex = 1;

        for (String module : MODULE_ORDER) {
            List<TestCaseResult> tests = byModule.get(module);
            if (tests == null || tests.isEmpty()) {
                continue;
            }

            int total = tests.size();
            long passed = tests.stream().filter(t -> "PASSED".equalsIgnoreCase(t.status)).count();
            long failed = tests.stream().filter(t -> "FAILED".equalsIgnoreCase(t.status)).count();
            long skipped = tests.stream().filter(t -> "SKIPPED".equalsIgnoreCase(t.status)).count();
            String moduleId = "module-" + moduleIndex + "-" + module.toLowerCase();

            section.append("    <section class=\"module\">\n")
                    .append("      <h3>")
                    .append(moduleIndex++)
                    .append(". ")
                    .append(escapeHtml(toDisplayModuleName(module)))
                    .append(" <span>")
                    .append(total)
                    .append(" tests</span></h3>\n")
                    .append("      <div class=\"module-summary\">\n")
                    .append("        <div class=\"summary-grid\">\n")
                    .append("          <div><span class=\"label\">Passed</span><span class=\"value ok\">").append(passed).append("</span></div>\n")
                    .append("          <div><span class=\"label\">Failed</span><span class=\"value bad\">").append(failed).append("</span></div>\n")
                    .append("          <div><span class=\"label\">Skipped</span><span class=\"value skip\">").append(skipped).append("</span></div>\n")
                    .append("          <div><span class=\"label\">Pass Rate</span><span class=\"value\">")
                    .append(String.format("%.2f%%", passed * 100.0 / total))
                    .append("</span></div>\n")
                    .append("        </div>\n")
                    .append("        <button type=\"button\" class=\"details-btn\" data-target=\"").append(moduleId).append("\">Details</button>\n")
                    .append("      </div>\n")
                    .append("      <div class=\"table-wrap module-details\" id=\"").append(moduleId).append("\" hidden>\n")
                    .append("        <table>\n")
                    .append("          <thead><tr><th>Test ID</th><th>Title</th><th>Status</th><th>Duration</th><th>Error</th></tr></thead>\n")
                    .append("          <tbody>");

            for (TestCaseResult testCase : tests) {
                String statusBadge = "PASSED".equalsIgnoreCase(testCase.status) ? "passed" :
                        "FAILED".equalsIgnoreCase(testCase.status) ? "failed" : "skipped";
                String testId = extractTestId(testCase.testCaseName, testCase.methodName);
                String error = "FAILED".equalsIgnoreCase(testCase.status)
                        ? (testCase.message == null || testCase.message.isBlank() ? "-" : escapeHtml(testCase.message))
                        : "-";

                section.append("<tr>")
                        .append("<td>").append(escapeHtml(testId)).append("</td>")
                        .append("<td>").append(escapeHtml(testCase.testCaseName)).append("</td>")
                        .append("<td><span class=\"badge ").append(statusBadge).append("\">").append(escapeHtml(testCase.status)).append("</span></td>")
                        .append("<td>").append(String.format("%.2f", testCase.durationSeconds)).append(" s</td>")
                        .append("<td style=\"color:")
                        .append("FAILED".equalsIgnoreCase(testCase.status) ? "var(--bad)" : "var(--muted)")
                        .append(";font-size:12px\">")
                        .append(error)
                        .append("</td>")
                        .append("</tr>");
            }

            section.append("</tbody>\n")
                    .append("        </table>\n")
                    .append("      </div>\n")
                    .append("    </section>\n");
        }

        return section.toString();
    }

    private static List<TestCaseResult> sortTestCases(List<TestCaseResult> testCases) {
        if (testCases == null) {
            return List.of();
        }

        return testCases.stream()
                .map(t -> new TestCaseResult(
                        normalizeModuleName(t.moduleName),
                        t.testCaseName,
                        t.methodName,
                        t.status,
                        t.durationSeconds,
                        t.message
                ))
                .sorted(Comparator
                        .comparingInt((TestCaseResult t) -> moduleIndex(t.moduleName))
                        .thenComparing(t -> extractTestId(t.testCaseName, t.methodName))
                        .thenComparing(TestCaseResult::testCaseName))
                .toList();
    }

    private static int moduleIndex(String moduleName) {
        int idx = MODULE_ORDER.indexOf(moduleName);
        return idx >= 0 ? idx : Integer.MAX_VALUE;
    }

    private static String normalizeModuleName(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            return "Unknown";
        }

        String cleaned = rawName.replace("Tests", "").replaceAll("\\s+", "");
        if (cleaned.equalsIgnoreCase("Login")) return "Login";
        if (cleaned.equalsIgnoreCase("Home")) return "Home";
        if (cleaned.equalsIgnoreCase("ProductDetails")) return "ProductDetails";
        if (cleaned.equalsIgnoreCase("Cart")) return "Cart";
        if (cleaned.equalsIgnoreCase("CheckoutInfo")) return "CheckoutInfo";
        if (cleaned.equalsIgnoreCase("CheckoutOverview")) return "CheckoutOverview";
        if (cleaned.equalsIgnoreCase("CheckoutComplete")) return "CheckoutComplete";
        return rawName;
    }

    private static String toDisplayModuleName(String module) {
        return switch (module) {
            case "Login" -> "LOGIN PAGE";
            case "Home" -> "HOME PAGE";
            case "ProductDetails" -> "PRODUCT DETAILS PAGE";
            case "Cart" -> "CART PAGE";
            case "CheckoutInfo" -> "CHECKOUT YOUR INFO PAGE";
            case "CheckoutOverview" -> "CHECKOUT OVERVIEW PAGE";
            case "CheckoutComplete" -> "CHECKOUT COMPLETE PAGE";
            default -> module;
        };
    }

    private static String extractTestId(String testCaseName, String methodName) {
        String source = (testCaseName != null && !testCaseName.isBlank()) ? testCaseName : methodName;
        if (source == null || source.isBlank()) {
            return "-";
        }

        int idx = source.indexOf(':');
        String candidate = idx > 0 ? source.substring(0, idx).trim() : source.trim();
        return candidate.isEmpty() ? "-" : candidate;
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value == null || value.isBlank() ? "0" : value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static double parseDouble(String value) {
        try {
            return Double.parseDouble(value == null || value.isBlank() ? "0" : value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static String escapeHtml(String value) {
        if (value == null) return "";
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static final List<String> MODULE_ORDER = List.of(
            "Login", "Home", "ProductDetails", "Cart", "CheckoutInfo", "CheckoutOverview", "CheckoutComplete"
    );

    public record ModuleSnapshot(
            String moduleName,
            int tests,
            int passed,
            int failed,
            int skipped,
            double durationSeconds
    ) {}

    public record TestCaseSnapshot(
            String moduleName,
            String testCaseName,
            String methodName,
            String status,
            double durationSeconds,
            String message
    ) {}

    private record ModuleResult(
            String moduleName,
            int tests,
            int passed,
            int failed,
            int skipped,
            double durationSeconds
    ) {}

    private record TestCaseResult(
            String moduleName,
            String testCaseName,
            String methodName,
            String status,
            double durationSeconds,
            String message
    ) {}
}
