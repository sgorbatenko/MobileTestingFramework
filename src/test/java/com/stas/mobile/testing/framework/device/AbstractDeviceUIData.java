
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
    protected SynchronizationHelper _syncHelper;
    protected DeviceElementQueryHelper _queryHelper;
    private LogController _logger = new LogController(AbstractUIData.class);
    protected EnvironmentUtil _env = EnvironmentUtil.getInstance();
    protected AppiumDriver _driver = WebDriverWrapper.getAppiumDriver();
    protected MobileHelper _mobileHelper;
}
