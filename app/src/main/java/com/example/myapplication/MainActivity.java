package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String CHANNEL_ID = "channelId";
    private IntentFilter intentFilter;
    private NetWorkChangeReceiver netWorkChangeReceiver;
    private EditText bookName,authorName,prices;

    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        Intent intentTest = getIntent();
        String userName = intentTest.getStringExtra("UserName");
        Log.d("GetUserName", "UserName=" + userName);
        Button btn = (Button) findViewById(R.id.button);
        bookName = (EditText)findViewById(R.id.bookName);
        authorName = (EditText)findViewById(R.id.author);
        prices = (EditText)findViewById(R.id.price);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setContentTitle("Title")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText("Content")
                        .setWhen(System.currentTimeMillis())
                        .setPriority(NotificationCompat.PRIORITY_MAX);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(1, builder.build());


                Log.d("Mytag", "onClick: Send Notification!");
            }
        });

        Button btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = LitePal.getDatabase();
                Book book = new Book();
                if(bookName.getText().toString().isEmpty() || authorName.getText().toString().isEmpty() || prices.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, "Values must is`t empty!", Toast.LENGTH_SHORT).show();
                else
                {
                    book.setName(bookName.getText().toString());
                    book.setAuthor(authorName.getText().toString());
                    book.setPrice(Float.parseFloat(prices.getText().toString()));
                    book.save();
                    bookName.setText("");
                    authorName.setText("");
                    prices.setText("");
                    Toast.makeText(MainActivity.this, "Data Insert Success!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnSelect = (Button)findViewById(R.id.buttonSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Book> list = LitePal.findAll(Book.class);
                for (int i=0; i<list.size(); ++i)
                {
                    Book book = list.get(i);
                    Log.d("Show Data:", "Id:" + book.getId());
                    Log.d("Show Data:", "BookName:" + book.getName());
                    Log.d("Show Data:", "Author:" + book.getAuthor());
                    Log.d("Show Data:", "Prices:" + book.getPrice());
                }
            }
        });

        //Dowload Task
        Button btnStartDownload = findViewById(R.id.buttonStartDownload);
        Button btnPauseDownload = findViewById(R.id.buttonPauseDownload);
        Button btnCancelDownload = findViewById(R.id.buttonCancelDownload);
        btnStartDownload.setOnClickListener(this);
        btnPauseDownload.setOnClickListener(this);
        btnCancelDownload.setOnClickListener(this);
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent); // 启动服务
        bindService(intent, connection, BIND_AUTO_CREATE); // 绑定服务
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        }

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Title");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Download Bind
        unbindService(connection);
//        unregisterReceiver(netWorkChangeReceiver);
    }

    private void SetNetWorkService()
    {
        netWorkChangeReceiver = new NetWorkChangeReceiver();
        intentFilter = new IntentFilter("android.net.com.CONNECTIVITY_CHANGE");
        registerReceiver(netWorkChangeReceiver, intentFilter);
        Log.d("SetNetWorkService!","SSSSSSSSSS");
    }

    @Override
    public void onClick(View v) {
        if (downloadBinder == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.buttonStartDownload:
                String url = "http://img0.dili360.com/pic/2022/02/23/6215a378f351b2h18242055_t.jpg";
                Log.d("StartDownload", "onClick: Start");
                downloadBinder.startDownload(url);
                break;
            case R.id.buttonPauseDownload:
                downloadBinder.pauseDownload();
                break;
            case R.id.buttonCancelDownload:
                downloadBinder.cancelDownload();
                break;
            default:
                break;
        }
    }

    class NetWorkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Network State Change!", Toast.LENGTH_LONG).show();
        }
    }
}