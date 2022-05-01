package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.bean.Quest;
import com.example.myapplication.bean.SubQuestion;
import com.example.myapplication.helper.AlertDialogCreator;
import com.example.myapplication.helper.NetWorkTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurseDetailActivity extends AppCompatActivity implements View.OnClickListener {
    List<SubQuestion> subQuestionList = new ArrayList<SubQuestion>();
    List<String> questionsDec = new ArrayList<String>();
    ListView listView;
    Quest quest;
    int userid;

    Button accept;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse_detail);
        //get questid from intent
        quest = (Quest) getIntent().getSerializableExtra("quest");
        userid = getIntent().getIntExtra("userid", 0);
        SetUI();

        if (NetWorkTask.isNetConnection(PurseDetailActivity.this))
        {
            DownloadTask task = new DownloadTask();
            task.execute();
        }
        else
            Toast.makeText(this, "网络连接错误", Toast.LENGTH_SHORT).show();

    }

    void SetUI()
    {
        TextView dataNumber = (TextView) findViewById(R.id.data_number);
        TextView price = (TextView) findViewById(R.id.price);
        TextView title = (TextView) findViewById(R.id.title);
        TextView description = (TextView) findViewById(R.id.description);
        accept = (Button) findViewById(R.id.accept);
        Button quit = (Button) findViewById(R.id.quit);
        listView = (ListView) findViewById(R.id.listview);

        dataNumber.setText(Integer.toString(quest.getDataNumber()));
        price.setText(Integer.toString(quest.getPrice()));
        title.setText(quest.getTitle());
        description.setText(quest.getDescription());
        accept.setOnClickListener(this);
        quit.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("数据详情");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.accept)
        {
            if (NetWorkTask.isNetConnection(this))
            {
                //执行购买逻辑
                BuyTask buyTask = new BuyTask();
                buyTask.execute();
            }
            else
                Toast.makeText(this, "网络连接错误", Toast.LENGTH_SHORT).show();
        }
        else
        {
            finish();
        }
    }


    class DownloadTask extends AsyncTask<Void, Integer, Void>
    {
        AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = AlertDialogCreator.MakeDialog(PurseDetailActivity.this);
            alertDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            subQuestionList = NetWorkTask.GetSubQuestion(quest.getQuestId());
            questionsDec.clear();
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            alertDialog.dismiss();

            for (SubQuestion subquestion:subQuestionList) {
                questionsDec.add(subquestion.getDescription());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PurseDetailActivity.this, android.R.layout.simple_list_item_1, questionsDec);
            listView.setAdapter(adapter);
        }
    }


    class BuyTask extends AsyncTask<Void, Integer, Void>
    {
        AlertDialog alertDialog;
        boolean isScoreEnough;
        int scoreUserHave;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = AlertDialogCreator.MakeDialog(PurseDetailActivity.this);
            alertDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, String> userInfo = NetWorkTask.GetUserInfo(userid);
            scoreUserHave = Integer.parseInt(userInfo.get("score"));
            if(scoreUserHave >= quest.getPrice())
            {
                isScoreEnough = true;
                NetWorkTask.BuyQuest(userid, quest.getQuestId());
            }
            else
            {
                isScoreEnough = false;
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            alertDialog.dismiss();
            if (isScoreEnough)
            {
                Toast.makeText(PurseDetailActivity.this, "购买成功，购买后剩余积分为：" + (scoreUserHave - quest.getPrice())
                ,Toast.LENGTH_SHORT).show();
                accept.setText("已购买");
                accept.setVisibility(View.INVISIBLE);
            }
            else
            {
                Toast.makeText(PurseDetailActivity.this, "积分不足，当前积分为" + scoreUserHave + " 购买所需积分为" + quest.getPrice()
                ,Toast.LENGTH_SHORT).show();
            }

        }
    }


}