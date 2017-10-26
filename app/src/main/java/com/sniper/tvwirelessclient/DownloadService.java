package com.sniper.tvwirelessclient;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadService extends Service implements DownloadListener{
    public DownloadBinder downloadBinder = new DownloadBinder();
    private int progress;



    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
       return downloadBinder;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onProgress(int progress) {
        Log.e("tst", "onProgress: "+progress);
        this.progress = progress;
        Intent intent = new Intent();
        intent.putExtra("progress",progress);
        intent.setAction("com.download.progress");
        sendBroadcast(intent);

    }

    class DownloadBinder extends Binder{

        public float getProgress(){

            return progress;
        }

        public void startDownload(String url){
            ExecutorService executor = Executors.newFixedThreadPool(5);

            for (int i = 0; i < 10; i++) {
                executor.execute(new DownloadTask(DownloadService.this,i,url));
            }
        }

    }


}
