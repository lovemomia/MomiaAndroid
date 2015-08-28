package com.youxing.duola.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.youxing.duola.R;
import com.youxing.duola.RootTabActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PushReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);

                    Log.d("GetuiSdkDemo", "receiver payload : " + data);

                    payloadData.append(data);
                    payloadData.append("\n");

                    showNotification(context, data);
                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
//                if (GetuiSdkDemoActivity.tView != null) {
//                    GetuiSdkDemoActivity.tView.setText(cid);
//                }
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 * 
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }

    private void showNotification(Context context, String message) {
        JSONObject json;
        try {
            json = new JSONObject(message);
        } catch (Exception e) {
            return;
        }

        String action = json.optString("action");
        if (TextUtils.isEmpty(action)) {
            action = "duola://home";
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(action));
        if (action.startsWith("duola://home") || action.startsWith("duola://mine")) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                message.hashCode(), intent, 0);

        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.push)
                .setContentTitle(json.optString("title"))
                .setDefaults(
                        Notification.DEFAULT_LIGHTS
                                | Notification.DEFAULT_SOUND)
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .setContentText(json.optString("content"));
        manager.notify(message.hashCode(), builder.build());
    }
}
