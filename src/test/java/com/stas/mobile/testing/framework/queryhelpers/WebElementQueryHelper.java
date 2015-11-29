package com.stas.mobile.testing.framework.queryhelpers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;
import com.stas.mobile.testing.framework.web.AbstractPageChunk;

public class WebElementQueryHelper {
	private static final String NO_JQUERY_FAILSAFE = "if(typeof jQuery == 'undefined') return null; ";
	private static final String SIZZLE_URL = "https://raw.github.com/jquery/sizzle/1.10.18/dist/sizzle.min.js";
	private LogController _logger = new LogController(
			WebElementQueryHelper.class);
	private static EnvironmentUtil _env = EnvironmentUtil.getInstance();
	private WebDriver _webDriver = null;
	private JavascriptExecutor _jsExec;

	public WebElementQueryHelper(WebDriver webDriver) {
		this._jsExec = ((JavascriptExecutor) webDriver);
		this._webDriver = webDriver;
	}

	public int getElementCount(String selector) {
		List<WebElement> elements = findElementsBySizzleCss(selector);
		return elements.size();
	}

	public WebElement findElement(AbstractPageChunk chunk) {
		String selector = chunk.getAbsoluteSelector();

		String[] tokens = selector.split(" ");
		if ((tokens.length == 2) && (tokens[0].trim().equals(tokens[1].trim()))) {
			selector = tokens[0].trim();
			this._logger
					.info("DeDuped a selector - this is a temporary hack.  Fixing in v2 of framework. New Selector "
							+ selector);
		}
		return findElementByExtendedCss(selector);
	}

	public boolean doesElementExist(String locator) {
		try {
			findElementByExtendedCss(locator);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	private String createJQuerySelectorExpression(String selector) {
		return "if(typeof jQuery == 'undefined') return null; return jQuery.find(\""
				+ selector + "\")";
	}

	private String createSizzleSelectorExpression(String cssLocator) {
		return "return Sizzle(\"" + cssLocator + "\")";
	}

	public boolean isElementPresent(String cssSelector) {
		String script = "if(typeof jQuery == 'undefined') return null; return jQuery.find('"
				+ cssSelector.trim() + ":visible')[0] != undefined";
		Object result = this._jsExec.executeScript(script, new Object[0]);
		if (result == null) {
			return false;
		}
		if ((result instanceof Boolean)) {
			return ((Boolean) result).booleanValue();
		}
		throw new IllegalStateException("Wrong result was returned by script "
				+ script + ": " + result);
	}

	public WebElement findElementByExtendedCss(String cssLocator) {
		this._logger.debug("In findElementByExtendedCss with " + cssLocator);
		if (_env.getIsMobileTest().booleanValue()) {
			if ((cssLocator.startsWith("//"))
					|| (cssLocator.startsWith("(.//"))
					|| (cssLocator.startsWith("(//"))) {
				this._logger.debug("Doing xpath search for element "
						+ cssLocator);
				return this._webDriver.findElement(By.xpath(cssLocator));
			}
			return this._webDriver.findElement(By.cssSelector(cssLocator));
		}
		if (_env.useRawCssOnly()) {
			if ((cssLocator.startsWith("//"))
					|| (cssLocator.startsWith("(.//"))) {
				this._logger.debug("Doing xpath search for element "
						+ cssLocator);
				return this._webDriver.findElement(By.xpath(cssLocator));
			}
			return this._webDriver.findElement(By.cssSelector(cssLocator));
		}
		if (cssLocator.startsWith("//")) {
			return this._webDriver.findElement(By.xpath(cssLocator));
		}
		List<WebElement> elements = findElementsBySizzleCss(cssLocator);
		if ((elements != null) && (elements.size() > 0)) {
			return elements.get(0);
		}
		throw new NoSuchElementException("selector '" + cssLocator
				+ "' cannot be found in DOM");
	}

	public List<WebElement> findElementsBySizzleCss(String cssLocator) {
		String javascriptExpression = "";
		if (!_env.getIsMobileTest().booleanValue()) {
			if ((!isJQueryLoaded()) && (!isSizzleLoaded().booleanValue())) {
				this._logger
						.debug("jQuery and Sizzle don't appear loaded.  Waiting 30 seconds for page to load.");
				try {
					Thread.sleep(3000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if (isJQueryLoaded()) {
				javascriptExpression = createJQuerySelectorExpression(cssLocator);
				this._logger.debug(String.format(
						"jQuery Expression to locate object [%s].",
						new Object[] { javascriptExpression }));
			} else if (isSizzleLoaded().booleanValue()) {
				javascriptExpression = createSizzleSelectorExpression(cssLocator);
				this._logger.debug(String.format(
						"Sizzle Expression to locate object [%s].",
						new Object[] { javascriptExpression }));
			} else if (_env.sizzleInject()) {
				this._logger
						.debug("jQuery or Sizzle is not loaded, attempting to inject Sizzle");
				injectSizzle();
				javascriptExpression = createSizzleSelectorExpression(cssLocator);
				this._logger.debug(String.format(
						"Sizzle Expression to locate object [%s].",
						new Object[] { javascriptExpression }));
			}
		} else {
			this._logger
					.info("Running in mobile web, only css & xpath supported. locator= "
							+ cssLocator);
			if ((cssLocator.startsWith("(.//"))
					|| (cssLocator.startsWith("(//"))
					|| (cssLocator.startsWith("//"))) {
				return this._webDriver.findElements(By.xpath(cssLocator));
			}
			return this._webDriver.findElements(By.cssSelector(cssLocator));
		}
		List<WebElement> elements = (List) ((JavascriptExecutor) this._webDriver)
				.executeScript(javascriptExpression, new Object[0]);
		return elements;
	}

	private void injectSizzle() {
		if (!isSizzleLoaded().booleanValue()) {
			injectSizzleScript();
		} else {
			return;
		}
		for (int i = 0; i < 40; i++) {
			if (isSizzleLoaded().booleanValue()) {
				return;
			}
			try {
				Thread.sleep(500L);
			} catch (InterruptedException localInterruptedException) {
			}
			if (i % 10 == 0) {
				this._logger
						.warn(String
								.format("Attempting to re-load SizzleCSS from {%s}",
										new Object[] { "https://raw.github.com/jquery/sizzle/1.10.18/dist/sizzle.min.js" }));

				injectSizzle();
			}
		}
		if (!isSizzleLoaded().booleanValue()) {
			this._logger
					.error("After so many tries, sizzle does not appear in DOM");
			throw new RuntimeException(
					"Sizzle loading from (https://raw.github.com/jquery/sizzle/1.10.18/dist/sizzle.min.js) has failed");
		}
	}

	public boolean isJQueryLoaded() {
		Boolean loaded = Boolean.valueOf(true);
		try {
			loaded = (Boolean) ((JavascriptExecutor) this._webDriver)
					.executeScript("return (window.jQuery != null);",
							new Object[0]);
		} catch (WebDriverException e) {
			this._logger
					.error(String
							.format("while trying to verify jQuery loading, WebDriver threw exception {%s} ",
									new Object[] { e.getMessage() }));
			e.printStackTrace();
			loaded = Boolean.valueOf(false);
		}
		return loaded.booleanValue();
	}

	public Boolean isSizzleLoaded() {
		Boolean loaded = Boolean.valueOf(true);
		try {
			loaded = (Boolean) ((JavascriptExecutor) this._webDriver)
					.executeScript("return (window.Sizzle != null);",
							new Object[0]);
		} catch (WebDriverException e) {
			this._logger
					.error(String
							.format("while trying to verify Sizzle loading, WebDriver threw exception {%s} ",
									new Object[] { e.getMessage() }));
			loaded = Boolean.valueOf(false);
		}
		return loaded;
	}

	public void injectSizzleScript() {
		((JavascriptExecutor) this._webDriver)
				.executeScript(
						" var bodyTag = document.getElementsByTagName('body')[0];if (bodyTag) { var sizzl = document.createElement('script'); sizzl.type = 'text/javascript'; sizzl.src = 'https://raw.github.com/jquery/sizzle/1.10.18/dist/sizzle.min.js'; bodyTag.appendChild(sizzl);}",
						new Object[0]);
	}
}
