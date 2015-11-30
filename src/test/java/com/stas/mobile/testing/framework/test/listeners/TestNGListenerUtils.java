package com.stas.mobile.testing.framework.test.listeners;

import java.lang.reflect.Method;

import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;

public class TestNGListenerUtils {
	private static EnvironmentUtil env = EnvironmentUtil.getInstance();
	private static LogController logger = new LogController(
			TestNGListenerUtils.class);

	public static String getDescriptionAttribute(ITestResult tr) {
		ITestNGMethod testNGMethod = tr.getMethod();
		Method method = testNGMethod.getConstructorOrMethod().getMethod();
		if (method.getAnnotations()[0].toString().contains("description")) {
			return method.getAnnotations()[0].toString();
		}
		return "";
	}

	public static String getCaseDescription(ITestResult testResult) {
		// List<String> ids = new ArrayList();
		// String idsString = getDescriptionAttribute(testResult);
		// idsString = idsString.substring(idsString.indexOf("(") + 1,
		// idsString.indexOf(")"));
		// String[] atts = idsString.split(",");
		// for (String key : atts) {
		// if (key.contains("description"))
		// {
		// String idStringsRaw = key.split("=")[1];
		// String[] idStrings = idStringsRaw.split("&");
		// for (String id : idStrings)
		// {
		// if (id.equals("param"))
		// {
		// ids.add(env.getCaseId());
		// break;
		// }
		// ids.add(id);
		// logger.debug("Adding - " + id + " to ids to log");
		// }
		// }
		// }
		// return ids.get(0);
		return "temp description";
	}

	public static String getTestTags(ITestResult tr) {
		ITestNGMethod testNGMethod = tr.getMethod();
		Method method = testNGMethod.getConstructorOrMethod().getMethod();
		if (method.getAnnotations()[0].toString().contains("groups")) {
			return method.getAnnotations()[0].toString();
		}
		return "";
	}

	public static String getCaseTags(ITestResult testResult) {
		String groupsString = getTestTags(testResult);
		groupsString = groupsString.substring(groupsString.indexOf("(") + 1,
				groupsString.indexOf(")"));
		String[] atts = groupsString.split("groups=\\[");
		String value = atts[1];
		logger.info(value);
		String tags = value.substring(0, value.indexOf("]"));
		String[] tagTokens = tags.split(",");
		String quotedTokens = "[";
		for (int i = 0; i < tagTokens.length; i++) {
			quotedTokens = quotedTokens + "\"" + tagTokens[i].trim() + "\"";
			if (i + 1 < tagTokens.length) {
				quotedTokens = quotedTokens + ",";
			}
		}
		quotedTokens = quotedTokens + "]";

		return quotedTokens;
	}
}
