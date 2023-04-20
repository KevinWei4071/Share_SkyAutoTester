package com.skyworth.skyautotester;

import static com.skyworth.skyautotester.Defines.SkyAutoTestProp;
import static com.skyworth.skyautotester.Defines.TAG_SkyAutoTest;
import static com.skyworth.skyautotester.Defines.wifiTestProperties;
import static com.skyworth.skyautotester.Defines.wifiTestStatusProp;
import static com.skyworth.skyautotester.utils.DataIOUtils.createSkyAutoTester;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skyworth.skyautotester.testReportItem.TestReport;
import com.skyworth.skyautotester.activity.MoreInfoActivity;
import com.skyworth.skyautotester.activity.UpgradeActivity;
import com.skyworth.skyautotester.dialog.AlertDialogs;
import com.skyworth.skyautotester.skyFactory.SkyFactoryManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    //实例化组件对象
    private TextView machine_info = null;
    private TextView software_version = null;
    private TextView panel_id = null;
    private TextView coocaa_version = null;
    private Button more_info = null;
    private Button test_report = null;
    private Button test_all_modules = null;
    private Button version_upgrade = null;
    private Button install_app = null;
    private Button uninstall_app = null;
    private Button space_clear = null;
    private Button media_player_test = null;
    private Button source_channel_test = null;
    private Button wifi_callback_test = null;
    //实例化对象
    private AlertDialogs alertDialogs = null;
    private SkyFactoryManager skyFactoryManager = null;

    private final static String TAG = TAG_SkyAutoTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建或判断是否存在文件夹
        try {
            createSkyAutoTester();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setSystemProperties();
        initUI();
        startIntent();
    }

    /**
     * 初始化主页UI
     * @param
     * @return
     */
    @SuppressLint("WrongViewCast")
    public void initUI()
    {
        machine_info = findViewById(R.id.machine_info);
        software_version = findViewById(R.id.software_version);
        panel_id = findViewById(R.id.panel_id);
        coocaa_version = findViewById(R.id.coocaa_version);
        more_info = findViewById(R.id.more_info);
        test_report = findViewById(R.id.test_report);
        test_all_modules = findViewById(R.id.test_all_modules);
        version_upgrade = findViewById(R.id.version_upgrade);
        version_upgrade.setSelected(true);
        version_upgrade.setFocusable(true);
        install_app = findViewById(R.id.install_app);
        uninstall_app = findViewById(R.id.uninstall_app);
        space_clear = findViewById(R.id.space_clear);

        media_player_test = findViewById(R.id.media_player_test);
        source_channel_test = findViewById(R.id.source_channel_test);
        wifi_callback_test = findViewById(R.id.wifi_callback_test);

        showSkyFactory();
    }

    /**
     * 主页基本信息显示
     * @param
     * @return
     */
    public void showSkyFactory()
    {
        skyFactoryManager = new SkyFactoryManager(this);
        machine_info.setText(skyFactoryManager.onGetModelType());
        software_version.setText(skyFactoryManager.onGetCpuVersion());
        coocaa_version.setText(skyFactoryManager.onGetCoocaaVersion());
        panel_id.setText(skyFactoryManager.onGetPanelID());
    }

    /**
     * 监听主页Intent
     * @param
     * @return
     */
    public void startIntent()
    {
        //更多信息
        more_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    Intent infoIntent = new Intent();
                    infoIntent.setClass(MainActivity.this, MoreInfoActivity.class);
                    startActivity(infoIntent);

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, R.string.INTERFACE_MISSING, Toast.LENGTH_LONG).show();
                }
            }
        });

        //测试报告
        test_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent reportIntent = new Intent();
                    reportIntent.setClass(MainActivity.this, TestReport.class);
                    startActivity(reportIntent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, R.string.INTERFACE_MISSING, Toast.LENGTH_LONG).show();
                }
            }
        });

        //一键测试
        test_all_modules.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                alertDialogs = new AlertDialogs(MainActivity.this);
                alertDialogs.setTitle("提示");
                alertDialogs.setMessage("确定开启一键测试?");
                alertDialogs.setYesOnclickListener("确定", new AlertDialogs.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        //设置相关prop属性
                        SystemProperties.set(SkyAutoTestProp, "true");
                        Toast.makeText(MainActivity.this,"开始本地媒体播放测试",Toast.LENGTH_LONG).show();

                        Intent autoIntent = new Intent();
                        autoIntent.setAction("com.skyworth.broadcast.mediaplay.module.START");
                        autoIntent.setPackage("com.tianci.localmedia");
                        sendBroadcast(autoIntent);
                        alertDialogs.dismiss();
                    }
                });
                alertDialogs.setNoOnclickListener("取消", new AlertDialogs.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        Toast.makeText(MainActivity.this,"已取消",Toast.LENGTH_LONG).show();
                        alertDialogs.dismiss();
                    }
                });
                alertDialogs.show();
            }
        });

        //版本升级
        version_upgrade.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    Intent upgradeIntent = new Intent();
                    upgradeIntent.setClass(MainActivity.this, UpgradeActivity.class);
                    startActivity(upgradeIntent);

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, R.string.INTERFACE_MISSING, Toast.LENGTH_LONG).show();
                }
            }
        });

        //应用安装
        install_app.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this, "功能暂不支持", Toast.LENGTH_LONG).show();
            }
        });

        //应用卸载
        uninstall_app.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this, "功能暂不支持", Toast.LENGTH_LONG).show();
            }
        });

        //空间清理
        space_clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this, "功能暂不支持", Toast.LENGTH_LONG).show();
            }
        });

        //本地媒体测试
        media_player_test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                alertDialogs = new AlertDialogs(MainActivity.this);
                alertDialogs.setTitle("提示");
                alertDialogs.setMessage("请确认是否开始本地媒体测试?");
                alertDialogs.setYesOnclickListener("确定", new AlertDialogs.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        Toast.makeText(MainActivity.this,"开始本地媒体播放测试",Toast.LENGTH_LONG).show();

                        Intent mediaIntent = new Intent();
                        mediaIntent.setAction("com.skyworth.broadcast.mediaplay.module.START");
                        mediaIntent.setPackage("com.tianci.localmedia");
                        sendBroadcast(mediaIntent);
                        alertDialogs.dismiss();
                    }
                });
                alertDialogs.setNoOnclickListener("取消", new AlertDialogs.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        Toast.makeText(MainActivity.this,"已取消",Toast.LENGTH_LONG).show();
                        alertDialogs.dismiss();
                    }
                });
                alertDialogs.show();
            }
        });

        //通道切换测试
        source_channel_test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                alertDialogs = new AlertDialogs(MainActivity.this);
                alertDialogs.setTitle("提示");
                alertDialogs.setMessage("确定开始进行通道切换测试?");
                alertDialogs.setYesOnclickListener("确定", new AlertDialogs.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        Toast.makeText(MainActivity.this,"开始通道切换测试",Toast.LENGTH_LONG).show();
                        Intent sourceIntent = new Intent();
                        sourceIntent.setAction("com.skyworth.broadcast.source.module.START");//通过广播通知其他应用
                        sourceIntent.setPackage("com.tianci.source");
                        sendBroadcast(sourceIntent);
                        alertDialogs.dismiss();
                    }
                });
                alertDialogs.setNoOnclickListener("取消", new AlertDialogs.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        Toast.makeText(MainActivity.this,"已取消",Toast.LENGTH_LONG).show();
                        alertDialogs.dismiss();
                    }
                });
                alertDialogs.show();
            }
        });

        //wifi回连测试
        wifi_callback_test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                alertDialogs = new AlertDialogs(MainActivity.this);
                alertDialogs.setTitle("提示");
                alertDialogs.setMessage("确定开始进行WiFi回连测试?");
                alertDialogs.setYesOnclickListener("确定", new AlertDialogs.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        Toast.makeText(MainActivity.this,"开始wifi回连测试",Toast.LENGTH_LONG).show();
                        //设置相关prop属性以实现开机后自动继续wifi回连测试
                        SystemProperties.set(SkyAutoTestProp, "true");
                        SystemProperties.set(wifiTestStatusProp, "true");

                        try {
                            String packageName = "com.skyworth.testwifi";
                            String ActivityName = "com.skyworth.testwifi.module.main.MainActivity";
                            ComponentName componentName = new ComponentName(packageName, ActivityName);
                            //这里Intent传值
                            Intent wifi_intent = new Intent();
                            wifi_intent.setComponent(componentName);
                            wifi_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//调用其他应用需要新增Flags
                            startActivity(wifi_intent);
                        } catch (Exception e) {
                            Log.e(TAG,"start testwifi Activity error is " + e);
                        }
                        alertDialogs.dismiss();
                    }
                });
                alertDialogs.setNoOnclickListener("取消", new AlertDialogs.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        Toast.makeText(MainActivity.this,"已取消",Toast.LENGTH_LONG).show();
                        alertDialogs.dismiss();
                    }
                });
                alertDialogs.show();
            }
        });

        //more
    }

    /**
     * 延时ms
     * @param num
     * @return
     */
    public void sleepms(int num){
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(num);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    /**
     * 初始化影响测试相关的系统属性
     * @param
     * @return
     */
    public static void setSystemProperties(){
        SystemProperties.set(SkyAutoTestProp, "false");
        SystemProperties.set(wifiTestProperties, "0");
        SystemProperties.set(wifiTestStatusProp, "false");
    }

}
