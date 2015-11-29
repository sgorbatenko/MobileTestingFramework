
package com.stas.mobile.testing.framework.device;

import io.appium.java_client.AppiumDriver;

import com.stas.mobile.testing.framework.AbstractUIData;
import com.stas.mobile.testing.framework.queryhelpers.DeviceElementQueryHelper;
import com.stas.mobile.testing.framework.synchronization.SynchronizationHelper;
import com.stas.mobile.testing.framework.util.drivers.WebDriverWrapper;
import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;

public abstract class AbstractDeviceUIData implements DeviceUIData
{
    protected SynchronizationHelper syncHelper;
    protected DeviceElementQueryHelper queryHelper;
    private LogController logger = new LogController(AbstractUIData.class);
    protected EnvironmentUtil env = EnvironmentUtil.getInstance();
    protected AppiumDriver driver = WebDriverWrapper.getAppiumDriver();
    protected MobileHelper mobileHelper;
}
