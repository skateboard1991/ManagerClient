package com.skateboard.managerclient.network.request;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.skateboard.managerclient.bean.Orders;
import com.skateboard.managerclient.network.base.BaseListener;
import com.skateboard.managerclient.network.base.BaseRequest;

/**
 * Created by skateboard on 16-5-16.
 */
public class OrderListRequest extends BaseRequest<Orders> {


    public OrderListRequest(int method, String url, Response.Listener successListener, Response.ErrorListener errorListener) {
        super(method, url, successListener, errorListener);
    }

    public OrderListRequest(int method, String url, BaseListener listener)
    {
        super(method,url,listener,listener);
    }

    @Override
    protected Orders parseData(String data) {
        Gson gson=new Gson();
        Orders orderList=gson.fromJson(data,Orders.class);
        return orderList;

    }

}
