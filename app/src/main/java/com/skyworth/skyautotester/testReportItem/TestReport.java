package com.skyworth.skyautotester.testReportItem;

//import static com.skyworth.factory.utils.FactoryUtils.copyFile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.skyworth.skyautotester.R;
import com.skyworth.skyautotester.adapter.ReportAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TestReport extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView mListview;
    FrameLayout mFrame;
    @SuppressWarnings("unchecked")
    List<ReportInfo> mList = new ArrayList();//listview的数据集合
    List<Fragment> fragmentList = new ArrayList<>();
    private ReportAdapter adapter;

    Button btn_upload_total = null;

    private final static String TAG = "SkyAutoTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sky_test_report);

        btn_upload_total = findViewById(R.id.btn_upload_total);
        btn_upload_total.setSelected(true);
        btn_upload_total.setFocusable(true);

        initView();
        initData();


        //下载总测试报告
        //先判断是否插入U盘，点击下载文件到U盘根目录下
        btn_upload_total.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    //Toast.makeText(TestReport.this, "下载全部报告！！", Toast.LENGTH_LONG).show();
                    String usbRootName = getUSBPath();
                    Log.i(TAG,"usbRootName = " + usbRootName);
                    if(usbRootName == null || usbRootName.equals("null"))
                    {
                        Toast.makeText(TestReport.this, "请先插入U盘,用于存放下载的总测试报告！", Toast.LENGTH_LONG).show();
                    }else{
                        String oldPath = "/data/SkyAutoTester/test.txt";
                        String newPath = usbRootName + "/test.txt";
                        //先判断该路径下有没有这个文件？（自动生成，文件名固定）
                        //copyFile(oldPath, newPath);     //复制单个文件
                    }

                } catch (Exception e) {
                    Toast.makeText(TestReport.this, "error！", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private String getUSBPath()
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



    //初始化数据
    private void initData() {
        //初始化数据集合
        listViewData();
        //加载fragment
        addFragment();
        //默认选中页面1
        replese(0);
    }
    //切换fragment
    private void replese(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏所有的fragment
        for(int i = 0; i < fragmentList.size(); i++ ){
            Fragment fragment = fragmentList.get(i);
            transaction.hide(fragment);
        }
        transaction.show(fragmentList.get(position));
        transaction.commit();
    }
    private void addFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ShowFragement showFragment = new ShowFragement();
        for(int i = 0; i < mList.size(); i++){
            Fragment fragment = showFragment.getShowFragment(mList.get(i).getFileName());
            fragmentList.add(fragment);
        }
        for(int i = 0; i < fragmentList.size(); i++){
            transaction.add(R.id.mFrame,fragmentList.get(i));
        }
        transaction.commit();
    }
    private void listViewData() {

        //List<String> fileNameList = DataIOUtils.showAllFileFromSkyAutoTester();//获取文件名list

        mList.add(new ReportInfo("MediaReport"));
        mList.add(new ReportInfo("SourceReport"));
        mList.add(new ReportInfo("WifiReport"));

        adapter = new ReportAdapter(mList,this);
        mListview.setAdapter(adapter);
        mListview.setOnItemClickListener(this);
    }
    //控件初始化
    private void initView() {
        mListview = findViewById(R.id.mListview);
        mFrame = findViewById(R.id.mFrame);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击item切换fragment
        replese(position);
        for(int i = 0; i < mList.size(); i++){
            //先把所有的item标记为未被选中
            mList.get(i).setChecked(false);
        }
        //找出被选中的item，把user中的ischecked属性改为true
        mList.get(position).setChecked(true);

        //刷新适配器
        adapter.notifyDataSetChanged();
    }
}
