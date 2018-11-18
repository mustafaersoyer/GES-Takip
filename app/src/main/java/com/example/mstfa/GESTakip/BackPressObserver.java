package com.example.mstfa.GESTakip;
//fragment geri tuşu kontrolü
public interface BackPressObserver {
    boolean isReadyToInterceptBackPress();
    void onBackPress();
}
