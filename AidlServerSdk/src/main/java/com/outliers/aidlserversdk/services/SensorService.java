package com.outliers.aidlserversdk.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.outliers.aidlserversdk.aidls.ISensorServer;
import com.outliers.aidlserversdk.aidls.ISensorServerCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class SensorService extends Service {

    Sensor rotationSensor;
    SensorListener sensorListener;
    private float[] sensorReadings;
    HashMap<String, ISensorServerCallback> clientCallbacks;

    @Override
    public void onCreate() {
        super.onCreate();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if(rotationSensor == null){
            Toast.makeText(this, "sensor not available", Toast.LENGTH_LONG).show();
        }
        sensorListener = new SensorListener();
        sensorManager.registerListener(sensorListener, rotationSensor,
                8000);
        clientCallbacks = new HashMap<>();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Nullable
    public float[] getRotationVec(){
        return sensorReadings;
    }

    private final ISensorServer.Stub binder = new ISensorServer.Stub() {
        @Override
        public void setCallback(ISensorServerCallback callback, String uuid){
            clientCallbacks.put(uuid, callback);
            Log.v("setCallback on id:", uuid+", keys="+clientCallbacks.keySet());
        }

        @Override
        public float[] getRotationVec(){
            return getRotationVec();
        }

        @Override
        public void removeCallback(ISensorServerCallback callback, String uuid) throws RemoteException {
            clientCallbacks.remove(uuid);
            Log.v("removeCallback", "reached service: size="+clientCallbacks.size()+", called on id:"+uuid+", keys="+clientCallbacks.keySet());
        }
    };

    public class SensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor == rotationSensor){
                sensorReadings = event.values;
                try {
                    for(ISensorServerCallback callback : clientCallbacks.values())
                        callback.onSensorReadingReceived(sensorReadings);
                        //Log.v("sending", sensorReadings[0]+","+sensorReadings[1]);
                } catch (RemoteException e) {
                    Log.e("serverCallbackNotif", Log.getStackTraceString(e));
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

}
