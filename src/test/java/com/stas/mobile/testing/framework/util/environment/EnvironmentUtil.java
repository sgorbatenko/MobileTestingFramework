
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
    private static LogController Logger;
    private static String CustomerS3BucketName;
    private static Browser BrowserType;
    private static String DeviceName;
    private static String PlatformVersion;
    private static String Url;
    private static String TestPlanId;
    private static String TestRunId;
    private static String SnapshotDirectory;
    private static Boolean TakeSnapShots = Boolean.valueOf(true);
    private static Boolean AutoAcceptAlerts = Boolean.valueOf(true);
    private static Boolean SetLogToDebug = Boolean.valueOf(false);
    private static Boolean LogResults = Boolean.valueOf(false);
    private static Boolean FailedTestDoReset = Boolean.valueOf(false);
    private static String WebDriverGridUrl = "";
    private static Boolean IsMobileTest = Boolean.valueOf(false);
    private static int AppThwackProjectId = 0;
    private static String AppThwackApiKey = "";
    private static int AppThwackAppId = 0;
    private static String AppiumGridUrl;
    private static String AppPackage;
    private static String AppActivity;
    private static String TestDroidUserName = "";
    private static String TestDroidPassword = "";
    private static String TestDroidProjectName = "";
    private static String TestDroidProjectDescription = "";
    private static String TestDroidTestRun = "";
    private static String TestDroidServer = "";
    private static String TargetAppPath = "";
    private static String TestGroup = "";
    private static boolean RawCssOnly = false;
    private static String PreviousWindowHandle = "";
    private static boolean IsMobileIOS = false;
    private static boolean IsMobileAndroid = false;
    private static boolean IsTablet = false;
    private static boolean IsSmallTablet = false;
    private static boolean IsPhablet = false;
    private static boolean IsSimulator = false;
    private static boolean IsPhone = false;
    private static boolean IsAndroidTablet = false;
    private static boolean UseKeystore = false;
    private static String KeystorePath = "";
    private static String KeystorePassword = "";
    private static String KeyAlias = "";
    private static String KeyPassword = "";
    private static String BrowserStackUserName = "";
    private static String BrowserStackKey = "";
    private static String IosBundleID = "";
    private static String IosUdid = "";
    private static boolean NoSign = false;
    private static boolean IsJenkinsRun = false;
    private static String JenkinsURL = "";
    private static boolean IsRemoteTestRun = false;
    private static String DataProvider = "null";
    private static String TestRailUrl = "";
    private static String TestRailUserName = "";
    private static String TestRailPassword = "";
    private static Map<String, String> Locators = new HashMap();
    private static boolean SizzleInjection = false;
    private static String CaseId = "";
    private static Level LogLevel;
    private static String BuildId = "";
    private static String ProductIOSId = "";
    private static String ProductAndroidId = "";
    private static String ProductWebId = "";
    private static String CompanyId = "";
    private static String CiJobName = "";
    private static boolean LogATOMResults = false;
    private static EnvironmentUtil Instance = new EnvironmentUtil();

    static
    {
        Logger = new LogController(EnvironmentUtil.class);

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
        BuildId = buildId;
        Logger.debug("Setting buildId to " + buildId);
    }

    public void setCustomerS3BucketName(String bucketName)
    {
        CustomerS3BucketName = bucketName;
    }

    public String getCustomerS3BucketName()
    {
        return CustomerS3BucketName;
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

                Locators.put(f.getName(), f.get(null).toString());
            }
            Logger.info(String
                .format("Loaded [%d] locators from [%s].", new Object[]{
                    Integer.valueOf(Locators.size()), className}));
        }
        catch (ClassNotFoundException e)
        {
            Logger.error("Error occured accessing class.");
            e.printStackTrace();
        }
        catch (IllegalArgumentException e1)
        {
            Logger.error("Error occured accessing constant in class.");
            e1.printStackTrace();
        }
        catch (IllegalAccessException e1)
        {
            Logger.error("Error occured accessing class constants.");
            e1.printStackTrace();
        }
    }

    public String getLocatorByName(String locator)
    {
        Logger.debug(String
            .format("Iterating through locator HashMap looking for %s.  There are [%d] pairs in the map.",
                new Object[]{locator,
                    Integer.valueOf(Locators.size())}));
        Iterator it = Locators.entrySet().iterator();
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
        ProductIOSId = productId;
        Logger.debug("Set IOS Id to " + productId);
    }

    public String getProductIOSId()
    {
        return ProductIOSId;
    }

    public void setProductAndroidId(String productAndroidId)
    {
        ProductAndroidId = productAndroidId;
        Logger.debug("Set Android Id to " + productAndroidId);
    }

    public String getProductAndroidId()
    {
        return ProductAndroidId;
    }

    public void setProductWebId(String productWebId)
    {
        ProductWebId = productWebId;
        Logger.debug("Set productWebId Id to " + productWebId);
    }

    public String getProductWebId()
    {
        return ProductWebId;
    }

    public void setCompanyId(String companyId)
    {
        CompanyId = companyId;
    }

    public String getCompanyId()
    {
        return CompanyId;
    }

    public String getBuildId()
    {
        return BuildId;
    }

    public String getCaseId()
    {
        return CaseId;
    }

    public void setCaseId(String caseId)
    {
        CaseId = caseId;
    }

    public String getTestRailUrl()
    {
        return TestRailUrl;
    }

    public String getTestRailUserName()
    {
        return TestRailUserName;
    }

    public String getTestRailPassword()
    {
        return TestRailPassword;
    }

    public String getJenkinsURL()
    {
        return JenkinsURL;
    }

    public boolean sizzleInject()
    {
        return SizzleInjection;
    }

    public boolean isMobileIOSTest()
    {
        return true;
    }

    public boolean isTablet()
    {
        return IsTablet;
    }

    public boolean isSmallTablet()
    {
        return IsSmallTablet;
    }

    public boolean isPhablet()
    {
        return IsPhablet;
    }

    public boolean isSimulator()
    {
        return IsSimulator;
    }

    public boolean isPhone()
    {
        return IsPhone;
    }

    public boolean useRawCssOnly()
    {
        return RawCssOnly;
    }

    public void setRawCssOnly(boolean value)
    {
        RawCssOnly = value;
        Logger.info("Set useRawCssOnly to " + RawCssOnly);
    }

    public String getPreviousWindowHandle()
    {
        return PreviousWindowHandle;
    }

    public void setPreviousWindowHandle(String handle)
    {
        PreviousWindowHandle = handle;
    }

    public String getTestDroidProjectName()
    {
        return TestDroidProjectName;
    }

    public String getTestDroidProjectDescription()
    {
        return TestDroidProjectDescription;
    }

    public String getTestDroidTestRun()
    {
        return TestDroidTestRun;
    }

    public String getTestDroidUserName()
    {
        return TestDroidUserName;
    }

    public String getTestDroidPassword()
    {
        return TestDroidPassword;
    }

    public String getAppPackage()
    {
        Logger.info(String.format("Using appPackage = [%s]",
            new Object[]{AppPackage}));
        return AppPackage;
    }

    public String getAppActivity()
    {
        Logger.info(String.format("Using appActivity = [%s]",
            new Object[]{AppActivity}));
        return AppActivity;
    }

    public String getAppiumGridUrl()
    {
        return AppiumGridUrl;
    }

    public String getTargetAppPath()
    {
        return TargetAppPath;
    }

    public String getTestGroup()
    {
        return TestGroup;
    }

    public void setIsMobileIOS(boolean value)
    {
        IsMobileIOS = value;
    }

    public void setIsMobileAndroid(boolean value)
    {
        IsMobileAndroid = value;
    }

    public void setIsTablet(boolean value)
    {
        IsTablet = value;
    }

    public void setIsSmallTablet(boolean value)
    {
        IsSmallTablet = value;
    }

    public void setIsPhablet(boolean value)
    {
        IsPhablet = value;
    }

    public void setIsSimulator(boolean value)
    {
        IsSimulator = value;
    }

    public void setIsPhone(boolean value)
    {
        IsPhone = value;
    }

    public boolean getIsMobileIOS()
    {
        return IsMobileIOS;
    }

    public boolean getIsMobileAndroid()
    {
        return IsMobileAndroid;
    }

    public boolean getIsTablet()
    {
        return IsTablet;
    }

    public boolean getIsSmallTablet()
    {
        return IsSmallTablet;
    }

    public boolean getIsPhablet()
    {
        return IsPhablet;
    }

    public boolean getIsSimulator()
    {
        return IsSimulator;
    }

    public boolean getIsPhone()
    {
        return IsPhone;
    }

    public String getDeviceName()
    {
        return DeviceName;
    }

    public String getCIJobName()
    {
        return CiJobName;
    }

    public String getPlatformVersion()
    {
        return PlatformVersion;
    }

    public void setIsAndroidTablet(boolean value)
    {
        IsAndroidTablet = value;
    }

    public boolean getIsAndroidTablet()
    {
        return IsAndroidTablet;
    }

    public void setUseKeystore(boolean value)
    {
        UseKeystore = value;
    }

    public boolean getUseKeystore()
    {
        return UseKeystore;
    }

    public void setKeystorePath(String path)
    {
        KeystorePath = path;
    }

    public String getKeystorePath()
    {
        return KeystorePath;
    }

    public void setKeystorePassword(String password)
    {
        KeystorePassword = password;
    }

    public String getKeystorePassword()
    {
        return KeystorePassword;
    }

    public void setKeyPassword(String password)
    {
        KeyPassword = password;
    }

    public String getKeyPassword()
    {
        return KeyPassword;
    }

    public void setKeyAlias(String alias)
    {
        KeyAlias = alias;
    }

    public String getAlias()
    {
        return KeyAlias;
    }

    public String getBrowserStackUserName()
    {
        return BrowserStackUserName;
    }

    public String getBrowserStackKey()
    {
        return BrowserStackKey;
    }

    public String getIosBundleId()
    {
        return IosBundleID;
    }

    public String getUdid()
    {
        return IosUdid;
    }

    public boolean getNoSign()
    {
        return NoSign;
    }

    public boolean getIsJenkinsRun()
    {
        return IsJenkinsRun;
    }

    public void setIsRemoteTestRun(boolean remote)
    {
        IsRemoteTestRun = remote;
    }

    public boolean getIsRemoteTestRun()
    {
        return IsRemoteTestRun;
    }

    public String getDataProvider()
    {
        return DataProvider;
    }

    private static void setBrowserStackUsernameAndKey()
    {
        if (System.getProperty("browserStackUserName") != null)
        {
            BrowserStackUserName = System.getProperty("browserStackUserName");
        }
        else
        {
            BrowserStackUserName = "brianrock2";
        }
        if (System.getProperty("browserStackKey") != null)
        {
            BrowserStackKey = System.getProperty("browserStackKey");
        }
        else
        {
            BrowserStackKey = "PLshKkTtwLerYy8Edj6d";
        }
    }

    private static void setIsJenkinsRun()
    {
        if (System.getProperty("isJenkinsRun") != null)
        {
            IsJenkinsRun = System.getProperty("isJenkinsRun").equals("true");
        }
        else
        {
            IsJenkinsRun = false;
        }
        Logger.info("Is Jenkins Run = " + IsJenkinsRun);
    }

    private static void setCIJobName()
    {
        if (System.getProperty("ciJobName") != null)
        {
            CiJobName = System.getProperty("ciJobName");
        }
        else
        {
            CiJobName = "NOTSET";
        }
        Logger.info("CI Job Name = " + CiJobName);
    }

    private static void setDataProvider()
    {
        if (System.getProperty("dataProvider") != null)
        {
            DataProvider = System.getProperty("dataProvider");
        }
        else
        {
            DataProvider = "";
        }
    }

    private static void setIsJenkinsURL()
    {
        if (System.getProperty("jenkinsURL") != null)
        {
            JenkinsURL = System.getProperty("jenkinsURL");
        }
    }

    private static void setTestGroup()
    {
        if (System.getProperty("groups") != null)
        {
            TestGroup = System.getProperty("groups");
        }
        else
        {
            TestGroup = "";
        }
    }

    private static void setIOSBundleId()
    {
        if (System.getProperty("iosBundleId") != null)
        {
            IosBundleID = System.getProperty("iosBundleId");
        }
        else
        {
            IosBundleID = "";
        }
    }

    private static void setIOSUdid()
    {
        if (System.getProperty("iosUdid") != null)
        {
            IosUdid = System.getProperty("iosUdid");
        }
        else
        {
            IosUdid = "";
        }
    }

    private static void setNoSign()
    {
        if (System.getProperty("noSign") != null)
        {
            NoSign = System.getProperty("noSign") != null;
        }
        else
        {
            NoSign = false;
        }
    }

    private static void setDeviceName()
    {
        if (System.getProperty("deviceName") != null)
        {
            DeviceName = System.getProperty("deviceName");
        }
        else
        {
            DeviceName = "";
        }
    }

    private static void setSizzleInjection()
    {
        if (System.getProperty("sizzleInjection") != null)
        {
            SizzleInjection = System.getProperty("sizzleInjection").equals(
                "true");
        }
        else
        {
            SizzleInjection = true;
        }
    }

    private static void setPlatformVersion()
    {
        if (System.getProperty("platformVersion") != null)
        {
            PlatformVersion = System.getProperty("platformVersion");
        }
        else
        {
            PlatformVersion = "";
        }
    }

    private static void setTargetAppPath()
    {
        if (System.getProperty("targetAppPath") != null)
        {
            TargetAppPath = System.getProperty("targetAppPath");
        }
        else
        {
            TargetAppPath = "";
        }
    }

    private static void setAppPackage()
    {
        if (System.getProperty("appPackage") != null)
        {
            AppPackage = System.getProperty("appPackage");
        }
        else
        {
            AppPackage = "";
        }
    }

    private static void setAppActivity()
    {
        if (System.getProperty("appActivity") != null)
        {
            AppActivity = System.getProperty("appActivity");
        }
        else
        {
            AppActivity = "";
        }
    }

    private static void setTestDroidCredentials()
    {
        if (System.getProperty("testDroidUserName") != null)
        {
            TestDroidUserName = System.getProperty("testDroidUserName");
        }
        else
        {
            TestDroidUserName = "brock@applausemail.com";
        }
        if (System.getProperty("testDroidPassword") != null)
        {
            TestDroidPassword = System.getProperty("testDroidPassword");
        }
        else
        {
            TestDroidPassword = "@pplause1";
        }
    }

    private static void setTestDroidProject()
    {
        if (System.getProperty("testDroidProjectName") != null)
        {
            TestDroidProjectName = System.getProperty("testDroidProjectName");
        }
        else
        {
            TestDroidProjectName = "";
        }
        if (System.getProperty("testDroidProjectDescription") != null)
        {
            TestDroidProjectDescription = System
                .getProperty("testDroidProjectDescription");
        }
        else
        {
            TestDroidProjectDescription = "";
        }
        if (System.getProperty("testDroidTestRun") != null)
        {
            TestDroidTestRun = System.getProperty("testDroidTestRun");
        }
        else
        {
            TestDroidTestRun = "";
        }
    }

    private static void setAppiumGridUrl()
    {
        if (System.getProperty("appiumGridUrl") != null)
        {
            AppiumGridUrl = System.getProperty("webDriverGridUrl");
        }
        else
        {
            AppiumGridUrl = "http://localhost:4444/wd/hub";
        }
    }

    public static void setAppThwackProjectId()
    {
        if (System.getProperty("appThwackProjectId") != null)
        {
            AppThwackProjectId = Integer.parseInt(System
                .getProperty("appThwackProjectId"));
        }
        else
        {
            AppThwackProjectId = 999;
        }
    }

    public static void setAppThwackAppId()
    {
        if (System.getProperty("appThwackAppId") != null)
        {
            AppThwackAppId = Integer.parseInt(System
                .getProperty("appThwackAppId"));
        }
        else
        {
            AppThwackAppId = 999;
        }
    }

    public static void setAppThwackApiKey()
    {
        if (System.getProperty("appThwackApiKey") != null)
        {
            AppThwackApiKey = System.getProperty("appThwackApiKey");
        }
        else
        {
            AppThwackApiKey = "EsfCWchlY8twR949UJMmGJYhH83uckf9St89KCGA";
        }
    }

    public int getAppThwackProjectId()
    {
        return AppThwackProjectId;
    }

    public int getAppThwackAppId()
    {
        return AppThwackAppId;
    }

    public String getAppThwackApiKey()
    {
        return AppThwackApiKey;
    }

    public void setIsMobileTest()
    {
        Logger.info("Running a mobile test.");
        IsMobileTest = Boolean.valueOf(true);
    }

    public Boolean getIsMobileTest()
    {
        return IsMobileTest;
    }

    public String getWebDriverGridURL()
    {
        return WebDriverGridUrl;
    }

    public boolean getFailedTestDoReset()
    {
        return FailedTestDoReset.booleanValue();
    }

    public void setFailedTestDoReset(boolean value)
    {
        Logger.info("Setting failedTestDoReset to " + value);
        FailedTestDoReset = Boolean.valueOf(value);
    }

    public static EnvironmentUtil getInstance()
    {
        return Instance;
    }

    public Boolean turnOnDebug()
    {
        return SetLogToDebug;
    }

    public Browser getBrowser()
    {
        return BrowserType;
    }

    public String getTestPlanId()
    {
        return TestPlanId;
    }

    public String getTestRunId()
    {
        return TestRunId;
    }

    public String getTestSnapshotDirectory()
    {
        return SnapshotDirectory;
    }

    private static void setBrowser()
    {
        if (System.getProperty("browser") != null)
        {
            BrowserType = Browser.valueOf(System.getProperty("browser"));
        }
        else
        {
            BrowserType = Browser.valueOf("");
        }
    }

    private static void setWebDriverGridUrl()
    {
        if (System.getProperty("webDriverGridUrl") != null)
        {
            WebDriverGridUrl = System.getProperty("webDriverGridUrl");
        }
        else
        {
            WebDriverGridUrl = "http://192.168.xxx.xxx:4444/wd/hub";
        }
    }

    private static void setTestRailIds()
    {
        if (System.getProperty("testplan") != null)
        {
            TestPlanId = System.getProperty("testplan");
        }
        else
        {
            TestPlanId = "";
        }
        if (System.getProperty("testrun") != null)
        {
            TestRunId = System.getProperty("testrun");
        }
        else
        {
            TestRunId = "";
        }
    }

    private static void setURL()
    {
        if (System.getProperty("url") != null)
        {
            Url = System.getProperty("url");
        }
        else
        {
            Url = "";
        }
    }

    private static void setSnapshotDirectory()
    {
        if (System.getProperty("snapshotDirectory") != null)
        {
            SnapshotDirectory = System.getProperty("snapshotDirectory");
        }
        else
        {
            SnapshotDirectory = "";
        }
    }

    public static void setLogResults(boolean value)
    {
        LogResults = Boolean.valueOf(value);
    }

    private static void setLogResults()
    {
        if (System.getProperty("logResults") != null)
        {
            if (System.getProperty("logResults").contains("true"))
            {
                LogResults = Boolean.valueOf(true);
            }
        }
        else
        {
            LogResults = Boolean.valueOf(false);
        }
        Logger.info("Log Results set to " + LogResults);
    }

    private static void setLogATOMResults()
    {
        if (System.getProperty("logATOMResults") != null)
        {
            if (System.getProperty("logATOMResults").contains("true"))
            {
                LogATOMResults = true;
            }
        }
        else
        {
            LogATOMResults = false;
        }
        Logger.info("Log ATOM Results set to " + LogATOMResults);
    }

    public boolean getLogATOMResults()
    {
        return LogATOMResults;
    }

    public Level getLogLevel()
    {
        if (LogLevel == null)
        {
            LogLevel = Level.INFO;
        }
        return LogLevel;
    }

    private static void setLogLevel()
    {
        if (System.getProperty("logLevel") != null)
        {
            String logLevelStr = System.getProperty("logLevel");
            if (logLevelStr.equals("trace"))
            {
                LogLevel = Level.TRACE;
            }
            else if (logLevelStr.equals("debug"))
            {
                LogLevel = Level.DEBUG;
            }
            else if (logLevelStr.equals("info"))
            {
                LogLevel = Level.INFO;
            }
            else if (logLevelStr.equals("error"))
            {
                LogLevel = Level.ERROR;
            }
            else if (logLevelStr.equals("warn"))
            {
                LogLevel = Level.WARN;
            }
            else if (logLevelStr.equals("fatal"))
            {
                LogLevel = Level.FATAL;
            }
        }
        else
        {
            LogLevel = Level.INFO;
        }
    }

    private static void checkForDebug()
    {
        if (System.getProperty("debug") != null)
        {
            if (System.getProperty("debug").toLowerCase().equals("true"))
            {
                SetLogToDebug = Boolean.valueOf(true);
                Logger.info("debug = true");
                LogLevel = Level.DEBUG;
            }
            else
            {
                SetLogToDebug = Boolean.valueOf(false);
            }
        }
        if (SetLogToDebug.booleanValue() == true)
        {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource("log4j-debug.properties");
            PropertyConfigurator.configure(url);
            Logger.info("Setting log4j to debug.");
        }
    }

    private static void setTakeSnapShots()
    {
        if (System.getProperty("takeSnapShots") != null)
        {
            TakeSnapShots = Boolean.valueOf(System.getProperty("takeSnapShots")
                .toLowerCase().equals("true"));
        }
        else
        {
            TakeSnapShots = Boolean.valueOf(false);
        }
        Logger.info(String.format("takeSnapShots is set to - [%s]",
            new Object[]{TakeSnapShots}));
    }

    private static void setAutoAcceptAlerts()
    {
        if (System.getProperty("autoAcceptAlerts") != null)
        {
            AutoAcceptAlerts = Boolean.valueOf(System.getProperty(
                "autoAcceptAlerts").equals("true"));
        }
        else
        {
            AutoAcceptAlerts = Boolean.valueOf(true);
        }
        Logger.info(String.format("autoAcceptAlerts is set to - [%s]",
            new Object[]{AutoAcceptAlerts}));
    }

    public Boolean getLogResults()
    {
        return LogResults;
    }

    public String getUrl()
    {
        return Url;
    }

    public boolean getTakeSnapShots()
    {
        return TakeSnapShots.booleanValue();
    }

    public boolean getAutoAcceptAlerts()
    {
        return AutoAcceptAlerts.booleanValue();
    }

    private static void setTestRailUrl()
    {
        if (System.getProperty("testRailUrl") != null)
        {
            TestRailUrl = System.getProperty("testRailUrl");
        }
    }

    private static void setTestRailUserName()
    {
        if (System.getProperty("testRailUserName") != null)
        {
            TestRailUserName = System.getProperty("testRailUserName");
        }
    }

    private static void setTestRailPassword()
    {
        if (System.getProperty("testRailPassword") != null)
        {
            TestRailPassword = System.getProperty("testRailPassword");
        }
    }
}
