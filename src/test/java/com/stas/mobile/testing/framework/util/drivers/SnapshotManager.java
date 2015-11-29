package com.stas.mobile.testing.framework.util.drivers;

import io.appium.java_client.AppiumDriver;

import java.io.File;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;

public class SnapshotManager {
	private static EnvironmentUtil env = EnvironmentUtil.getInstance();
	private static LogController logger = new LogController(
			SnapshotManager.class);

	public static String takeScreenshot(String methodName, WebDriver driver) {
		String target = null;
		String snapshotName = methodName;
		if (env.getTakeSnapShots()) {
			if (env.getIsMobileTest().booleanValue()) {
				target = takeRemoteDeviceSnapShot(snapshotName,
						(AppiumDriver) driver);
			} else if (env.getIsRemoteTestRun()) {
				target = takeRemoteSnapShot(snapshotName, driver);
			} else {
				target = takeSnapshot(snapshotName, driver);
			}
		}
		return target;
	}

	private static String takeSnapshot(String snapshotName, WebDriver driver) {
		File tempScreenshot = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		snapshotName = WebDriverWrapper.getRunId() + "_" + snapshotName
				+ ".png";

		// s3.writeStringFileInBucket(snapshotName, tempScreenshot);
		//
		// logger.info(String.format("Stored snapshot here - [%s]", new
		// Object[]{s3
		// .getSignedS3URLForObject(snapshotName)}));
		// return s3.getSignedS3URLForObject(snapshotName);
		return "update path";
	}

	public static String takeRemoteSnapShot(String snapshotName,
			WebDriver driver) {
		File f = ((ScreenShotRemoteWebDriver) driver)
				.getScreenshotAs(OutputType.FILE);
		snapshotName = WebDriverWrapper.getRunId() + "_" + snapshotName
				+ ".png";

		// s3.writeStringFileInBucket(snapshotName, f);
		// logger.info(String.format("Stored snapshot here - [%s]", new
		// Object[]{s3
		// .getSignedS3URLForObject(snapshotName)}));
		// return s3.getSignedS3URLForObject(snapshotName);
		return "update path" + snapshotName;
	}

	public static String takeRemoteDeviceSnapShot(String snapshotName,
			AppiumDriver driver) {
		File f = driver.getScreenshotAs(OutputType.FILE);
		snapshotName = WebDriverWrapper.getRunId() + "_" + snapshotName
				+ ".png";
		//
		// s3.writeStringFileInBucket(snapshotName, f);
		// logger.info(String.format("Stored snapshot here - [%s]", new
		// Object[]{s3
		// .getSignedS3URLForObject(snapshotName)}));
		// return s3.getSignedS3URLForObject(snapshotName);
		return "update path";
	}
}
