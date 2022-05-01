package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.helper.AlertDialogCreator;
import com.example.myapplication.helper.NetWorkTask;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText userName,passWord;
    Button login;
    TextView registerText;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        SetUI();
    }

    void SetUI()
    {
        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        registerText = findViewById(R.id.register);
        alertDialog = AlertDialogCreator.MakeDialog(this);

        getSupportActionBar().hide();
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (NetWorkTask.isNetConnection(LoginActivity.this))
                {
                    LoginTask loginTask = new LoginTask();
                    loginTask.execute();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    class LoginTask extends AsyncTask<Void, Integer, Boolean>
    {
        int userid = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String name = userName.getText().toString().trim();
            String password = passWord.getText().toString().trim();

            HashMap<String, Integer> hashMap = NetWorkTask.LogOn(name, password);
            userid = hashMap.get("userid");
            return hashMap.get("logon") == 1;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            alertDialog.dismiss();
            if(!aBoolean)
                Toast.makeText(LoginActivity.this, "login Fail!", Toast.LENGTH_SHORT).show();
            else
            {
                Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
                finish();
            }
        }
    }
}