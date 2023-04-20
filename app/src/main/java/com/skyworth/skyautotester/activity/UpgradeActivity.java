package com.skyworth.skyautotester.activity;

import static com.skyworth.skyautotester.Defines.TAG_SkyAutoTest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skyworth.skyautotester.R;
import com.skyworth.skyautotester.adapter.UpgradeAdapter;
import com.skyworth.skyautotester.entity.UpGradeHistoryDetail;
import com.skyworth.skyautotester.entity.UpGradeTotal;
import com.skyworth.skyautotester.utils.DataIOUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpgradeActivity extends AppCompatActivity {

    private ArrayList<String> upgradeList;
    private ArrayList<String> updateHistory;
    private UpgradeAdapter upgradeAdapter;

    private ListView listView_upgrade_list;
    private ListView listView_upgrade_history;
    private final static String TAG = TAG_SkyAutoTest;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        listView_upgrade_list = findViewById(R.id.listView_upgrade_list);
        listView_upgrade_history = findViewById(R.id.listView_upgrade_history);
        listView_upgrade_list.setItemsCanFocus(true);

        //进入该界面就判断list文件，展示旧的升级信息，若没有，则显示null
        interfaceInit();
    }

    /**
     * 版本升级界面加载
     * @param
     * @return
     */
    public boolean interfaceInit(){
        boolean fileIsExist = DataIOUtils.isExistFile(new File("/data/SkyAutoTester/update.list"));
        if(fileIsExist == false){
            Log.i(TAG, "该文件还未生成");
            Toast.makeText(this,"无版本升级记录", Toast.LENGTH_LONG).show();
            return false;
        }

        upgradeList = new ArrayList<>();
        UpGradeTotal upGradeTotal1 = DataIOUtils.ShowUpgradeInfo();
        if(null == upGradeTotal1){
            Log.i(TAG, "升级列表获取失败");
        }else{
            Map<String, List<String>> map1 = upGradeTotal1.getUpGradeList();
            for(Map.Entry<String, List<String>> entry : map1.entrySet()){
                Log.i(TAG,"k1="+entry.getKey());
                List<String> list = entry.getValue();
                for (String l: list) {
                    Log.i(TAG,"v1="+l);
                    upgradeList.add(l);
                }
            }
            showUpgradeListInfo();
        }

        updateHistory = new ArrayList<>();
        UpGradeTotal upGradeTotal2 = DataIOUtils.ShowUpgradeInfo();
        if(null == upGradeTotal2){
            Log.i(TAG, "升级历史记录获取失败");
        }else{
            Map<String, List<UpGradeHistoryDetail>> map2 = upGradeTotal2.getUpGradeHistoryInfo();
            for(Map.Entry<String, List<UpGradeHistoryDetail>> entry :map2.entrySet()){
                Log.i(TAG,"k2="+entry.getKey());
                List<UpGradeHistoryDetail> l2 = entry.getValue();
                for (UpGradeHistoryDetail l2print:l2){
                    Log.i(TAG,"v2="+l2print.getDetail().toString());
                    updateHistory.add(l2print.getDetail().toString());
                }
            }
            showUpgradeHistoryInfo();
        }
        return true;
    }

    /**
     * 显示升级列表信息
     * @param
     * @return
     */
    private void showUpgradeListInfo() {
        upgradeAdapter = new UpgradeAdapter(upgradeList, this);
        listView_upgrade_list.setAdapter(upgradeAdapter);
    }

    /**
     * 显示升级历史记录信息
     * @param
     * @return
     */
    private void showUpgradeHistoryInfo(){
        upgradeAdapter = new UpgradeAdapter(updateHistory,this);
        listView_upgrade_history.setAdapter(upgradeAdapter);
    }

}