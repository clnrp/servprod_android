package br.com.servprod;

/**
 * Created by cleoner on 01/10/17.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

public class AuxService extends Service {

    private static final String TAG = "ServProd Service";
    private Integer state;
    private boolean thread = true;
    private MainThread mainThread = null;

    public AuxService(Integer state) {
        this.state = state;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        //GPSReader();
        String imei = SharedData.getImei();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mainThread == null) {
            mainThread = new MainThread();
            mainThread.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    class MainThread extends Thread {
        final int TIME = 10000;

        public MainThread() {
        }

        public void run() {
            while (thread) {
                try {

                    Thread.sleep(TIME);
                } catch (Exception e) {
                    Log.e(TAG, "Exception:", e);
                }
            }
        }
    }
}
