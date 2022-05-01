package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        SetUI();

    }

    void SetUI()
    {
        Intent intent = getIntent();
        String userName = intent.getStringExtra("username");
        int userId = intent.getIntExtra("userid", 0);

        TextView name = (TextView) findViewById(R.id.username);
        TextView id = (TextView) findViewById(R.id.userid);
        ActionBar actionBar = getSupportActionBar();

        name.setText(userName);
        id.setText(Integer.toString(userId));
        actionBar.setTitle("个人资料");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}