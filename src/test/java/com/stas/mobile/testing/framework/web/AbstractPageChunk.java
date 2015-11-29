
package com.stas.mobile.testing.framework.web;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.stas.mobile.testing.framework.AbstractUIData;
import com.stas.mobile.testing.framework.UIData;
import com.stas.mobile.testing.framework.queryhelpers.WebElementQueryHelper;
import com.stas.mobile.testing.framework.synchronization.AjaxHelper;
import com.stas.mobile.testing.framework.util.drivers.WebDriverWrapper;
import com.stas.mobile.testing.framework.util.logger.LogController;

public abstract class AbstractPageChunk extends AbstractUIData
{
    public static final int DEFAULT_TIMEOUT = 60;
    protected static UIData parent;
    protected String selector;
    protected WebElementQueryHelper queryHelper;
    protected AjaxHelper syncHelper;
    protected LogController logger = new LogController(AbstractPageChunk.class);

    public AbstractPageChunk(UIData parent, String selector)
    {
        parent = parent;
        this.selector = selector;
        this.logger.debug("Selector = " + selector);
        WebDriver driver = WebDriverWrapper.getWebDriver();
        this.queryHelper = new WebElementQueryHelper(driver);
        this.syncHelper = new AjaxHelper(driver);
        parent.addChild(this);
    }

    public WebElement getWebElement()
    {
        this.logger.debug("getWebElement(): " + getAbsoluteSelector());
        final AbstractPageChunk that = this;
        WebDriverWait wait = new WebDriverWait(getDriver(), 60L);
        Function<WebDriver, WebElement> function = new Function()
        {
            public Object apply(Object input)
            {
                return (WebElement) AbstractPageChunk.this.getWebElementImmediately(that);
            }
        };
        try
        {
            return (WebElement) wait.until(function);
        }
        catch (TimeoutException e)
        {
            throw new RuntimeException("Timeout for element: " + getAbsoluteSelector(), e);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            throw new RuntimeException("Exception getting element: " + getAbsoluteSelector(), e);
        }
    }

    public WebElement getVisibleWebElement(int defaultTimeout)
    {
        this.logger.debug("Getting visible element: " + getAbsoluteSelector());
        WebDriverWait wait = new WebDriverWait(getDriver(), defaultTimeout);
        final AbstractPageChunk that = this;
        Function<WebDriver, WebElement> isVisible = new Function()
        {
            public Object apply(Object input)
            {
                WebElement webElement = AbstractPageChunk.this.getWebElementImmediately(that);
                if ((webElement != null) && (webElement.isDisplayed()))
                {
                    return webElement;
                }
                return null;
            }
        };
        try
        {
            this.logger.debug("In getVisibleWebElement and absolute selector = " +
                getAbsoluteSelector());
            return (WebElement) wait.until(isVisible);
        }
        catch (TimeoutException e)
        {
            throw new RuntimeException("Timeout for element: " + getAbsoluteSelector(), e);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Exception getting element: " + getAbsoluteSelector(), e);
        }
    }

    public boolean exists()
    {
        try
        {
            return getWebElementImmediately(this) != null;
        }
        catch (NoSuchElementException e)
        {
            this.logger.debug("NoSuchElementException returned.");
        }
        return false;
    }

    public boolean visible()
    {
        return getWebElementImmediately(this).isDisplayed();
    }

    public String getSelector()
    {
        return this.selector;
    }

    public WebDriver getDriver()
    {
        return WebDriverWrapper.getWebDriver();
    }

    public UIData getParent()
    {
        return parent;
    }

    public void setSelector(String newBaseSelector)
    {
        this.selector = newBaseSelector;
    }

    public AbstractPage getBaseParent()
    {
        UIData parent = getParent();
        for (;;)
        {
            if (parent.getParent() == null)
            {
                return (AbstractPage) parent;
            }
            parent = parent.getParent();
        }
    }

    public String toString()
    {
        return "Selector: " + getSelector();
    }

    public void focus()
    {
        this.logger.debug("Focusing on element: " + getAbsoluteSelector());
        ((JavascriptExecutor) getDriver()).executeScript("jQuery('" +
            getAbsoluteSelector().trim() + "').focus()", new Object[0]);
    }

    public void hover()
    {
        this.logger.debug("Hovering on element: " + getAbsoluteSelector());
        Actions builder = new Actions(getDriver());
        builder.moveToElement(getWebElement()).perform();
    }

    public void click()
    {
        WebElement element = getVisibleWebElement(60);
        this.syncHelper.suspend(100);
        try
        {
            element.click();
        }
        catch (StaleElementReferenceException e)
        {
            this.logger.info("StateElementReferenceException thrown, attempting to reattach");

            getVisibleWebElement(60).click();
        }
    }

    private WebElement getWebElementImmediately(AbstractPageChunk chunk)
    {
        return this.queryHelper.findElement(chunk);
    }
}
