
package com.stas.mobile.testing.framework;

import java.util.Set;

import org.openqa.selenium.WebDriver;

public abstract interface UIData
{
    public abstract WebDriver getDriver();

    public abstract Set<UIData> getChildren();

    public abstract UIData getParent();

    public abstract void addChild(UIData paramUIData);

    public abstract String getSelector();

    public abstract String getAbsoluteSelector();
}
