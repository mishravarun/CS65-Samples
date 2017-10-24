package com.varunmishra.foregroundservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

/**
 * Created by varun on 10/24/17.
 */

public class WakeLockService extends Service {
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;

    @Override
    public void onCreate() {
        super.onCreate();

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeLockService");
        wakeLock.acquire();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(wakeLock != null){
            wakeLock.release();
        }
//        wakeLock = null;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
