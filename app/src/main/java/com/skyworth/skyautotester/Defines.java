package com.skyworth.skyautotester;

public class Defines {
    //静态信息
    public final static String TAG_SkyAutoTest = "SkyAutoTest";
    //静态系统属性
    public final static String SkyAutoTestProp = "persist.skytest.auto.status";
    public final static String upgradeTestProperties = "persist.skytest.upgrade.status";
    public final static String wifiTestProperties = "persist.skytest.wifi.status";
    public final static String wifiTestStatusProp = "persist.skytest.wifistatus";
    //静态广播
    public final static String startMediaTest = "com.skyworth.broadcast.mediaplay.module.START";
    public final static String startSourceTest = "com.skyworth.broadcast.source.module.START";
    public final static String finishMediaTest = "com.skyworth.broadcast.mediaplay.module.FINISH";
    public final static String finishSourceTest = "com.skyworth.broadcast.source.module.FINISH";
    public final static String finishWifiTest = "com.skyworth.broadcast.wifi.module.FINISH";

}
