
package com.stas.mobile.testing.framework.device;

import io.appium.java_client.AppiumDriver;

public abstract interface DeviceUIData
{
    public abstract AppiumDriver getDriver();

    public abstract String getSelector();
}