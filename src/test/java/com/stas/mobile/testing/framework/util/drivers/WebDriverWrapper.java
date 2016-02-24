
package com.stas.mobile.testing.framework.util.drivers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.stas.mobile.testing.framework.util.environment.Browser;
import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;
import com.testdroid.api.http.MultipartFormDataContent;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class WebDriverWrapper
{
    private static WebDriver driver = null;
    private static String userAgent = null;
    private static LogController logger = new LogController(WebDriverWrapper.class);
    private static DesiredCapabilities dc = new DesiredCapabilities();
    private static EnvironmentUtil env = EnvironmentUtil.getInstance();
    private static DeviceLabApiUtils deviceLabApiUtil = new DeviceLabApiUtils();
    private static AppiumDriver appiumDriver;
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    public static String os = "unset";
    public static String osVersion = "unset";
    public static String manufacturer = "unset";
    public static String runDate = null;
    public static String buildId = "";
    public static String runId;

    public static WebDriver getWebDriver()
    {
        if (driver == null)
        {
            Browser browser = EnvironmentUtil.getInstance().getBrowser();
            return getWebDriver(browser);
        }
        return driver;
    }

    public static String getRunId()
    {
        String productId = getProductId();
        if (runId == null)
        {
            if (runDate == null)
            {
                runDate = String.valueOf(System.currentTimeMillis());
            }
            runId = productId + "^^" + env.getCIJobName() + "^^" + getBuildId() + "^^" + runDate;

            logger.info("Run Id is set to - " + runId);
        }
        return runId;
    }

    private static String getProductId()
    {
        String productId = "";
        if (env.getIsMobileIOS())
        {
            productId = env.getProductIOSId();
        }
        if (env.getIsMobileAndroid())
        {
            productId = env.getProductAndroidId();
        }
        else
        {
            productId = env.getProductWebId();
        }
        return productId;
    }

    public static void quitAndResetDriver()
    {
        driver.quit();
        driver = null;
    }

    public static String getBuildId()
    {
        buildId = env.getBuildId();
        return buildId;
    }

    public static WebDriver getNewWebDriver()
    {
        Browser browser = EnvironmentUtil.getInstance().getBrowser();
        return getWebDriver(browser);
    }

    public static AppiumDriver getDeviceDriver()
    {
        Browser browser = env.getBrowser();
        logger.info(String.format("Getting browser or device type %s.", new Object[]{browser
            .toString()}));
        return getAppiumDriver(browser);
    }

    public static synchronized AppiumDriver getAppiumDriver()
    {
        if (!env.getIsMobileIOS())
        {
            return appiumDriver;
        }
        try
        {
            Thread.sleep(1500L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        logger.debug("Returning current driver object." + appiumDriver
            .getCapabilities());

        return appiumDriver;
    }

    public static AppiumDriver getAppiumDriver(Browser browser)
    {
        switch (browser)
        {
            case LOCAL_NEXUS_SEVEN_DEVICE:
                getNexusSevenDriver();
                os = "Android";
                osVersion = "localSys";
                manufacturer = "Google";
                break;
            case LOCAL_ANDROID_MOBILE:
                getAndroidMobileDriver();
                os = "Android";
                osVersion = "localSys";
                manufacturer = "Google";
                break;
            case LOCAL_ANDROID_TABLET:
                getAndroidTabletDriver();
                os = "Android";
                osVersion = "localSys";
                manufacturer = "Google";
                break;
            case APPTHWACK_NEXUS_SEVEN:
                getAppThwackNexusSevenDriver();
                os = "Android";
                osVersion = "4.4.4";
                manufacturer = "Google";
                break;
            case APPTHWACK_NEXUS_FIVE:
                getAppThwackNexusFiveDriver();
                os = "Android";
                osVersion = "4.4.4";
                manufacturer = "Google";
                break;
            case APPTHWACK_IPHONE_SIX:
                getAppThwackIphoneSixDriver();
                os = "IOS";
                osVersion = "8.0";
                manufacturer = "Apple";
                break;
            case APPTHWACK_IPAD_FOUR:
                getAppThwackIpadFourDriver();
                os = "IOS";
                osVersion = "8.1";
                manufacturer = "Apple";
                break;
            case LOCAL_SIM_IPHONE_FIVE:
                getLocalSimulatorIphoneFiveDriver();
                os = "IOS";
                osVersion = "simulator";
                manufacturer = "Apple";
                break;
            case LOCAL_SIM_IPHONE_FIVE_S:
                getLocalSimulatorIphoneFiveSDriver();
                os = "IOS";
                osVersion = "simulator";
                manufacturer = "Apple";
                break;
            case LOCAL_SIM_IPHONE_SIX:
                getLocalSimulatorIphoneSixDriver();
                os = "IOS";
                osVersion = "simulator";
                manufacturer = "Apple";
                break;
            case LOCAL_SIM_IPHONE_SIX_PLUS:
                getLocalSimulatorIphoneSixPlusDriver();
                os = "IOS";
                osVersion = "simulator";
                manufacturer = "Apple";
                break;
            case LOCAL_SIM_IPAD:
                getLocalSimulatorIpadDriver();
                os = "IOS";
                osVersion = "simulator";
                manufacturer = "Apple";
                break;
            case LOCAL_IPAD_DEVICE:
                getLocalIpadDeviceDriver();
                os = "IOS";
                osVersion = "localSys";
                manufacturer = "Apple";
                break;
            case LOCAL_IPHONE_DEVICE:
                getLocalIphoneDeviceDriver();
                os = "IOS";
                osVersion = "localSys";
                manufacturer = "Apple";
                break;
            case AC_EU_IPOD_TOUCH_5GEN:
                getPrivateTestdDroidIosDevice("ipod", "iOS iPod Touch 5Gen", "iPod Touch 5gen 8.2 EU");

                os = "IOS";
                osVersion = "8.2";
                manufacturer = "Apple";
                break;
            case AC_EU_IPHONE_5S:
                getPrivateTestdDroidIosDevice("iphone", "iOS iPhone 5S", "iPhone 5S 8.2 EU");

                os = "IOS";
                osVersion = "8.2";
                manufacturer = "Apple";
                break;
            case AC_EU_IPHONE_6:
                getPrivateTestdDroidIosDevice("iphone", "iOS iPhone 6", "iPhone 6 8.1.3 EU");

                os = "IOS";
                osVersion = "8.1.3";
                manufacturer = "Apple";
                break;
            case AC_EU_IPAD_MINI:
                getPrivateTestdDroidIosDevice("ipad", "iOS iPad Mini", "iPad Mini 2 7.0.3 EU");

                os = "IOS";
                osVersion = "8.1.2";
                manufacturer = "Apple";
                break;
            case AC_US_IPOD_TOUCH_5GEN:
                getPrivateTestdDroidIosDevice("ipod", "iOS iPod Touch 5Gen", "iPod Touch 5th Gen 8.1.2 US");

                os = "IOS";
                osVersion = "8.1.2";
                manufacturer = "Apple";
                break;
            case AC_US_ATT_IPHONE_5S:
                getPrivateTestdDroidIosDevice("iphone", "iOS iPhone 5S", "iPhone 5S ATT 8.1.3 US");

                os = "IOS";
                osVersion = "8.1.3";
                manufacturer = "Apple";
                break;
            case AC_US_ATT_IPHONE_6:
                getPrivateTestdDroidIosDevice("iphone", "iOS iPhone 6", "iPhone 6 8.1 AT&T US");

                os = "IOS";
                osVersion = "8.1";
                manufacturer = "Apple";
                break;
            case AC_US_ATT_IPHONE_6P:
                getPrivateTestdDroidIosDevice("iphone", "iOS iPhone 6", "iPhone 6 Plus ATT 8.3 US");

                os = "IOS";
                osVersion = "8.3";
                manufacturer = "Apple";
                break;
            case AC_US_IPAD_AIR:
                getPrivateTestdDroidIosDevice("ipad", "iOS iPad Air", "iPad Air 2 8.3 US");

                os = "IOS";
                osVersion = "8.3";
                manufacturer = "Apple";
                break;
            case AC_US_IPAD_MINI:
                getPrivateTestdDroidIosDevice("ipad", "iOS iPad Mini", "iPad Mini 2 Retina 8.1.2 US");

                os = "IOS";
                osVersion = "8.1.2";
                manufacturer = "Apple";
                break;
            case TD_IPHONE_6:
                getTestdDroidIosDevice("iphone", "iOS iPhone 6", "iPhone 6 A1586 8.2");

                os = "IOS";
                osVersion = "8.2";
                manufacturer = "Apple";
                break;
            case TD_IPHONE_4S:
                getTestdDroidIosDevice("iphone", "iOS iPhone 4s", "iPhone 4S A1387 6.1.3");

                os = "IOS";
                osVersion = "6.1.3";
                manufacturer = "Apple";
                break;
            case TD_IPHONE_5:
                getTestdDroidIosDevice("iphone", "iOS iPhone 5c", "iPhone 5 A1429 6.1.4");

                os = "IOS";
                osVersion = "6.1.4";
                manufacturer = "Apple";
                break;
            case TD_IPHONE_5C:
                getTestdDroidIosDevice("iphone", "iOS iPhone 5c", "iPhone 5C A1532 8.1");

                os = "IOS";
                osVersion = "8.1";
                manufacturer = "Apple";
                break;
            case TD_IPHONE_5S:
                getTestdDroidIosDevice("iphone", "iOS iPhone 5s", "iPhone 5S A1457 8.1.1");

                os = "IOS";
                osVersion = "8.1.1";
                manufacturer = "Apple";
                break;
            case TD_IPHONE_6P:
                getTestdDroidIosDevice("iphone", "iOS iPhone 6p", "iPhone 6 Plus A1524 8.2");

                os = "IOS";
                osVersion = "8.2";
                manufacturer = "Apple";
                break;
            case TD_IPAD_2:
                getTestdDroidIosDevice("ipad", "iOS iPad 2", "iPad 2 A1395 7.0.4");
                os = "IOS";
                osVersion = "7.0.4";
                manufacturer = "Apple";
                break;
            case TD_IPAD_3:
                getTestdDroidIosDevice("ipad", "iOS iPad 3", "iPad 3 A1416 8.2");
                os = "IOS";
                osVersion = "8.2";
                manufacturer = "Apple";
                break;
            case TD_IPAD_4:
                getTestdDroidIosDevice("ipad", "iOS ipad 4", "iPad 4 A1458 6.0.1");
                os = "IOS";
                osVersion = "6.0.1";
                manufacturer = "Apple";
                break;
            case TD_IPAD_AIR:
                getTestdDroidIosDevice("ipad", "iOS iPad Air", "iPad Air A1474 7.0.3");

                os = "IOS";
                osVersion = "7.0.3";
                manufacturer = "Apple";
                break;
            case TD_IPAD_MINI:
                getTestdDroidIosDevice("ipad", "iOS iPad Mini", "iPad Mini A1432 8.1");

                os = "IOS";
                osVersion = "8.1";
                manufacturer = "Apple";
                break;
            case TD_IPOD_TOUCH_5:
                getTestdDroidIosDevice("ipod", "iOS iPod Touch 5", "iPod touch 5Gen A1509 7.0.2");

                os = "IOS";
                osVersion = "7.0.2";
                manufacturer = "Apple";
                break;
            case TD_NEXUS_SEVEN:
                getTestDroidNexusSeven();
                os = "ANDROID";
                osVersion = "4.4.4";
                manufacturer = "Google";
                break;
            case TD_GALAXY_S_THREE:
                getTestDroidGalaxySThree();
                os = "ANDROID";
                osVersion = "4.3";
                manufacturer = "Google";
                break;
            case TD_NEXUS_FIVE:
                getTestDroidNexusFive();
                os = "ANDROID";
                osVersion = "4.4.4";
                manufacturer = "Asus";

                break;
            case LOCAL_GENERIC_DEVICE:
                getLocalGenericDriver();
                os = "ANDROID";
                osVersion = "localSys";
                manufacturer = "localSys";

                break;
            case LOCAL_GENERIC_DRIVER:
                getLocalGenericDriverFromFile();
                os = "ANDROID";
                osVersion = "localSys";
                manufacturer = "localSys";

                break;
            case AC_US_HTC_ONE:
                getPrivateCloudTestDroidAndroidDevice("HTC One M8", "HTC One M8 US", false, "4.4.2");

                os = "ANDROID";
                osVersion = "4.4.2";
                manufacturer = "Google";
                break;
            case AC_US_SAMSUNG_GALAXY_S5:
                getPrivateCloudTestDroidAndroidDevice("Samsung Galaxy S5", "Samsung Galaxy S5 SM-G900R4 US", false, "5.0");

                os = "ANDROID";
                osVersion = "5.0";
                manufacturer = "Google";
                break;
            case AC_US_MOTO_G:
                getPrivateCloudTestDroidAndroidDevice("Moto G", "Motorola Moto G XT1028 US", false, "4.4.4");

                os = "ANDROID";
                osVersion = "4.4.4";
                manufacturer = "Google";
                break;
            case AC_EU_MOTO_G:
                getPrivateCloudTestDroidAndroidDevice("Moto G", "Motorola Moto G XT1039 EU", false, "4.4.4");

                os = "ANDROID";
                osVersion = "4.4.4";
                manufacturer = "Google";
                break;
            case AC_US_NEXUS_7:
                getPrivateCloudTestDroidAndroidDevice("Nexus 7", "Asus Google Nexus 7 (2013) 4.4.4 US", false, "4.4.4");

                os = "ANDROID";
                osVersion = "4.4.4";
                manufacturer = "Google";
                break;
            case AC_EU_NEXUS_7:
                getPrivateCloudTestDroidAndroidDevice("Nexus 7", "Asus Google Nexus 7 (2013) 5.1.1 US/EU", false, "5.1.1");

                os = "ANDROID";
                osVersion = "5.1.1";
                manufacturer = "Google";
                break;
            case AC_US_SAMSUNG_GALAXY_TAB3_10:
                getPrivateCloudTestDroidAndroidDevice("Samsung Galaxy Tab 3",
                    "Samsung Galaxy Tab 3 10.1 P5210 US",
                    true,
                    "4.4.2");

                os = "ANDROID";
                osVersion = "4.4.2";
                manufacturer = "Google";
                break;
            case TD_SAMSUNG_GALAXY_S6:
                getTestDroidAndroidDevice("samsung galaxy S6", "TBD name", false, "TBD Version");

                os = "ANDROID";
                osVersion = "TBD";
                manufacturer = "Google";

                break;
            case TD_SAMSUNG_GALAXY_S5:
                getTestDroidAndroidDevice("samsung galaxy S5", "Samsung Galaxy S V SM-G900T (T-Mobile) US", false, "4.4.2");

                os = "ANDROID";
                osVersion = "4.4.2";
                manufacturer = "Samsung";

                break;
            case TD_SAMSUNG_GALAXY_S4:
                getTestDroidAndroidDevice("samsung galaxy 34", "Samsung Galaxy S IV SGH-M919", false, "4.4.4");

                os = "ANDROID";
                osVersion = "4.4.4";
                manufacturer = "Samsung";
                break;
            case TD_SAMSUNG_GALAXY_NOTE3:
                getTestDroidAndroidDevice("samsung galaxy note3", "Samsung Galaxy Note 3 SM-N900V", false, "4.4.4");

                os = "ANDROID";
                osVersion = "4.4.4";
                manufacturer = "Samsung";

                break;
            case TD_SAMSUNG_GALAXY_TAB3_7:
                getTestDroidAndroidDevice("samsun galaxy tab3 7", "Samsung Galaxy Tab 3 7.0 SM-T210", true, "4.1.2");

                os = "ANDROID";
                osVersion = "4.1.2";
                manufacturer = "Samsung";

                break;
            case TD_SAMSUNG_GALAXY_TAB3_10:
                getTestDroidAndroidDevice("samsung galaxy tab3 10", "Samsung Galaxy Tab 3 10.1 GT-P5210 4.4.2", true, "4.4.2");

                os = "ANDROID";
                osVersion = "4.4.2";
                manufacturer = "Samsung";

                break;
            case TD_NEXUS_7:
                getTestDroidAndroidDevice("nexus 7", "Asus Google Nexus 7 (2013) ME571KL 5.0", false, "5.0");

                os = "ANDROID";
                osVersion = "5.0";
                manufacturer = "Asus";
                break;
            case TD_NEXUS_9:
                getTestDroidAndroidDevice("nexus 9", "HTC Google Nexus 9 5.0.1", true, "5.0.1");

                os = "ANDROID";
                osVersion = "5.0.1";
                manufacturer = "Samsung";

                break;
            case TD_NEXUS_10:
                getTestDroidAndroidDevice("nexus 10", "Samsung Google Nexus 10 GT-P8110 4.4.2", true, "4.4.2");

                os = "ANDROID";
                osVersion = "4.4.2";
                manufacturer = "Samsung";

                break;
            case TD_LG_G3:
                getTestDroidAndroidDevice("lg g3", "LG G3 D855", false, "4.4.2");
                os = "ANDROID";
                osVersion = "4.4.2";
                manufacturer = "LG";
                break;
            case TD_HTC_1:
                getTestDroidAndroidDevice("htc one m7", "HTC One M7 5.0.2", false, "5.0.2");

                os = "ANDROID";
                osVersion = "5.0.2";
                manufacturer = "HTC";
                break;
            case TD_ASUS_ZENFONE_6:
                getTestDroidAndroidDevice("asus zenfone 6", "Asus ZenFone 6 T00G", false, "4.3");

                os = "ANDROID";
                osVersion = "4.3";
                manufacturer = "ASUS";
                break;
            case TD_MOTO_G:
                getTestDroidAndroidDevice("moto G", "Motorola Moto G XT1028", false, "4.4.4");

                os = "ANDROID";
                osVersion = "4.4.4";
                manufacturer = "Motorola";
                break;
            case TD_MOTO_X:
                getTestDroidAndroidDevice("moto x ghost", "Motorola Moto X Ghost XT1056", false, "4.4.4");

                os = "ANDROID";
                osVersion = "4.4.4";
                manufacturer = "Motorola";
                break;
            case TD_SONY_ERICSON_XPERIA_Z1:
                getTestDroidAndroidDevice("sony xperiz z1", "Sony Xperia Z1 Compact D5503", false, "4.4.4");

                os = "ANDROID";
                osVersion = "4.4.4";
                manufacturer = "Sony";
                break;
            case TD_LGW_OPTIMUS_L5:
                getTestDroidAndroidDevice("optimuzs L5", "LG Optimus L5 Dual E615", false, "4.0.4");

                os = "ANDROID";
                osVersion = "4.0.4";
                manufacturer = "LG";
            default:
                break;
        }
        return appiumDriver;
    }

    public static WebDriver getWebDriver(Browser browser)
    {
        switch (browser)
        {
            case FIREFOX:
                getFirefoxBrowser();
                os = "localSys";
                osVersion = "localSys";
                manufacturer = "computer";
                break;
            case CHROME:
                getChromeBrowser();
                os = "localSys";
                osVersion = "localSys";
                manufacturer = "computer";
                break;
            case SAFARI:
                getSafariBrowser();
                os = "localSys";
                osVersion = "localSys";
                manufacturer = "computer";
                break;
            case IE:
                getIEBrowser();
                os = "localSys";
                osVersion = "localSys";
                manufacturer = "computer";
                break;
            case REMOTE_CHROME:
                getRemoteChromeBrowser();
                break;
            case REMOTE_FIREFOX:
                getRemoteFirefoxBrowser();
                break;
            case REMOTE_INTERNETEXPLORER:
                getRemoteIEBrowser();
                break;
            case REMOTE_SAFARI:
                getRemoteSafariBrowser();
                break;
            case REMOTE_SIMULATED_IPAD:
                getRemoteFakeDevice(
                    "Mozilla/5.0 (iPad; CPU OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9B176 Safari/7534.48.3",
                    2048,
                    1496);

                break;
            case LOCAL_SIMULATED_IPAD_THREE:
                getLocalFakeDevice(
                    "Mozilla/5.0 (iPad; CPU OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9B176 Safari/7534.48.3",
                    2048,
                    1496);

                break;
            case REMOTE_SIMULATED_NEXUS_SEVEN:
                getRemoteFakeDevice(
                    "Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19",
                    966,
                    444);

                break;
            case LOCAL_SIMULATED_NEXUS_SEVEN:
                getLocalFakeDevice(
                    "Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19",
                    966,
                    444);

                break;
            case LOCAL_NEXUS_SEVEN_MOBILE_WEB:
                getNexusSevenMobileWebDriver();
                os = "Android";
                osVersion = "localSys";
                manufacturer = "Google";
                break;
            case LOCAL_NEXUS_NINE_MOBILE_WEB:
                getLocalNexusNineMobileWebDriver();
                os = "Android";
                osVersion = "localSys";
                manufacturer = "Google";
                break;
            case SIM_IPAD_MOBILE_WEB:
                getLocalIpadSimulatedMobileWebDriver();
                break;
            case LOCAL_IPHONE_MOBILE_WEB:
                getLocalIphoneMobileWebDriver();
                break;
            case LOCAL_IPAD_MOBILE_WEB:
                getLocalIpadMobileWebDriver();
                break;
            case SIM_IPHONE_MOBILE_WEB:
                getLocalIphoneSimulatedMobileWebDriver();
                break;
            case APPTHWACK_SAFARI_IOS:
                getAppThwackSafariIpad();
                break;
            case TD_NEXUS_SEVEN_CHROME_WEB:
                getTestDroidNexusSevenChromeWeb();
                break;
            case BS_FIREFOX_31:
                getBrowserStackBrowser("Firefox", "31.0", "Windows", "7");
                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_CHROME_36:
                getBrowserStackBrowser("Chrome", "36.0", "Windows", "7");
                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_CHROME_39:
                getBrowserStackBrowser("Chrome", "39.0", "Windows", "7");
                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_SAFARI_7_MAC:
                getBrowserStackBrowser("Safari", "7.0", "OS X", "Mavericks");
                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_IE_10:
                getBrowserStackBrowser("IE", "10.0", "Windows", "7");
                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_IE_11:
                getBrowserStackBrowser("IE", "11.0", "Windows", "7");
                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_FIREFOX_35_LS:
                getBrowserStackBrowser("Firefox", "35.0", "Windows", "7", "2048x1536");

                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_CHROME_39_LS:
                getBrowserStackBrowser("Chrome", "39.0", "Windows", "7", "2048x1536");

                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_IE_11_LS:
                getBrowserStackBrowser("IE", "11.0", "Windows", "7", "2048x1536");

                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_FIREFOX_31_WIN_7:
                getBrowserStackBrowser("Firefox", "31.0", "Windows", "7", "2048x1536");

                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_FIREFOX_36_WIN_7:
                getBrowserStackBrowser("Firefox", "36.0", "Windows", "7", "2048x1536");

                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_FIREFOX_37_WIN_7:
                getBrowserStackBrowser("Firefox", "37.0", "Windows", "7", "2048x1536");

                os = "Window";
                osVersion = "7";
                manufacturer = "PC";
                break;
            case BS_FIREFOX_36_WIN_8:
                getBrowserStackBrowser("Firefox", "36.0", "Windows", "8", "2048x1536");

                os = "Window";
                osVersion = "8";
                manufacturer = "PC";
                break;
            case BS_FIREFOX_37_WIN_8:
                getBrowserStackBrowser("Firefox", "37.0", "Windows", "8", "2048x1536");

                os = "Window";
                osVersion = "8";
                manufacturer = "PC";
                break;
            case BS_FIREFOX_31_WIN_XP:
                getBrowserStackBrowser("Firefox", "31.0", "Windows", "XP", "2048x1536");

                os = "Window";
                osVersion = "XP";
                manufacturer = "PC";
                break;
            case BS_FIREFOX_31_OSX_YOSEMITE:
                getBrowserStackBrowser("Firefox", "31.0", "OS X", "Yosemite", "1920x1080");

                os = "iOS";
                osVersion = "Yosemite";
                manufacturer = "Mac";
                break;
            case BS_FIREFOX_31_OSX_MAVERICKS:
                getBrowserStackBrowser("Firefox", "31.0", "OS X", "Mavericks", "1920x1080");

                os = "iOS";
                osVersion = "Maavericks";
                manufacturer = "Mac";
                break;
            case BS_CHROME_39_WIN_7:
                getBrowserStackBrowser("Chrome", "39.0", "Windows", "7", "2048x1536");

                os = "Windows";
                osVersion = "7";
                manufacturer = "PC";

                break;
            case BS_CHROME_39_WIN_XP:
                getBrowserStackBrowser("Chrome", "39.0", "Windows", "XP", "2048x1536");

                os = "Windows";
                osVersion = "XP";
                manufacturer = "PC";

                break;
            case BS_CHROME_39_OSX_YOSEMITE:
                getBrowserStackBrowser("Chrome", "39.0", "OS X", "Yosemite", "2048x1536");

                os = "iOS";
                osVersion = "Yosemite";
                manufacturer = "Mac";

                break;
            case BS_CHROME_39_OSX_MAVERICKS:
                getBrowserStackBrowser("Chrome", "39.0", "OS X", "Mavericks", "2048x1536");

                os = "iOS";
                osVersion = "Maverics";
                manufacturer = "Mac";

                break;
            case BS_SAFARI_51_WIN_XP:
                getBrowserStackBrowser("Safari", "5.1", "Windows", "XP", "2048x1536");

                os = "Windows";
                osVersion = "XP";
                manufacturer = "PC";

                break;
            case BS_SAFARI_51_WIN_7:
                getBrowserStackBrowser("Safari", "5.1.0", "Windows", "7", "2048x1536");

                os = "Windows";
                osVersion = "7";
                manufacturer = "PC";

                break;
            case BS_SAFARI_8_OSX_YOSEMITE:
                getBrowserStackBrowser("Safari", "8.0", "OS X", "Yosemite", "2048x1536");

                os = "iOS";
                osVersion = "Yosemite";
                manufacturer = "Mac";

                break;
            case BS_SAFARI_7_OSX_MAVERICKS:
                getBrowserStackBrowser("Safari", "7.0", "OS X", "Mavericks", "2048x1536");

                os = "iOS";
                osVersion = "Maverics";
                manufacturer = "Mac";

                break;
            case BS_IE_11_WIN_7:
                getBrowserStackBrowser("IE", "11.0", "Windows", "7", "2048x1536");
                os = "Windows";
                osVersion = "7";
                manufacturer = "PC";

                break;
            case BS_IE_10_WIN_7:
                getBrowserStackBrowser("IE", "10.0", "Windows", "7", "2048x1536");
                os = "Windows";
                osVersion = "7";
                manufacturer = "PC";

                break;
            case BS_IE_11_WIN_81:
                getBrowserStackBrowser("IE", "11.0", "Windows", "8.1", "2048x1536");
                os = "Windows";
                osVersion = "8.1";
                manufacturer = "PC";

                break;
            case TD_IPAD_SAFARI_WEB:
                getTestDroidIPadSafariWeb();
            default:
                break;
        }
        return driver;
    }

    public static void getBrowserStackBrowser(String browser, String version, String os1, String osVersion1)
    {
        String URL = "http://" + env.getBrowserStackUserName()
            + ":"
            + env.getBrowserStackKey()
            + "@hub.browserstack.com/wd/hub";
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browser", browser);
        caps.setCapability("browser_version", version);
        caps.setCapability("os", os1);
        caps.setCapability("os_version", osVersion1);
        caps.setCapability("acceptSslCerts", true);
        caps.setCapability("name", getRunId());
        if (env.turnOnDebug().booleanValue())
        {
            logger.info("Setting BS to debug.");
            caps.setCapability("browserstack.debug", "true");
        }
        try
        {
            driver = new RemoteWebDriver(new URL(URL), caps);
            driver.manage().window().setSize(new Dimension(1440, 1600));
            driver.manage().window().setPosition(new Point(0, 0));
            env.setIsRemoteTestRun(true);
        }
        catch (MalformedURLException e)
        {
            logger.error("Exception generated when tgrying to create the Remote WebDriver.");
            e.printStackTrace();
        }
        userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;", new Object[0]);
        logger.info(
            String.format("BrowserStack [%s] Driver is initialized with user agent [%s].", new Object[]{browser, userAgent}));

        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    public static void getBrowserStackBrowser(String browser, String version, String os1, String osVersion1, String resolution)
    {
        String URL = "http://" + env.getBrowserStackUserName()
            + ":"
            + env.getBrowserStackKey()
            + "@hub.browserstack.com/wd/hub";
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browser", browser);
        caps.setCapability("browser_version", version);
        caps.setCapability("os", os1);
        caps.setCapability("os_version", osVersion1);
        caps.setCapability("browserstack.debug", "true");
        caps.setCapability("resolution", resolution);
        caps.setCapability("name", getRunId());
        caps.setCapability("acceptSslCerts", "true");
        caps.setCapability("acceptSslCerts", true);
        if (env.turnOnDebug().booleanValue())
        {
            caps.setCapability("browserstack.debug", "true");
        }
        try
        {
            driver = new RemoteWebDriver(new URL(URL), caps);
            driver.manage().window().setSize(new Dimension(1440, 1600));
            driver.manage().window().setPosition(new Point(0, 0));
            env.setIsRemoteTestRun(true);
        }
        catch (MalformedURLException e)
        {
            logger.error("Exception generated when tgrying to create the Remote WebDriver.");
            e.printStackTrace();
        }
        userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;", new Object[0]);
        logger.info(
            String.format("BrowserStack [%s] Driver is initialized with user agent [%s].", new Object[]{browser, userAgent}));

        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    public static void getTestDroidIPadSafariWeb()
    {
        env.setIsMobileTest();
        env.setIsRemoteTestRun(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("device", "iPad");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPad");
            capabilities.setCapability("browserName", "Safari");
            capabilities.setCapability("testdroid_username", "brock@applausemail.com");

            capabilities.setCapability(MobileCapabilityType.APP, "safari");
            capabilities.setCapability("testdroid_password", "@pplause1");

            capabilities.setCapability("testdroid_project", "Mastercard");
            capabilities.setCapability("testdroid_description", "Mastercard");
            capabilities.setCapability("testdroid_testrun", getRunId());
            capabilities.setCapability("testdroid_device", "iPhone 4S A1387 6.1.3");

            capabilities.setCapability("testdroid_app", "sample/BitbarIOSSample.ipa");

            capabilities.setCapability("testdroid_target", "iOS");

            logger.info("Capabilities:" + capabilities.toString());
            appiumDriver = new IOSDriver(new URL("http://appium.testdroid.com/wd/hub"), capabilities);
        }
        catch (Exception e)
        {
            logger.info("Error Occured - " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void getTestDroidNexusSevenChromeWeb()
    {
        env.setIsMobileTest();
        env.setIsRemoteTestRun(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("testdroid_username", "brock@applausemail.com");

            capabilities.setCapability("testdroid_password", "@pplause1");

            capabilities.setCapability("testdroid_target", "chrome");
            capabilities.setCapability("testdroid_project", "New Project");
            capabilities.setCapability("testdroid_testrun", getRunId());
            capabilities.setCapability("testdroid_device", "Asus Google Nexus 7 (2013) ME571KL 4.4.4");

            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "AndroidDevice");
            capabilities.setCapability("browserName", "chrome");

            driver = new AndroidDriver(new URL("http://appium.testdroid.com/wd/hub"), capabilities);
        }
        catch (Exception e)
        {
            logger.info("Error Occured - " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void getTestDroidNexusSeven()
    {
        env.setIsRemoteTestRun(true);
        env.setIsMobileIOS(false);
        env.setIsMobileTest();
        env.setIsAndroidTablet(true);
        env.setIsTablet(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability("platformVersion", "4.4.4");
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, env.getAppPackage());
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, env.getAppActivity());
            capabilities.setCapability("device", "android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Nexus 7");
            capabilities.setCapability("testdroid_username", env
                .getTestDroidUserName());
            capabilities.setCapability("testdroid_password", env
                .getTestDroidPassword());
            capabilities.setCapability("testdroid_project", env
                .getTestDroidProjectName());
            capabilities.setCapability("testdroid_description", env
                .getTestDroidProjectDescription());
            capabilities.setCapability("testdroid_testrun", getRunId());
            capabilities.setCapability("testdroid_device", "Asus Google Nexus 7 (2013) ME571KL 4.4.4");

            capabilities.setCapability("testdroid_app", "latest");
            capabilities.setCapability("testdroid_target", "Android");
            capabilities.setCapability(MobileCapabilityType.APP, env.getAppPackage());
            logger.info("About to create driver from Test Droid.");
            appiumDriver = new AndroidDriver(new URL("http://appium.testdroid.com/wd/hub"), capabilities);

            logger.info(String.format("created  [%s]", new Object[]{appiumDriver
                .getRemoteAddress()}));
            env.setIsRemoteTestRun(true);
        }
        catch (Exception e)
        {
            logger.info("Error Occured - " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void getTestDroidAndroidDevice(String deviceName, String testDroidDeviceName, boolean isAndroidTablet,
        String platformVersion)
    {
        env.setIsRemoteTestRun(true);
        env.setIsMobileIOS(false);
        env.setIsMobileAndroid(true);
        env.setIsMobileTest();
        env.setIsAndroidTablet(isAndroidTablet);
        env.setIsTablet(isAndroidTablet);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability("platformVersion", platformVersion);
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, env.getAppPackage());
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, env.getAppActivity());
            capabilities.setCapability("device", "android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            capabilities.setCapability("testdroid_username", env
                .getTestDroidUserName());
            capabilities.setCapability("testdroid_password", env
                .getTestDroidPassword());
            capabilities.setCapability("testdroid_project", env
                .getTestDroidProjectName());
            capabilities.setCapability("testdroid_description", env
                .getTestDroidProjectDescription());
            capabilities.setCapability("testdroid_testrun", getRunId());
            capabilities.setCapability("testdroid_device", testDroidDeviceName);
            capabilities.setCapability("testdroid_app", "latest");
            capabilities.setCapability("testdroid_target", "Android");
            capabilities.setCapability(MobileCapabilityType.APP, env.getAppPackage());

            logger.info("About to create driver from Test Droid.");
            appiumDriver = new AndroidDriver(new URL("http://appium.testdroid.com/wd/hub"), capabilities);

            logger.info(String.format("created  [%s]", new Object[]{appiumDriver
                .getRemoteAddress()}));
            env.setIsRemoteTestRun(true);
        }
        catch (Exception e)
        {
            logger.info("Error Occured - " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void getPrivateCloudTestDroidAndroidDevice(String deviceName, String testDroidDeviceName,
        boolean isAndroidTablet, String platformVersion)
    {
        env.setIsRemoteTestRun(true);
        env.setIsMobileIOS(false);
        env.setIsMobileAndroid(true);
        env.setIsMobileTest();
        env.setIsAndroidTablet(isAndroidTablet);
        env.setIsTablet(isAndroidTablet);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability("platformVersion", platformVersion);
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, env.getAppPackage());
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, env.getAppActivity());
            capabilities.setCapability("device", "android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            capabilities.setCapability("testdroid_username", env
                .getTestDroidUserName());
            capabilities.setCapability("testdroid_password", env
                .getTestDroidPassword());
            capabilities.setCapability("testdroid_project", env
                .getTestDroidProjectName());
            capabilities.setCapability("testdroid_description", env
                .getTestDroidProjectDescription());

            capabilities.setCapability("testdroid_testrun", getRunId() + "^^" + env
                .getBrowser());
            capabilities.setCapability("testdroid_device", testDroidDeviceName);
            capabilities.setCapability("testdroid_app", "latest");
            capabilities.setCapability("testdroid_target", "Android");
            capabilities.setCapability(MobileCapabilityType.APP, env.getAppPackage());
            if (env.getUseKeystore())
            {
                capabilities.setCapability("useKeystore", true);
                capabilities.setCapability("keystorePath", env
                    .getKeystorePath());
                capabilities.setCapability("keystorePassword", env
                    .getKeystorePassword());
                capabilities.setCapability("keyAlias", env.getAlias());
                capabilities.setCapability("keyPassword", env.getKeyPassword());
            }
            logger.info("About to create driver from Test Droid.");
            appiumDriver = new AndroidDriver(new URL("http://appium.testdroid.com:8083/wd/hub"), capabilities);

            logger.info(String.format("created  [%s]", new Object[]{appiumDriver
                .getRemoteAddress()}));
            env.setIsRemoteTestRun(true);
        }
        catch (Exception e)
        {
            logger.info("Error Occured - " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void getTestDroidNexusFive()
    {
        env.setIsRemoteTestRun(true);
        env.setIsMobileAndroid(true);
        env.setIsPhone(true);
        env.setIsAndroidTablet(false);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability("platformVersion", "4.4");
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, env.getAppPackage());
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, env.getAppActivity());
            capabilities.setCapability("device", "android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Nexus 5");
            capabilities.setCapability("testdroid_username", env
                .getTestDroidUserName());
            capabilities.setCapability("testdroid_password", env
                .getTestDroidPassword());
            capabilities.setCapability("testdroid_project", env
                .getTestDroidProjectName());
            capabilities.setCapability("testdroid_description", env
                .getTestDroidProjectDescription());
            capabilities.setCapability("testdroid_testrun", getRunId() + "^^" + env
                .getBrowser());
            capabilities.setCapability("testdroid_device", "LG Google Nexus 5 D821 4.4");

            capabilities.setCapability("testdroid_app", "latest");
            capabilities.setCapability("testdroid_target", "Android");
            capabilities.setCapability(MobileCapabilityType.APP, env.getAppPackage());
            logger.info("About to create driver from Test Droid.");
            appiumDriver = new AndroidDriver(new URL("http://appium.testdroid.com/wd/hub"), capabilities);

            logger.info(String.format("created  [%s]", new Object[]{appiumDriver
                .getRemoteAddress()}));
            env.setIsRemoteTestRun(true);
        }
        catch (Exception e)
        {
            logger.info("Error Occured - " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void getTestDroidGalaxySThree()
    {
        env.setIsRemoteTestRun(true);
        env.setIsMobileAndroid(true);
        env.setIsPhone(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability("platformVersion", "4.3");
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, env.getAppPackage());
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, env.getAppActivity());
            capabilities.setCapability("device", "android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Nexus 7");
            capabilities.setCapability("testdroid_username", env
                .getTestDroidUserName());
            capabilities.setCapability("testdroid_password", env
                .getTestDroidPassword());
            capabilities.setCapability("testdroid_project", env
                .getTestDroidProjectName());
            capabilities.setCapability("testdroid_description", env
                .getTestDroidProjectDescription());
            capabilities.setCapability("testdroid_testrun", getRunId() + "^^" + env
                .getBrowser());
            capabilities.setCapability("testdroid_device", "Samsung Galaxy S III GT-I9300");

            capabilities.setCapability("testdroid_app", "latest");
            capabilities.setCapability("testdroid_target", "Android");
            capabilities.setCapability(MobileCapabilityType.APP, env.getAppPackage());
            logger.info("About to create driver from Test Droid.");
            appiumDriver = new AndroidDriver(new URL("http://appium.testdroid.com/wd/hub"), capabilities);

            logger.info(String.format("created  [%s]", new Object[]{appiumDriver
                .getRemoteAddress()}));
            env.setIsRemoteTestRun(true);
        }
        catch (Exception e)
        {
            logger.info("Error Occured - " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void getNexusSevenMobileWebDriver()
    {
        env.setIsMobileTest();
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browserName", "chrome");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "nexus_seven");
            capabilities.setCapability("platformVersion", "5.0");
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, "com.android.chrome");
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, ".Main");

            driver = new RemoteWebDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalNexusNineMobileWebDriver()
    {
        env.setIsMobileTest();
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browserName", "Chrome");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "nexus_nine");
            capabilities.setCapability("platformVersion", "5.0.1");

            driver = new RemoteWebDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalIpadSimulatedMobileWebDriver()
    {
        env.setIsMobileTest();
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browserName", "safari");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPad 2");
            capabilities.setCapability("platformVersion", "8.2");
            capabilities.setCapability("uuid", "66cd479f4a78256cfec6e6e271765992e35ef5b5");

            driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalIpadMobileWebDriver()
    {
        env.setIsMobileTest();
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("bundleid", "com.apple.mobilesafari");
            capabilities.setCapability("browserName", "Safari");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPad Mini");
            capabilities.setCapability("platformVersion", "8.3");
            capabilities.setCapability("udid", "66cd479f4a78256cfec6e6e271765992e35ef5b5");

            driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

            driver.get("https://itf.masterpass.mastercard.com/");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalIphoneMobileWebDriver()
    {
        env.setIsMobileTest();
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browserName", "safari");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone");
            capabilities.setCapability("platformVersion", "8.3");
            capabilities.setCapability("udid", "52877fb3164cd7a8d53c708d2bf3ee78a15772d0");

            driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getIpadMobileWebDriver()
    {
        env.setIsMobileTest();
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("bundleid", "com.apple.mobilesafari");
            capabilities.setCapability("browserName", "Safari");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPad Mini");
            capabilities.setCapability("platformVersion", "8.3");
            capabilities.setCapability("udid", "66cd479f4a78256cfec6e6e271765992e35ef5b5");

            driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

            driver.get("https://itf.masterpass.mastercard.com/");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalSimulatorIphoneFiveSDriver()
    {
        env.setIsMobileTest();
        env.setIsMobileIOS(true);
        env.setIsPhone(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 5s");
            capabilities.setCapability(MobileCapabilityType.APP, env.getTargetAppPath());
            capabilities.setCapability("platformVersion", env.getPlatformVersion());

            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());

            appiumDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

            logger.info(String.format("Created local iPhone 5s Simulator with [%s].", new Object[]{env

                .getTargetAppPath()}));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalSimulatorIphoneSixDriver()
    {
        env.setIsMobileTest();
        env.setIsMobileIOS(true);
        env.setIsPhone(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 6");
            capabilities.setCapability("platformVersion", env.getPlatformVersion());
            capabilities.setCapability(MobileCapabilityType.APP, env.getTargetAppPath());

            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());

            appiumDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

            logger.info(String.format("Created local iPhone 6 Simulator with [%s].", new Object[]{env

                .getTargetAppPath()}));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalGenericDriverFromFile()
    {
        Properties props = new Properties();

        String driverPropertiesFile = System.getProperty("driverPropertiesFile");
        try
        {
            FileInputStream input = new FileInputStream(driverPropertiesFile);
            props.load(input);
        }
        catch (IOException e)
        {
            logger.error("Exception encountered loading properties from file named:" + driverPropertiesFile);
        }
        env.setIsMobileTest();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        for (Map.Entry<Object, Object> entry : props.entrySet())
        {
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (key.startsWith("applause_"))
            {
                String applauseKey = key.substring("applause_".length());
                Boolean boolValue = Boolean.valueOf(value.toString());
                if (applauseKey.equals("isMobileIOS"))
                {
                    env.setIsMobileIOS(boolValue.booleanValue());
                    logger.info("Setting isMobileIOS to " + boolValue);
                }
                else if (applauseKey.equals("isSimulator"))
                {
                    env.setIsSimulator(boolValue.booleanValue());
                    logger.info("Setting isSimulator to " + boolValue);
                }
                else if (applauseKey.equals("isTablet"))
                {
                    env.setIsTablet(boolValue.booleanValue());
                    logger.info("Setting isTablet to " + boolValue);
                }
                else if (applauseKey.equals("isPhablet"))
                {
                    env.setIsPhablet(boolValue.booleanValue());
                    logger.info("Setting isPhablet to " + boolValue);
                }
                else
                {
                    logger.warn("Invalid Applause flag specified: " + applauseKey + ". Ignoring...");
                }
            }
            else
            {
                capabilities.setCapability(key, value);
            }
        }
        String platformName = props.getProperty(MobileCapabilityType.PLATFORM_NAME);
        try
        {
            if (platformName.equals("iOS"))
            {
                env.setIsMobileIOS(true);
                appiumDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
            }
            else
            {
                env.setIsMobileIOS(false);
                appiumDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        String deviceName = props.getProperty(MobileCapabilityType.DEVICE_NAME);
        String appPath = props.getProperty(MobileCapabilityType.APP);
        logger.info(String.format("Created device [%s] using [%s].", new Object[]{deviceName, appPath}));
    }

    public static void getLocalGenericDriver()
    {
        Properties props = new Properties();
        try
        {
            InputStream inputStream = WebDriverWrapper.class.getClassLoader().getResourceAsStream("GenericDriver.properties");
            props.load(inputStream);
        }
        catch (IOException e)
        {
            logger.error("Problem encountered trying to load generic properties");
            e.printStackTrace();
        }
        String platformName = props.getProperty(MobileCapabilityType.PLATFORM_NAME);
        String platformVersion = props.getProperty("platformVersion");
        String deviceName = props.getProperty(MobileCapabilityType.DEVICE_NAME);
        String app = props.getProperty(MobileCapabilityType.APP);
        String noReset = props.getProperty("noReset");

        String appActivity = props.getProperty("appActivity");
        String appPackage = props.getProperty("appPackage");
        String useKeystore = props.getProperty("useKeystore");
        String keystorePath = props.getProperty("keystorePath");

        String bundleId = props.getProperty("bundleId");
        String udid = props.getProperty("udid");
        String autoAcceptAlerts = props.getProperty("autoAcceptAlerts");
        String autoDismissAlerts = props.getProperty("autoDismissAlerts");

        String locationServicesEnabled = props.getProperty("locationServicesEnabled");

        env.setIsMobileTest();
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformVersion);
            if (platformName.length() > 0)
            {
                capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
            }
            if (deviceName.length() > 0)
            {
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            }
            if (app.length() > 0)
            {
                capabilities.setCapability(MobileCapabilityType.APP, app);
            }
            if (noReset.length() > 0)
            {
                capabilities.setCapability("noReset", noReset);
            }
            if (appActivity.length() > 0)
            {
                capabilities.setCapability("appActivity", appActivity);
            }
            if (appPackage.length() > 0)
            {
                capabilities.setCapability("appPackage", appPackage);
            }
            if (useKeystore.length() > 0)
            {
                capabilities.setCapability("useKeystore", useKeystore);
            }
            if (keystorePath.length() > 0)
            {
                capabilities.setCapability("keystorePath", keystorePath);
            }
            if (bundleId.length() > 0)
            {
                capabilities.setCapability("bundleId", bundleId);
            }
            if (udid.length() > 0)
            {
                capabilities.setCapability("udid", udid);
            }
            if (autoAcceptAlerts.length() > 0)
            {
                capabilities.setCapability("autoAcceptAlerts", autoAcceptAlerts);
            }
            if (autoDismissAlerts.length() > 0)
            {
                capabilities.setCapability("autoDismissAlerts", autoDismissAlerts);
            }
            if (locationServicesEnabled.length() > 0)
            {
                capabilities.setCapability("locationServicesEnabled", locationServicesEnabled);
            }
            if (platformName.equals("iOS"))
            {
                env.setIsMobileIOS(true);
                appiumDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
            }
            else
            {
                env.setIsMobileIOS(false);
                appiumDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
            }
            logger.info(String.format("Created local [%s] with [%s].", new Object[]{deviceName, env
                .getTargetAppPath()}));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalSimulatorIphoneSixPlusDriver()
    {
        env.setIsMobileTest();
        env.setIsMobileIOS(true);
        env.setIsPhone(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 6 Plus");
            capabilities.setCapability(MobileCapabilityType.APP, env.getTargetAppPath());
            capabilities.setCapability(MobileCapabilityType.APP, env.getPlatformVersion());

            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());

            appiumDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

            logger.info(String.format("Created local iPhone 6 Plus Simulator with [%s].", new Object[]{env

                .getTargetAppPath()}));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalSimulatorIphoneFiveDriver()
    {
        env.setIsMobileTest();
        env.setIsMobileIOS(true);
        env.setIsPhone(true);
        env.setIsSimulator(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 5");
            capabilities.setCapability("platformVersion", "7.1");
            capabilities.setCapability(MobileCapabilityType.APP, env.getTargetAppPath());

            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());

            appiumDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

            logger.info(String.format("Created local iPhone 5 Simulator with [%s].", new Object[]{env

                .getTargetAppPath()}));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalSimulatorIpadDriver()
    {
        env.setIsMobileTest();
        env.setIsMobileIOS(true);
        env.setIsTablet(true);
        env.setIsSimulator(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability("platformVersion", "9.2");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPad Air");
            capabilities.setCapability(MobileCapabilityType.APP, env.getTargetAppPath());

            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());

            appiumDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

            logger.info(String.format("Created local iPhone Simulator with [%s].", new Object[]{env

                .getTargetAppPath()}));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalIpadDeviceDriver()
    {
        env.setIsMobileTest();
        env.setIsMobileIOS(true);
        env.setIsTablet(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, env.getDeviceName());
            capabilities.setCapability(MobileCapabilityType.APP, env.getTargetAppPath());
            capabilities.setCapability("udid", env.getUdid());
            capabilities.setCapability("bundleId", env.getIosBundleId());
            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());

            capabilities.setCapability("waitForAppScript", "$.delay(10000); true;");

            appiumDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

            logger.info(String.format("Created local iPad Device with [%s].", new Object[]{env
                .getTargetAppPath()}));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalIphoneDeviceDriver()
    {
        env.setIsMobileTest();
        env.setIsMobileIOS(true);
        env.setIsPhone(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, env.getDeviceName());
            capabilities.setCapability(MobileCapabilityType.APP, env.getTargetAppPath());
            capabilities.setCapability("udid", env.getUdid());
            capabilities.setCapability("bundleId", env.getIosBundleId());
            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());

            appiumDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

            logger.info(String.format("Created local iPhone Device with [%s].", new Object[]{env
                .getTargetAppPath()}));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getLocalIphoneSimulatedMobileWebDriver()
    {
        env.setIsMobileTest();
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browserName", "safari");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPad Air");
            capabilities.setCapability("platformVersion", "8.3");
            capabilities.setCapability(MobileCapabilityType.APP, "safari");
            driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getAppThwackSafariIpad()
    {
        env.setIsMobileTest();
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("takesScreenshot", true);
            capabilities.setCapability("browserName", "Safari");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "ios");
            capabilities.setCapability("automationName", "appium");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Apple iPad 4");
            capabilities.setCapability("platformVersion", "7.1");
            capabilities.setCapability(MobileCapabilityType.APP, Integer.valueOf(env.getAppThwackAppId()));
            capabilities.setCapability("apiKey", env.getAppThwackApiKey());
            capabilities.setCapability("project", Integer.valueOf(env.getAppThwackProjectId()));
            logger.info("About to call driver...");
            driver = new IOSDriver(new URL("https://appthwack.com/wd/hub"), capabilities);

            logger.info("After get driver call");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void getRemoteFakeDevice(String userAgent1, int width, int height)
    {
        try
        {
            dc.setBrowserName("firefox");
            dc.setCapability("takesScreenshot", true);

            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("general.useragent.override", userAgent1);
            dc.setCapability("firefox_profile", profile);

            driver = new ScreenShotRemoteWebDriver(new URL(env.getWebDriverGridURL()), dc);
            Dimension windowSize = new Dimension(width, height);

            driver.manage().window().setSize(windowSize);

            String returnedUserAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;",
                new Object[0]);
            logger.info("Remote Fake Device with " + width
                + "x"
                + height
                + " Initialized with user agent ["
                + returnedUserAgent
                + "]");
        }
        catch (MalformedURLException e)
        {
            logger.warn("Exception creating Remote Fake Device");
            e.printStackTrace();
        }
    }

    private static void getLocalFakeDevice(String userAgent1, int width, int height)
    {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("general.useragent.override", userAgent1);
        driver = new FirefoxDriver(profile);

        Dimension windowSize = new Dimension(width, height);
        driver.manage().window().setSize(windowSize);
    }

    public static void getFirefoxBrowser()
    {
        System.setProperty("webdriver.assume.untrusted.issuer", "false");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();

        userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;", new Object[0]);
        logger.info("Firefox Driver is initialized with user agent [" + userAgent + "]");

        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    public static void getChromeBrowser()
    {
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1440, 1600));
        driver.manage().window().setPosition(new Point(0, 0));

        userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;", new Object[0]);
        logger.info("Chrome Driver is initialized with user agent [" + userAgent + "]");

        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    public static void getSafariBrowser()
    {
        System.setProperty("webdriver.assume.untrusted.issuer", "false");
        driver = new SafariDriver();
        driver.manage().window().maximize();

        userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;", new Object[0]);
        logger.info("Safari Driver is initialized with user agent [" + userAgent + "]");

        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    public static void getRemoteSafariBrowser()
    {
        try
        {
            dc.setBrowserName("safari");
            dc.setCapability("takesScreenshot", true);

            driver = new ScreenShotRemoteWebDriver(new URL(env.getWebDriverGridURL()), dc);
            driver.manage().window().maximize();

            userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;", new Object[0]);
            logger.info("Remote Safari Initialized with user agent [" + userAgent + "]");
        }
        catch (MalformedURLException e)
        {
            logger.warn("Exception creating Remote Safari Driver");
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    public static void getRemoteChromeBrowser()
    {
        try
        {
            dc.setBrowserName("chrome");
            dc.setCapability("takesScreenshot", true);

            driver = new ScreenShotRemoteWebDriver(new URL(env.getWebDriverGridURL()), dc);

            userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;", new Object[0]);
            logger.info("Remote Chrome Initialized with user agent [" + userAgent + "]");
        }
        catch (MalformedURLException e)
        {
            logger.warn("Exception creating Remote Chrome Driver");
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    public static void getRemoteFirefoxBrowser()
    {
        try
        {
            dc.setBrowserName("firefox");
            dc.setCapability("takesScreenshot", true);

            driver = new ScreenShotRemoteWebDriver(new URL(env.getWebDriverGridURL()), dc);
            driver.manage().window().maximize();

            userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;", new Object[0]);
            logger.info("Remote Firefox Initialized with user agent [" + userAgent + "]");
        }
        catch (MalformedURLException e)
        {
            logger.warn("Exception creating Remote Firefox Driver");
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    public static void getRemoteIEBrowser()
    {
        try
        {
            dc = DesiredCapabilities.internetExplorer();

            dc.setCapability("takesScreenshot", true);
            dc.setCapability("ensureCleanSession", true);

            dc.setCapability("ignoreProtectedModeSetting", true);

            driver = new ScreenShotRemoteWebDriver(new URL(env.getWebDriverGridURL()), dc);

            userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;", new Object[0]);
            logger.info("Remote Internet Explorer Initialized with user agent [" + userAgent + "]");
        }
        catch (MalformedURLException e)
        {
            logger.error("Exception in Remote IE - " + e.getMessage());
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    public static void getIEBrowser()
    {
        driver = new InternetExplorerDriver();
        driver.manage().window().setSize(new Dimension(1440, 1600));
        driver.manage().window().setPosition(new Point(0, 0));

        userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;", new Object[0]);
        logger.info("Chrome Driver is initialized with user agent [" + userAgent + "]");

        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    public static void getNexusSevenDriver()
    {
        env.setIsMobileTest();
        env.setIsMobileAndroid(true);

        env.setIsPhablet(true);
        env.setIsSmallTablet(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browserName", "");
            capabilities.setCapability("browserName", "selendroid");

            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Nexus 7");
            capabilities.setCapability(MobileCapabilityType.APP, env.getTargetAppPath());
            capabilities.setCapability("noSign", env.getNoSign());
            capabilities.setCapability("platformVersion", "4.4.4");
            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, env.getAppPackage());
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, env.getAppActivity());
            appiumDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getAndroidMobileDriver()
    {
        env.setIsMobileTest();
        env.setIsPhone(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browserName", "");
            capabilities.setCapability("browserName", "selendroid");

            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "nexus_seven");
            capabilities.setCapability(MobileCapabilityType.APP, env.getTargetAppPath());
            capabilities.setCapability("noSign", env.getNoSign());
            capabilities.setCapability("platformVersion", "4.4.2");
            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, env.getAppPackage());
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, env.getAppActivity());
            appiumDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getAndroidTabletDriver()
    {
        env.setIsMobileTest();
        env.setIsAndroidTablet(true);
        env.setIsMobileAndroid(true);
        env.setIsTablet(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, env.getDeviceName());
            capabilities.setCapability(MobileCapabilityType.APP, env.getTargetAppPath());
            capabilities.setCapability("noSign", env.getNoSign());
            capabilities.setCapability("platformVersion", env
                .getPlatformVersion());
            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, env.getAppPackage());
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, env.getAppActivity());
            appiumDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void getAppThwackNexusSevenDriver()
    {
        env.setIsRemoteTestRun(true);
        env.setIsAndroidTablet(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "android");
            capabilities.setCapability("platformVersion", "4.4.4");
            capabilities.setCapability("automationName", "Appium");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Asus Nexus 7 2");
            capabilities.setCapability(MobileCapabilityType.APP, Integer.valueOf(env.getAppThwackAppId()));
            capabilities.setCapability("apiKey", env.getAppThwackApiKey());
            capabilities.setCapability("project", Integer.valueOf(env.getAppThwackProjectId()));
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, env.getAppPackage());
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, env.getAppActivity());
            logger.info("About to create app thwack nexus 7.");
            appiumDriver = new AndroidDriver(new URL("https://appthwack.com/wd/hub"), capabilities);

            env.setIsRemoteTestRun(true);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void getAppThwackNexusFiveDriver()
    {
        env.setIsRemoteTestRun(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "android");
            capabilities.setCapability("platformVersion", "4.4.4");
            capabilities.setCapability("automationName", "Appium");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "LG Nexus 5");
            capabilities.setCapability(MobileCapabilityType.APP, Integer.valueOf(env.getAppThwackAppId()));
            capabilities.setCapability("apiKey", env.getAppThwackApiKey());
            capabilities.setCapability("project", Integer.valueOf(env.getAppThwackProjectId()));
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, env.getAppPackage());
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, env.getAppActivity());
            logger.info("About to create app thwack nexus 5.");
            appiumDriver = new AndroidDriver(new URL("https://appthwack.com/wd/hub"), capabilities);

            env.setIsRemoteTestRun(true);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void getAppThwackIphoneSixDriver()
    {
        env.setIsMobileIOS(true);
        env.setIsMobileTest();
        env.setIsRemoteTestRun(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "ios");
            capabilities.setCapability("platformVersion", "8.0");
            capabilities.setCapability("automationName", "appium");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Apple iPhone 6");
            capabilities.setCapability(MobileCapabilityType.APP, Integer.valueOf(env.getAppThwackAppId()));
            capabilities.setCapability("apiKey", env.getAppThwackApiKey());
            capabilities.setCapability("project", Integer.valueOf(env.getAppThwackProjectId()));
            logger.info("About to create appthwack iphone 6.");
            appiumDriver = new IOSDriver(new URL("https://appthwack.com/wd/hub"), capabilities);

            env.setIsRemoteTestRun(true);
            logger.info("Created appthwack iphone 6");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void getAppThwackIpadFourDriver()
    {
        env.setIsMobileIOS(true);
        env.setIsMobileTest();
        env.setIsRemoteTestRun(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "ios");
            capabilities.setCapability("platformVersion", "8.1");
            capabilities.setCapability("automationName", "appium");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Apple iPad 4");
            capabilities.setCapability(MobileCapabilityType.APP, Integer.valueOf(env.getAppThwackAppId()));
            capabilities.setCapability("apiKey", env.getAppThwackApiKey());
            capabilities.setCapability("project", Integer.valueOf(env.getAppThwackProjectId()));
            logger.info("About to create appthwack ipad 4.");
            appiumDriver = new IOSDriver(new URL("https://appthwack.com/wd/hub"), capabilities);

            env.setIsRemoteTestRun(true);
            logger.info("Created appthwack iphone 6");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void getTestDroidNexusSevenDriver()
    {
        env.setIsRemoteTestRun(true);
        env.setIsMobileAndroid(true);
        env.setIsSmallTablet(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "android");
            capabilities.setCapability("platformVersion", "4.4.4");
            capabilities.setCapability("automationName", "Appium");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Asus Nexus 7 2");
            capabilities.setCapability(MobileCapabilityType.APP, "121794");
            capabilities.setCapability("apiKey", "EsfCWchlY8twR949UJMmGJYhH83uckf9St89KCGA");

            capabilities.setCapability("project", "24527");
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, "com.fox.now");
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, "com.fox.now.SplashScreenActivity");

            logger.info("About to create driver.");
            appiumDriver = new AndroidDriver(new URL("https://appthwack.com/wd/hub"), capabilities);

            logger.info(String.format("created  [%s]", new Object[]{appiumDriver
                .getRemoteAddress()}));
            env.setIsRemoteTestRun(true);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void getTDIphoneSixDriver()
    {
        env.setIsRemoteTestRun(true);
        env.setIsMobileTest();
        env.setIsMobileIOS(true);
        env.setIsPhone(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability("device", "iphone");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iOS Phone");
            capabilities.setCapability("testdroid_username", env
                .getTestDroidUserName());
            capabilities.setCapability("testdroid_password", env
                .getTestDroidPassword());
            capabilities.setCapability("testdroid_project", env
                .getTestDroidProjectName());
            capabilities.setCapability("testdroid_description", env
                .getTestDroidProjectDescription());
            capabilities.setCapability("testdroid_testrun", getRunId() + "^^" + env
                .getBrowser());
            capabilities
                .setCapability("testdroid_device", "iPhone 6 A1586 8.2");
            capabilities.setCapability("testdroid_app", "latest");
            capabilities.setCapability("testdroid_target", "iOS");
            capabilities.setCapability(MobileCapabilityType.APP, env.getIosBundleId());
            logger.info("About to create driver from Test Droid.");
            appiumDriver = new IOSDriver(new URL("http://appium.testdroid.com/wd/hub"), capabilities);

            logger.info(String.format("created  [%s]", new Object[]{appiumDriver
                .getRemoteAddress()}));
            env.setIsRemoteTestRun(true);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void getTestdDroidIosDevice(String device, String deviceName, String testDroidDeviceName)
    {
        env.setIsRemoteTestRun(true);
        env.setIsMobileTest();
        env.setIsMobileIOS(true);
        if ((device == "iphone") || (device == "ipod"))
        {
            env.setIsPhone(true);
        }
        else if (device == "ipad")
        {
            env.setIsTablet(true);
        }
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability("device", device);
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            capabilities.setCapability("testdroid_project", env
                .getTestDroidProjectName());
            capabilities.setCapability("testdroid_description", env
                .getTestDroidProjectDescription());
            capabilities.setCapability("testdroid_testrun", getRunId() + "^^" + env
                .getBrowser());
            capabilities.setCapability("testdroid_username", env
                .getTestDroidUserName());
            capabilities.setCapability("testdroid_password", env
                .getTestDroidPassword());
            capabilities.setCapability("testdroid_device", testDroidDeviceName);
            capabilities.setCapability("testdroid_app", "latest");
            capabilities.setCapability("testdroid_target", "iOS");
            capabilities.setCapability(MobileCapabilityType.APP, env.getIosBundleId());

            capabilities.setCapability("newCommandTimeout", Integer.valueOf(200));

            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());

            logger.info("About to create ios driver from Test Droid.");

            appiumDriver = new IOSDriver(new URL("http://appium.testdroid.com/wd/hub"), capabilities);

            logger.info(String.format("created  [%s]", new Object[]{appiumDriver
                .getRemoteAddress()}));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void getPrivateTestdDroidIosDevice(String device, String deviceName, String testDroidDeviceName)
    {
        env.setIsRemoteTestRun(true);
        env.setIsMobileTest();
        env.setIsMobileIOS(true);
        if ((device == "iphone") || (device == "ipod"))
        {
            env.setIsPhone(true);
        }
        else if (device == "ipad")
        {
            env.setIsTablet(true);
        }
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability("device", device);
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            capabilities.setCapability("testdroid_project", env
                .getTestDroidProjectName());
            capabilities.setCapability("testdroid_description", env
                .getTestDroidProjectDescription());
            capabilities.setCapability("testdroid_testrun", getRunId() + "^^" + env
                .getBrowser());
            capabilities.setCapability("testdroid_username", env
                .getTestDroidUserName());
            capabilities.setCapability("testdroid_password", env
                .getTestDroidPassword());
            capabilities.setCapability("testdroid_device", testDroidDeviceName);
            capabilities.setCapability("testdroid_app", "latest");
            capabilities.setCapability("testdroid_target", "iOS");
            capabilities.setCapability(MobileCapabilityType.APP, env.getIosBundleId());

            capabilities.setCapability("newCommandTimeout", Integer.valueOf(200));

            capabilities.setCapability("autoAcceptAlerts", env
                .getAutoAcceptAlerts());

            logger.info("About to create ios driver from Test Droid.");

            appiumDriver = new IOSDriver(new URL("http://appium.testdroid.com:8083/wd/hub"), capabilities);

            logger.info(String.format("created  [%s]", new Object[]{appiumDriver
                .getRemoteAddress()}));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void getTDIpadDriver()
    {
        env.setIsRemoteTestRun(true);
        env.setIsMobileTest();
        env.setIsMobileIOS(true);
        env.setIsTablet(true);
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability("device", "iphone");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPad Air");
            capabilities.setCapability("testdroid_username", env
                .getTestDroidUserName());
            capabilities.setCapability("testdroid_password", env
                .getTestDroidPassword());
            capabilities.setCapability("testdroid_project", env
                .getTestDroidProjectName());
            capabilities.setCapability("testdroid_description", env
                .getTestDroidProjectDescription());
            capabilities.setCapability("testdroid_testrun", getRunId() + "^^" + env
                .getBrowser());
            capabilities.setCapability("testdroid_device", "iPad Mini A1432 8.1");

            capabilities.setCapability("testdroid_app", "latest");
            capabilities.setCapability("testdroid_target", "iOS");
            capabilities.setCapability(MobileCapabilityType.APP, env.getIosBundleId());
            logger.info("About to create driver from Test Droid.");
            appiumDriver = new IOSDriver(new URL("http://appium.testdroid.com/wd/hub"), capabilities);

            logger.info(String.format("created  [%s]", new Object[]{appiumDriver
                .getRemoteAddress()}));
            env.setIsRemoteTestRun(true);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void closeApplicationSession()
    {
        logger.info("Closing current test session.");

        logger.info(String.format("Calling Close App Session, env.getIsRemoteTestRun() = [%s]", new Object[]{

            Boolean.valueOf(env.getIsRemoteTestRun())}));
        appiumDriver.quit();
    }

    public static void closeBrowser(WebDriver wd)
    {
        try
        {
            wd.quit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String uploadFile(String filePath)
        throws IOException
    {
        final HttpHeaders headers = new HttpHeaders().setBasicAuthentication(env
            .getTestDroidUserName(), env.getTestDroidPassword());

        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(request -> {
            request.setParser(new JsonObjectParser(WebDriverWrapper.JSON_FACTORY));
            request.setHeaders(headers);
        });
        MultipartFormDataContent multipartContent = new MultipartFormDataContent();
        FileContent fileContent = new FileContent("application/octet-stream", new File(filePath));

        MultipartFormDataContent.Part filePart = new MultipartFormDataContent.Part("file", fileContent);

        multipartContent.addPart(filePart);

        HttpRequest request = requestFactory.buildPostRequest(new GenericUrl("http://appium.testdroid.com/upload"),
            multipartContent);

        HttpResponse response = request.execute();
        System.out.println("response:" + response.parseAsString());

        AppiumResponse appiumResponse = request.execute().parseAs(AppiumResponse.class);

        System.out.println("File id:" + appiumResponse.uploadStatus.fileInfo.file);

        return appiumResponse.uploadStatus.fileInfo.file;
    }

    public static class UploadStatus
    {
        @Key("message")
        String message;
        @Key("uploadCount")
        Integer uploadCount;
        @Key("expiresIn")
        Integer expiresIn;
        @Key("uploads")
        WebDriverWrapper.UploadedFile fileInfo;
    }

    public static class UploadedFile
    {
        @Key("file")
        String file;
    }

    public static class AppiumResponse
    {
        Integer status;
        @Key("sessionId")
        String sessionId;
        @Key("value")
        WebDriverWrapper.UploadStatus uploadStatus;
    }
}
