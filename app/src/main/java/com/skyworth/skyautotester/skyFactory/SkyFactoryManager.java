package com.skyworth.skyautotester.skyFactory;

import static com.skyworth.skyautotester.Defines.TAG_SkyAutoTest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.SystemProperties;
import android.util.Log;

import com.tianci.plugins.factory.BaseAdvancedManager;
import com.tianci.plugins.factory.BaseFactoryManager;
import com.tianci.plugins.factory.BaseGeneralManager;
import com.tianci.plugins.factory.BaseInfoManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SkyFactoryManager {
    private final static String TAG = TAG_SkyAutoTest;
    private Context mContext = null;
    private static boolean isInitialized = false;

    private BaseFactoryManager.OnInitListener mListener = null;
    private BaseFactoryManager factoryManager = null;
    private BaseInfoManager infoManager = null;
    private BaseGeneralManager generalManager = null;
    private BaseAdvancedManager advancedManager = null;

    //wifi相关
    private boolean mWifiStatus = false;
    String wifiInfo = "";
    private SkyWifiSetting mWifiSetting = null;

    public SkyFactoryManager(final Context context)
    {
        mContext = context;
        mListener = new BaseFactoryManager.OnInitListener() {
            @Override
            public void onInit() {
                isInitialized = true;
            }
        };

        factoryManager = LoadPlugin.LoadFactoryManager(mContext);
        factoryManager.init(mContext, mListener);
        infoManager = factoryManager.getInfoManager();
        generalManager = factoryManager.getGeneralManager();
        advancedManager = factoryManager.getAdvancedManager();
    }

    //主页相关数据
    public String onGetModelType()
    {
        String str = "null";

        String brand= SystemProperties.get("ro.product.brand","null");
        if("null".equals(brand))
        {
            brand="Skyworth";
        }

        String model=SystemProperties.get("ro.base.skymodel","null");
        String type=SystemProperties.get("ro.base.skytype","null");
        if("null".equals(model) ||"null".equals(type))
        {
            model=SystemProperties.get("ro.build.skymodel");
            type=SystemProperties.get("ro.build.skytype");
        }

        str = brand+" " +model+" "+type;
        Log.i(TAG,"onGetModelType="+str);
        return  str;
    }

    public String onGetCpuVersion()
    {
        String cpuVer = "null";
        cpuVer = infoManager.getCpuVersion();
        Log.i(TAG,"onGetCpuVersion="+cpuVer);
        return cpuVer;
    }

    public String onGetCoocaaVersion()
    {
        String UIVer = "null";
        UIVer = infoManager.getCoocaaVersion();
        Log.i(TAG,"onGetCpuVersion="+UIVer);
        return UIVer;
    }

    public String onGetPanelID()
    {
        String panelID = "null";
        panelID = infoManager.getPanelID();
        Log.i(TAG,"onGetPanelID="+panelID);
        return panelID;
    }

    //更多信息里软件相关数据
    public String onGetEEPVersion()
    {
        String EEPVer = "null";
        EEPVer = infoManager.getEepromDate();
        Log.i(TAG,"onGetEEPVersion="+EEPVer);
        return EEPVer;
    }

    public  String onGetPanelDataVersion()
    {
        String PanelDataVer = "null";
        PanelDataVer = infoManager.getPanelDataVersion();
        Log.i(TAG,"onGetPanelDataVersion="+PanelDataVer);
        return PanelDataVer;
    }

    public  String onGetBarcode()
    {
        String Barcode = "null";
        Barcode = generalManager.getBarcode();
        Log.i(TAG,"onGetBarcode="+Barcode);
        return Barcode;
    }

    public  String onGetBootLogo()
    {
        String BootLogo = "null";
        String cmd = "BOOTLOGO_INFO";
        BootLogo = infoManager.getExtraInfo(cmd);
        Log.i(TAG,"onGetBootLogo="+BootLogo);
        return BootLogo;
    }

    public  String onGet2800Version()
    {
        String Ver = "null";
        Ver = SystemProperties.get("persist.sys.rtk2800version","null");
        Log.i(TAG,"onGet2800Version="+Ver);
        if(Ver.equals("null"))
        {
            return "Not Support";
        }
        else{
            return Ver;
        }
    }

    public  String onGetMcuVersion()
    {
        String McuVer = "null";
        McuVer = SystemProperties.get("third.get.mcuversion","null");
        Log.i(TAG,"onGetMcuVersion="+McuVer);
        if(McuVer.equals("null"))
        {
            return "Not Support";
        }
        else{
            return McuVer;
        }
    }

    public  String onGetSupportHSR()
    {
        int value = -1;
        //value = advancedManager.isSupportHSR();
        Log.i(TAG,"onGetSupportHSR="+value);
        if(value > 0)
        {
            return "Support";
        }
        else{
            return "Not Support";
        }
    }

    //更多信息里硬件相关数据
    public String updateWifiInfo()
    {
        mWifiSetting = SkyWifiSetting.getInstance(mContext);
        wifiInfo = mWifiSetting.getWifiModule();

        if("NULL".equals(wifiInfo))
        {
            wifiInfo = wifiInfo + "/" + "NULL" ;
        }else
        {
            wifiInfo = wifiInfo + "/" + mWifiSetting.wifi_frequency ;
        }
        checkWifi();
        Log.i(TAG,"updateWifiInfo="+wifiInfo);
        return wifiInfo;
    }

    private void checkWifi()
    {
        registerBroadcast();
        int wifiState = mWifiSetting.getWifiState();
        Log.i(TAG,"wifiState:" + wifiState);

        mWifiStatus = mWifiSetting.isWifiEnable();
        Log.i(TAG,"mWifiStatus:" + mWifiStatus);

        if (wifiState == WifiManager.WIFI_STATE_DISABLING
                || wifiState == WifiManager.WIFI_STATE_DISABLED)
            mWifiStatus = false;
        Log.i(TAG,"mWifiStatus:" + mWifiStatus);
        if (mWifiStatus == false)
        {
            try
            {
                Log.i(TAG,"setWifiEnabled(true)");
                mWifiSetting.setWifiEnable();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {
            getWifiMac();
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            final String action = intent.getAction();
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION))
            {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (wifiState)
                {
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.i(TAG,"WIFI_STATE_ENABLED");
                        getWifiMac();
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        Log.i(TAG,"WIFI_STATE_DISABLED");
                        break;
                }
            }
        }
    };

    private void registerBroadcast()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mContext.registerReceiver(mReceiver, filter);
    }

    private void getWifiMac()
    {
        Log.i(TAG,"getWifiMac");

        String wifiMac = mWifiSetting.getWifiMacAddress();
        if (wifiMac != null && !wifiMac.equals("00:00:00:00:00:00"))
        {
            if(wifiMac.contains("00:00:00:00"))
            {
                try {
                    List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                    for (NetworkInterface nif : all) {
                        if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                        byte[] macBytes = nif.getHardwareAddress();
                        if (macBytes != null) {
                            StringBuilder res1 = new StringBuilder();
                            for (byte b : macBytes) {
                                res1.append(String.format("%02X:",b));
                            }

                            if (res1.length() > 0) {
                                res1.deleteCharAt(res1.length() - 1);
                            }
                            wifiMac = res1.toString();
                        }
                    }
                }catch (Exception ex) {
                }
            }
            wifiInfo = wifiInfo + "/" + (wifiMac).toUpperCase(Locale.ENGLISH);
        }
    }
}
