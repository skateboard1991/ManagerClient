package com.skateboard.managerclient.network.base;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by skateboard on 16-5-5.
 */
public class BaseListener<T> implements Response.Listener<T>,Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(T response) {

    }
}
