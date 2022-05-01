package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.bean.EpidemicInfoSearch;
import com.example.myapplication.bean.NoiseSearch;
import com.example.myapplication.bean.Quest;
import com.example.myapplication.bean.SleepQualitySearch;
import com.example.myapplication.bean.SubQuestion;
import com.example.myapplication.bean.UserInfoSearch;
import com.example.myapplication.chart.AnotherBarActivity;
import com.example.myapplication.chart.LineChartActivity;
import com.example.myapplication.chart.PieChartActivity;
import com.example.myapplication.chart.ScatterChartActivity;
import com.example.myapplication.helper.AlertDialogCreator;
import com.example.myapplication.helper.NetWorkTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ChartTypeActivity extends AppCompatActivity {
    ListView listView;
    Quest quest;
    SubQuestion selectedQuestion;

    List<EpidemicInfoSearch> epidemicSearchLists = null;
    List<NoiseSearch> noiseSearchLists = null;
    List<UserInfoSearch> userInfoSearchList = null;
    List<SleepQualitySearch> sleepQualitySearchList = null;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_type);

        Intent intent = getIntent();
        quest = (Quest) intent.getSerializableExtra("quest");
        selectedQuestion = (SubQuestion) intent.getSerializableExtra("question");
        SetUI();

        if (NetWorkTask.isNetConnection(this))
        {
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute();
        }
        else
            Toast.makeText(this, "网络连接错误", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void SetUI()
    {
        TextView title = (TextView) findViewById(R.id.title);
        TextView subquestion = (TextView) findViewById(R.id.question);

        title.setText(quest.getTitle());
        subquestion.setText(selectedQuestion.getDescription());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("选择图表");
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listview);
        String chartType[] = {"折线图","散点图","扇形图","柱状图"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChartTypeActivity.this, android.R.layout.simple_list_item_1, chartType);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (NetWorkTask.isNetConnection(ChartTypeActivity.this))
                {
                    Intent intent = null;
                    switch (i)
                    {
                        case 0:
                            intent = new Intent(ChartTypeActivity.this, LineChartActivity.class);
                            break;
                        case 1:
                            intent = new Intent(ChartTypeActivity.this, ScatterChartActivity.class);
                            break;
                        case 2:
                            intent = new Intent(ChartTypeActivity.this, PieChartActivity.class);
                            break;
                        case 3:
                            intent = new Intent(ChartTypeActivity.this, AnotherBarActivity.class);
                            break;
                        default:
                            break;
                    }
                    ArrayList<Integer> datas = FilterData(selectedQuestion.getQuestionId());
                    intent.putIntegerArrayListExtra("datas", datas);
                    intent.putExtra("question", selectedQuestion);
                    startActivity(intent);
                }
                else
                    Toast.makeText(ChartTypeActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ArrayList<Integer> FilterData(int questionId)
    {
        ArrayList<Integer> datas = new ArrayList<>();

        switch (questionId)
        {
            case 1:
            {
                for (UserInfoSearch data: userInfoSearchList) {
                    datas.add(data.getSex());
                }
                break;
            }
            case 2:
            {
                for (UserInfoSearch data: userInfoSearchList) {
                    datas.add(data.getAge());
                }
                break;
            }
            case 3:
            {
                for (EpidemicInfoSearch data: epidemicSearchLists) {
                    datas.add(data.getInRiskArea());
                }
                break;
            }
            case 4:
            {
                for (EpidemicInfoSearch data: epidemicSearchLists) {
                    datas.add(data.getTemperature());
                }
                break;
            }
            case 5:
            {
                for (EpidemicInfoSearch data: epidemicSearchLists) {
                    datas.add(data.getIsFever());
                }
                break;
            }
            case 6:
            {
                for (EpidemicInfoSearch data: epidemicSearchLists) {
                    datas.add(data.getIsContact());
                }
                break;
            }
            case 7:
            {
                for (SleepQualitySearch data: sleepQualitySearchList) {
                    datas.add(data.getSleepEnough());
                }
                break;
            }
            case 8:
            {
                for (SleepQualitySearch data: sleepQualitySearchList) {
                    datas.add(data.getSleepTime());
                }
                break;
            }
            case 9:
            {
                for (SleepQualitySearch data: sleepQualitySearchList) {
                    datas.add(data.getSleepDifficulty());
                }
                break;
            }
            case 10:
            {
                for (NoiseSearch data: noiseSearchLists) {
                    datas.add(data.getNoiseDb());
                }
                break;
            }
        }

        return datas;
    }

    class DownloadTask extends AsyncTask<Void, Integer, Void>
    {
        AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = AlertDialogCreator.MakeDialog(ChartTypeActivity.this);
            alertDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            String jsonStr = NetWorkTask.GetData(quest.getQuestId());
            NetWorkTask.log("JsonStr:", jsonStr);
            Gson gson = new Gson();
            switch (quest.getQuestId())
            {
                case 1:
                {
                    userInfoSearchList = gson.fromJson(jsonStr, new TypeToken<List<UserInfoSearch>>(){}.getType());
                    NetWorkTask.log("Type:", "userinfo " + userInfoSearchList.size());
                    break;
                }
                case 2:
                {
                    epidemicSearchLists = gson.fromJson(jsonStr, new TypeToken<List<EpidemicInfoSearch>>(){}.getType());
                    NetWorkTask.log("Type:", "epidemic " + epidemicSearchLists.size());
                    break;
                }
                case 3:
                {
                    sleepQualitySearchList = gson.fromJson(jsonStr, new TypeToken<List<SleepQualitySearch>>(){}.getType());
                    NetWorkTask.log("Type:", "sleep " + sleepQualitySearchList.size());
                    break;
                }
                case 4:
                {
                    noiseSearchLists = gson.fromJson(jsonStr, new TypeToken<List<NoiseSearch>>(){}.getType());
                    NetWorkTask.log("Type:", "noise " + noiseSearchLists.size());
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            alertDialog.dismiss();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            alertDialog.dismiss();

        }
    }

}