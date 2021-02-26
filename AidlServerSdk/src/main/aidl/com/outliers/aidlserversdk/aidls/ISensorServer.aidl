package com.outliers.aidlserversdk.aidls;

import com.outliers.aidlserversdk.aidls.ISensorServerCallback;

interface ISensorServer {
    void setCallback(ISensorServerCallback callback, String uuid);
    float[] getRotationVec();  // can be used for one time reading
    void removeCallback(ISensorServerCallback callback, String id);
}