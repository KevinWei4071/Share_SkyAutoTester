package com.skyworth.skyautotester.testReportItem;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DocumentHandling {
    private final static String TAG = "SkyAutoTester";
    public static  String getUSBPath()
    {
        List<String> results = new ArrayList<String>();
        try
        {
            Process process = Runtime.getRuntime().exec("mount");
            InputStream inStream = process.getInputStream();
            BufferedReader BR = new BufferedReader(new InputStreamReader(inStream, "ISO-8859-1"));
            String line1 = null;
            while ((line1 = BR.readLine()) != null)
            {
                results.add(line1);
            }
            BR.close();

        } catch (Exception e)
        {
            ;
        }
        for (String mount : results)
        {
            String[] items = mount.split(" ");

            if (items[0].startsWith("/dev/block/vold") || items[0].startsWith("/mnt/mmcblk1p1"))
            {

                if(items[1].contains("mnt"))
                    return items[1];
                else
                    return items[2];
            }
        }
        return "null";
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static  void copyFile(String oldPath, String newPath) {
        try {
            int byteSum = 0;
            int byteRead = 0;
            int flag = 0;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteRead = inStream.read(buffer)) != -1) {  //直接覆盖？
                    byteSum += byteRead; //字节数 文件大小
                    //System.out.println(bytesum);
                    fs.write(buffer, 0, byteRead);
                    flag = 1;
                }
                inStream.close();
            }else{
                Log.i(TAG, "复制的文件不存在！");
                //Toast.makeText(TestReport.this, "下载的文件不存在！", Toast.LENGTH_LONG).show();
            }

            if(flag == 1){
                //Toast.makeText(TestReport.this, "下载成功", Toast.LENGTH_LONG).show();
                Log.i(TAG, "文件复制成功！");
            }else{
                //Toast.makeText(TestReport.this, "下载失败", Toast.LENGTH_LONG).show();
                Log.i(TAG, "文件复制失败！");
            }

        }
        catch (Exception e) {
            Log.i(TAG,"复制单个文件操作出错");
            e.printStackTrace();
        }
    }

}
