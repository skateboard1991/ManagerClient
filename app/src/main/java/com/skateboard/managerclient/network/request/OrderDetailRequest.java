package com.skateboard.managerclient.network.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.skateboard.managerclient.K;
import com.skateboard.managerclient.bean.Order;
import com.skateboard.managerclient.network.base.BaseListener;
import com.skateboard.managerclient.network.base.BaseRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by skateboard on 16-5-23.
 */
public class OrderDetailRequest extends BaseRequest<Order>
{

    public OrderDetailRequest(int method, String url, Response.Listener successListener, Response.ErrorListener errorListener)
    {
        super(method, url, successListener, errorListener);
    }

    public OrderDetailRequest(int method, String url, BaseListener listener)
    {
        super(method,url,listener,listener);
    }

    @Override
    protected Order parseData(String data)
    {
        Gson gson=new Gson();
        Order order=gson.fromJson(data,Order.class);
        return order;
    }

}
