package com.skyworth.skyautotester.skyFactory;

import static com.skyworth.skyautotester.Defines.TAG_SkyAutoTest;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkyWifiSetting
{
    private final static String TAG = TAG_SkyAutoTest;
    private static SkyWifiSetting instance = null;
    private WifiManager mWifiManager = null;
    private final static String QUOTED = "\"";
    public final static String USB_DEVICE_PATH = "/sys/bus/usb/devices/";
    public final static String PCI_DEVICE_PATH = "/sys/bus/pci/devices/";
    public static Map<String, String> wifi_map = new HashMap<String, String>();
    public static Map<String, String> wifi6_map = new HashMap<String, String>();
    public static String wifi_frequency = "2.4G_5G";

    public SkyWifiSetting(Context context)
    {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static SkyWifiSetting getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new SkyWifiSetting(context);
        }
        return instance;
    }

    public static String getWifiModule()
    {
        wifi_map.put("148f:3070", "RT3070");
        wifi_map.put("148f:5370", "RT5370");
        wifi_map.put("148f:5372", "RT5372");
        wifi_map.put("148f:5572", "RT5572");
        wifi_map.put("148f:7601", "MT7601U,2.4G");
        wifi_map.put("0e8d:76a0", "MT7662TU");
        wifi_map.put("0e8d:7662", "MT7662TU");
        wifi_map.put("0e8d:7961", "MT7921AU");
        wifi_map.put("0cf3:9271", "AR9271");
        wifi_map.put("0cf3:937x", "AR9374");
        wifi_map.put("0cf3:102x", "QCA1021G");
        wifi_map.put("0cf3:102x", "QCA1021X");
        wifi_map.put("0bda:8176", "RTL8188CUS,2.4G");
        wifi_map.put("0bda:8179", "RTL8188EUS,2.4G");
        wifi_map.put("0bda:0179", "RTL8188ETV,2.4G");
        wifi_map.put("0bda:f179", "RTL8188FTV,2.4G");
        wifi_map.put("0bda:018c", "RTL8188GTV,2.4G");
        wifi_map.put("0bda:8171", "RTL8188SU,2.4G");
        wifi_map.put("0bda:8178", "RTL8192CU,2.4G");
        wifi_map.put("0bda:8193", "RTL8192DU-VC,2.4G");
        wifi_map.put("0bda:8194", "RTL8192DU-VS,2.4G");
        wifi_map.put("0bda:818b", "RTL8192EU,2.4G");
        wifi_map.put("0bda:818c", "U8192E2,2.4G");
        wifi_map.put("0bda:f192", "RTL8192FC,2.4G");
        wifi_map.put("0bda:8172", "RTL8192SU,2.4G");
        wifi_map.put("0bda:b720", "RTL8723BU,2.4G");
        wifi_map.put("0bda:d723", "RTL8723DU,2.4G");
        wifi_map.put("0bda:0811", "RTL8811AU");
        wifi_map.put("0bda:8812", "RTL8812AU");
        wifi_map.put("0bda:881a", "RTL8812AU-VS");
        wifi_map.put("0bda:8813", "RTL8814AU");
        wifi_map.put("0bda:b82c", "RTL8822BU");
        wifi_map.put("0bda:c82c", "RTL8822CU");
        wifi_map.put("0bda:c82e", "RTL8822CU-VN");
        wifi_map.put("0e8d:7668", "MT7668BU");
        wifi_map.put("0e8d:7663", "MT7633BU");
        wifi_map.put("007a:8888", "ATBM6032,2.4G");
        wifi_map.put("3452:6600", "ECR6600U,2.4G");//wifi6
        wifi_map.put("0bda:a85b", "RTL8852BU");

        wifi6_map.put("14E4:449D", "BCM43752P,WIFI6");

        String wifiName = "NULL";
        String command1 = "cat /sys/module/atbm603x_wifi_usb/parameters/chip_name";
        String command2 = "cat /proc/net/wlan/efuse_dump | egrep 0x000";
        String command3 = "cat /proc/net/wlanb/efuse_dump | egrep 0x000";

        File usb_devices_path = new File(USB_DEVICE_PATH);
        InputStreamReader read;
        BufferedReader reader;
        for (String device_path : usb_devices_path.list())
        {
            String vid = "";
            String did = "";
            String deviceId = "";
            String line;
            File device = new File(USB_DEVICE_PATH + device_path + "/uevent");
            try
            {
                read = new InputStreamReader(new FileInputStream(device), "UTF-8");
                reader = new BufferedReader(read);
                while ((line = reader.readLine()) != null)
                {
                    if (line.startsWith("PRODUCT="))
                    {
                        String product = line.substring(8);
                        String[] ids = product.split("/");
                        vid = ids[0];
                        did = ids[1];
                        vid = formatId(vid);
                        did = formatId(did);
                        deviceId = vid + ":" + did;
                        Log.i(TAG,"deviceId:" + deviceId);

                        for (Map.Entry<String, String> entry : wifi_map.entrySet())
                        {
                            if (entry.getKey().equals(deviceId))
                            {
                                String dev = entry.getValue();
                                String[] dev_para = dev.split(",");

                                if("NULL".equals(wifiName) )
                                {
                                    wifiName = dev_para[0];
                                }else if( !wifiName.contains(dev_para[0]) )
                                {
                                    wifiName = wifiName + "/" + dev_para[0];
                                }
                                //Log.d("dym111" , "wifiName:" + wifiName);
                                if (dev_para.length > 1)
                                {
                                    wifi_frequency = dev_para[1];
                                }
                            }
                        }
                        break;
                    }
                }
                reader.close();
                read.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if("NULL".equals(wifiName))
        {
            File pci_devices_path = new File(PCI_DEVICE_PATH);
            if(!pci_devices_path.exists())
            {
                return wifiName;
            }
            for (String device_path : pci_devices_path.list())
            {
                String deviceId = "";
                String line;
                File device = new File(PCI_DEVICE_PATH + device_path + "/uevent");
                try
                {
                    read = new InputStreamReader(new FileInputStream(device), "UTF-8");
                    reader = new BufferedReader(read);
                    while ((line = reader.readLine()) != null)
                    {
                        //Debugger.d("line = " + line);
                        if (line.startsWith("PCI_ID="))
                        {
                            deviceId = line.substring(7);
                            Log.i(TAG,"deviceId:" + deviceId);

                            for (Map.Entry<String, String> entry : wifi6_map.entrySet())
                            {
                                if (entry.getKey().equals(deviceId))
                                {
                                    String dev = entry.getValue();
                                    String[] dev_para = dev.split(",");
                                    wifiName = dev_para[0];
                                    if (dev_para.length > 1)
                                    {
                                        wifi_frequency = dev_para[1];
                                    }
                                }
                            }
                            break;
                        }
                    }
                    reader.close();
                    read.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        //无法通过wifi模块vid uid区分的模块型号，再通过获取wifi模块其他信息区分
        if("ATBM6032".equals(wifiName)&&"6032is".equals(getWifiInfo(command1))){
            wifiName = "ATBM6032s";
        }else if((-1 != (wifiName.indexOf("MT7633BU"))) && ("0x000=0x63".equals(getWifiInfo(command2)) || "0x000=0x63".equals(getWifiInfo(command3)))){
            wifiName = wifiName.replace("MT7633BU", "MT7663BU");
        }else if("MT7668BU".equals(wifiName)&&"0x000=0x38".equals(getWifiInfo(command2))){
            wifiName = "MT7638BU";
            String MT7638Type = null;
            MT7638Type = MT7638Module();
            if("MT7638GU".equals(MT7638Type))
            {
                //Log.d("zhuorong" , "MT7638=" + MT7638Type);
                wifi_frequency = "2.4G";
                wifiName = "MT7638GU";
            }
        }

        Log.i(TAG,"wifiName:" + wifiName);

        return wifiName;
    }

    public static String formatId(String id)
    {
        String result = id;
        if (id.length() < 4)
        {
            String tmp = "";
            for (int i = 0; i < 4 - id.length(); i++)
            {
                tmp += "0";
            }
            tmp += id;
            result = tmp;
        }
        return result;
    }

    public static String getWifiInfo(String str)
    {
        String wifiInfo = "";
        try
        {
            List<String> results = new ArrayList<String>();
            Process process = Runtime.getRuntime().exec(str);
            InputStream inStream = process.getInputStream();
            BufferedReader BR = new BufferedReader(new InputStreamReader(inStream, "ISO-8859-1"));
            String line1 = null;
            while ((line1 = BR.readLine()) != null)
            {
                results.add(line1);
            }
            BR.close();
            if (results.size() > 0)
            {
                wifiInfo = results.get(0);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            return wifiInfo;
        }
    }

    public static String MT7638Module()
    {
        Process p;
        String cmd = "iwpriv wlan0 driver GET_WIFI_CHIP";
        String resultstr = null;
        String ret = null;

        try
        {
            p = Runtime.getRuntime().exec(cmd);
            InputStream fis=p.getInputStream();
            BufferedReader br=new BufferedReader( new InputStreamReader(fis,"ISO-8859-1"));
            String line=null;
            int i = 0;
            while((line=br.readLine())!=null)
            {
                if(line.contains("MTK wifi:"))
                {
                    resultstr = line;
                    String[] data = resultstr.split(":");
                    ret = data[1].trim();
                    break;
                }
                i++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return ret;
    }



    public int getWifiState()
    {
        return mWifiManager.getWifiState();
    }

    public boolean isWifiEnable()
    {
        return mWifiManager.isWifiEnabled();
    }

    public void setWifiEnable() {
    }

    public String getWifiMacAddress() {
        return "false";
    }
}
