package com.skateboard.managerclient.network.base;


import com.android.volley.Request;
import com.skateboard.managerclient.network.base.CVolley;


/**
 * Created by skateboard on 16-5-5.
 */
public abstract class RequestHolder<T> {


    public void execute()
    {
       CVolley.requestQueue.add(newRequest());
    }

    public abstract  Request<T> newRequest();

}
