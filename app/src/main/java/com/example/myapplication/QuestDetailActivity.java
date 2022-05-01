package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.bean.Quest;

public class QuestDetailActivity extends AppCompatActivity implements View.OnClickListener{
    Quest quest;
    int userid;
    TextView price,title,description;
    Button accept,quit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_detail);
        quest = (Quest) getIntent().getSerializableExtra("quest");
        userid = getIntent().getIntExtra("userid", 0);
        SetUI();
    }

    void SetUI()
    {
        accept = (Button) findViewById(R.id.accept);
        quit = (Button) findViewById(R.id.quit);
        price = (TextView) findViewById(R.id.price);
        title = (TextView)findViewById(R.id.title);
        description = (TextView)findViewById(R.id.description);

        accept.setOnClickListener(this);
        quit.setOnClickListener(this);
        price.setText(Integer.toString(quest.getReward()));
        title.setText(quest.getTitle());
        description.setText(quest.getDescription());

        getSupportActionBar().setTitle("任务详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.accept)
        {
            Intent intent = new Intent(QuestDetailActivity.this, SubQuestionActivity.class);
            intent.putExtra("quest", quest);
            intent.putExtra("userid", userid);
            startActivity(intent);
        }
        else
        {
            finish();
        }
    }
}