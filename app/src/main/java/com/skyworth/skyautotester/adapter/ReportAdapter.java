package com.skyworth.skyautotester.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skyworth.skyautotester.R;
import com.skyworth.skyautotester.testReportItem.ReportInfo;

import java.util.List;

public class ReportAdapter extends BaseAdapter {

    List<ReportInfo> mList;
    Context context;

    public ReportAdapter(List<ReportInfo> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReportAdapter.ViewHolder holder;
        if(convertView == null){
            holder = new ReportAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.sky_item_test_report_left, null);
            holder.tvText = convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
        }else{
            holder = (ReportAdapter.ViewHolder) convertView.getTag();
        }

        String name = mList.get(position).getFileName();
        switch(name){
            case "MediaReport":
                holder.tvText.setText("多媒体检测");
                break;
            case "SourceReport":
                holder.tvText.setText("信号源检测");
                break;
            case "WifiReport":
                holder.tvText.setText("Wifi检测");
                break;
            default:
                holder.tvText.setText("其他检测项");
                break;
        }
        /*if(mList.get(position).isChecked()){
            holder.tvText.setTextColor(Color.RED);
            holder.tvLine.setVisibility(View.VISIBLE);
            holder.llMenu.setBackgroundColor(Color.WHITE);
        }else{
            holder.tvText.setTextColor(Color.BLACK);
            holder.tvLine.setVisibility(View.GONE);
            holder.llMenu.setBackgroundColor(Color.parseColor("#ebebeb"));
        }*/
        return convertView;
    }
    class ViewHolder {
        TextView tvText;
    }
}
