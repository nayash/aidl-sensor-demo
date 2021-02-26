package com.outliers.aidlserversdk.aidls;

import com.outliers.aidlserversdk.aidls.ISensorServerCallback;

interface ISensorServer {
    void setCallback(ISensorServerCallback callback, String uuid);
    float[] getRotationVec();
    void removeCallback(ISensorServerCallback callback, String id);
}