package com.stas.mobile.testing.framework.device;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.stas.mobile.testing.framework.queryhelpers.DeviceElementQueryHelper;
import com.stas.mobile.testing.framework.synchronization.SynchronizationHelper;
import com.stas.mobile.testing.framework.util.drivers.SnapshotManager;
import com.stas.mobile.testing.framework.util.drivers.WebDriverWrapper;
import com.stas.mobile.testing.framework.util.logger.LogController;

public abstract class AbstractDeviceChunk extends AbstractDeviceUIData {
	protected String _selector;
	protected SnapshotManager _snapshotManager = new SnapshotManager();
	private LogController _logger = new LogController(AbstractDeviceChunk.class);

	public AbstractDeviceChunk(String selector) {
		this._selector = selector;
		this._queryHelper = new DeviceElementQueryHelper(this._driver);
		this._syncHelper = new SynchronizationHelper(this._driver);
		this._mobileHelper = new MobileHelper();
	}

	public MobileElement getMobileElement() {
		final AbstractDeviceChunk that = this;
		WebDriverWait wait = new WebDriverWait(getDriver(), 30L);

		Function<WebDriver, MobileElement> function = new Function<WebDriver, MobileElement>() {
			public MobileElement apply(WebDriver input) {
				return AbstractDeviceChunk.this
						.getMobileElementImmediately(that);
			}
		};
		try {
			return wait.until(function);
		} catch (TimeoutException te) {
			throw new RuntimeException("Timeout for element: " + getSelector(),
					te);
		} catch (Exception e) {
			throw new RuntimeException("Exception getting element: "
					+ getSelector(), e);
		}
	}

	public List<MobileElement> getMobileElements() {
		final AbstractDeviceChunk that = this;
		WebDriverWait wait = new WebDriverWait(getDriver(), 30L);

		ExpectedCondition<List<MobileElement>> condition = new ExpectedCondition() {
			// public List<MobileElement> apply(WebDriver input)
			// {
			// return
			// AbstractDeviceChunk.this.getListOfMobileElementImmediately(that);
			// }

			public Object apply(Object input) {
				return AbstractDeviceChunk.this
						.getListOfMobileElementImmediately(that);
			}
		};
		try {
			return wait.until(condition);
		} catch (TimeoutException te) {
			throw new RuntimeException(
					"Timeout for elements: " + getSelector(), te);
		} catch (Exception e) {
			throw new RuntimeException("Exception getting elements: "
					+ getSelector(), e);
		}
	}

	public MobileElement getVisibleMobileElement(int defaultTimeout) {
		WebDriverWait wait = new WebDriverWait(getDriver(), defaultTimeout);
		final AbstractDeviceChunk that = this;
		Function<WebDriver, MobileElement> isVisible = new Function() {
			public Object apply(Object input) {
				MobileElement mobileElement = AbstractDeviceChunk.this
						.getMobileElementImmediately(that);
				if ((mobileElement != null) && (mobileElement.isDisplayed())) {
					return mobileElement;
				}
				return null;
			}
		};
		try {
			return wait.until(isVisible);
		} catch (TimeoutException te) {
			throw new RuntimeException("Timeout for element: " + getSelector(),
					te);
		} catch (Exception e) {
			throw new RuntimeException("Exception getting element: "
					+ getSelector(), e);
		}
	}

	public boolean exists() {
		try {
			return getMobileElementImmediately(this) != null;
		} catch (NoSuchElementException e) {
			this._logger.debug("NoSuchElementException returned.");
		}
		return false;
	}

	public boolean isDisplayed() {
		if (exists()) {
			return getMobileElementImmediately(this).isDisplayed();
		}
		return false;
	}

	public String getSelector() {
		return this._selector;
	}

	public AppiumDriver getDriver() {
		return WebDriverWrapper.getAppiumDriver();
	}

	@Override
	public String toString() {
		return "Selector: " + getSelector();
	}

	public void tap() {
		MobileElement element = getVisibleMobileElement(30000);
		this._syncHelper.suspend(100);
		try {
			element.click();
		} catch (StaleElementReferenceException e) {
			e.printStackTrace();

			getVisibleMobileElement(30000).click();
		}
	}

	private MobileElement getMobileElementImmediately(AbstractDeviceChunk chunk) {
		this._logger.debug(String.format(
				"In AbstractDeviceChunk, about to call DEQH with [%s]",
				new Object[] { this._selector }));

		return this._queryHelper.findElement(chunk.getSelector());
	}

	private List<MobileElement> getListOfMobileElementImmediately(
			AbstractDeviceChunk chunk) {
		return this._queryHelper.findElements(chunk.getSelector());
	}
}
