package com.jyq.qs.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;


import com.jyq.qs.R;
import com.jyq.qs.activity.MainActivity;
import com.jyq.qs.utils.APPUtils;
import com.jyq.qs.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.PushService;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver
{
    private static final String TAG = "JIGUANG-Example";
    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(final Context context, Intent intent)
    {
        try
        {
            Intent pushintent = new Intent(context, PushService.class);//启动极光推送的服务
            context.startService(pushintent);

            Bundle bundle = intent.getExtras();
            LogUtil.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
            {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtil.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            }
            else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
            {
                LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            }
            else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
            {
                LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);


                new Handler().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        playVoice(context);
                    }
                });


            }
            else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
            {
                LogUtil.d(TAG, "[MyReceiver] 用户点击打开了通知");


                if (APPUtils.isForeground(context, MainActivity.class.getName()))
                {
                    return;
                }
                //打开自定义的Activity
                Intent intent1 = context.getPackageManager().getLaunchIntentForPackage("com.jyq.qs");
                if (intent1 != null)
                {
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }

            }
            else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))
            {
                LogUtil.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            }
            else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))
            {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                LogUtil.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + "" + connected);
            }
            else
            {
                LogUtil.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        }
        catch (Exception e)
        {

        }

    }

    private boolean isPalying;

    public void playVoice(Context context)
    {

        if (isPalying)
        {
            return;
        }
        try
        {
            if (null == mediaPlayer)
            {
                mediaPlayer = MediaPlayer.create(context, R.raw.order);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        //mediaPlayer.reset();
                        isPalying = false;
                    }
                });
            }

            mediaPlayer.start();
            isPalying = true;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle)
    {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet())
        {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID))
            {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }
            else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE))
            {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            }
            else if (key.equals(JPushInterface.EXTRA_EXTRA))
            {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA)))
                {
                    LogUtil.i(TAG, "This message has no Extra data");
                    continue;
                }

                try
                {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext())
                    {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
                    }
                }
                catch (JSONException e)
                {
                    LogUtil.e(TAG, "Get message extra JSON error!");
                }

            }
            else
            {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle)
    {
        //		if (MainActivity.isForeground) {
        //			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        //			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        //			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
        //			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
        //			if (!ExampleUtil.isEmpty(extras)) {
        //				try {
        //					JSONObject extraJson = new JSONObject(extras);
        //					if (extraJson.length() > 0) {
        //						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
        //					}
        //				} catch (JSONException e) {
        //
        //				}
        //
        //			}
        //			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        //		}
    }
}
