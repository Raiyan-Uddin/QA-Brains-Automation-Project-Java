// src/main/java/com/qabrains/config/ExecutionContext.java

package com.qabrains.config;

import java.time.LocalDateTime;

/**
 * Singleton that maintains the authoritative execution context for the entire test session.
 *
 * Responsibilities:
 *   - Enforces a single ExecutionMode for the entire test run
 *   - Provides centralized access to execution mode across all components
 *   - Prevents inconsistent mode reporting by caching the mode once set
 *   - Tracks execution session metadata (start time, environment info)
 *
 * USAGE:
 *   1. Initialize once at suite start: ExecutionContext.getInstance().initialize(executionMode);
 *   2. Access anywhere in code: ExecutionMode mode = ExecutionContext.getInstance().getExecutionMode();
 *
 * This ensures that all failure reports, diagnostics, and logs use the SAME
 * execution mode for the entire session.
 */
public class ExecutionContext {

    private static final ExecutionContext INSTANCE = new ExecutionContext();

    private ExecutionMode executionMode;
    private LocalDateTime executionStartTime;
    private boolean initialized = false;

    /**
     * Private constructor for singleton pattern.
     */
    private ExecutionContext() {
    }

    /**
     * Returns the singleton instance of ExecutionContext.
     *
     * @return ExecutionContext singleton
     */
    public static ExecutionContext getInstance() {
        return INSTANCE;
    }

    /**
     * Initializes the execution context with the authoritative execution mode.
     * This should be called ONCE at the start of test suite execution.
     * Subsequent calls will be ignored to maintain consistency.
     *
     * @param executionMode The ExecutionMode for this test session
     */
    public synchronized void initialize(ExecutionMode executionMode) {
        if (initialized) {
            System.out.println("[WARN] ExecutionContext is already initialized. Ignoring re-initialization.");
            return;
        }

        this.executionMode = executionMode;
        this.executionStartTime = LocalDateTime.now();
        this.initialized = true;

        System.out.println("\n" + "=".repeat(70));
        System.out.println("[EXECUTION MODE] " + executionMode.getDisplayName());
        System.out.println("  Description: " + executionMode.getDescription());
        System.out.println("  Initialized at: " + executionStartTime);
        System.out.println("=".repeat(70) + "\n");
    }

    /**
     * Returns the current execution mode.
     * If not initialized, defaults to AppConfig.HEADLESS value.
     *
     * @return ExecutionMode for the current session
     */
    public synchronized ExecutionMode getExecutionMode() {
        if (!initialized) {
            // Default initialization if not explicitly set
            ExecutionMode defaultMode = ExecutionMode.fromHeadlessFlag(AppConfig.HEADLESS);
            initialize(defaultMode);
        }
        return executionMode;
    }

    /**
     * Returns true if the current execution mode is headless.
     *
     * @return true for headless execution, false for headed
     */
    public synchronized boolean isHeadless() {
        return getExecutionMode().isHeadless();
    }

    /**
     * Returns the time when execution context was initialized.
     *
     * @return Execution start time
     */
    public synchronized LocalDateTime getExecutionStartTime() {
        return executionStartTime;
    }

    /**
     * Returns true if the execution context has been initialized.
     *
     * @return true if initialized, false otherwise
     */
    public synchronized boolean isInitialized() {
        return initialized;
    }

    /**
     * Resets the execution context (useful for testing).
     * This should NOT be called during normal test execution.
     */
    public synchronized void reset() {
        executionMode = null;
        executionStartTime = null;
        initialized = false;
        System.out.println("[DEBUG] ExecutionContext has been reset.");
    }

    @Override
    public String toString() {
        return "ExecutionContext{" +
                "mode=" + (executionMode != null ? executionMode.getDisplayName() : "NOT_INITIALIZED") +
                ", startTime=" + executionStartTime +
                '}';
    }
}

