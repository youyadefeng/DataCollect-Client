package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.bean.Quest;

import java.util.List;

public class ChartAdapter extends ArrayAdapter<Quest> {
    int resourceId;
    public ChartAdapter(@NonNull Context context, int resource, @NonNull List<Quest> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Quest quest = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.description = (TextView) view.findViewById(R.id.description);
            viewHolder.number = (TextView)view.findViewById(R.id.data_number);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(quest.getTitle());
        viewHolder.description.setText(quest.getDescription());
        viewHolder.number.setText(Integer.toString(quest.getDataNumber()));
        return view;
    }

    class ViewHolder
    {
        TextView title;
        TextView description;
        TextView number;
    }
}
