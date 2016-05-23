package com.skateboard.managerclient.network.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.skateboard.managerclient.network.base.BaseListener;
import com.skateboard.managerclient.network.base.BaseRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by skateboard on 16-5-23.
 */
public class SignInRequest extends BaseRequest<String>
{
    private HashMap<String, String> params;

    public SignInRequest(int method, String url, Response.Listener successListener, Response.ErrorListener errorListener)
    {
        super(method, url, successListener, errorListener);
    }

    public SignInRequest(int method, String url, BaseListener listener)
    {
        super(method, url, listener, listener);
    }

    public SignInRequest(int method, String url, BaseListener listener, HashMap<String, String> params)
    {
        super(method, url, listener, listener);
        this.params = params;
    }

    @Override
    protected String parseData(String data)
    {
        return data;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError
    {
        return params;
    }

}
