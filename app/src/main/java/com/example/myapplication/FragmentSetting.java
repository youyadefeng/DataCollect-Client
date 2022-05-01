package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.helper.AlertDialogCreator;
import com.example.myapplication.helper.NetWorkTask;

import java.util.HashMap;


public class FragmentSetting extends Fragment implements View.OnClickListener{
    TextView userName, scoreText;
    int userid = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        SetClickListener(view);

        MainPageActivity mainPageActivity = (MainPageActivity) getActivity();
        userid = mainPageActivity.GetUserId();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();

        if(NetWorkTask.isNetConnection(getActivity()))
        {
            DownloadTask task = new DownloadTask();
            task.execute();
        }
        else
            Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();

    }

    void SetClickListener(View view)
    {
        //点击时设置阴影
        ConstraintLayout score = (ConstraintLayout) view.findViewById(R.id.price);
        ConstraintLayout signin = (ConstraintLayout) view.findViewById(R.id.signin);
        ConstraintLayout userinfo = (ConstraintLayout)view.findViewById(R.id.userinfo);
        ConstraintLayout about = (ConstraintLayout)view.findViewById(R.id.about);
        ImageView headIcon = (ImageView) view.findViewById(R.id.head_icon);
        userName = (TextView) view.findViewById(R.id.username);
        scoreText = (TextView) view.findViewById(R.id.score_text);

        score.setOnClickListener(this);
        signin.setOnClickListener(this);
        userinfo.setOnClickListener(this);
        about.setOnClickListener(this);
        headIcon.setOnClickListener(this);
        userName.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.price:
            {
                Toast.makeText(getActivity(), "当前积分为:" + scoreText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.signin:
            {
                if(NetWorkTask.isNetConnection(getActivity()))
                {
                    SignInTask task = new SignInTask();
                    task.execute();
                }
                else
                    Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.about:
            {
                Toast.makeText(getActivity(), "毕设项目app", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.github:
            {

                if(NetWorkTask.isNetConnection(getActivity()))
                {
                    //Intent i = new Intent(Intent.ACTION_VIEW);
                    //i.setData(Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/LineChartActivity1.java"));
                    //startActivity(i);
                }
                else
                    Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
                break;
            }

            //three item in one logic path
            case R.id.userinfo:
            case R.id.username:
            case R.id.head_icon:
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);

                intent.putExtra("username", userName.getText().toString());
                intent.putExtra("userid", userid);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    class DownloadTask extends AsyncTask<Void, Integer, Void>
    {
        AlertDialog alertDialog;
        String score, name;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = AlertDialogCreator.MakeDialog(getActivity());
            alertDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, String> hashMap = NetWorkTask.GetUserInfo(userid);

            score = hashMap.get("score");
            name = hashMap.get("name");
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

            userName.setText(name);
            scoreText.setText(score);
        }
    }


    class SignInTask extends AsyncTask<Void, Integer, Void>
    {
        AlertDialog alertDialog;
        int isSuccessful, nowScore;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = AlertDialogCreator.MakeDialog(getActivity());
            alertDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            isSuccessful = NetWorkTask.SignIn(userid);
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

            if(isSuccessful == 1)
            {
                DownloadTask task = new DownloadTask();
                task.execute();
                Toast.makeText(getActivity(), "签到成功！", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getActivity(), "签到失败：今日已签到", Toast.LENGTH_SHORT).show();
            }
        }
    }

}