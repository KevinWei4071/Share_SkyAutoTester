package com.skyworth.skyautotester.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.skyworth.skyautotester.R;
import com.skyworth.skyautotester.skyFactory.SkyFactoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoreInfoActivity extends AppCompatActivity {
    private final static int MAX_SOFTWARE_NUM = 7;
    private final static int MAX_HARDWARE_NUM = 1;
    private ListView software_listView = null;
    private ListView hardware_listView = null;

    private SkyFactoryManager skyFactoryManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        software_listView = findViewById(R.id.software_listview);
        hardware_listView = findViewById(R.id.hardware_listview);

        skyFactoryManager = new SkyFactoryManager(this);

        setSoftwareInfo();
        setHardwareInfo();
    }

    public void setSoftwareInfo()
    {
        String[] software_type = {"EEP版本",
                "配屏数据版本号",
                "Barcode",
                "开机图片",
                "2800版本",
                "MCU版本",
                "是否支持HSR"
        };

        String[] software_data = {skyFactoryManager.onGetEEPVersion(),
                skyFactoryManager.onGetPanelDataVersion(),
                skyFactoryManager.onGetBarcode(),
                skyFactoryManager.onGetBootLogo(),
                skyFactoryManager.onGet2800Version(),
                skyFactoryManager.onGetMcuVersion(),
                skyFactoryManager.onGetSupportHSR()
        };

        List<Map<String, Object>> listItems=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < MAX_SOFTWARE_NUM; i++) {
            Map<String, Object> listItem=new HashMap<String,Object>();
            listItem.put("header", software_type[i]);
            listItem.put("second", software_data[i]);
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.moreinfo_list_items,new String[]{"header","second"},new int[]{R.id.tvF,R.id.tvS});
        software_listView.setAdapter(simpleAdapter);
    }

    public void setHardwareInfo()
    {

        String[] hardware_type = {"WIFI信息"
        };

        String[] hardware_data = {skyFactoryManager.updateWifiInfo()
        };

        List<Map<String, Object>> listItems=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < MAX_HARDWARE_NUM; i++) {
            Map<String, Object> listItem=new HashMap<String,Object>();
            listItem.put("header", hardware_type[i]);
            listItem.put("second", hardware_data[i]);
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.moreinfo_list_items,new String[]{"header","second"},new int[]{R.id.tvF,R.id.tvS});
        hardware_listView.setAdapter(simpleAdapter);
    }

}
