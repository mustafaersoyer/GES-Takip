package com.example.mstfa.solarrelaxtab;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//Recycler view için data

public class Ariza {
    private String hataKodu;
    private String hataDerecesi;
    private String hataAciklama;
    private String hataDerece;
    private String inverterAdi;
    private String altTesis;
    private String tarih;

    public String getHataKodu() {
        return hataKodu;
    }

    public String getHataDerecesi() {
        return hataDerecesi;
    }

    public String getHataAciklama() {
        return hataAciklama;
    }

    public String getInverterAdi() {
        return inverterAdi;
    }

    public String getAltTesis() {
        return altTesis;
    }

    public String getTarih() {
        return tarih;
    }

    public void setHataKodu(String hataKodu) {
        this.hataKodu = hataKodu;
    }

    public void setHataDerecesi(String hataDerecesi) {
        this.hataDerecesi = hataDerecesi;
    }

    public void setHataAciklama(String hataAciklama) {
        this.hataAciklama = hataAciklama;
    }

    public void setInverterAdi(String inverterAdi) {
        this.inverterAdi = inverterAdi;
    }

    public void setAltTesis(String altTesis) {
        this.altTesis = altTesis;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }
    Calendar c = Calendar.getInstance();
    String yil;
    static String serverResponseMessage;
    public ArrayList<Ariza> getData(){
       yil = Integer.toString(c.get(Calendar.YEAR));
       final ArrayList<Ariza> dataList=new ArrayList<>();

       String url="http://192.168.30.240:80/hataKayit.php?tarih="+MainActivity.getYil()+"&tesisId="+Giris.getTesisID()+"";
            StringRequest jsonForGetRequest = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("getYil:","getYil: "+MainActivity.getYil());
                            JSONObject obj = new JSONObject(response);
                            JSONObject jsonBody;
                            final int UZUNLUK = obj.length();
                            int i = 0;
                            while (i < UZUNLUK - 2) {
                                i++;
                                jsonBody = obj.getJSONObject("bilgi" + i);
                                Ariza gecici=new Ariza();
                                gecici.setAltTesis("Alt Tesis: "+jsonBody.getString("altTesis"));
                                gecici.setHataDerecesi("Hata Derecesi: "+ jsonBody.getString("hataDerece"));
                                gecici.setHataKodu("Hata Kodu: "+jsonBody.getString("hataKodu"));
                                gecici.setInverterAdi("İnverter Adı: "+jsonBody.getString("inverterAdi"));
                                gecici.setTarih("Tarih: "+jsonBody.getString("tarih"));
                                gecici.setHataAciklama("Hata Açıklaması: "+jsonBody.getString("hataAciklama"));
                                dataList.add(gecici);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    JSONObject jsonObject = null;
                    String errorMessage = null;

                    switch (response.statusCode) {
                        case 400:
                            errorMessage = new String(response.data);
                            try {
                                jsonObject = new JSONObject(errorMessage);
                                 serverResponseMessage = (String) jsonObject.get("hataMesaj");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                return param;
            }
        };

        jsonForGetRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonForGetRequest);
        return dataList;

    }
    public static String getHataMesaj(){
        return serverResponseMessage;
    }
}
