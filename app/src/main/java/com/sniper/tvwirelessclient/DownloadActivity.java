package com.sniper.tvwirelessclient;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TEst";
    protected Button button2;
    protected TextView textView3;
    BroadcastReceiver broadcastReceiver;
    private String downloadUrl ="http://v4.music.126.net/20171025145622/e535c80fe37ff42143f766b9edcbda7a/cloudmusic/JjIzITAiICQgITQwISAgIA==/mv/231123/482685604627067_852x480.mp4";
    DownloadService.DownloadBinder downloadBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_download);
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "onReceive: ====" );
                textView3.setText("进度："+ intent.getIntExtra("progress",0));
            }
        };
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.download.progress");
        registerReceiver(broadcastReceiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        unbindService(serviceConnection);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button2) {
            downloadBinder.startDownload(downloadUrl);
        }
    }

    private void initView() {
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(DownloadActivity.this);
        textView3 = (TextView) findViewById(R.id.textView3);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;

            textView3.setText("进度："+ downloadBinder.getProgress());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };




}
