// src/main/java/com/qabrains/utils/BrowserFactory.java

package com.qabrains.utils;

import com.microsoft.playwright.*;
import com.qabrains.config.AppConfig;

/**
 * Factory class to create and manage Playwright Browser instances.
 * Configured for Chromium only.
 * Centralized browser configuration — used by BaseTest.
 */
public class BrowserFactory {

    /**
     * Creates a new Playwright instance.
     */
    public static Playwright createPlaywright() {
        return Playwright.create();
    }

    /**
     * Launches Chromium browser with configured options.
     *
     * @param playwright The Playwright instance.
     * @return Browser instance (Chromium).
     */
    public static Browser launchBrowser(Playwright playwright) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(AppConfig.HEADLESS)
                .setSlowMo(AppConfig.SLOW_MO);

        System.out.println("🌀 Launching Chromium browser...");
        return playwright.chromium().launch(options);
    }

    /**
     * Creates a new browser context with viewport settings.
     *
     * @param browser The Browser instance.
     * @return BrowserContext instance.
     */
    public static BrowserContext createContext(Browser browser) {
        return browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(AppConfig.VIEWPORT_WIDTH, AppConfig.VIEWPORT_HEIGHT));
    }

    /**
     * Creates a new page from the given context.
     *
     * @param context The BrowserContext instance.
     * @return Page instance.
     */
    public static Page createPage(BrowserContext context) {
        Page page = context.newPage();
        page.setDefaultTimeout(AppConfig.DEFAULT_TIMEOUT);
        return page;
    }
}