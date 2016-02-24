
package com.stas.mobile.testing.framework.device.controls;

import com.stas.mobile.testing.framework.util.logger.LogController;

public class Image extends BaseDeviceControl
{
    protected LogController logger = new LogController(Image.class);

    public Image(String selector)
    {
        super(selector);
    }

    public void clickImage()
    {
        this.logger.info(String.format("Clicking image with selector [%s].", new Object[]{this._selector}));
        this.element.click();
    }

    public String getSrcUrl()
    {
        return this.element.getAttribute("src");
    }

    public String getTitle()
    {
        return this.element.getAttribute("title");
    }

    public String getWidth()
    {
        return this.element.getAttribute("width");
    }

    public String getHeight()
    {
        return this.element.getAttribute("height");
    }
}
