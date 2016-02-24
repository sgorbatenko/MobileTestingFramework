
package com.stas.mobile.testing.framework.test.listeners;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.stas.mobile.testing.framework.util.drivers.SnapshotManager;
import com.stas.mobile.testing.framework.util.drivers.WebDriverWrapper;
import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;

public class TestListener extends RunListener
{
    private EnvironmentUtil _env = EnvironmentUtil.getInstance();

    // private TestRailLogger testRailLogger = new TestRailLogger();
    private static LogController logger = new LogController(TestListener.class);

    @Override
    public void testRunFinished(Result context)
    {
        if (this._env.getLogATOMResults())
        {
            String failed = String.valueOf(context.getFailureCount());
            String passed = String.valueOf(context.getRunCount() - context.getFailureCount());
            String skipped = String.valueOf(context.getIgnoreCount());

            String url = "";
            // LogController.flushRunLogToS3(
            // WebDriverWrapper.getRunId(), this.env.getCustomerS3BucketName());
            String result = "pass";
            if ((context.getFailureCount() > 0) ||
                (context.getIgnoreCount() > 0))
            {
                result = "fail";
            }
            String externalUrl = "";

            AtomLogger.logTestRunResultToAtom(WebDriverWrapper.getRunId(),
                WebDriverWrapper.runDate,
                result,
                this._env
                    .getCIJobName(),
                passed,
                failed,
                skipped,
                this._env
                    .getBuildId(),
                this._env
                    .getCompanyId(),
                getProductId(),
                getDeviceType(),
                this._env
                    .getBrowser().name(),
                WebDriverWrapper.os,
                WebDriverWrapper.osVersion,
                WebDriverWrapper.manufacturer,
                url,
                externalUrl);
        }
    }

    @Override
    public void testRunStarted(Description description)
    {
        if (this._env.getLogATOMResults())
        {
            AtomLogger.logTestRunResultToAtom(WebDriverWrapper.getRunId(),
                WebDriverWrapper.runDate,
                "",
                this._env
                    .getCIJobName(),
                "0",
                "0",
                "0",
                this._env
                    .getBuildId(),
                this._env.getCompanyId(),
                getProductId(),
                getDeviceType(),
                this._env.getBrowser().name(),
                WebDriverWrapper.os,
                WebDriverWrapper.osVersion,
                WebDriverWrapper.manufacturer,
                "",
                "");
        }
    }

    @Override
    public void testStarted(Description description)
    {
        logger.info("=== Starting test " + description + "===");
    }

    @Override
    public void testFinished(Description description)
    {
        logger.info(String.format("Test Passed [" + description));
        logger.info("=== Completed test " + description + " ===");
    }

    @Override
    public void testFailure(Failure failure)
    {
        try
        {
            if (!this._env.getIsMobileTest().booleanValue())
            {
                SnapshotManager.takeScreenshot(failure.getTestHeader(),
                    WebDriverWrapper.getWebDriver());
            }
            else
            {
                SnapshotManager.takeRemoteDeviceSnapShot("FAILED " + failure.getTestHeader(),
                    WebDriverWrapper.getAppiumDriver());
            }
        }
        catch (Exception exp)
        {
            logger.info("Exception thrown when trying to get screen shot. Resetting Driver.");
            logger.info(exp.getMessage());
            WebDriverWrapper.getNewWebDriver();
        }
        logger.info("Test Result : Fail [" + failure + "]");
        this._env.setFailedTestDoReset(true);

        logger.info("=== Completed test " + failure.getTestHeader() + " ===");
    }

    private void logTestCaseResult(String result, String testName, String testTags, String testDescription,
        String executionTime, String url)
    {
        AtomLogger.logTestCaseResultToAtom(WebDriverWrapper.getRunId(),
            testName,
            testTags,
            testDescription,
            executionTime,
            WebDriverWrapper.runDate,
            result,
            this._env

                .getBuildId(),
            this._env
                .getCompanyId(),
            getProductId(),
            getDeviceType(),
            this._env
                .getBrowser().name(),
            WebDriverWrapper.os,
            WebDriverWrapper.osVersion,
            WebDriverWrapper.manufacturer,
            url);
    }

    private String getDeviceType()
    {
        String deviceType = "";
        if (this._env.getIsMobileIOS())
        {
            deviceType = "mobile and tablet";
        }
        if (this._env.getIsMobileAndroid())
        {
            deviceType = "mobile and tablet";
        }
        else
        {
            deviceType = "web and application";
        }
        return deviceType;
    }

    private String getProductId()
    {
        String productId = "";
        if (this._env.getIsMobileIOS())
        {
            productId = this._env.getProductIOSId();
        }
        if (this._env.getIsMobileAndroid())
        {
            productId = this._env.getProductAndroidId();
        }
        else
        {
            productId = this._env.getProductWebId();
        }
        return productId;
    }
}
