package com.skateboard.managerclient.xiaomi;

import android.content.Context;

import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

/**
 * Created by skateboard on 16-5-20.
 */
public class MessageReceiver extends PushMessageReceiver
{
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage)
    {
        super.onReceivePassThroughMessage(context, miPushMessage);
        XiaoMiPushManager.onReceivePassThroughMessage(context,miPushMessage);
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage)
    {
        super.onNotificationMessageClicked(context, miPushMessage);
        XiaoMiPushManager.onNotificationMessageClicked(context,miPushMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage)
    {
        super.onNotificationMessageArrived(context, miPushMessage);
        XiaoMiPushManager.onNotificationMessageArrived(context,miPushMessage);
    }


    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage)
    {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
        XiaoMiPushManager.onReceiveRegisterResult(context,miPushCommandMessage);
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage)
    {
        super.onCommandResult(context, miPushCommandMessage);
        XiaoMiPushManager.onCommandResult(context,miPushCommandMessage);
    }
}
