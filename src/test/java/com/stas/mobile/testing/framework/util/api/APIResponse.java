
package com.stas.mobile.testing.framework.util.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

public class APIResponse
{
    private int _responseCode;
    private String _resonseMessgae;
    private String _reponse;
    private Header[] _headers;

    public int getResponseCode()
    {
        return _responseCode;
    }

    public void setResponseCode(int responseCode)
    {
        _responseCode = responseCode;
    }

    public String getResonseMessgae()
    {
        return _resonseMessgae;
    }

    public void setResonseMessgae(String resonseMessgae)
    {
        _resonseMessgae = resonseMessgae;
    }

    public String getReponse()
    {
        return _reponse;
    }

    public void setReponse(String reponse)
    {
        _reponse = reponse;
    }

    public void setHeaders(Header[] headers)
    {
        _headers = headers;
    }

    public Header[] getHeaders()
    {
        return _headers;
    }

    public List<Header> getHeaderByName(String name, boolean ignoreCase)
    {
        List<Header> result = new ArrayList();
        if (_headers != null)
        {
            for (Header header : _headers)
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
