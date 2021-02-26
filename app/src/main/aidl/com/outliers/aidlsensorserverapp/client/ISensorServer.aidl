package com.outliers.aidlsensorserverapp.client;

import com.outliers.aidlsensorserverapp.client.ISensorServerCallback;

interface ISensorServer {
    void setCallback(ISensorServerCallback callback);
    float[] getRotationVec();
}