
package com.stas.mobile.testing.framework.util.drivers;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stas.mobile.testing.framework.util.api.RestfulApiUtil;
import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;
import com.testdroid.api.http.MultipartFormDataContent;

public class DeviceLabApiUtils
{
    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static LogController logger = new LogController(DeviceLabApiUtils.class);
    public static final String TEST_DROID_URL = "";
    public static final String TEST_DROID_AUTH = "/oauth/token";
    public static RestfulApiUtil apiUtil = new RestfulApiUtil("");
    public static EnvironmentUtil env = EnvironmentUtil.getInstance();

    public String getAuthToken()
    {
        String token = "";

        List<NameValuePair> params = new ArrayList();

        params.add(new BasicNameValuePair("client_id", "testdroid-cloud-api"));
        params.add(new BasicNameValuePair("grant_type", "password"));
        params.add(new BasicNameValuePair("username", env
            .getTestDroidUserName()));
        params.add(new BasicNameValuePair("password", env
            .getTestDroidPassword()));
        try
        {
            org.apache.http.HttpResponse response = apiUtil.doPost("/oauth/token", null, null, params);

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

    public static String uploadFile(String filePath)
        throws IOException
    {
        final HttpHeaders headers = new HttpHeaders().setBasicAuthentication("brock@applausemail.com", "@pplause1");

        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(request -> {
            request.setParser(new JsonObjectParser(DeviceLabApiUtils.JSON_FACTORY));
            request.setHeaders(headers);
        });
        MultipartFormDataContent multipartContent = new MultipartFormDataContent();
        FileContent fileContent = new FileContent("application/octet-stream", new File(filePath));

        MultipartFormDataContent.Part filePart = new MultipartFormDataContent.Part("file", fileContent);

        multipartContent.addPart(filePart);

        HttpRequest request = requestFactory.buildPostRequest(new GenericUrl("http://appium.testdroid.com/upload"),
            multipartContent);

        com.google.api.client.http.HttpResponse response = request.execute();
        logger.debug("response:" + response.parseAsString());

        AppiumResponse appiumResponse = request.execute().parseAs(AppiumResponse.class);

        logger.debug("File id:" + appiumResponse.uploadStatus.fileInfo.file);

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
        DeviceLabApiUtils.UploadedFile fileInfo;
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
        DeviceLabApiUtils.UploadStatus uploadStatus;
    }
}
