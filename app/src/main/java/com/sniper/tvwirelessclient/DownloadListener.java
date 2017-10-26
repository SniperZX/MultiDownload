package com.sniper.tvwirelessclient;

/**
 * Created by zhaohongru on 2017-10-24.
 * PackageName: com.sniper.tvwirelessclient.DownloadListener
 * Description：
 */
public interface DownloadListener {
    public void onSuccess();
    public void onFailed();
    public void onProgress(int progress);



}
