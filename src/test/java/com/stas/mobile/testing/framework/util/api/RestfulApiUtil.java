
package com.stas.mobile.testing.framework.util.api;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.stas.mobile.testing.framework.util.logger.LogController;

public class RestfulApiUtil
{
    private static String baseUrl;
    private static LogController logger = new LogController(RestfulApiUtil.class);

    public RestfulApiUtil(String baseUrl)
    {
        baseUrl = baseUrl;
        logger.debug("Created RestfulApiUtil with base url : " + baseUrl);
    }

    public HttpResponse doPost(String endPoint, String token, StringEntity json, List<NameValuePair> params)
        throws IOException
    {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        HttpPost post = new HttpPost(baseUrl + "/" + endPoint);
        logger.debug("Doing post to " + baseUrl + "/" + endPoint);
        try
        {
            logger.debug(String.format("Executing Rest API Post Request:", new Object[0]));
            logger.debug(String.format("URL : [%s]", new Object[]{baseUrl + "/" + endPoint}));
            if (json != null)
            {
                post.addHeader("content-type", "application/json");
                post.setEntity(json);
            }
            if (token != null)
            {
                post.addHeader("Authorization", "Bearer " + token + "");
            }
            if (params != null)
            {
                post.setEntity(new UrlEncodedFormEntity(params));
            }
            StringWriter writer = new StringWriter();
            IOUtils.copy(post.getEntity().getContent(), writer);
            String contentString = writer.toString();
            logger.debug(String.format("POST Params : [%s]", new Object[]{contentString}));

            return httpClient.execute(post);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }

    public APIResponse doRestfulJsonPost(String url, StringEntity params, String apiKey)
    {
        APIResponse apiResponse = new APIResponse();

        HttpClient httpClient = HttpClientBuilder.create().build();
        try
        {
            HttpPost request = new HttpPost(url);
            request.addHeader("content-type", "application/json");
            request.addHeader("ApiKey", apiKey);
            request.setEntity(params);

            StringWriter swriter = new StringWriter();
            IOUtils.copy(request.getEntity().getContent(), swriter);
            String scontentString = swriter.toString();
            logger.debug(String.format("POST Params : [%s]", new Object[]{scontentString}));

            HttpResponse response = httpClient.execute(request);

            apiResponse.setResponseCode(response.getStatusLine()
                .getStatusCode());
            apiResponse.setResonseMessgae(response.getStatusLine()
                .getReasonPhrase());

            apiResponse.setHeaders(response.getAllHeaders());

            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer);
            String contentString = writer.toString();
            logger.debug(
                String.format("Response Params : [%s] ", new Object[]{contentString}));
            if ((contentString.contains("[")) || (contentString.contains("]")))
            {
                contentString.replace('[', '\000');
                contentString.replace(']', '\000');
            }
            apiResponse.setReponse(contentString);
            return apiResponse;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.error("Exception Occurred when generating doing POST.");
            ex.printStackTrace();
        }
        return null;
    }

    public HttpResponse doGet(String endPoint, Header[] headers, List<NameValuePair> params)
        throws IOException
    {
        if (params != null)
        {
            if (!endPoint.endsWith("?"))
            {
                endPoint = endPoint + "?";
            }
            endPoint = endPoint + URLEncodedUtils.format(params, "utf-8");
        }
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        HttpGet get = new HttpGet(baseUrl + "/" + endPoint);
        logger.debug("Doing post to " + baseUrl + "/" + endPoint);
        try
        {
            logger.debug(String.format("Executing Rest API GET Request:", new Object[0]));
            logger.debug(String.format("URL : [%s]", new Object[]{baseUrl + "/" + endPoint}));
            for (Header header : headers)
            {
                get.addHeader(header);
                logger.debug(String.format("Header added %s", new Object[]{header.getName() + ":" + header.getValue()}));
            }
            return httpClient.execute(get);
        }
        catch (Exception e)
        {
            logger.error("Exception occured when posting API call.");
            e.printStackTrace();
        }
        return response;
    }

    public APIResponse doRestfulJsonGet(String url, Header[] headers, String token, String apiKey)
        throws UnsupportedEncodingException
    {
        APIResponse apiResponse = new APIResponse();

        headers = buildRestHeaders(headers, token, apiKey);
        try
        {
            HttpResponse response = doGet(url, headers, null);
            apiResponse.setResponseCode(response.getStatusLine()
                .getStatusCode());
            apiResponse.setResonseMessgae(response.getStatusLine()
                .getReasonPhrase());

            apiResponse.setHeaders(response.getAllHeaders());

            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer);
            String contentString = writer.toString();
            logger.debug(
                String.format("Response Params : [%s] ", new Object[]{contentString}));
            if ((contentString.contains("[")) || (contentString.contains("]")))
            {
                contentString.replace('[', '\000');
                contentString.replace(']', '\000');
            }
            apiResponse.setReponse(contentString);
            return apiResponse;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.error("Exception Occurred when generating doing POST.");
            ex.printStackTrace();
        }
        return null;
    }

    public HttpResponse doPut(String endPoint, Header[] headers, StringEntity params)
        throws IOException
    {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        HttpPut put = new HttpPut(baseUrl + "/" + endPoint);
        logger.debug("Doing put to " + baseUrl + "/" + endPoint);
        try
        {
            logger.debug(String.format("Executing Rest API PUT Request: %s", new Object[]{EntityUtils.toString(params)}));
            logger.debug(String.format("URL : [%s]", new Object[]{baseUrl + "/" + endPoint}));
            for (Header header : headers)
            {
                put.addHeader(header);
                logger.info(String.format("Header added %s", new Object[]{header.getName() + ":" + header.getValue()}));
            }
            if (params != null)
            {
                put.addHeader("content-type", "application/json");
                put.setEntity(params);
            }
            return httpClient.execute(put);
        }
        catch (Exception e)
        {
            logger.error("Exception occured when posting API call.");
            e.printStackTrace();
        }
        return response;
    }

    public APIResponse doRestfulJsonPut(String url, Header[] headers, StringEntity params, String token, String apiKey)
    {
        APIResponse apiResponse = new APIResponse();

        headers = buildRestHeaders(headers, token, apiKey);
        try
        {
            HttpResponse response = doPut(url, headers, params);
            apiResponse.setResponseCode(response.getStatusLine()
                .getStatusCode());
            apiResponse.setResonseMessgae(response.getStatusLine()
                .getReasonPhrase());

            apiResponse.setHeaders(response.getAllHeaders());

            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer);
            String contentString = writer.toString();
            logger.debug(
                String.format("Response Params : [%s] ", new Object[]{contentString}));
            if ((contentString.contains("[")) || (contentString.contains("]")))
            {
                contentString.replace('[', '\000');
                contentString.replace(']', '\000');
            }
            apiResponse.setReponse(contentString);
            return apiResponse;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.error("Exception Occurred when generating doing PUT.");
            ex.printStackTrace();
        }
        return null;
    }

    public HttpResponse doDelete(String endPoint, Header[] headers, StringEntity params)
        throws IOException
    {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        HttpDeleteWithBody delete = new HttpDeleteWithBody(baseUrl + "/" + endPoint);
        logger.debug("Doing DELETE to " + baseUrl + "/" + endPoint);
        try
        {
            logger.debug(String.format("Executing Rest API DELETE Request: %s", new Object[]{EntityUtils.toString(params)}));
            logger.debug(String.format("URL : [%s]", new Object[]{baseUrl + "/" + endPoint}));
            for (Header header : headers)
            {
                delete.addHeader(header);
                logger.info(String.format("Header added %s", new Object[]{header.getName() + ":" + header.getValue()}));
            }
            delete.addHeader("Content-Type", "application/json");
            delete.addHeader("Accept", "*/*");
            delete.setEntity(params);

            return httpClient.execute(delete);
        }
        catch (Exception e)
        {
            logger.error("Exception occured when DELETE API call was made.");
            e.printStackTrace();
        }
        return response;
    }

    public APIResponse doRestfulJsonDelete(String url, Header[] headers, StringEntity params, String token, String apiKey)
    {
        APIResponse apiResponse = new APIResponse();

        headers = buildRestHeaders(headers, token, apiKey);
        try
        {
            HttpResponse response = doDelete(url, headers, params);
            apiResponse.setResponseCode(response.getStatusLine()
                .getStatusCode());
            apiResponse.setResonseMessgae(response.getStatusLine()
                .getReasonPhrase());

            apiResponse.setHeaders(response.getAllHeaders());

            String contentString = "string";
            apiResponse.setReponse(contentString);
            return apiResponse;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.error("Exception Occurred when generating doing DELETE.");
            ex.printStackTrace();
        }
        return null;
    }

    private Header[] buildRestHeaders(Header[] headers, String token, String apiKey)
    {
        if (headers == null)
        {
            headers = new Header[0];
        }
        if (token != null)
        {
            Header authHeader = new BasicHeader("Authorization", "Bearer " + token + "");

            Header[] new_headers = new Header[headers.length + 1];
            System.arraycopy(headers, 0, new_headers, 0, headers.length);
            new_headers[headers.length] = authHeader;
            headers = new_headers;
        }
        if (apiKey != null)
        {
            Header apiKeyHeader = new BasicHeader("apiKey", apiKey + "");

            Header[] new_headers = new Header[headers.length + 1];
            System.arraycopy(headers, 0, new_headers, 0, headers.length);
            new_headers[headers.length] = apiKeyHeader;
            headers = new_headers;
        }
        return headers;
    }
}
