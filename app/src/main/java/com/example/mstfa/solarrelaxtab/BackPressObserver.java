package com.example.mstfa.solarrelaxtab;
//fragment geri tuşu kontrolü
public interface BackPressObserver {
    boolean isReadyToInterceptBackPress();
    void onBackPress();
}
