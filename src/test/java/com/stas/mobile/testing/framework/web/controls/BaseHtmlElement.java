
package com.stas.mobile.testing.framework.web.controls;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import com.stas.mobile.testing.framework.UIData;
import com.stas.mobile.testing.framework.web.AbstractPageChunk;

public class BaseHtmlElement extends AbstractPageChunk
{
    public static final String CLASS_ATTRIBUTE = "class";

    public BaseHtmlElement(UIData parent, String selector)
    {
        super(parent, selector);
    }

    public String getAttributeValue(String name)
    {
        return getWebElement().getAttribute(name);
    }

    public Boolean isDisplayed()
    {
        if (getWebElement() == null)
        {
            return Boolean.valueOf(false);
        }
        WebElement element = getWebElement();
        try
        {
            return Boolean.valueOf(element.isDisplayed());
        }
        catch (StaleElementReferenceException se)
        {
            this._logger.debug("Stale Element - try grabbing element again.");
            element = this._queryHelper.findElementByExtendedCss(
                getAbsoluteSelector());
        }
        return Boolean.valueOf(element.isDisplayed());
    }

    public Boolean isEnabled()
    {
        WebElement element = getWebElement();
        return Boolean.valueOf(element.isEnabled());
    }

    public void waitToChangeVisibility(boolean appear)
    {
        this._syncHelper.waitForElementAppearanceChanged(this, appear);
    }

    public void submit()
    {
        WebElement button = getWebElement();
        button.submit();
    }

    public Dimension getDimension()
    {
        WebElement element = getWebElement();
        return element.getSize();
    }

    public Point getLocation()
    {
        WebElement element = getWebElement();
        return element.getLocation();
    }

    public boolean elementHasFocus()
    {
        // WebElement element = getWebElement();
        return this._queryHelper.doesElementExist(getAbsoluteSelector() + ":focus");
    }
}
