
package com.stas.mobile.testing.framework.device;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.SwipeElementDirection;

import java.util.Set;

import org.openqa.selenium.Dimension;

import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;

public class MobileHelper
{
    private static LogController logger = new LogController(MobileHelper.class);
    private EnvironmentUtil env = EnvironmentUtil.getInstance();

    public static boolean changeContext(String desiredContext, AppiumDriver appDriver)
    {
        try
        {
            Set<String> contextNames = appDriver.getContextHandles();

            logger.info("ContextNames size: " + contextNames.size());
            for (String contextName : contextNames)
            {
                System.out.println(contextName);
                if (contextName.contains(desiredContext))
                {
                    logger.info("Switching to " + desiredContext + " context");
                    appDriver.context(contextName);
                    Thread.currentThread();
                    Thread.sleep(4000L);
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            logger.info("error switching context");
            return false;
        }
        return false;
    }

    public void swipeWithDirection(SwipeDirection direction, MobileElement element, AppiumDriver driver, int swipeDuration)
    {
        switch (direction)
        {
            case RIGHT:
                if (this.env.getIsMobileIOS())
                {
                    element.swipe(SwipeElementDirection.RIGHT, swipeDuration);
                }
                else
                {
                    Dimension size = element.getSize();
                    int startX = (int) (size.width * 0.9D);
                    int endX = (int) (size.width * 0.1D);
                    int startY = size.height / 2;
                    driver.swipe(startX, startY, endX, startY, swipeDuration);
                }
                break;
            case LEFT:
                if (this.env.getIsMobileIOS())
                {
                    element.swipe(SwipeElementDirection.LEFT, swipeDuration);
                }
                else
                {
                    Dimension size = element.getSize();
                    int startX = (int) (size.width * 0.1D);
                    int endX = (int) (size.width * 0.9D);
                    int startY = size.height / 2;
                    driver.swipe(startX, startY, endX, startY, swipeDuration);
                }
                break;
        }
    }
}
