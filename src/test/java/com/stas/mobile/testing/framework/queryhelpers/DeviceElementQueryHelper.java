
package com.stas.mobile.testing.framework.queryhelpers;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;

public class DeviceElementQueryHelper
{
    private LogController logger = new LogController(DeviceElementQueryHelper.class);
    private EnvironmentUtil env = EnvironmentUtil.getInstance();
    private IOSDriver iosDriver;
    private AndroidDriver androidDriver;

    public DeviceElementQueryHelper(Object driver)
    {
        if (this.env.getIsMobileIOS())
        {
            this.iosDriver = ((IOSDriver) driver);
        }
        else
        {
            this.androidDriver = ((AndroidDriver) driver);
        }
    }

    public MobileElement findElement(String selector)
    {
        if (this.env.getIsMobileIOS())
        {
            if (selector.contains("//"))
            {
                this.logger.debug(
                    String.format("Found IOS Xpath Selector [%s], using findElementByXPath.", new Object[]{selector}));

                return (MobileElement) this.iosDriver.findElementByXPath(selector);
            }
            if (selector.startsWith("."))
            {
                this.logger.debug(
                    String.format("Found IOS UIAutomator Selector [%s], using findElementByIosUIAutomation.",
                        new Object[]{selector}));

                return (MobileElement) this.iosDriver.findElementByIosUIAutomation(selector);
            }
            if (selector.startsWith("$"))
            {
                selector = selector.replace("$", "");
                this.logger.debug(
                    String.format("Found clas Name selector [%s], using findElement(By.className()).", new Object[]{selector}));

                return (MobileElement) this.iosDriver.findElement(
                    By.className(selector));
            }
            this.logger.debug(String.format("Found IOS id Selector [%s], using findElementById.", new Object[]{selector}));

            return (MobileElement) this.iosDriver.findElementById(selector);
        }
        if (selector.contains("UiSelector"))
        {
            this.logger.debug(
                String.format("Found UiSelector in locator [%s], using findElementByAndroidUIAutomator.",
                    new Object[]{selector}));

            return (MobileElement) this.androidDriver.findElementByAndroidUIAutomator(selector);
        }
        if (selector.contains("//"))
        {
            this.logger.debug(
                String.format("Found Android Xpath Selector [%s], using findElementByXPath.", new Object[]{selector}));

            return (MobileElement) this.androidDriver.findElementByXPath(selector);
        }
        if (selector.contains("~"))
        {
            selector = selector.replace("~", "");
            this.logger.debug(
                String.format("Found Android name Selector [%s], using findElementByName.", new Object[]{selector}));

            return (MobileElement) this.androidDriver.findElementByName(selector);
        }
        this.logger.debug(String.format("Attempted to find locator [%s] ById.", new Object[]{selector}));

        return (MobileElement) this.androidDriver.findElementById(selector);
    }

    public List<MobileElement> findElements(String selector)
    {
        if (this.env.getIsMobileIOS())
        {
            this.logger.debug(
                String.format("Found IOS UIAutomator Selector [%s], using findElementByIosUIAutomation.",
                    new Object[]{selector}));

            List<MobileElement> elements = (List) this.iosDriver.findElementByIosUIAutomation(selector);
            return elements;
        }
        if (selector.contains("UiSelector"))
        {
            selector = "new UiSelector().resourceId(\"" + selector + "\")";
        }
        this.logger.info(
            String.format("Selector is a Android UiSelector [%s], using findByAndroidUIAutomator.", new Object[]{selector}));

        List<MobileElement> elements = (List) this.androidDriver.findElementByAndroidUIAutomator(selector);
        return elements;
    }

    public int getAndroidElementCount(String selector)
    {
        return getMobileElementCount(selector);
    }

    public int getMobileElementCount(String selector)
    {
        List<WebElement> elements = null;
        if (this.env.getIsMobileIOS())
        {
            if (selector.contains("//"))
            {
                this.logger.debug(
                    String.format("Found IOS Xpath Selector [%s], using findElementsByXPath.", new Object[]{selector}));

                elements = this.iosDriver.findElementsByXPath(selector);
            }
            else if (selector.startsWith("."))
            {
                this.logger.debug(
                    String.format("Found IOS UIAutomator Selector [%s], using findElementsByIosUIAutomation.",
                        new Object[]{selector}));

                elements = this.iosDriver.findElementsByIosUIAutomation(selector);
            }
            else if (selector.startsWith("$"))
            {
                selector = selector.replace("$", "");
                this.logger.debug(
                    String.format("Found clas Name selector [%s], using findElements(By.className()).", new Object[]{selector}));

                elements = this.iosDriver.findElements(By.className(selector));
            }
            else
            {
                this.logger.debug(String.format("Found IOS id Selector [%s], using findElementsById.", new Object[]{selector}));

                elements = this.iosDriver.findElementsById(selector);
            }
        }
        else if (selector.contains("UiSelector"))
        {
            this.logger.debug(
                String.format("Found UiSelector in locator [%s], using findElementsByAndroidUIAutomator.",
                    new Object[]{selector}));

            elements = this.androidDriver.findElementsByAndroidUIAutomator(selector);
        }
        else if (selector.contains("//"))
        {
            this.logger.debug(
                String.format("Found Android Xpath Selector [%s], using findElementsByXPath.", new Object[]{selector}));

            elements = this.androidDriver.findElementsByXPath(selector);
        }
        else if (selector.contains("~"))
        {
            selector = selector.replace("~", "");
            this.logger.debug(
                String.format("Found Android name Selector [%s], using findElementsByName.", new Object[]{selector}));

            elements = this.androidDriver.findElementsByName(selector);
        }
        else
        {
            this.logger.debug(String.format("Attempted to find locator [%s] ById.", new Object[]{selector}));

            elements = this.androidDriver.findElementsById(selector);
        }
        return elements.size();
    }

    public boolean doesElementExist(String selector)
    {
        try
        {
            if (this.env.getIsMobileIOS())
            {
                this.logger.info("Checking to see if the element exists");
                if (selector.contains("//"))
                {
                    this.iosDriver.findElementByXPath(selector);
                }
                else
                {
                    this.iosDriver.findElementById(selector);
                }
                return true;
            }
            if (selector.contains("UiSelector"))
            {
                this.logger.debug(
                    String.format("Found UiSelector in locator [%s], using findElementByAndroidUIAutomator.",
                        new Object[]{selector}));

                this.androidDriver.findElementByAndroidUIAutomator(selector);
            }
            else
            {
                this.logger.debug(String.format("Attempted to find locator [%s] ById.", new Object[]{selector}));

                this.androidDriver.findElementById(selector);
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
}
