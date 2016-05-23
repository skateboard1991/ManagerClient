package com.skateboard.managerclient.network.request;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.skateboard.managerclient.bean.States;
import com.skateboard.managerclient.network.base.BaseListener;
import com.skateboard.managerclient.network.base.BaseRequest;

/**
 * Created by skateboard on 16-5-23.
 */
public class ProcessStateRequest extends BaseRequest<States>
{
    public ProcessStateRequest(int method, String url, Response.Listener successListener, Response.ErrorListener errorListener)
    {
        super(method, url, successListener, errorListener);
    }

    public ProcessStateRequest(int method, String url, BaseListener listener)
    {
        super(method,url,listener,listener);
    }

    @Override
    protected States parseData(String data)
    {
        Gson gson=new Gson();
        States states=gson.fromJson(data,States.class);
        return states;
    }
}
