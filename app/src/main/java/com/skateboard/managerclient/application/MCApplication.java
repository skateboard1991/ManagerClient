package com.skateboard.managerclient.application;


import android.app.Application;
import com.skateboard.managerclient.network.base.CVolley;
import com.skateboard.managerclient.xiaomi.XiaoMiPushManager;

/**
 * Created by skateboard on 16-5-18.
 */
public class MCApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        CVolley.init(getApplicationContext());
        XiaoMiPushManager.registerPush(getApplicationContext());
    }

}
