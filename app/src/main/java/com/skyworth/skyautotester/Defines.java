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

    /************************ 图片类型 START *******************************/
    public final static String PIC_TYPE_AV = "AV";
    public final static String PIC_TYPE_HDMI1 = "HDMI1";
    public final static String PIC_TYPE_HDMI2 = "HDMI2";
    public final static String PIC_TYPE_HDMI3 = "HDMI3";
    public final static String PIC_TYPE_HDMI4 = "HDMI4";
    public final static String PIC_TYPE_VOD = "VOD";
    /************************ 图片类型 END *******************************/
}
