package com.outliers.aidlsensorserverapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.outliers.aidlsensorserverapp.client.ISensorServer;
import com.outliers.aidlsensorserverapp.client.ISensorServerCallback;

public class MainActivity extends AppCompatActivity implements ServiceConnection, ISensorServerCallback {

    ISensorServer sensorService;
    TextView tvReadings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvReadings = findViewById(R.id.tv_sensor_readings);


        Intent intent = new Intent("android.intent.action.SENSOR_SERVICE");
        intent.setPackage("com.outliers.aidlsensorserverapp");
        startService(intent);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        sensorService = ISensorServer.Stub.asInterface(service);

        try {
            sensorService.setCallback(this);
        } catch (RemoteException e) {
            Log.e("clientCallbackSet", e.getMessage());
        }
        Log.v("onServiceConnected", name.getClassName());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public IBinder asBinder() {
        return null;
    }

    @Override
    public void onSensorReadingReceived(float[] sensorReadings) throws RemoteException {
        tvReadings.setText(String.format("x={}, y={}, z={}", sensorReadings));
    }
}