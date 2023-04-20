package com.skyworth.skyautotester;

import static com.skyworth.skyautotester.Defines.*;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.Toast;

public class SkyTestReceiver extends BroadcastReceiver {
    private final static String TAG = TAG_SkyAutoTest;
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        //接收到本地媒体广播
        if (action.equals(finishMediaTest))
        {
            Log.i(TAG, "SkyTestReceiver " + finishMediaTest);
            if("true".equals(SystemProperties.get(SkyAutoTestProp)))
            {
                Toast.makeText(context,"开始wifi回连测试",Toast.LENGTH_LONG).show();
                try {
                    SystemProperties.set(wifiTestStatusProp, "true");
                    String packageName = "com.skyworth.testwifi";
                    String ActivityName = "com.skyworth.testwifi.module.main.MainActivity";
                    ComponentName componentName = new ComponentName(packageName, ActivityName);
                    //这里Intent传值
                    Intent intentWifi = new Intent();
                    intentWifi.setComponent(componentName);
                    intentWifi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentWifi);
                } catch (Exception e) {
                    Log.e(TAG,"SkyTestReceiver start testwifi Activity error is " + e);
                }
            }else {
                Intent intent1 = new Intent(context, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                Toast.makeText(context, "监测到媒体播放测试结束", Toast.LENGTH_LONG).show();
            }
        }
        //接收到通道切换广播
        else if (action.equals(finishSourceTest))
        {
            Log.i(TAG, "SkyTestReceiver " + finishSourceTest);
            if("true".equals(SystemProperties.get(SkyAutoTestProp))){
                Toast.makeText(context,"开始本地媒体播放测试",Toast.LENGTH_LONG).show();
                try {
                    Intent mediaPlayApk = new Intent();
                    mediaPlayApk.setAction("com.skyworth.broadcast.mediaplay.module.START");
                    mediaPlayApk.setPackage("com.tianci.localmedia");
                    context.sendBroadcast(mediaPlayApk);
                } catch (Exception e) {
                    Log.e(TAG,"SkyTestReceiver start mediaplay Activity error is " + e);
                }
            }else {
                Intent intent2 = new Intent(context, MainActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
                Toast.makeText(context, "监测到通道切换测试结束", Toast.LENGTH_LONG).show();
            }
        }
        //接收到WiFi测试广播
        else if (action.equals(finishWifiTest))
        {
            Log.i(TAG, "SkyTestReceiver " + finishWifiTest);
            Intent intent3 = new Intent(context, MainActivity.class);
            intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//非activity启动startActivity需要设置此Flags
            context.startActivity(intent3);
            Toast.makeText(context, "监测到WiFi回连测试结束", Toast.LENGTH_LONG).show();
        }
    }

}