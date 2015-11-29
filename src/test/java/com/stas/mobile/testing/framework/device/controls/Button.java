
package com.stas.mobile.testing.framework.device.controls;

import com.stas.mobile.testing.framework.util.logger.LogController;

public class Button extends BaseDeviceControl
{
    private LogController logger = new LogController(Button.class);

    public Button(String selector)
    {
        super(selector);
    }

    public void pressButton()
    {
        this.logger.info(String.format("Tapping Button with selector [%].", new Object[]{this._selector
            .toString()}));
        tap();
    }

    public String getButtonString()
    {
        return this.element.getText();
    }
}
