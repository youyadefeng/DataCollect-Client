package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.adapter.ChartAdapter;
import com.example.myapplication.adapter.QuestAdapter;
import com.example.myapplication.bean.Quest;
import com.example.myapplication.helper.NetWorkTask;

import java.util.ArrayList;
import java.util.List;


public class FragmentChart extends Fragment {
    private List<Quest> questList = new ArrayList<Quest>();
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    DownloadTask downloadTask;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiper);
        listView = (ListView) view.findViewById(R.id.listview);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onRefresh() {
                if (NetWorkTask.isNetConnection(getActivity()))
                {
                    if (downloadTask == null || downloadTask.getStatus() != AsyncTask.Status.RUNNING)
                        downloadTask = new DownloadTask();
                    downloadTask.execute();
                }
                else
                {
                    Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
                }

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ChartDetailActivity.class);
                intent.putExtra("quest", questList.get(i));
                startActivity(intent);
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        if (NetWorkTask.isNetConnection(getActivity()))
        {
            downloadTask = new DownloadTask();
            downloadTask.execute();
        }
        else
            Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (downloadTask != null)
            downloadTask.cancel(true);
    }


    class DownloadTask extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MainPageActivity activity = (MainPageActivity)getActivity();
            questList = NetWorkTask.GetHavingQuest(activity.GetUserId());
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ChartAdapter chartAdapter = new ChartAdapter(getActivity(), R.layout.chart_layout, questList);
            listView.setAdapter(chartAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}