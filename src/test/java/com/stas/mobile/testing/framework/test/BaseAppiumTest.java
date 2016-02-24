
package com.stas.mobile.testing.framework.test;

import java.net.URL;

import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.stas.mobile.testing.framework.device.DeviceControl;
import com.stas.mobile.testing.framework.queryhelpers.DeviceElementQueryHelper;
import com.stas.mobile.testing.framework.synchronization.SynchronizationHelper;
import com.stas.mobile.testing.framework.test.listeners.TestListener;
import com.stas.mobile.testing.framework.util.drivers.WebDriverWrapper;
import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;

import io.appium.java_client.AppiumDriver;

public class BaseAppiumTest extends TestListener
{
    protected static AppiumDriver driver;
    protected static SynchronizationHelper syncHelper;
    protected EnvironmentUtil env = EnvironmentUtil.getInstance();
    protected static LogController logger = new LogController(
        BaseAppiumTest.class);
    private static WebDriverWrapper deviceWrapper;
    private DeviceControl deviceControl = new DeviceControl();
    protected static DeviceElementQueryHelper queryHelper;

    public BaseAppiumTest()
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL log = null;
        if (this.env.turnOnDebug().booleanValue())
        {
            log = loader.getResource("log4j-debug.properties");
        }
        else
        {
            log = loader.getResource("log4j.properties");
        }
        PropertyConfigurator.configure(log);
    }

    @BeforeClass
    public static void setupClass()
    {
        logger.info("Starting Before Class Setup.");
        driver = WebDriverWrapper.getDeviceDriver();
        queryHelper = new DeviceElementQueryHelper(driver);
        syncHelper = new SynchronizationHelper(driver);
        logger.info("Completed before class setup work - Created appium driver");
    }

    @AfterClass
    public static void afterClass()
    {
        WebDriverWrapper.closeApplicationSession();
    }
}
