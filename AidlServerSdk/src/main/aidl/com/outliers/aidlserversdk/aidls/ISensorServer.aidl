package com.outliers.aidlserversdk.aidls;

import com.outliers.aidlserversdk.aidls.ISensorServerCallback;

interface ISensorServer {
    void setCallback(ISensorServerCallback callback);
    float[] getRotationVec();
}