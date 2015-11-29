
package com.stas.mobile.testing.framework.test.listeners;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stas.mobile.testing.framework.util.api.RestfulApiUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;

public class AtomLogger
{
    public static final String ATOM_URL = "https://360.applause.com";
    public static final String RUN_RESULT_ATOM_ENDPOINT = "automation/api/run";
    public static final String TEST_CASE_RESULT_ATOM_ENDPOINT = "automation/api/testcase";
    public static final String AUTH_TOKEN_END_POINT = "auth/token";
    public static RestfulApiUtil apiUtil = new RestfulApiUtil("https://360.applause.com");
    private static LogController logger = new LogController(AtomLogger.class);

    public static void logTestRunResultToAtom(String runId, String runDate, String result, String jobName,
        String testPassCount, String testFailCount, String testSkipCount, String buildId, String companyId, String productId,
        String deviceType, String deviceName, String operatingSystem, String osVersion, String manufacturer, String s3LogUrl,
        String externalReportUrl)
    {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try
        {
            HttpPost request = new HttpPost("https://360.applause.com/automation/api/run");

            StringEntity params = new StringEntity("{\"run_id\": \"" + runId
                + "\", "
                + "\"run_date\": "
                + runDate
                + ","
                + "\"result\": \""
                + result
                + "\","
                + "\"job_name\": \""
                + jobName
                + "\","
                + "\"test_pass_count\": "
                + testPassCount
                + ","
                + "\"test_fail_count\": "
                + testFailCount
                + ","
                + "\"test_skip_count\": "
                + testSkipCount
                + ","
                + "\"build_id\":  "
                + buildId
                + ","
                + "\"company_id\": "
                + companyId
                + ","
                + "\"product_id\": "
                + productId
                + ","
                + "\"device_type\": \""
                + deviceType
                + "\","
                + "\"device_name\": \""
                + deviceName
                + "\","
                + "\"operating_system\": \""
                + operatingSystem
                + "\","
                + "\"operating_system_version\": \""
                + osVersion
                + "\","
                + "\"manufacturer\": \""
                + manufacturer
                + "\","
                + "\"s3_log_url\": \""
                + s3LogUrl
                + "\","
                + "\"external_report_url\": \""
                + externalReportUrl
                + "\"}");

            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + getToken());
            request.setEntity(params);

            StringWriter swriter = new StringWriter();
            IOUtils.copy(request.getEntity().getContent(), swriter);
            String scontentString = swriter.toString();
            logger.debug(String.format("POST Params : [%s]", new Object[]{scontentString}));

            HttpResponse response = httpClient.execute(request);

            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer);
            String contentString = writer.toString();
            logger.debug(
                String.format("Response Params : [%s] ", new Object[]{contentString}));
        }
        catch (Exception ex)
        {
            logger.error("Exception Occurred when generating a run result.");
            ex.printStackTrace();
        }
    }

    public static void logTestCaseResultToAtom(String runId, String testName, String testTags, String testDescription,
        String testExecutionTime, String runDate, String result, String buildId, String companyId, String productId,
        String deviceType, String deviceName, String operatingSystem, String osVersion, String manufacturer, String s3LogUrl)
    {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try
        {
            HttpPost request = new HttpPost("https://360.applause.com/automation/api/testcase");

            StringEntity params = new StringEntity("{\"run_id\": \"" + runId
                + "\", "
                + "\"test_name\": \""
                + testName
                + "\","
                + "\"test_tags\": "
                + testTags
                + ","
                + "\"test_description\": \""
                + testDescription
                + "\","
                + "\"test_execution_time\": "
                + testExecutionTime
                + ","
                + "\"run_date\": "
                + runDate
                + ","
                + "\"result\": \""
                + result
                + "\","
                + "\"build_id\": "
                + buildId
                + ","
                + "\"company_id\": "
                + companyId
                + ","
                + "\"product_id\": "
                + productId
                + ","
                + "\"device_type\": \""
                + deviceType
                + "\","
                + "\"device_name\": \""
                + deviceName
                + "\","
                + "\"operating_system\": \""
                + operatingSystem
                + "\","
                + "\"operating_system_version\": \""
                + osVersion
                + "\","
                + "\"manufacturer\": \""
                + manufacturer
                + "\","
                + "\"s3_log_url\": \""
                + s3LogUrl
                + "\"}");

            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + getToken());
            request.setEntity(params);

            StringWriter swriter = new StringWriter();
            IOUtils.copy(request.getEntity().getContent(), swriter);
            String scontentString = swriter.toString();
            logger.debug(String.format("POST Params : [%s]", new Object[]{scontentString}));

            HttpResponse response = httpClient.execute(request);

            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer);
            String contentString = writer.toString();
            logger.debug(String.format("Response : [%s] ", new Object[]{contentString}));
        }
        catch (Exception ex)
        {
            logger.error("Exception Occurred when generating a test result.");
            ex.printStackTrace();
        }
    }

    public static String getToken()
    {
        String token = "";

        List<NameValuePair> params = new ArrayList();

        params.add(new BasicNameValuePair("email", "atom-automation@applause.com"));

        params.add(new BasicNameValuePair("password", "IReZ78QalFlKM0T6W04kV"));
        try
        {
            HttpResponse response = apiUtil.doPost("auth/token", null, null, params);

            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer);
            String contentString = writer.toString();
            logger.debug(String.format("Response : [%s] ", new Object[]{contentString}));

            contentString = contentString.replace("Response : ", "");
            logger.debug("Content String = " + contentString);

            JsonObject jo = new JsonParser().parse(contentString).getAsJsonObject();
            JsonObject data = jo.get("data").getAsJsonObject();
            token = data.get("access_token").getAsString();
            logger.debug(String.format("Token = [%s]", new Object[]{token}));
        }
        catch (IOException e)
        {
            logger.error("Exception Occurred when generating a token.");
            e.printStackTrace();
        }
        return token;
    }
}
