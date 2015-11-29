
package com.stas.mobile.testing.framework.util.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

public class APIResponse
{
    private int responseCode;
    private String resonseMessgae;
    private String reponse;
    private Header[] headers;

    public int getResponseCode()
    {
        return this.responseCode;
    }

    public void setResponseCode(int responseCode)
    {
        this.responseCode = responseCode;
    }

    public String getResonseMessgae()
    {
        return this.resonseMessgae;
    }

    public void setResonseMessgae(String resonseMessgae)
    {
        this.resonseMessgae = resonseMessgae;
    }

    public String getReponse()
    {
        return this.reponse;
    }

    public void setReponse(String reponse)
    {
        this.reponse = reponse;
    }

    public void setHeaders(Header[] headers)
    {
        this.headers = headers;
    }

    public Header[] getHeaders()
    {
        return this.headers;
    }

    public List<Header> getHeaderByName(String name, boolean ignoreCase)
    {
        List<Header> result = new ArrayList();
        if (this.headers != null)
        {
            for (Header header : this.headers)
            {
                if (ignoreCase)
                {
                    if (header.getName().toUpperCase().equals(name.toUpperCase()))
                    {
                        result.add(header);
                    }
                }
                else if (header.getName().equals(name))
                {
                    result.add(header);
                }
            }
        }
        return result;
    }
}
