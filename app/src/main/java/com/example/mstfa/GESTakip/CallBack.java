package com.example.mstfa.GESTakip;

//Asenkronize çalışan fonksiyondan verileri doğru zamanda alabilmek için. (Volley)
interface CallBack {
    void onSuccess(float[] liste);
    void onFail(String msg);
}