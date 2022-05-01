package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.bean.Quest;
import com.example.myapplication.bean.SexResearch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        DownloadTask task = new DownloadTask();
        task.execute();
    }

    class DownloadTask extends AsyncTask<Void, Integer, Void>
    {
        OkHttpClient client;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            client = new OkHttpClient();
        }

        //传入WIFI的IP地址
        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String jsonStr = response.body().string();
                Log.d("Get Response Suceess!", "str:" + jsonStr);
                return jsonStr;
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            TestQuest();
            return null;
        }

        void TestQuestWithUserId(int userid)
        {
            Request request = new Request.Builder()
                    .url("http://192.168.1.104:8080/App/Quest")
                    .addHeader("User", "1")
                    .build();
            try {
                Response response = client.newCall(request).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void TestQuest()
        {
            try{
                String data = run("http://192.168.1.104:8080/App/Quest");
                Gson gson = new Gson();
                List<Quest> list = null;
                list = gson.fromJson(data, new TypeToken<List<Quest>>(){}.getType());
                if (!list.isEmpty())
                {
                    for(int i=0; i<list.size(); ++i)
                    {
                        Log.d("Element" + i, list.get(i).getTitle());
                        Log.d("Element" + i, list.get(i).getDescription());
                        Log.d("Element" + i, " " + list.get(i).getReward());
                        Log.d("Element" + i, " " + list.get(i).getQuestId());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        void TestSexresearch()
        {
            try {
                String data = run("http://192.168.1.104:8080/App/GetDates/SexResearch");
                Log.d("GetData", "doInBackground: " + data);
                Gson gson = new Gson();
                List<SexResearch> sexResearchList = null;
                sexResearchList = gson.fromJson(data, new TypeToken<List<SexResearch>>(){}.getType());
                for(int i=0; i<sexResearchList.size(); ++i)
                    Log.d("Element", "onCreate: " + sexResearchList.get(i).getSex());
                //Log.d("GetData", "onCreate: SexResearch" + data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        void TestHeader()
        {
            Request request = new Request.Builder()
                    .url("http://192.168.1.104:8080/App/Test")
                    .addHeader("MyHeader", "TTTTTT")
                    .build();

            try{
                Response response = client.newCall(request).execute();
                Log.d("Test Header", "doInBackground: " + response.header("ServiceHeader"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}