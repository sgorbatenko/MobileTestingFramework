package com.stas.mobile.testing.framework.queryhelpers;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;

public class DeviceElementQueryHelper {
	private LogController _logger = new LogController(
			DeviceElementQueryHelper.class);
	private EnvironmentUtil _env = EnvironmentUtil.getInstance();
	private IOSDriver _iosDriver;
	private AndroidDriver _androidDriver;

	public DeviceElementQueryHelper(Object driver) {
		if (_env.getIsMobileIOS()) {
			_iosDriver = ((IOSDriver) driver);
		} else {
			_androidDriver = ((AndroidDriver) driver);
		}
	}

	public MobileElement findElement(String selector) {
		if (_env.getIsMobileIOS()) {
			if (selector.contains("//")) {
				_logger.debug(String
						.format("Found IOS Xpath Selector [%s], using findElementByXPath.",
								new Object[] { selector }));

				return (MobileElement) _iosDriver.findElementByXPath(selector);
			}
			if (selector.startsWith(".")) {
				_logger.debug(String
						.format("Found IOS UIAutomator Selector [%s], using findElementByIosUIAutomation.",
								new Object[] { selector }));

				return (MobileElement) _iosDriver
						.findElementByIosUIAutomation(selector);
			}
			if (selector.startsWith("$")) {
				String classNameSelector = selector.replace("$", "");
				_logger.debug(String
						.format("Found clas Name selector [%s], using findElement(By.className()).",
								new Object[] { classNameSelector }));

				return (MobileElement) _iosDriver.findElement(By
						.className(classNameSelector));
			}
			_logger.debug(String.format(
					"Found IOS id Selector [%s], using findElementById.",
					new Object[] { selector }));

			return (MobileElement) _iosDriver.findElementById(selector);
		}
		if (selector.contains("UiSelector")) {
			_logger.debug(String
					.format("Found UiSelector in locator [%s], using findElementByAndroidUIAutomator.",
							new Object[] { selector }));

			return (MobileElement) _androidDriver
					.findElementByAndroidUIAutomator(selector);
		}
		if (selector.contains("//")) {
			_logger.debug(String
					.format("Found Android Xpath Selector [%s], using findElementByXPath.",
							new Object[] { selector }));

			return (MobileElement) _androidDriver.findElementByXPath(selector);
		}
		if (selector.contains("~")) {
			String androidNameSelector = selector.replace("~", "");
			_logger.debug(String
					.format("Found Android name Selector [%s], using findElementByName.",
							new Object[] { androidNameSelector }));

			return (MobileElement) _androidDriver
					.findElementByName(androidNameSelector);
		}
		_logger.debug(String.format("Attempted to find locator [%s] ById.",
				new Object[] { selector }));

		return (MobileElement) _androidDriver.findElementById(selector);
	}

	public List<MobileElement> findElements(String selector) {

		if (_env.getIsMobileIOS()) {
			_logger.debug(String
					.format("Found IOS UIAutomator Selector [%s], using findElementByIosUIAutomation.",
							new Object[] { selector }));

			List<MobileElement> elements = (List) _iosDriver
					.findElementByIosUIAutomation(selector);
			return elements;
		}
		if (selector.contains("UiSelector")) {
			String uiSelector = "new UiSelector().resourceId(\"" + selector
					+ "\")";
			_logger.info(String
					.format("Selector is a Android UiSelector [%s], using findByAndroidUIAutomator.",
							new Object[] { uiSelector }));

			List<MobileElement> elements = (List) _androidDriver
					.findElementByAndroidUIAutomator(uiSelector);
			return elements;
		}
		return null;
	}

	public int getAndroidElementCount(String selector) {
		return getMobileElementCount(selector);
	}

	public int getMobileElementCount(String selector) {
		List<WebElement> elements = null;
		if (_env.getIsMobileIOS()) {
			if (selector.contains("//")) {
				_logger.debug(String
						.format("Found IOS Xpath Selector [%s], using findElementsByXPath.",
								new Object[] { selector }));

				elements = _iosDriver.findElementsByXPath(selector);
			} else if (selector.startsWith(".")) {
				_logger.debug(String
						.format("Found IOS UIAutomator Selector [%s], using findElementsByIosUIAutomation.",
								new Object[] { selector }));

				elements = _iosDriver.findElementsByIosUIAutomation(selector);
			} else if (selector.startsWith("$")) {
				String classNameSelector = selector.replace("$", "");
				_logger.debug(String
						.format("Found clas Name selector [%s], using findElements(By.className()).",
								new Object[] { classNameSelector }));

				elements = _iosDriver.findElements(By
						.className(classNameSelector));
			} else {
				_logger.debug(String.format(
						"Found IOS id Selector [%s], using findElementsById.",
						new Object[] { selector }));

				elements = _iosDriver.findElementsById(selector);
			}
		} else if (selector.contains("UiSelector")) {
			_logger.debug(String
					.format("Found UiSelector in locator [%s], using findElementsByAndroidUIAutomator.",
							new Object[] { selector }));

			elements = _androidDriver
					.findElementsByAndroidUIAutomator(selector);
		} else if (selector.contains("//")) {
			_logger.debug(String
					.format("Found Android Xpath Selector [%s], using findElementsByXPath.",
							new Object[] { selector }));

			elements = _androidDriver.findElementsByXPath(selector);
		} else if (selector.contains("~")) {
			String nameSelector = selector.replace("~", "");
			_logger.debug(String
					.format("Found Android name Selector [%s], using findElementsByName.",
							new Object[] { nameSelector }));

			elements = _androidDriver.findElementsByName(nameSelector);
		} else {
			_logger.debug(String.format("Attempted to find locator [%s] ById.",
					new Object[] { selector }));

			elements = _androidDriver.findElementsById(selector);
		}
		return elements.size();
	}

	public boolean doesElementExist(String selector) {
		try {
			if (_env.getIsMobileIOS()) {
				_logger.info("Checking to see if the element exists");
				if (selector.contains("//")) {
					_iosDriver.findElementByXPath(selector);
				} else {
					_iosDriver.findElementById(selector);
				}
				return true;
			}
			if (selector.contains("UiSelector")) {
				_logger.debug(String
						.format("Found UiSelector in locator [%s], using findElementByAndroidUIAutomator.",
								new Object[] { selector }));

				_androidDriver.findElementByAndroidUIAutomator(selector);
			} else {
				_logger.debug(String.format(
						"Attempted to find locator [%s] ById.",
						new Object[] { selector }));

				_androidDriver.findElementById(selector);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
