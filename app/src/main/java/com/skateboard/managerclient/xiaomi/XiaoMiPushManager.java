package com.skateboard.managerclient.xiaomi;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.skateboard.managerclient.K;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;

import java.util.List;

/**
 * Created by skateboard on 16-5-20.
 */
public class XiaoMiPushManager
{
    public static void registerPush(Context context)
    {
        if (shouldInit(context))
        {
            MiPushClient.registerPush(context, K.APPID, K.APPKEY);
        }
    }

    private static boolean shouldInit(Context context)
    {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos)
        {
            if (info.pid == myPid && mainProcessName.equals(info.processName))
            {
                return true;
            }
        }
        return false;
    }

    public static void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage)
    {

    }

    public static void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage)
    {

    }

    public static void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage)
    {
    }

    public static void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage)
    {
    }

    public static void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage)
    {
    }
}
