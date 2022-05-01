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
import com.example.myapplication.helper.AlertDialogCreator;
import com.example.myapplication.helper.NetWorkTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChartDetailActivity extends AppCompatActivity {
    List<SubQuestion> questionList = null;
    List<String> questionsDec = new ArrayList<String>();
    ListView listView;
    Quest quest;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_detail);
        quest = (Quest)getIntent().getSerializableExtra("quest");
        SetUI();

        if (NetWorkTask.isNetConnection(this))
        {
            DownloadTask task = new DownloadTask();
            task.execute();
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
        TextView description = (TextView) findViewById(R.id.description);

        title.setText(quest.getTitle());
        description.setText(quest.getDescription());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("选择要展示的数据");
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChartDetailActivity.this, ChartTypeActivity.class);
                intent.putExtra("quest", quest);
                intent.putExtra("question", questionList.get(i));

                startActivity(intent);
            }
        });
    }


    class DownloadTask extends AsyncTask<Void, Integer, Void>
    {
        AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = AlertDialogCreator.MakeDialog(ChartDetailActivity.this);
            alertDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            questionList = NetWorkTask.GetSubQuestion(quest.getQuestId());
            questionsDec.clear();

            questionList.sort(new Comparator<SubQuestion>() {
                @Override
                public int compare(SubQuestion t1, SubQuestion t2) {
                    return t1.getQuestionId() - t2.getQuestionId();
                }
            });
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

            for (SubQuestion subquestion: questionList) {
                questionsDec.add(subquestion.getDescription());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChartDetailActivity.this, android.R.layout.simple_list_item_1, questionsDec);
            listView.setAdapter(adapter);
        }
    }

}