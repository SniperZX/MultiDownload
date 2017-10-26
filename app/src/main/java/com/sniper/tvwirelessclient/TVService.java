package com.sniper.tvwirelessclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TVService extends Service {
    public TVService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
