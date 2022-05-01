package com.example.myapplication.adapter;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.BaseData;
import com.example.myapplication.R;

import java.util.List;

public class MyAdapter extends ArrayAdapter<BaseData> {
    int resourceId;
    public MyAdapter(@NonNull Context context, int resource, @NonNull List<BaseData> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BaseData data = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView title = (TextView) view.findViewById(R.id.text);
        title.setText(data.title);
        return view;
    }
}
