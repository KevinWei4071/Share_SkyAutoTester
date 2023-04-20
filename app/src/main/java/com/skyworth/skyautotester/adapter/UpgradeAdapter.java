package com.skyworth.skyautotester.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skyworth.skyautotester.R;

import java.util.ArrayList;

public class UpgradeAdapter extends BaseAdapter {

    private ArrayList arrayList;
    LayoutInflater layoutInflater;
    public UpgradeAdapter(ArrayList arrayList, Context context){
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.sky_item_usb_info, null);
        String tempUsbInfo = (String) arrayList.get(position);
        TextView usbPath = convertView.findViewById(R.id.upgrade_info);
        usbPath.setText(tempUsbInfo);
        return convertView;
    }
}