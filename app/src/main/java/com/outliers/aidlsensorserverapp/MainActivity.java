package com.outliers.aidlsensorserverapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.outliers.aidlserversdk.aidls.ISensorServer;
import com.outliers.aidlserversdk.aidls.ISensorServerCallback;

import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity implements ServiceConnection {

    ISensorServer sensorService;
    TextView tvReadings;
    Intent sensorIntent;
    InternalHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvReadings = findViewById(R.id.tv_sensor_readings);

        handler = new InternalHandler(tvReadings);
        sensorIntent = new Intent("android.intent.action.SENSOR_SERVICE");
        sensorIntent.setPackage("com.outliers.aidlsensorserverapp");
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(sensorIntent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        sensorService = ISensorServer.Stub.asInterface(service);
        try {
            sensorService.setCallback(callback);
            Log.e("MainActivity", "setcallback called: "+this);
        } catch (RemoteException e) {
            Log.e("clientCallbackSet", e.getMessage());
        }
        Log.v("onServiceConnected", name.getClassName());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // sensorService.unregisterCallback(); // TODO need to add this method to aidl and use here
    }

    /*public void onSensorReadingReceived(float[] sensorReadings) {
        Message msg = new Message();
        msg.what = 1;
        msg.obj = sensorReadings;
        handler.sendMessage(msg);
    }*/

    private static class InternalHandler extends Handler {
        private final WeakReference<TextView> weakTextView;

        InternalHandler(TextView textView) {
            weakTextView = new WeakReference<>(textView);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    TextView textView = weakTextView.get();
                    float[] sensorReadings = (float[]) msg.obj;
                    if (textView != null) {
                        textView.setText(String.format("x=%.3f, y=%.3f, z=%.3f",
                                sensorReadings[0], sensorReadings[1], sensorReadings[2]));
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    ISensorServerCallback callback = new ISensorServerCallback.Stub(){
        @Override
        public void onSensorReadingReceived(float[] sensorReadings) throws RemoteException {
            //Log.v("received", sensorReadings[0]+","+sensorReadings[1]);
            Message msg = new Message();
            msg.what = 1;
            msg.obj = sensorReadings;
            handler.sendMessage(msg);
        }
    };
}