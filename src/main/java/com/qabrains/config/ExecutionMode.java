// src/main/java/com/qabrains/config/ExecutionMode.java

package com.qabrains.config;

/**
 * Enumeration of execution modes for test execution.
 *
 * Defines the possible execution modes:
 *   - HEADLESS: Browser runs without UI (faster, suitable for CI/CD)
 *   - HEADED: Browser UI is displayed (useful for debugging and visual verification)
 *
 * This enum ensures consistent, authoritative execution mode throughout the test suite
 * and facilitates accurate reporting of test execution context.
 */
public enum ExecutionMode {
    HEADLESS("Headless", "Browser runs without UI - faster execution, suitable for CI/CD"),
    HEADED("Headed", "Browser UI is displayed - useful for debugging and visual verification");

    private final String displayName;
    private final String description;

    ExecutionMode(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Returns the display-friendly name of the execution mode.
     *
     * @return Display name (e.g., "Headless", "Headed")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns a description of the execution mode.
     *
     * @return Description of when this mode is used
     */
    public String getDescription() {
        return description;
    }

    /**
     * Determines execution mode from a boolean headless flag.
     *
     * @param isHeadless true for headless mode, false for headed
     * @return Corresponding ExecutionMode
     */
    public static ExecutionMode fromHeadlessFlag(boolean isHeadless) {
        return isHeadless ? HEADLESS : HEADED;
    }

    /**
     * Returns true if this is headless mode.
     *
     * @return true for HEADLESS, false for HEADED
     */
    public boolean isHeadless() {
        return this == HEADLESS;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

