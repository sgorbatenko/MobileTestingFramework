
package com.stas.mobile.testing.framework.util.environment;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;

import com.stas.mobile.testing.framework.util.logger.LogController;

public class EnvironmentUtil
{
    private static LogController logger;
    private static String customerS3BucketName;
    private static Browser browser;
    private static String deviceName;
    private static String platformVersion;
    private static String url;
    private static String testPlanId;
    private static String testRunId;
    private static String snapshotDirectory;
    private static Boolean takeSnapShots = Boolean.valueOf(true);
    private static Boolean autoAcceptAlerts = Boolean.valueOf(true);
    private static Boolean setLogToDebug = Boolean.valueOf(false);
    private static Boolean logResults = Boolean.valueOf(false);
    private static Boolean failedTestDoReset = Boolean.valueOf(false);
    private static String webDriverGridUrl = "";
    private static Boolean isMobileTest = Boolean.valueOf(false);
    private static int appThwackProjectId = 0;
    private static String appThwackApiKey = "";
    private static int appThwackAppId = 0;
    private static String appiumGridUrl;
    private static String appPackage;
    private static String appActivity;
    private static String testDroidUserName = "";
    private static String testDroidPassword = "";
    private static String testDroidProjectName = "";
    private static String testDroidProjectDescription = "";
    private static String testDroidTestRun = "";
    private static String testDroidServer = "";
    private static String targetAppPath = "";
    private static String testGroup = "";
    private static boolean rawCssOnly = false;
    private static String previousWindowHandle = "";
    private static boolean isMobileIOS = false;
    private static boolean isMobileAndroid = false;
    private static boolean isTablet = false;
    private static boolean isSmallTablet = false;
    private static boolean isPhablet = false;
    private static boolean isSimulator = false;
    private static boolean isPhone = false;
    private static boolean isAndroidTablet = false;
    private static boolean useKeystore = false;
    private static String keystorePath = "";
    private static String keystorePassword = "";
    private static String keyAlias = "";
    private static String keyPassword = "";
    private static String browserStackUserName = "";
    private static String browserStackKey = "";
    private static String iosBundleID = "";
    private static String iosUdid = "";
    private static boolean noSign = false;
    private static boolean isJenkinsRun = false;
    private static String jenkinsURL = "";
    private static boolean isRemoteTestRun = false;
    private static String dataProvider = "null";
    private static String testRailUrl = "";
    private static String testRailUserName = "";
    private static String testRailPassword = "";
    private static Map<String, String> locators = new HashMap();
    private static boolean sizzleInjection = false;
    private static String caseId = "";
    private static Level logLevel;
    private static String buildId = "";
    private static String productIOSId = "";
    private static String productAndroidId = "";
    private static String productWebId = "";
    private static String companyId = "";
    private static String ciJobName = "";
    private static boolean logATOMResults = false;
    private static EnvironmentUtil instance = new EnvironmentUtil();

    static
    {
        logger = new LogController(EnvironmentUtil.class);

        setTakeSnapShots();
        setCIJobName();
        setAutoAcceptAlerts();
        setBrowser();
        setURL();
        setTestGroup();
        setSnapshotDirectory();
        setTestRailIds();
        setLogLevel();
        checkForDebug();
        setLogResults();
        setLogATOMResults();
        setWebDriverGridUrl();
        setAppThwackProjectId();
        setAppThwackApiKey();
        setAppThwackAppId();
        setAppiumGridUrl();
        setAppPackage();
        setAppActivity();
        setTestDroidCredentials();
        setTestDroidProject();
        setTargetAppPath();
        setBrowserStackUsernameAndKey();
        setDeviceName();
        setPlatformVersion();
        setIOSBundleId();
        setIOSUdid();
        setNoSign();
        setIsJenkinsRun();
        setIsJenkinsURL();
        setDataProvider();
        setTestRailUrl();
        setTestRailUserName();
        setTestRailPassword();
    }

    public void setBuildId(String buildId)
    {
        buildId = buildId;
        logger.debug("Setting buildId to " + buildId);
    }

    public void setCustomerS3BucketName(String bucketName)
    {
        customerS3BucketName = bucketName;
    }

    public String getCustomerS3BucketName()
    {
        return customerS3BucketName;
    }

    public static void loadLocatorsForPackage(String className)
    {
        Class clazz = null;
        try
        {
            clazz = Class.forName(className);

            Field[] fields = clazz.getFields();
            for (Field f : fields)
            {
                f.setAccessible(true);

                locators.put(f.getName(), f.get(null).toString());
            }
            logger.info(String.format("Loaded [%d] locators from [%s].", new Object[]{
                Integer.valueOf(locators.size()), className}));
        }
        catch (ClassNotFoundException e)
        {
            logger.error("Error occured accessing class.");
            e.printStackTrace();
        }
        catch (IllegalArgumentException e1)
        {
            logger.error("Error occured accessing constant in class.");
            e1.printStackTrace();
        }
        catch (IllegalAccessException e1)
        {
            logger.error("Error occured accessing class constants.");
            e1.printStackTrace();
        }
    }

    public String getLocatorByName(String locator)
    {
        logger.debug(
            String.format("Iterating through locator HashMap looking for %s.  There are [%d] pairs in the map.",
                new Object[]{locator,
                    Integer.valueOf(locators.size())}));
        Iterator it = locators.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pairs = (Map.Entry) it.next();
            if (pairs.getKey().toString().equals(locator))
            {
                return pairs.getValue().toString();
            }
        }
        Assert.fail("Key containing " + locator + " not found in Locator map.");
        return null;
    }

    public void setProductIOSId(String productId)
    {
        productIOSId = productId;
        logger.debug("Set IOS Id to " + productId);
    }

    public String getProductIOSId()
    {
        return productIOSId;
    }

    public void setProductAndroidId(String productAndroidId)
    {
        productAndroidId = productAndroidId;
        logger.debug("Set Android Id to " + productAndroidId);
    }

    public String getProductAndroidId()
    {
        return productAndroidId;
    }

    public void setProductWebId(String productWebId)
    {
        productWebId = productWebId;
        logger.debug("Set productWebId Id to " + productWebId);
    }

    public String getProductWebId()
    {
        return productWebId;
    }

    public void setCompanyId(String companyId)
    {
        companyId = companyId;
    }

    public String getCompanyId()
    {
        return companyId;
    }

    public String getBuildId()
    {
        return buildId;
    }

    public String getCaseId()
    {
        return caseId;
    }

    public void setCaseId(String caseId)
    {
        caseId = caseId;
    }

    public String getTestRailUrl()
    {
        return testRailUrl;
    }

    public String getTestRailUserName()
    {
        return testRailUserName;
    }

    public String getTestRailPassword()
    {
        return testRailPassword;
    }

    public String getJenkinsURL()
    {
        return jenkinsURL;
    }

    public boolean sizzleInject()
    {
        return sizzleInjection;
    }

    public boolean isMobileIOSTest()
    {
        return true;
    }

    public boolean isTablet()
    {
        return isTablet;
    }

    public boolean isSmallTablet()
    {
        return isSmallTablet;
    }

    public boolean isPhablet()
    {
        return isPhablet;
    }

    public boolean isSimulator()
    {
        return isSimulator;
    }

    public boolean isPhone()
    {
        return isPhone;
    }

    public boolean useRawCssOnly()
    {
        return rawCssOnly;
    }

    public void setRawCssOnly(boolean value)
    {
        rawCssOnly = value;
        logger.info("Set useRawCssOnly to " + rawCssOnly);
    }

    public String getPreviousWindowHandle()
    {
        return previousWindowHandle;
    }

    public void setPreviousWindowHandle(String handle)
    {
        previousWindowHandle = handle;
    }

    public String getTestDroidProjectName()
    {
        return testDroidProjectName;
    }

    public String getTestDroidProjectDescription()
    {
        return testDroidProjectDescription;
    }

    public String getTestDroidTestRun()
    {
        return testDroidTestRun;
    }

    public String getTestDroidUserName()
    {
        return testDroidUserName;
    }

    public String getTestDroidPassword()
    {
        return testDroidPassword;
    }

    public String getAppPackage()
    {
        logger.info(String.format("Using appPackage = [%s]", new Object[]{appPackage}));
        return appPackage;
    }

    public String getAppActivity()
    {
        logger.info(String.format("Using appActivity = [%s]", new Object[]{appActivity}));
        return appActivity;
    }

    public String getAppiumGridUrl()
    {
        return appiumGridUrl;
    }

    public String getTargetAppPath()
    {
        return targetAppPath;
    }

    public String getTestGroup()
    {
        return testGroup;
    }

    public void setIsMobileIOS(boolean value)
    {
        isMobileIOS = value;
    }

    public void setIsMobileAndroid(boolean value)
    {
        isMobileAndroid = value;
    }

    public void setIsTablet(boolean value)
    {
        isTablet = value;
    }

    public void setIsSmallTablet(boolean value)
    {
        isSmallTablet = value;
    }

    public void setIsPhablet(boolean value)
    {
        isPhablet = value;
    }

    public void setIsSimulator(boolean value)
    {
        isSimulator = value;
    }

    public void setIsPhone(boolean value)
    {
        isPhone = value;
    }

    public boolean getIsMobileIOS()
    {
        return isMobileIOS;
    }

    public boolean getIsMobileAndroid()
    {
        return isMobileAndroid;
    }

    public boolean getIsTablet()
    {
        return isTablet;
    }

    public boolean getIsSmallTablet()
    {
        return isSmallTablet;
    }

    public boolean getIsPhablet()
    {
        return isPhablet;
    }

    public boolean getIsSimulator()
    {
        return isSimulator;
    }

    public boolean getIsPhone()
    {
        return isPhone;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public String getCIJobName()
    {
        return ciJobName;
    }

    public String getPlatformVersion()
    {
        return platformVersion;
    }

    public void setIsAndroidTablet(boolean value)
    {
        isAndroidTablet = value;
    }

    public boolean getIsAndroidTablet()
    {
        return isAndroidTablet;
    }

    public void setUseKeystore(boolean value)
    {
        useKeystore = value;
    }

    public boolean getUseKeystore()
    {
        return useKeystore;
    }

    public void setKeystorePath(String path)
    {
        keystorePath = path;
    }

    public String getKeystorePath()
    {
        return keystorePath;
    }

    public void setKeystorePassword(String password)
    {
        keystorePassword = password;
    }

    public String getKeystorePassword()
    {
        return keystorePassword;
    }

    public void setKeyPassword(String password)
    {
        keyPassword = password;
    }

    public String getKeyPassword()
    {
        return keyPassword;
    }

    public void setKeyAlias(String alias)
    {
        keyAlias = alias;
    }

    public String getAlias()
    {
        return keyAlias;
    }

    public String getBrowserStackUserName()
    {
        return browserStackUserName;
    }

    public String getBrowserStackKey()
    {
        return browserStackKey;
    }

    public String getIosBundleId()
    {
        return iosBundleID;
    }

    public String getUdid()
    {
        return iosUdid;
    }

    public boolean getNoSign()
    {
        return noSign;
    }

    public boolean getIsJenkinsRun()
    {
        return isJenkinsRun;
    }

    public void setIsRemoteTestRun(boolean remote)
    {
        isRemoteTestRun = remote;
    }

    public boolean getIsRemoteTestRun()
    {
        return isRemoteTestRun;
    }

    public String getDataProvider()
    {
        return dataProvider;
    }

    private static void setBrowserStackUsernameAndKey()
    {
        if (System.getProperty("browserStackUserName") != null)
        {
            browserStackUserName = System.getProperty("browserStackUserName");
        }
        else
        {
            browserStackUserName = "brianrock2";
        }
        if (System.getProperty("browserStackKey") != null)
        {
            browserStackKey = System.getProperty("browserStackKey");
        }
        else
        {
            browserStackKey = "PLshKkTtwLerYy8Edj6d";
        }
    }

    private static void setIsJenkinsRun()
    {
        if (System.getProperty("isJenkinsRun") != null)
        {
            isJenkinsRun = System.getProperty("isJenkinsRun").equals("true");
        }
        else
        {
            isJenkinsRun = false;
        }
        logger.info("Is Jenkins Run = " + isJenkinsRun);
    }

    private static void setCIJobName()
    {
        if (System.getProperty("ciJobName") != null)
        {
            ciJobName = System.getProperty("ciJobName");
        }
        else
        {
            ciJobName = "NOTSET";
        }
        logger.info("CI Job Name = " + ciJobName);
    }

    private static void setDataProvider()
    {
        if (System.getProperty("dataProvider") != null)
        {
            dataProvider = System.getProperty("dataProvider");
        }
        else
        {
            dataProvider = "";
        }
    }

    private static void setIsJenkinsURL()
    {
        if (System.getProperty("jenkinsURL") != null)
        {
            jenkinsURL = System.getProperty("jenkinsURL");
        }
    }

    private static void setTestGroup()
    {
        if (System.getProperty("groups") != null)
        {
            testGroup = System.getProperty("groups");
        }
        else
        {
            testGroup = "";
        }
    }

    private static void setIOSBundleId()
    {
        if (System.getProperty("iosBundleId") != null)
        {
            iosBundleID = System.getProperty("iosBundleId");
        }
        else
        {
            iosBundleID = "";
        }
    }

    private static void setIOSUdid()
    {
        if (System.getProperty("iosUdid") != null)
        {
            iosUdid = System.getProperty("iosUdid");
        }
        else
        {
            iosUdid = "";
        }
    }

    private static void setNoSign()
    {
        if (System.getProperty("noSign") != null)
        {
            noSign = System.getProperty("noSign") != null;
        }
        else
        {
            noSign = false;
        }
    }

    private static void setDeviceName()
    {
        if (System.getProperty("deviceName") != null)
        {
            deviceName = System.getProperty("deviceName");
        }
        else
        {
            deviceName = "";
        }
    }

    private static void setSizzleInjection()
    {
        if (System.getProperty("sizzleInjection") != null)
        {
            sizzleInjection = System.getProperty("sizzleInjection").equals("true");
        }
        else
        {
            sizzleInjection = true;
        }
    }

    private static void setPlatformVersion()
    {
        if (System.getProperty("platformVersion") != null)
        {
            platformVersion = System.getProperty("platformVersion");
        }
        else
        {
            platformVersion = "";
        }
    }

    private static void setTargetAppPath()
    {
        if (System.getProperty("targetAppPath") != null)
        {
            targetAppPath = System.getProperty("targetAppPath");
        }
        else
        {
            targetAppPath = "";
        }
    }

    private static void setAppPackage()
    {
        if (System.getProperty("appPackage") != null)
        {
            appPackage = System.getProperty("appPackage");
        }
        else
        {
            appPackage = "";
        }
    }

    private static void setAppActivity()
    {
        if (System.getProperty("appActivity") != null)
        {
            appActivity = System.getProperty("appActivity");
        }
        else
        {
            appActivity = "";
        }
    }

    private static void setTestDroidCredentials()
    {
        if (System.getProperty("testDroidUserName") != null)
        {
            testDroidUserName = System.getProperty("testDroidUserName");
        }
        else
        {
            testDroidUserName = "brock@applausemail.com";
        }
        if (System.getProperty("testDroidPassword") != null)
        {
            testDroidPassword = System.getProperty("testDroidPassword");
        }
        else
        {
            testDroidPassword = "@pplause1";
        }
    }

    private static void setTestDroidProject()
    {
        if (System.getProperty("testDroidProjectName") != null)
        {
            testDroidProjectName = System.getProperty("testDroidProjectName");
        }
        else
        {
            testDroidProjectName = "";
        }
        if (System.getProperty("testDroidProjectDescription") != null)
        {
            testDroidProjectDescription = System.getProperty("testDroidProjectDescription");
        }
        else
        {
            testDroidProjectDescription = "";
        }
        if (System.getProperty("testDroidTestRun") != null)
        {
            testDroidTestRun = System.getProperty("testDroidTestRun");
        }
        else
        {
            testDroidTestRun = "";
        }
    }

    private static void setAppiumGridUrl()
    {
        if (System.getProperty("appiumGridUrl") != null)
        {
            appiumGridUrl = System.getProperty("webDriverGridUrl");
        }
        else
        {
            appiumGridUrl = "http://localhost:4444/wd/hub";
        }
    }

    public static void setAppThwackProjectId()
    {
        if (System.getProperty("appThwackProjectId") != null)
        {
            appThwackProjectId = Integer.parseInt(
                System.getProperty("appThwackProjectId"));
        }
        else
        {
            appThwackProjectId = 999;
        }
    }

    public static void setAppThwackAppId()
    {
        if (System.getProperty("appThwackAppId") != null)
        {
            appThwackAppId = Integer.parseInt(
                System.getProperty("appThwackAppId"));
        }
        else
        {
            appThwackAppId = 999;
        }
    }

    public static void setAppThwackApiKey()
    {
        if (System.getProperty("appThwackApiKey") != null)
        {
            appThwackApiKey = System.getProperty("appThwackApiKey");
        }
        else
        {
            appThwackApiKey = "EsfCWchlY8twR949UJMmGJYhH83uckf9St89KCGA";
        }
    }

    public int getAppThwackProjectId()
    {
        return appThwackProjectId;
    }

    public int getAppThwackAppId()
    {
        return appThwackAppId;
    }

    public String getAppThwackApiKey()
    {
        return appThwackApiKey;
    }

    public void setIsMobileTest()
    {
        logger.info("Running a mobile test.");
        isMobileTest = Boolean.valueOf(true);
    }

    public Boolean getIsMobileTest()
    {
        return isMobileTest;
    }

    public String getWebDriverGridURL()
    {
        return webDriverGridUrl;
    }

    public boolean getFailedTestDoReset()
    {
        return failedTestDoReset.booleanValue();
    }

    public void setFailedTestDoReset(boolean value)
    {
        logger.info("Setting failedTestDoReset to " + value);
        failedTestDoReset = Boolean.valueOf(value);
    }

    public static EnvironmentUtil getInstance()
    {
        return instance;
    }

    public Boolean turnOnDebug()
    {
        return setLogToDebug;
    }

    public Browser getBrowser()
    {
        return browser;
    }

    public String getTestPlanId()
    {
        return testPlanId;
    }

    public String getTestRunId()
    {
        return testRunId;
    }

    public String getTestSnapshotDirectory()
    {
        return snapshotDirectory;
    }

    private static void setBrowser()
    {
        if (System.getProperty("browser") != null)
        {
            browser = Browser.valueOf(System.getProperty("browser"));
        }
        else
        {
            browser = Browser.valueOf("");
        }
    }

    private static void setWebDriverGridUrl()
    {
        if (System.getProperty("webDriverGridUrl") != null)
        {
            webDriverGridUrl = System.getProperty("webDriverGridUrl");
        }
        else
        {
            webDriverGridUrl = "http://192.168.xxx.xxx:4444/wd/hub";
        }
    }

    private static void setTestRailIds()
    {
        if (System.getProperty("testplan") != null)
        {
            testPlanId = System.getProperty("testplan");
        }
        else
        {
            testPlanId = "";
        }
        if (System.getProperty("testrun") != null)
        {
            testRunId = System.getProperty("testrun");
        }
        else
        {
            testRunId = "";
        }
    }

    private static void setURL()
    {
        if (System.getProperty("url") != null)
        {
            url = System.getProperty("url");
        }
        else
        {
            url = "";
        }
    }

    private static void setSnapshotDirectory()
    {
        if (System.getProperty("snapshotDirectory") != null)
        {
            snapshotDirectory = System.getProperty("snapshotDirectory");
        }
        else
        {
            snapshotDirectory = "";
        }
    }

    public static void setLogResults(boolean value)
    {
        logResults = Boolean.valueOf(value);
    }

    private static void setLogResults()
    {
        if (System.getProperty("logResults") != null)
        {
            if (System.getProperty("logResults").contains("true"))
            {
                logResults = Boolean.valueOf(true);
            }
        }
        else
        {
            logResults = Boolean.valueOf(false);
        }
        logger.info("Log Results set to " + logResults);
    }

    private static void setLogATOMResults()
    {
        if (System.getProperty("logATOMResults") != null)
        {
            if (System.getProperty("logATOMResults").contains("true"))
            {
                logATOMResults = true;
            }
        }
        else
        {
            logATOMResults = false;
        }
        logger.info("Log ATOM Results set to " + logATOMResults);
    }

    public boolean getLogATOMResults()
    {
        return logATOMResults;
    }

    public Level getLogLevel()
    {
        if (logLevel == null)
        {
            logLevel = Level.INFO;
        }
        return logLevel;
    }

    private static void setLogLevel()
    {
        if (System.getProperty("logLevel") != null)
        {
            String logLevelStr = System.getProperty("logLevel");
            if (logLevelStr.equals("trace"))
            {
                logLevel = Level.TRACE;
            }
            else if (logLevelStr.equals("debug"))
            {
                logLevel = Level.DEBUG;
            }
            else if (logLevelStr.equals("info"))
            {
                logLevel = Level.INFO;
            }
            else if (logLevelStr.equals("error"))
            {
                logLevel = Level.ERROR;
            }
            else if (logLevelStr.equals("warn"))
            {
                logLevel = Level.WARN;
            }
            else if (logLevelStr.equals("fatal"))
            {
                logLevel = Level.FATAL;
            }
        }
        else
        {
            logLevel = Level.INFO;
        }
    }

    private static void checkForDebug()
    {
        if (System.getProperty("debug") != null)
        {
            if (System.getProperty("debug").toLowerCase().equals("true"))
            {
                setLogToDebug = Boolean.valueOf(true);
                logger.info("debug = true");
                logLevel = Level.DEBUG;
            }
            else
            {
                setLogToDebug = Boolean.valueOf(false);
            }
        }
        if (setLogToDebug.booleanValue() == true)
        {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource("log4j-debug.properties");
            PropertyConfigurator.configure(url);
            logger.info("Setting log4j to debug.");
        }
    }

    private static void setTakeSnapShots()
    {
        if (System.getProperty("takeSnapShots") != null)
        {
            takeSnapShots = Boolean.valueOf(System.getProperty("takeSnapShots").toLowerCase()
                .equals("true"));
        }
        else
        {
            takeSnapShots = Boolean.valueOf(false);
        }
        logger.info(String.format("takeSnapShots is set to - [%s]", new Object[]{takeSnapShots}));
    }

    private static void setAutoAcceptAlerts()
    {
        if (System.getProperty("autoAcceptAlerts") != null)
        {
            autoAcceptAlerts = Boolean.valueOf(System.getProperty("autoAcceptAlerts").equals("true"));
        }
        else
        {
            autoAcceptAlerts = Boolean.valueOf(true);
        }
        logger.info(String.format("autoAcceptAlerts is set to - [%s]", new Object[]{autoAcceptAlerts}));
    }

    public Boolean getLogResults()
    {
        return logResults;
    }

    public String getUrl()
    {
        return url;
    }

    public boolean getTakeSnapShots()
    {
        return takeSnapShots.booleanValue();
    }

    public boolean getAutoAcceptAlerts()
    {
        return autoAcceptAlerts.booleanValue();
    }

    private static void setTestRailUrl()
    {
        if (System.getProperty("testRailUrl") != null)
        {
            testRailUrl = System.getProperty("testRailUrl");
        }
    }

    private static void setTestRailUserName()
    {
        if (System.getProperty("testRailUserName") != null)
        {
            testRailUserName = System.getProperty("testRailUserName");
        }
    }

    private static void setTestRailPassword()
    {
        if (System.getProperty("testRailPassword") != null)
        {
            testRailPassword = System.getProperty("testRailPassword");
        }
    }
}
