package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private List<BaseData> baseDataList = new ArrayList<BaseData>();
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //ListView
        InitList();
        listView = (ListView) findViewById(R.id.listview);
        myAdapter = new MyAdapter(MainActivity2.this, R.layout.customed_layout, baseDataList);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity2.this, "111", Toast.LENGTH_SHORT).show();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DownloadTask();
            }
        });

    }

    void DownloadTask()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        baseDataList.get(0).title = "change title!";
                        listView.setAdapter(myAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    void InitList()
    {
        BaseData data1 = new BaseData("yy1");
        BaseData data2 = new BaseData("yy2");
        BaseData data3 = new BaseData("yy3");
        BaseData data4 = new BaseData("yy4");
        baseDataList.add(data1);
        baseDataList.add(data2);
        baseDataList.add(data3);
        baseDataList.add(data4);
        baseDataList.add(data1);
        baseDataList.add(data2);
        baseDataList.add(data3);
        baseDataList.add(data4);
        baseDataList.add(data1);
        baseDataList.add(data2);
        baseDataList.add(data3);
        baseDataList.add(data4);
    }


}