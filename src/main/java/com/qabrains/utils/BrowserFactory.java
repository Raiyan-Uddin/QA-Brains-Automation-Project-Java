// src/main/java/com/qabrains/utils/BrowserFactory.java

package com.qabrains.utils;

import com.microsoft.playwright.*;
import com.qabrains.config.AppConfig;
import com.qabrains.config.ExecutionContext;

/**
 * Factory class to create Playwright objects in one place.
 *
 * Beginner note:
 * - Tests should not directly configure browser launch options.
 * - They ask this class to create Playwright/Browser/Context/Page objects.
 * - This keeps setup consistent for every test class.
 */
public class BrowserFactory {

    /**
     * Step 1: create Playwright runtime.
     */
    public static Playwright createPlaywright() {
        return Playwright.create();
    }

    /**
     * Step 2: launch Chromium browser.
     * Uses the single execution mode selected for the run (headless/headed).
     */
    public static Browser launchBrowser(Playwright playwright) {
        // Read the authoritative mode once (true=headless, false=headed).
        boolean runHeadless = ExecutionContext.getInstance().isHeadless();

        // Build launch options from configuration.
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(runHeadless)
                .setSlowMo(AppConfig.SLOW_MO);

        System.out.println("[LAUNCH] Starting Chromium browser ("
                + ExecutionContext.getInstance().getExecutionMode().getDisplayName() + ")...");

        // Actual browser launch.
        return playwright.chromium().launch(launchOptions);
    }

    /**
     * Step 3: create isolated browser context for a test.
     * Each context is like a fresh browser profile.
     */
    public static BrowserContext createContext(Browser browser) {
        return browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(AppConfig.VIEWPORT_WIDTH, AppConfig.VIEWPORT_HEIGHT));
    }

    /**
     * Step 4: open a new page (tab) from context and set default timeout.
     */
    public static Page createPage(BrowserContext context) {
        Page page = context.newPage();
        page.setDefaultTimeout(AppConfig.DEFAULT_TIMEOUT);
        return page;
    }
}