package com.skyworth.skyautotester.testReportItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.skyworth.skyautotester.R;
import com.skyworth.skyautotester.utils.DataIOUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowFragement extends Fragment {
    TextView tv_test_result;
    Button btn_afresh_test;
    private String name;
    private MyTestListAdapter listAdapter;
    ListView listView_single_test_report;
    ArrayList<ReportInfo> reportTotalInfoList;

    private final static String TAG = "SkyAutoTest";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            name = bundle.getString("name");
        }

        reportTotalInfoList = new ArrayList<ReportInfo>();

        //数据转换，每一行数据都要转换成一个ReportInfo对象
        Map<String, List<String>> map1 = DataIOUtils.GenericFormatDataResp(name+".txt");
        if(null == map1){
            //Toast.makeText(getContext(), "获取失败！！", Toast.LENGTH_LONG).show();
            Log.i(TAG,"获取失败！！");
        }else{
            for(Map.Entry<String, List<String>> entry : map1.entrySet()){
                Log.i(TAG,"k1="+entry.getKey());
                List<String> list = entry.getValue();
                for (String l: list) {
                    Log.i(TAG,"v1="+l);
                    ReportInfo tempReportInfo = null;
                    tempReportInfo = new ReportInfo(name);
                    tempReportInfo.setTestStatus(entry.getKey());
                    tempReportInfo.setTestItemName(l);
                    reportTotalInfoList.add(tempReportInfo);
                }
            }
        }
    }

    public static Fragment getShowFragment(String name){
        ShowFragement showFragment = new ShowFragement();
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        showFragment.setArguments(bundle);
        return showFragment;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sky_fragment_test_report_right,null);
        btn_afresh_test = view.findViewById(R.id.btn_afresh_test);
        btn_afresh_test.setVisibility(View.GONE);
        tv_test_result = view.findViewById(R.id.tv_test_result);
        tv_test_result.setText(name);

        listView_single_test_report = view.findViewById(R.id.listView_single_test_report);
        showTestInfo();

        /*btn_afresh_test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    Toast.makeText(getContext(), "重新测试！！", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "error！", Toast.LENGTH_LONG).show();
                }

            }
        });*/
        return view;
    }

    private void showTestInfo(){
        listAdapter = new MyTestListAdapter(reportTotalInfoList,this.getContext());
        listView_single_test_report.setAdapter(listAdapter);
    }

    class MyTestListAdapter extends BaseAdapter {
        private ArrayList mList;
        LayoutInflater layoutInflater;
        public MyTestListAdapter(ArrayList mList, Context context){
            this.mList = mList;
            this.layoutInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return mList.size();
        }
        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.sky_item_test_report_info, null);

            String tempTestItemName = (String) reportTotalInfoList.get(position).getTestItemName();
            String tempTestStatus = (String) reportTotalInfoList.get(position).getTestStatus();

            TextView test_item_name = convertView.findViewById(R.id.test_item_name);
            test_item_name.setText(tempTestItemName);

            TextView test_status = convertView.findViewById(R.id.test_status);
            test_status.setText(tempTestStatus);

            if(tempTestStatus.equals("FAILED")){
                test_status.setTextColor(Color.rgb(255, 0, 0));
            }

            return convertView;
        }
    }

}
