
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
    private EnvironmentUtil env = EnvironmentUtil.getInstance();
    // private TestRailLogger testRailLogger = new TestRailLogger();
    private static SnapshotManager snapshotManager = new SnapshotManager();
    private static TestNGListenerUtils ngListenerUtil = new TestNGListenerUtils();
    private static LogController logger = new LogController(TestListener.class);

    public void onFinish(ITestContext context)
    {
        if (this.env.getLogATOMResults())
        {
            String failed = String.valueOf(context.getFailedTests().size());
            String passed = String.valueOf(context.getPassedTests().size());
            String skipped = String.valueOf(context.getSkippedTests().size());

            String url = "";
//            LogController.flushRunLogToS3(
//                WebDriverWrapper.getRunId(), this.env.getCustomerS3BucketName());
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
                this.env
                    .getCIJobName(),
                passed,
                failed,
                skipped,
                this.env
                    .getBuildId(),
                this.env
                    .getCompanyId(),
                getProductId(),
                getDeviceType(),
                this.env
                    .getBrowser().name(),
                WebDriverWrapper.os,
                WebDriverWrapper.osVersion,
                WebDriverWrapper.manufacturer,
                url,
                externalUrl);
        }
    }

    public void onStart(ITestContext arg0)
    {
        if (this.env.getLogATOMResults())
        {
            AtomLogger.logTestRunResultToAtom(WebDriverWrapper.getRunId(),
                WebDriverWrapper.runDate,
                "",
                this.env
                    .getCIJobName(),
                "0",
                "0",
                "0",
                this.env
                    .getBuildId(),
                this.env.getCompanyId(),
                getProductId(),
                getDeviceType(),
                this.env.getBrowser().name(),
                WebDriverWrapper.os,
                WebDriverWrapper.osVersion,
                WebDriverWrapper.manufacturer,
                "",
                "");
        }
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0)
    {
    }

    public void onTestSkipped(ITestResult testResult)
    {
        ITestNGMethod testNGMethod = testResult.getMethod();
        Method method = testNGMethod.getConstructorOrMethod().getMethod();
        try
        {
            if (!this.env.getIsMobileTest().booleanValue())
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
            TestNGListenerUtils.getCaseDescription(testResult) + "]", new Object[0]));
        if (this.env.getLogATOMResults())
        {
//            String url = LogController.flushMethodLogToS3(
//                WebDriverWrapper.getRunId(), method.getName(), this.env
//                    .getCustomerS3BucketName());

            String testTags = TestNGListenerUtils.getCaseTags(testResult);

            String testDescription = TestNGListenerUtils.getCaseDescription(testResult);
            String executionTime = String.valueOf(testResult.getEndMillis() - testResult
                .getStartMillis());

            logTestCaseResult("skip", method.getName(), testTags, testDescription, executionTime, "");
        }
        logger.info("=== Completed test " + method.getName() + " ===");
    }

    public void onTestStart(ITestResult testResult)
    {
        logger.info("=== Starting test " + testResult.getName() + "===");
    }

    public void onTestSuccess(ITestResult testResult)
    {
        if (this.env.getLogResults().booleanValue())
        {
//            this.testRailLogger.logResult(this.env.getTestRunId(),
//                TestNGListenerUtils.getCaseDescription(testResult), 1);
        }
        ITestNGMethod testNGMethod = testResult.getMethod();
        Method method = testNGMethod.getConstructorOrMethod().getMethod();
        try
        {
            if (!this.env.getIsMobileTest().booleanValue())
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
            TestNGListenerUtils.getCaseDescription(testResult) + "]", new Object[0]));
        if (this.env.getLogATOMResults())
        {
//            String url = LogController.flushMethodLogToS3(
//                WebDriverWrapper.getRunId(), method.getName(), this.env
//                    .getCustomerS3BucketName());

            String testTags = TestNGListenerUtils.getCaseTags(testResult);

            String testDescription = TestNGListenerUtils.getCaseDescription(testResult);
            String executionTime = String.valueOf(testResult.getEndMillis() - testResult
                .getStartMillis());

            logTestCaseResult("pass", method.getName(), testTags, testDescription, executionTime, "");
        }
        logger.info("=== Completed test " + method.getName() + " ===");
    }

    public void onTestFailure(ITestResult testResult)
    {
        if (this.env.getLogResults().booleanValue())
        {
//            this.testRailLogger.logResult(this.env.getTestRunId(),
//                TestNGListenerUtils.getCaseDescription(testResult), 5);
        }
        ITestNGMethod testNGMethod = testResult.getMethod();
        Method method = testNGMethod.getConstructorOrMethod().getMethod();
        try
        {
            if (!this.env.getIsMobileTest().booleanValue())
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
            TestNGListenerUtils.getCaseDescription(testResult) + "]");
        this.env.setFailedTestDoReset(true);
        if (this.env.getLogATOMResults())
        {
//            String url = LogController.flushMethodLogToS3(
//                WebDriverWrapper.getRunId(), method.getName(), this.env
//                    .getCustomerS3BucketName());

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
            this.env

                .getBuildId(),
            this.env
                .getCompanyId(),
            getProductId(),
            getDeviceType(),
            this.env
                .getBrowser().name(),
            WebDriverWrapper.os,
            WebDriverWrapper.osVersion,
            WebDriverWrapper.manufacturer,
            url);
    }

    private String getDeviceType()
    {
        String deviceType = "";
        if (this.env.getIsMobileIOS())
        {
            deviceType = "mobile and tablet";
        }
        if (this.env.getIsMobileAndroid())
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
        if (this.env.getIsMobileIOS())
        {
            productId = this.env.getProductIOSId();
        }
        if (this.env.getIsMobileAndroid())
        {
            productId = this.env.getProductAndroidId();
        }
        else
        {
            productId = this.env.getProductWebId();
        }
        return productId;
    }
}
