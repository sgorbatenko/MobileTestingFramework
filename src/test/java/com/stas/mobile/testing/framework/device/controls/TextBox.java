
package com.stas.mobile.testing.framework.device.controls;

import io.appium.java_client.TouchAction;

import com.stas.mobile.testing.framework.util.drivers.WebDriverWrapper;
import com.stas.mobile.testing.framework.util.logger.LogController;

public class TextBox
                    extends BaseDeviceControl
{
    private LogController logger = new LogController(TextBox.class);

    public TextBox(String selector)
    {
        super(selector);
    }

    public void enterText(String value)
    {
        this.logger.info(String.format("Entering [%s] into TextBox with selector [%s].", new Object[]{value, this._selector

            .toString()}));
        if (this._env.getIsMobileIOS())
        {
            this.element.setValue(value);
        }
        else
        {
            this.element.sendKeys(new CharSequence[]{value});
        }
    }

    public void clearTextBoxWithTip(String tip)
    {
        this.logger.info(String.format("Clearing textbox with selector [%s].", new Object[]{this._selector
            .toString()}));
        if (this._env.getIsMobileIOS())
        {
            this.element.clear();
        }
        else
        {
            this.element.click();
            while (this.element.getAttribute("name").length() > 0)
            {
                this._driver.sendKeyEvent(67);
                this.logger.debug(String.format("Clearing current value [%s] with length [%d].", new Object[]{this.element

                    .getAttribute("name"),
                    Integer.valueOf(this.element.getAttribute("name").length())}));
                if (this.element.getAttribute("name").equals(tip))
                {
                    break;
                }
            }
        }
    }

    public void clearTextBox()
    {
        this.logger.info(String.format("Clearing textbox with selector [%s].", new Object[]{this._selector
            .toString()}));
        TouchAction action = new TouchAction(WebDriverWrapper.getAppiumDriver());
        if (this._env.getIsMobileIOS())
        {
            this.element.clear();
        }
        else
        {
            this.element.click();
            this._driver.performTouchAction(action.longPress(this.element));
            this._driver.sendKeyEvent(67);
        }
    }

    public String getCurrentText()
    {
        this.logger.info(String.format("Returning value from textbox with selector [%s].", new Object[]{this._selector

            .toString()}));
        return this.element.getText();
    }

    public void clickTextBox()
    {
        this.logger.info(String.format("Clicking textbox with selector [%s].", new Object[]{this._selector
            .toString()}));
        this.element.click();
    }
}
