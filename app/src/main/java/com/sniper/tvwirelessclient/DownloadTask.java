package com.sniper.tvwirelessclient;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhaohongru on 2017-10-24.
 * PackageName: com.sniper.tvwirelessclient.DownloadTask
 * Description：
 */
public class DownloadTask implements Runnable {

    int index;
    public static final int FAILED = 1;
    public static final int SUCCESS = 2;
    public static final int PASUED = 3;
    public static final int DOWNLONDING = 4;
    DownloadListener downloadListener;
    int pieces =10;
    String downloadUrl;

    public DownloadTask(DownloadListener downloadListener,int index,String downloadUrl) {
        super();
        this.downloadListener = downloadListener;
        this.index = index;
        this.downloadUrl = downloadUrl;

    }
//
//    @Override
//    protected Integer doInBackground(String... params) {
//
//
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected void onPostExecute(Integer integer) {
//        super.onPostExecute(integer);
//    }
//
//    @Override
//    protected void onProgressUpdate(Integer... values) {
//        super.onProgressUpdate(values);
//        downloadListener.onProgress(values[0]);
//
//    }
//取网络上文件大小
    public long getContentLength(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response!=null&&response.isSuccessful()){
                long length = response.body().contentLength();
                response.close();
                return length;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  0;
    }


    @Override
    public void run() {
        InputStream inputStream = null;
        long downloadlength = 0;
        RandomAccessFile rfile = null;

  //      String downloadUrl = params[0];

        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
        if(fileName.indexOf("?")!=-1) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }
        String directory = Environment.getExternalStorageDirectory()+"/";

        File file = new File(directory+fileName);
        if(file.exists()){
            downloadlength = file.length();
        //    file.delete();
        }
        Log.e("test", "DownloadTask: "+index );
        long contentLength = getContentLength(downloadUrl);
        Log.e("test", "DownloadTask1: "+index );
        if(contentLength==0){
            return ;
        }else if (downloadlength==contentLength){
            return ;
        }


        Log.e("test", "doInBackground: "+contentLength );

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = null;
//        switch (index){
//            case 0:
//               request = new Request.Builder()
//                        .url(downloadUrl)
//                        .header("RANGE","bytes="+0*(int)(contentLength/5)+"-"+(0*(int)(contentLength/5)+(int)(contentLength/5)-1))
//                        .build();
//                downloadlength = 0*(int)(contentLength/5);
//                break;
//            case 1:
//                request = new Request.Builder()
//                        .url(downloadUrl)
//                        .header("RANGE","bytes="+1*(int)(contentLength/5)+"-"+(1*(int)(contentLength/5)+(int)(contentLength/5)-1))
//                        .build();
//                downloadlength = 1*(int)(contentLength/5)-1;
//                break;
//            case 2:
//                request = new Request.Builder()
//                        .url(downloadUrl)
//                        .header("RANGE","bytes="+2*(int)(contentLength/5)+"-"+(2*(int)(contentLength/5)+(int)(contentLength/5)-1))
//                        .build();
//                downloadlength = 2*(int)(contentLength/5)-1;
//                break;
//            case 3:
//                request = new Request.Builder()
//                        .url(downloadUrl)
//                        .header("RANGE","bytes="+3*(int)(contentLength/5)+"-"+(3*(int)(contentLength/5)+(int)(contentLength/5)-1))
//                        .build();
//                downloadlength = 3*(int)(contentLength/5)-1;
//                break;
//            case 4:
//                request = new Request.Builder()
//                        .url(downloadUrl)
//                        .header("RANGE","bytes="+(4*(int)(contentLength/5))+"-"+contentLength)
//                        .build();
//                downloadlength = 4*(int)(contentLength/5)-1;
//                break;
//
//        }
//        Request request = new Request.Builder()
//                .url(downloadUrl)
//                .header("RANGE","bytes="+downloadlength+0*(contentLength/5)+"-"+)
//                .build();

        if(index<(pieces-1)){
            request = new Request.Builder()
                    .url(downloadUrl)
                    .header("RANGE","bytes="+index*(int)(contentLength/pieces)+"-"+(index*(int)(contentLength/pieces)+(int)(contentLength/pieces)-1))
                    .build();

        }else if(index==(pieces-1)){
            request = new Request.Builder()
                    .url(downloadUrl)
                    .header("RANGE","bytes="+(index*(int)(contentLength/pieces))+"-"+contentLength)
                    .build();
        }
        downloadlength = (index*(int)(contentLength/pieces));

        Log.e("test", "doInBackground: "+request.headers().get("RANGE") );
        Log.e("test", "doInBackground: "+downloadlength );
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response!=null&&response.isSuccessful()){
                try {
                    inputStream = response.body().byteStream();
                    rfile = new RandomAccessFile(file,"rw");
                    rfile.seek(downloadlength);
                    byte[] b = new byte[1024];
                    int len;
                    int total = 0;
                    while ((len=inputStream.read(b))!=-1){
                        total += len;
                        rfile.write(b,0,len);
                        int progress = (int)(file.length()*100/contentLength);
                      //  publishProgress(progress);
                        downloadListener.onProgress(progress);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
