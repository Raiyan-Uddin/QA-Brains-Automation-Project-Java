// src/main/java/com/qabrains/base/BaseTest.java

package com.qabrains.base;

import com.microsoft.playwright.*;
import com.qabrains.config.AppConfig;
import com.qabrains.utils.BrowserFactory;
import com.qabrains.utils.TestListener;
import org.testng.annotations.*;

/**
 * Base Test class that ALL test classes must extend.
 *
 * Handles:
 *   - Playwright lifecycle (create/close)
 *   - Browser lifecycle (launch/close)
 *   - Context and Page lifecycle (create/close per test)
 *   - Common setup and teardown
 *
 * USAGE:
 *   1. Extend this class in your test class.
 *   2. Use 'page' object in your tests.
 *   3. Override browserType() if you want a different browser.
 *
 * EXAMPLE:
 *   public class LoginTests extends BaseTest {
 *       @Test
 *       public void testLogin() {
 *           page.navigate("...");
 *       }
 *   }
 */
@Listeners(TestListener.class)
public class BaseTest {

    // ========================
    // PLAYWRIGHT OBJECTS
    // ========================
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    // ========================
    // SUITE SETUP — Runs ONCE before all tests in the suite
    // ========================
    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        System.out.println("\n🔧 Initializing Playwright framework...");
    }

    // ========================
    // CLASS SETUP — Runs ONCE before all tests in a class
    // ========================
    @BeforeClass(alwaysRun = true)
    public void classSetup() {
        System.out.println("\n🌐 Setting up browser for test class: " + this.getClass().getSimpleName());
        playwright = BrowserFactory.createPlaywright();
        browser = BrowserFactory.launchBrowser(playwright);
    }

    // ========================
    // TEST SETUP — Runs before EACH test method
    // ========================
    @BeforeMethod(alwaysRun = true)
    public void testSetup() {
        context = BrowserFactory.createContext(browser);
        page = BrowserFactory.createPage(context);
        System.out.println("📄 New page created for test.");
    }

    // ========================
    // TEST TEARDOWN — Runs after EACH test method
    // ========================
    @AfterMethod(alwaysRun = true)
    public void testTeardown() {
        if (context != null) {
            context.close();
            System.out.println("🧹 Browser context closed.");
        }
    }

    // ========================
    // CLASS TEARDOWN — Runs ONCE after all tests in a class
    // ========================
    @AfterClass(alwaysRun = true)
    public void classTeardown() {
        if (browser != null) {
            browser.close();
            System.out.println("🌐 Browser closed for class: " + this.getClass().getSimpleName());
        }
        if (playwright != null) {
            playwright.close();
            System.out.println("🔧 Playwright closed.");
        }
    }

    // ========================
    // SUITE TEARDOWN — Runs ONCE after all tests in the suite
    // ========================
    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        System.out.println("\n🏁 Playwright framework shutdown complete.");
    }
}