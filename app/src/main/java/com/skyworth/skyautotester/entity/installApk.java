package com.skyworth.skyautotester.entity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class installApk {

    public static  void installMyApk(Context context){
        try{//这里有文件流的读写，需要处理一下异常
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri;
            File file = new File("/data/SkyAutoTester/TVTest.apk");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                //如果SDK版本 =24，即：Build.VERSION.SDK_INT  = 24
                String packageName = context.getApplicationContext().getPackageName();
                String authority = new StringBuilder(packageName).append(".provider").toString();
                uri = FileProvider.getUriForFile(context, authority, file);
            } else{
                uri = Uri.fromFile(file);
            }
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
