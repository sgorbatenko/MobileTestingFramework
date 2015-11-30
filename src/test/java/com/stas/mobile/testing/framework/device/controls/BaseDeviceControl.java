
package com.stas.mobile.testing.framework.device.controls;

import io.appium.java_client.MobileElement;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import com.stas.mobile.testing.framework.device.AbstractDeviceChunk;

public class BaseDeviceControl extends AbstractDeviceChunk
{
    MobileElement element = null;

    public BaseDeviceControl(String selector)
    {
        super(selector);
        this.element = getMobileElement();
    }

    public String getAttributeValue(String name)
    {
        return this.element.getAttribute(name);
    }

    @Override
    public boolean isDisplayed()
    {
        if (this.element == null)
        {
            return false;
        }
        MobileElement mobileElement = getMobileElement();
        return mobileElement.isDisplayed();
    }

    public boolean isEnabled()
    {
        return this.element.isEnabled();
    }

    public Dimension getDimension()
    {
        return this.element.getSize();
    }

    public Point getLocation()
    {
        return this.element.getLocation();
    }
}
