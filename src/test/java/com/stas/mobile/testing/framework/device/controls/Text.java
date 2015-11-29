
package com.stas.mobile.testing.framework.device.controls;

import com.stas.mobile.testing.framework.util.logger.LogController;

public class Text
                 extends BaseDeviceControl
{
    private LogController logger = new LogController(Text.class);

    public Text(String selector)
    {
        super(selector);
    }

    public String getStringValue()
    {
        this.logger.info(String.format("Returning string value [%s] of element with locator [%s].", new Object[]{this.element

            .getAttribute("name"), this.element}));
        return this.element.getText();
    }

    public void clickText()
    {
        this.logger.info(String.format("Clicking Text Element with selector [%s].", new Object[]{this.selector
            .toString()}));
        this.element.click();
    }

    public boolean isTextDisplayed()
    {
        return this.element.isDisplayed();
    }
}
