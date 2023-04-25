package com.skyworth.skyautotester.service;

import static com.skyworth.skyautotester.Defines.*;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.util.Log;

import java.io.File;
public class SkyTestService extends Service {
    private final static int SKYTEST_STATUS = 0;
    private final static int UPGRADE_TEST = 1;
    private final static int WIFI_TEST = 2;

    //文件路径
    private final static String upgradeFilePath = "/data/SkyAutoTester/Upgrade.list";

    private static String upgradeTestFlag;
    private static String wifiTestFlag;
    private static String wifiTestStatus;
    private static String autoTestFlag;

    private final static String TAG = TAG_SkyAutoTest;

    private static Context mContext = null;
    private static Binder mBinder = null;

    public SkyTestService() {}

    @Override
    public void onCreate(){  //创建服务
        Log.i(TAG, "onCreate");
        super.onCreate();

        mContext = getApplicationContext();

        //通过单独线程处理需要开机自动运行的部分
        Thread thread = new Thread(){
            public void run()
            {
                //开机获取系统Properties状态
                mHandler.sendEmptyMessageDelayed(SKYTEST_STATUS, 3000);
            }
        };
        thread.start();

    }

    //通过Handler机制实现系统启动后延时30s判断相关prop属性
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(Looper.getMainLooper())
    {
        public void handleMessage(Message msg)
        {
            if(msg.what == SKYTEST_STATUS)
            {
                getSystemProperties();

                if("true".equals(autoTestFlag))
                {
                    if("true".equals(wifiTestStatus))
                    {
                        mHandler.sendEmptyMessageDelayed(WIFI_TEST, 30000);
                    } else if ("true".equals(upgradeTestFlag))
                    {

                    }
                }else{
                    Log.i(TAG, "SkyTestService do not test");
                }
            } else if (msg.what == UPGRADE_TEST)
            {
                setUpgradeTest();
            } else if(msg.what == WIFI_TEST)
            {
                setWifiTest();
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startid){  //启动服务
        return START_STICKY;
    }

    @Override
    public void onDestroy(){  //销毁服务
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {  //绑定服务
        return null;
    }

    @Override
    public void onRebind(Intent intent) {  //重新绑定服务
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {  //解绑服务
        return true;  //返回false表示只能绑定一次，返回true表示允许多次绑定
    }


    /**
     * 获取系统prop属性
     * @param
     * @return
     */
    public static void getSystemProperties(){
        autoTestFlag = SystemProperties.get(SkyAutoTestProp, "false");
        upgradeTestFlag = SystemProperties.get(upgradeTestProperties, "0");
        wifiTestFlag = SystemProperties.get(wifiTestProperties, "0");
        wifiTestStatus= SystemProperties.get(wifiTestStatusProp, "false");
    }
    /**
     * 启动主页Activity
     * @param
     * @return
     */
    public void startSkyAutoTesterActivity()
    {
        Log.d(TAG, "startSkyAutoTesterActivity");
        Intent MainIntent=new Intent(this, com.skyworth.skyautotester.MainActivity.class);
        MainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(MainIntent);
    }

    /**
     * 服务监听并设置升级测试相关参数
     * @param
     * @return
     */
    public void setUpgradeTest()
    {
        Log.d(TAG, "setUpgradeTest");
        SystemProperties.set(upgradeTestFlag, "false");
        startSkyAutoTesterActivity();
    }

    /**
     * 服务监听并启动wifi测试
     * @param
     * @return
     */
    public void setWifiTest()
    {
        Log.d(TAG, "SkyTestService setWifiTest");
        int wifiTestNum = Integer.parseInt(wifiTestFlag);
        if(wifiTestNum > 1){
            try {
                String packageName = "com.skyworth.testwifi";
                String ActivityName = "com.skyworth.testwifi.module.main.MainActivity";
                ComponentName componentName = new ComponentName(packageName, ActivityName);
                //这里Intent传值
                Intent wifi_intent = new Intent();
                wifi_intent.setComponent(componentName);
                wifi_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(wifi_intent);
            } catch (Exception e) {
                Log.e("MAIN_ACTIVITY","SkyTestService start Activity error is " + e);
            }

        }else{
            SystemProperties.set(wifiTestStatus, "false");
            startSkyAutoTesterActivity();
        }
    }

    /**
     * 重启
     * @param
     * @return
     */
    public boolean reboot(){
        Log.d(TAG, "reboot");
        boolean ret = true;
        try{
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            pm.reboot("");
        }catch (Exception e){
            ret = false;
        }
        return  ret;
    }

    /**
     * 文件是否存在
     * @param
     * @return
     */
    public static boolean feilExist(String filePath)
    {
        Log.d(TAG, "feilExist");
        File file = new File(filePath);
        if(file.exists()){
            Log.i(TAG, "The" + filePath + "exist");
            return true;
        }
        return false;
    }

}