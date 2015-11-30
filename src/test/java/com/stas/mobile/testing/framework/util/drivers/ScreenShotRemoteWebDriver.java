
package com.stas.mobile.testing.framework.util.drivers;

import java.net.URL;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ScreenShotRemoteWebDriver extends RemoteWebDriver
{
    public ScreenShotRemoteWebDriver(URL url, DesiredCapabilities dc)
    {
        super(url, dc);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target)
        throws WebDriverException
    {
        if (((Boolean) getCapabilities().getCapability("takesScreenshot")).booleanValue())
        {
            return target.convertFromBase64Png(execute("screenshot")
                .getValue().toString());
        }
        return null;
    }
}
