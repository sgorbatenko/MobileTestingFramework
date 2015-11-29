
package com.stas.mobile.testing.framework.device;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.stas.mobile.testing.framework.queryhelpers.DeviceElementQueryHelper;
import com.stas.mobile.testing.framework.synchronization.SynchronizationHelper;
import com.stas.mobile.testing.framework.util.drivers.SnapshotManager;
import com.stas.mobile.testing.framework.util.drivers.WebDriverWrapper;
import com.stas.mobile.testing.framework.util.logger.LogController;

public abstract class AbstractDeviceChunk extends AbstractDeviceUIData
{
    protected String selector;
    protected SnapshotManager snapshotManager = new SnapshotManager();
    private LogController logger = new LogController(AbstractDeviceChunk.class);

    public AbstractDeviceChunk(String selector)
    {
        this.selector = selector;
        this.queryHelper = new DeviceElementQueryHelper(this.driver);
        this.syncHelper = new SynchronizationHelper(this.driver);
        this.mobileHelper = new MobileHelper();
    }

    public MobileElement getMobileElement()
    {
        final AbstractDeviceChunk that = this;
        WebDriverWait wait = new WebDriverWait(getDriver(), 30L);

        Function<WebDriver, MobileElement> function = new Function()
        {
//            public MobileElement apply(WebDriver input)
//            {
//                return AbstractDeviceChunk.this.getMobileElementImmediately(that);
//            }

            public Object apply(Object input)
            {
                return (MobileElement) AbstractDeviceChunk.this.getMobileElementImmediately(that);
            }
        };
        try
        {
            return (MobileElement) wait.until(function);
        }
        catch (TimeoutException te)
        {
            throw new RuntimeException("Timeout for element: " + getSelector(), te);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Exception getting element: " + getSelector(), e);
        }
    }

    public List<MobileElement> getMobileElements()
    {
        final AbstractDeviceChunk that = this;
        WebDriverWait wait = new WebDriverWait(getDriver(), 30L);

        ExpectedCondition<List<MobileElement>> condition = new ExpectedCondition()
        {
//            public List<MobileElement> apply(WebDriver input)
//            {
//                return AbstractDeviceChunk.this.getListOfMobileElementImmediately(that);
//            }

            public Object apply(Object input)
            {
                return (List<MobileElement>) AbstractDeviceChunk.this.getListOfMobileElementImmediately(that);
            }
        };
        try
        {
            return (List) wait.until(condition);
        }
        catch (TimeoutException te)
        {
            throw new RuntimeException("Timeout for elements: " + getSelector(), te);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Exception getting elements: " + getSelector(), e);
        }
    }

    public MobileElement getVisibleMobileElement(int defaultTimeout)
    {
        WebDriverWait wait = new WebDriverWait(getDriver(), defaultTimeout);
        final AbstractDeviceChunk that = this;
        Function<WebDriver, MobileElement> isVisible = new Function()
        {
            public Object apply(Object input)
            {
                MobileElement mobileElement = AbstractDeviceChunk.this.getMobileElementImmediately(that);
                if ((mobileElement != null) && (mobileElement.isDisplayed()))
                {
                    return (MobileElement) mobileElement;
                }
                return null;
            }
        };
        try
        {
            return (MobileElement) wait.until(isVisible);
        }
        catch (TimeoutException te)
        {
            throw new RuntimeException("Timeout for element: " + getSelector(), te);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Exception getting element: " + getSelector(), e);
        }
    }

    public boolean exists()
    {
        try
        {
            return getMobileElementImmediately(this) != null;
        }
        catch (NoSuchElementException e)
        {
            this.logger.debug("NoSuchElementException returned.");
        }
        return false;
    }

    public boolean isDisplayed()
    {
        if (exists())
        {
            return getMobileElementImmediately(this).isDisplayed();
        }
        return false;
    }

    public String getSelector()
    {
        return this.selector;
    }

    public AppiumDriver getDriver()
    {
        return WebDriverWrapper.getAppiumDriver();
    }

    public String toString()
    {
        return "Selector: " + getSelector();
    }

    public void tap()
    {
        MobileElement element = getVisibleMobileElement(30000);
        this.syncHelper.suspend(100);
        try
        {
            element.click();
        }
        catch (StaleElementReferenceException e)
        {
            e.printStackTrace();

            getVisibleMobileElement(30000)
                .click();
        }
    }

    private MobileElement getMobileElementImmediately(AbstractDeviceChunk chunk)
    {
        this.logger.debug(String.format("In AbstractDeviceChunk, about to call DEQH with [%s]", new Object[]{this.selector}));

        return this.queryHelper.findElement(chunk.getSelector());
    }

    private List<MobileElement> getListOfMobileElementImmediately(AbstractDeviceChunk chunk)
    {
        return this.queryHelper.findElements(chunk.getSelector());
    }
}
