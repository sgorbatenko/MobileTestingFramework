
package com.stas.mobile.testing.framework.util.drivers;

import java.net.URL;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ScreenShotRemoteWebDriver
                                      extends RemoteWebDriver
    implements TakesScreenshot
{
    public ScreenShotRemoteWebDriver(URL url, DesiredCapabilities dc)
    {
        super(url, dc);
    }

    public <X> X getScreenshotAs(OutputType<X> target)
        throws WebDriverException
    {
        if (((Boolean) getCapabilities().getCapability("takesScreenshot")).booleanValue())
        {
            return (X) target.convertFromBase64Png(execute("screenshot")
                .getValue().toString());
        }
        return null;
    }
}
