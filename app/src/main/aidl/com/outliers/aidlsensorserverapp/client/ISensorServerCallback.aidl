package com.outliers.aidlsensorserverapp.client;

interface ISensorServerCallback {
    void onSensorReadingReceived(out float[] sensorReadings);
}