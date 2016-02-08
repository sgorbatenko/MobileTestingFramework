
package com.stas.mobile.testing.framework.web;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.Locatable;

import com.stas.mobile.testing.framework.AbstractUIData;
import com.stas.mobile.testing.framework.UIData;
import com.stas.mobile.testing.framework.queryhelpers.WebElementQueryHelper;
import com.stas.mobile.testing.framework.synchronization.AjaxHelper;
import com.stas.mobile.testing.framework.util.drivers.SnapshotManager;
import com.stas.mobile.testing.framework.util.drivers.WebDriverWrapper;
import com.stas.mobile.testing.framework.util.logger.LogController;
import com.stas.mobile.testing.framework.web.controls.BaseHtmlElement;

public abstract class AbstractPage extends AbstractUIData
{
    public static final String AUT_URL_KEY = "aut.url";
    private LogController _logger = new LogController(AbstractPage.class);
    protected WebDriver _driver;
    protected WebElementQueryHelper _queryHelper = new WebElementQueryHelper(
        getDriver());
    protected BaseHtmlElement dataLoadingWindow;
    protected AjaxHelper _syncHelper;
    protected static SnapshotManager snapshotManager = new SnapshotManager();

    protected AbstractPage(WebDriver driver)
    {
        _driver = driver;
        _syncHelper = new AjaxHelper(driver);
    }

    @Override
    public String getSelector()
    {
        return null;
    }

    @Override
    public UIData getParent()
    {
        return null;
    }

    @Override
    public WebDriver getDriver()
    {
        return WebDriverWrapper.getWebDriver();
    }

    @Deprecated
    public void scrollUp()
    {
        _logger.info("Scrolling to top of page");
        getDriver().findElement(
            By.tagName("body")).sendKeys(new CharSequence[]{Keys.HOME});
    }

    @Deprecated
    public void scrollDown()
    {
        _logger.info("Scrolling to bottom of page");
        WebDriver driver = getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
            "window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));",
            new Object[0]);

        _syncHelper.suspend(100);
    }

    @Deprecated
    public void scrollToElement(String elementPath)
    {
        _logger.debug("About to scroll to " + elementPath);
        int y = 0;
        try
        {
            _syncHelper.waitForElementToAppear(elementPath);

            Locatable hoverItem = (Locatable) _queryHelper.findElementByExtendedCss(elementPath);
            y = hoverItem.getCoordinates().onPage().getY();
        }
        catch (StaleElementReferenceException sere)
        {
            _logger.warn("Stale Reference Exception was thrown, refreshing page and trying to get coordinates again");
            getDriver().navigate().refresh();
            _syncHelper.waitForElementToAppear(elementPath);
            Locatable hoverItem = (Locatable) _driver.findElement(
                By.cssSelector(elementPath));
            y = hoverItem.getCoordinates().onPage().getY();
        }
        ((JavascriptExecutor) _driver).executeScript("window.scrollBy(0," + y + ");", new Object[0]);
    }

    public void reload()
    {
        getDriver().navigate().refresh();
    }
}
