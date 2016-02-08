
package com.stas.mobile.testing.framework.test.listeners;

import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.stas.mobile.testing.framework.util.drivers.SnapshotManager;
import com.stas.mobile.testing.framework.util.drivers.WebDriverWrapper;
import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;

public class TestListener extends TestListenerAdapter
{
    private EnvironmentUtil _env = EnvironmentUtil.getInstance();

    // private TestRailLogger testRailLogger = new TestRailLogger();
    private static LogController logger = new LogController(TestListener.class);

    @Override
    public void onFinish(ITestContext context)
    {
        if (this._env.getLogATOMResults())
        {
            String failed = String.valueOf(context.getFailedTests().size());
            String passed = String.valueOf(context.getPassedTests().size());
            String skipped = String.valueOf(context.getSkippedTests().size());

            String url = "";
            // LogController.flushRunLogToS3(
            // WebDriverWrapper.getRunId(), this.env.getCustomerS3BucketName());
            String result = "pass";
            if ((context.getFailedTests().size() > 0) ||
                (context.getSkippedTests().size() > 0))
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
    public void onStart(ITestContext arg0)
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
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0)
    {
    }

    @Override
    public void onTestSkipped(ITestResult testResult)
    {
        ITestNGMethod testNGMethod = testResult.getMethod();
        Method method = testNGMethod.getConstructorOrMethod().getMethod();
        try
        {
            if (!this._env.getIsMobileTest().booleanValue())
            {
                SnapshotManager.takeScreenshot(method.getName(),
                    WebDriverWrapper.getWebDriver());
            }
            else
            {
                SnapshotManager.takeRemoteDeviceSnapShot("SKIPPED_" + method
                    .getName(),
                    WebDriverWrapper.getAppiumDriver());
            }
        }
        catch (Exception exp)
        {
            logger.info("Exception thrown when trying to get screen shot. Resetting Driver.");
            logger.info(exp.getMessage());
            WebDriverWrapper.getNewWebDriver();
        }
        logger.info(String.format("Test Skipped [" +
            TestNGListenerUtils.getCaseDescription(testResult)
            + "]", new Object[0]));
        if (this._env.getLogATOMResults())
        {
            // String url = LogController.flushMethodLogToS3(
            // WebDriverWrapper.getRunId(), method.getName(), this.env
            // .getCustomerS3BucketName());

            String testTags = TestNGListenerUtils.getCaseTags(testResult);

            String testDescription = TestNGListenerUtils.getCaseDescription(testResult);
            String executionTime = String.valueOf(testResult.getEndMillis() - testResult
                .getStartMillis());

            logTestCaseResult("skip", method.getName(), testTags, testDescription, executionTime, "");
        }
        logger.info("=== Completed test " + method.getName() + " ===");
    }

    @Override
    public void onTestStart(ITestResult testResult)
    {
        logger.info("=== Starting test " + testResult.getName() + "===");
    }

    @Override
    public void onTestSuccess(ITestResult testResult)
    {
        if (this._env.getLogResults().booleanValue())
        {
            // this.testRailLogger.logResult(this.env.getTestRunId(),
            // TestNGListenerUtils.getCaseDescription(testResult), 1);
        }
        ITestNGMethod testNGMethod = testResult.getMethod();
        Method method = testNGMethod.getConstructorOrMethod().getMethod();
        try
        {
            if (!this._env.getIsMobileTest().booleanValue())
            {
                SnapshotManager.takeScreenshot(method.getName(),
                    WebDriverWrapper.getWebDriver());
            }
            else
            {
                SnapshotManager.takeRemoteDeviceSnapShot("PASSED" + method
                    .getName(),
                    WebDriverWrapper.getAppiumDriver());
            }
        }
        catch (Exception exp)
        {
            logger.info("Exception thrown when trying to get screen shot. Resetting Driver.");
            logger.info(exp.getMessage());

            WebDriverWrapper.getNewWebDriver();
        }
        logger.info(String.format("Test Passed [" +
            TestNGListenerUtils.getCaseDescription(testResult)
            + "]", new Object[0]));
        if (this._env.getLogATOMResults())
        {
            // String url = LogController.flushMethodLogToS3(
            // WebDriverWrapper.getRunId(), method.getName(), this.env
            // .getCustomerS3BucketName());

            String testTags = TestNGListenerUtils.getCaseTags(testResult);

            String testDescription = TestNGListenerUtils.getCaseDescription(testResult);
            String executionTime = String.valueOf(testResult.getEndMillis() - testResult
                .getStartMillis());

            logTestCaseResult("pass", method.getName(), testTags, testDescription, executionTime, "");
        }
        logger.info("=== Completed test " + method.getName() + " ===");
    }

    @Override
    public void onTestFailure(ITestResult testResult)
    {
        if (this._env.getLogResults().booleanValue())
        {
            // this.testRailLogger.logResult(this.env.getTestRunId(),
            // TestNGListenerUtils.getCaseDescription(testResult), 5);
        }
        ITestNGMethod testNGMethod = testResult.getMethod();
        Method method = testNGMethod.getConstructorOrMethod().getMethod();
        try
        {
            if (!this._env.getIsMobileTest().booleanValue())
            {
                SnapshotManager.takeScreenshot(method.getName(),
                    WebDriverWrapper.getWebDriver());
            }
            else
            {
                SnapshotManager.takeRemoteDeviceSnapShot("FAILED" + method
                    .getName(),
                    WebDriverWrapper.getAppiumDriver());
            }
        }
        catch (Exception exp)
        {
            logger.info("Exception thrown when trying to get screen shot. Resetting Driver.");
            logger.info(exp.getMessage());
            WebDriverWrapper.getNewWebDriver();
        }
        logger.info("Test Result : Fail [" +
            TestNGListenerUtils.getCaseDescription(testResult)
            + "]");
        this._env.setFailedTestDoReset(true);
        if (this._env.getLogATOMResults())
        {
            // String url = LogController.flushMethodLogToS3(
            // WebDriverWrapper.getRunId(), method.getName(), this.env
            // .getCustomerS3BucketName());

            String testTags = TestNGListenerUtils.getCaseTags(testResult);

            String testDescription = TestNGListenerUtils.getCaseDescription(testResult);
            String executionTime = String.valueOf(testResult.getEndMillis() - testResult
                .getStartMillis());

            logTestCaseResult("fail", method.getName(), testTags, testDescription, executionTime, "");
        }
        logger.info("=== Completed test " + method.getName() + " ===");
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
