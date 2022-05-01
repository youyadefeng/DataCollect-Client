package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainPageActivity extends AppCompatActivity {
    private int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //get userinfo
        Intent intent = getIntent();
        userid = intent.getIntExtra("userid", 0);

        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("任务广场");

        //bottomnavigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.quest:
                        actionBar.show();
                        actionBar.setTitle("任务广场");
                        ReplaceFragment(new FragmentQuest());
                        break;
                    case R.id.purse:
                        actionBar.show();
                        actionBar.setTitle("购买数据");
                        ReplaceFragment(new FragmentPurse());
                        break;
                    case R.id.chart:
                        ReplaceFragment(new FragmentChart());
                        actionBar.show();
                        actionBar.setTitle("查看数据");
                        break;
                    case R.id.setting:
                        ReplaceFragment(new FragmentSetting());
                        actionBar.setTitle("账号管理");
                        //actionBar.hide();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        ReplaceFragment(new FragmentQuest());
    }

    void ReplaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    public int GetUserId()
    {
        return userid;
    }
}