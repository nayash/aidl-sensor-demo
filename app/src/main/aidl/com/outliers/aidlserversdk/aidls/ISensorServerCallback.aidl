package com.outliers.aidlserversdk.aidls;

interface ISensorServerCallback {
    void onSensorReadingReceived(in float[] sensorReadings);
}