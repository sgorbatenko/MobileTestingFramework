
package com.stas.mobile.testing.framework.synchronization;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.google.common.base.Function;
import com.stas.mobile.testing.framework.UIData;
import com.stas.mobile.testing.framework.queryhelpers.WebElementQueryHelper;
import com.stas.mobile.testing.framework.util.TestHelper;
import com.stas.mobile.testing.framework.util.environment.Browser;
import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;
import com.stas.mobile.testing.framework.web.AbstractPageChunk;
import com.stas.mobile.testing.framework.web.controls.BaseHtmlElement;

public class AjaxHelper
{
    private WebElementQueryHelper _queryHelper;
    private WebDriver driver;
    private LogController logger = new LogController(AjaxHelper.class);
    private EnvironmentUtil envUtil = new EnvironmentUtil();
    private AjaxHelper _syncHelper;

    public AjaxHelper(WebDriver wd)
    {
        this._queryHelper = new WebElementQueryHelper(wd);
        this.driver = wd;
    }

    public void waitIfIE()
    {
        if (this.envUtil.getBrowser() == Browser.REMOTE_INTERNETEXPLORER)
        {
            this.logger.info("Execute IE Wait");
            suspend(1500);
        }
    }

    public void waitForElementAppearanceChanged(UIData element, boolean appear)
    {
        String selector = element.getAbsoluteSelector();
        waitForElementWithSelectorAppearanceChange(selector, appear);
    }

    public void waitForElementNoTimeout(UIData element, boolean appear)
    {
        waitForElementForNoTimeout(element, appear, 60);
    }

    public void waitForElementForNoTimeout(UIData element, boolean appear,
        int timeout)
    {
        String absoluteSelector = element.getAbsoluteSelector();
        waitForElementWithSelectorForNoTimeout(absoluteSelector,
            appear,
            timeout);
    }

    public void waitForElementWithSelectorForNoTimeout(String absoluteSelector,
        boolean appear, int timeout)
    {
        try
        {
            waitForElementAppearanceChangedForTimeout(absoluteSelector,
                appear,
                timeout);
        }
        catch (RuntimeException e)
        {
            System.err.println("Haven't found the element '" + absoluteSelector
                + "' - proceeding without exception");
        }
    }

    public void waitForElementWithSelectorAppearanceChange(String selector,
        boolean appear)
    {
        waitForElementWithSelectorAppearanceChangeForTimeout(selector,
            appear,
            60);
    }

    public void waitForElementAppearanceChangedForTimeout(String selector,
        boolean appear, int timeout)
    {
        waitForElementWithSelectorAppearanceChangeForTimeout(selector,
            appear,
            timeout);
    }

    public void waitForElementWithSelectorAppearanceChangeForTimeout(
        final String selector, final boolean appear, int timeout)
    {
        this.logger.debug("Waiting for element to "
            + (appear ? "appear: " : "disappear: ") + selector);

        WebDriverWait wait = new WebDriverWait(this.driver, timeout);
        Function<WebDriver, Boolean> function = input -> {
            boolean condition = AjaxHelper.this._queryHelper
                .isElementPresent(selector);
            if (!appear)
            {
                condition = !condition;
            }
            return Boolean.valueOf(condition);
        };
        try
        {
            wait.until(function);
        }
        catch (TimeoutException e)
        {
            throw new RuntimeException("Timeout for element: " + selector, e);
        }
        catch (Exception e)
        {
            throw new RuntimeException(
                "Exception getting element: " + selector, e);
        }
    }

    public void waitForElementToAppear(String selector)
    {
        long end = TestHelper.getCurrentGMT6Time() + 15000L;

        WebElement element = null;
        boolean nseLogMessage = true;
        while (TestHelper.getCurrentGMT6Time() < end)
        {
            try
            {
                element = this._queryHelper.findElementByExtendedCss(selector);
                if (element == null)
                {
                    suspend(1000);
                    this.logger.info("Waiting for " + selector + " to appear");
                }
                else
                {
                    break;
                }

            }
            catch (NoSuchElementException nse)
            {
                if (nseLogMessage)
                {
                    this.logger.debug("Element [" + selector
                        + "] wasn't located, waiting and rerunning loop");

                    nseLogMessage = false;
                }
            }
        }
        if (element == null)
        {
            Assert.fail("Element [" + selector
                + "] was not found before timeout.");
        }
    }

    public void waitForElementToAppear(BaseHtmlElement element)
    {
        long end = TestHelper.getCurrentGMT6Time() + 150000L;

        boolean nseLogMessage = true;
        while (TestHelper.getCurrentGMT6Time() < end)
        {
            try
            {
                if (!element.isDisplayed().booleanValue())
                {
                    suspend(2000);
                    this.logger.info("Waiting for "
                        + element.getAbsoluteSelector() + " to appear");
                }
                else if (element.isDisplayed().booleanValue())
                {
                    break;
                }
            }
            catch (NoSuchElementException nse)
            {
                if (nseLogMessage)
                {
                    this.logger.debug("Element ["
                        + element.getAbsoluteSelector()
                        + "] wasn't located, waiting and rerunning loop");

                    nseLogMessage = false;
                }
            }
        }
        if (!element.isDisplayed().booleanValue())
        {
            Assert.fail("Element [" + element.getAbsoluteSelector()
                + "] was not found before timeout.");
        }
    }

    public void waitForElementToAppear(String selector, int waitTime,
        int retryCount, WebDriver wd)
    {
        int count = 0;
        while (!this._queryHelper.doesElementExist(selector))
        {
            scrollDown(wd);
            this._syncHelper.suspend(waitTime);
            scrollUp(wd);
            count++;
            if (count > retryCount)
            {
                Assert.fail(String.format(
                    "Element with selector [%s] not found before timeout.",
                    new Object[]{selector}));
            }
        }
    }

    public void waitForElementToAppear(BaseHtmlElement element, int waitTime,
        int retryCount, WebDriver wd)
    {
        int count = 0;
        while (!element.exists())
        {
            scrollDown(wd);
            this._syncHelper.suspend(waitTime);
            scrollUp(wd);
            count++;
            if (count > retryCount)
            {
                Assert.fail(String.format(
                    "Element with selector [%s] not found before timeout.",
                    new Object[]{element

                        .getAbsoluteSelector()}));
            }
        }
    }

    public void waitForElementToDisappear(String selector)
    {
        long end = TestHelper.getCurrentGMT6Time() + 60L;
        while (TestHelper.getCurrentGMT6Time() < end)
        {
            WebElement element = this._queryHelper
                .findElementByExtendedCss(selector);
            if (element != null)
            {
                suspend(100);
                if (!element.isDisplayed())
                {
                    break;
                }
                this.logger.info("Waiting for " + selector + " to vanish");
            }
        }
    }

    public void suspend(int millis)
    {
        this.logger.debug("Suspending thread " + millis + "ms.");
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void dragAndDrop(String whatSelector, String whereSelector)
    {
        this.logger.debug("Dragging '" + whatSelector
            + "' to '"
            + whereSelector
            + "'");

        waitForElementWithSelectorAppearanceChange(whatSelector, true);
        waitForElementWithSelectorAppearanceChange(whereSelector, true);
        Actions builder = new Actions(this.driver);
        builder.dragAndDrop(
            this._queryHelper.findElementByExtendedCss(whatSelector),
            this._queryHelper.findElementByExtendedCss(whereSelector));
        builder.perform();
    }

    public void moveMouseTo(AbstractPageChunk element)
    {
        this.logger.debug("Moving the mouse explicitly to "
            + element.getAbsoluteSelector());
        Actions actions = new Actions(this.driver);
        actions.moveToElement(element.getWebElement()).perform();
    }

    public void fireOnClick(UIData element)
    {
        this.logger.debug("Firing 'click' explicitly "
            + element.getAbsoluteSelector());
        waitForElementAppearanceChanged(element, true);
        try
        {
            ((JavascriptExecutor) this.driver).executeScript("jQuery('"
                + element.getAbsoluteSelector() + "').click()",
                new Object[0]);
        }
        catch (WebDriverException e)
        {
            this.logger.error("Webdriver throws exeption"
                + element.getAbsoluteSelector()
                + "' - proceeding without exception");
        }
    }

    public void scrollToElement(String elementPath, WebDriver wd)
    {
        this._queryHelper = new WebElementQueryHelper(wd);
        this.logger.debug("About to scroll to " + elementPath);
        int y = 0;
        try
        {
            this._syncHelper.waitForElementToAppear(elementPath);

            Locatable hoverItem = (Locatable) this._queryHelper
                .findElementByExtendedCss(elementPath);
            y = hoverItem.getCoordinates().onPage().getY();
        }
        catch (StaleElementReferenceException sere)
        {
            this.logger
                .warn("Stale Reference Exception was thrown, refreshing page and trying to get coordinates again");
            wd.navigate().refresh();
            this._syncHelper.waitForElementToAppear(elementPath);
            Locatable hoverItem = (Locatable) wd.findElement(By
                .cssSelector(elementPath));
            y = hoverItem.getCoordinates().onPage().getY();
        }
        ((JavascriptExecutor) wd).executeScript(
            "window.scrollBy(0," + y + ");", new Object[0]);
    }

    public void scrollUp(WebDriver wd)
    {
        this.logger.info("Scrolling to top of page");
        wd.findElement(By.tagName("body")).sendKeys(
            new CharSequence[]{Keys.HOME});
    }

    public void scrollDown(WebDriver wd)
    {
        this.logger.info("Scrolling to bottom of page");
        JavascriptExecutor js = (JavascriptExecutor) wd;
        js.executeScript(
            "window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));",
            new Object[0]);
    }

    public void reload(WebDriver wd)
    {
        wd.navigate().refresh();
    }
}
