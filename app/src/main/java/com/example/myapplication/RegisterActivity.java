package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {
    EditText userName,passWord;
    Button registerBtn;
    TextView loginText;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SetUI();
    }

    void SetUI()
    {
        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        registerBtn = (Button) findViewById(R.id.register);
        loginText = (TextView) findViewById(R.id.change_to_login);
        alertDialog = AlertDialogCreator.MakeDialog(this);

        getSupportActionBar().hide();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (NetWorkTask.isNetConnection(RegisterActivity.this))
                {
                    RegisterTask registerTask = new RegisterTask();
                    registerTask.execute();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                finish();
            }
        });
    }

    class RegisterTask extends AsyncTask<Void, Integer, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean isSucceess = false;
            String name = userName.getText().toString().trim();
            String password = passWord.getText().toString().trim();

            isSucceess = NetWorkTask.Register(name, password);
            return isSucceess;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            alertDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
        }
    }

}