package com.skyworth.skyautotester.utils;

import android.content.Context;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class USBUtils {
    private final static String TAG = "SkyAutoTest";

    public static String F_L() {
        return CommonUtils.getInstance().getFUNCTION_LINE();
    }

    public static void InstallApk(Context context) throws IOException {
        List<String> allApkFromUsb = getAllApkFromUsb(context);
        if (null == allApkFromUsb) {

        }
    }

    public static List<String> getAllApkFromUsb(Context context) throws IOException {
        Process process = null;
        try {
            String rootname = getUSBPath();
            Log.i(TAG, F_L() + "rootname=" + rootname);
            if (StringUtils.isEmpty(rootname)) {
                return null;
            }
            Log.i(TAG, F_L());
//            File desktop = new File(rootname + "/");
            File desktop = new File("/data/SkyAutoTest/");
            Log.i(TAG, F_L());
            if (!desktop.exists()) {
                Log.i(TAG, rootname + "不存在");
            }
            Log.i(TAG, F_L());
            String[] arr = desktop.list();
            Log.i(TAG, F_L());
            if (arr.length == 0 || null == arr) {
                Log.i(TAG, F_L() + "usb without file");
            } else {
                for (String str : arr) {
                    if (str.endsWith(".apk")) {
                        Log.i(TAG, "有这些apk=" + str);
                       /* String exec = "pm install -r /data/SkyAutoTest/" + str;
                        String []execArr={"su","-c",exec};
                        Log.i(TAG, F_L() + "exec => " + exec);
                        process = Runtime.getRuntime().exec(execArr);*/
                        installSilent("/data/SkyAutoTest/" + str);

                        //int exitVal = process.waitFor() == 0 ? Log.i(TAG, "完成安装 " + str) : Log.i(TAG, "安装失败 " + str);
                        //Log.i(TAG, "exitVal = " + exitVal);
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, F_L() + Log.getStackTraceString(e));
        } finally {
            if (process != null) {
                process.getInputStream().close();
                process.getErrorStream().close();
                process.getOutputStream().close();
            }
        }
        return null;
    }


    /**
     * Silent install
     *
     * @param path Package
     * @return true: success false: failed
     */
    public static boolean installSilent(String path) {
        boolean result = false;
        BufferedReader es = null;
        DataOutputStream os = null;

        try {
            Process process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());

            String command = "pm install -r " + path + "\n";
            os.write(command.getBytes(Charset.forName("utf-8")));
            os.flush();
            os.writeBytes("exit\n");
            os.flush();

            process.waitFor();
            es = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = es.readLine()) != null) {
                builder.append(line);
            }
            Log.d(TAG, "install msg is " + builder.toString());

        /* Installation is considered a Failure if the result contains
            the Failure character, or a success if it is not.
             */
            if (!builder.toString().contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (es != null) {
                    es.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        return result;
    }

    public static String getUSBPath() {
        List<String> results = new ArrayList<String>();
        try {
            Process process = Runtime.getRuntime().exec("mount");
            InputStream inStream = process.getInputStream();
            BufferedReader BR = new BufferedReader(new InputStreamReader(inStream, "ISO-8859-1"));
            String line1 = null;
            while ((line1 = BR.readLine()) != null) {
                results.add(line1);
            }
            BR.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String mount : results) {
            String[] items = mount.split(" ");
            if (items[0].startsWith("/dev/block/vold") || items[0].startsWith("/mnt/mmcblk1p1")) {
                if (items[1].contains("mnt"))
                    return items[1];
                else
                    return items[2];
            }
        }
        return "null";
    }

}
