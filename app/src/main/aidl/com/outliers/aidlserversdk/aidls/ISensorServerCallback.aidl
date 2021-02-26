package com.outliers.aidlserversdk.aidls;

interface ISensorServerCallback {
    void onSensorReadingReceived(out float[] sensorReadings);
}