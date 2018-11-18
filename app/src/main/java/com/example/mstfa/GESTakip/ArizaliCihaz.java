package com.example.mstfa.GESTakip;

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
import java.util.HashMap;
import java.util.Map;

public class ArizaliCihaz {
    private String arizaKodu;
    private String arizaAciklama;
    private String inverterAdi;

    final public String getArizaKodu() {
        return arizaKodu;
    }

    final public String getArizaAciklama() {
        return arizaAciklama;
    }

    final public String getInverterAdi() {
        return inverterAdi;
    }

    final public void setArizaKodu(String arizaKodu) {
        this.arizaKodu = arizaKodu;
    }

    final public void setArizaAciklama(String arizaAciklama) {
        this.arizaAciklama = arizaAciklama;
    }

    final public void setInverterAdi(String inverterAdi) {
        this.inverterAdi = inverterAdi;
    }

    final public ArrayList<ArizaliCihaz> getData(){

        final ArrayList<ArizaliCihaz> dataList=new ArrayList<>();
        String url="http://192.168.30.240:80/arizalicihaz.php?tesisId="+Giris.getTesisID()+"";
        StringRequest jsonForGetRequest = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    final public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject jsonBody;
                            int uzunluk = obj.length();
                            int i = 0;
                            while (i < uzunluk - 2) {
                                i++;
                                jsonBody = obj.getJSONObject("bilgi" + i);
                                String arizaKodu = jsonBody.getString("arizaKodu");
                                String arizaAciklama = jsonBody.getString("arizaAciklama");
                                String inverterAdi = jsonBody.getString("inverterAdi");
                                ArizaliCihaz gecici=new ArizaliCihaz();
                                gecici.setArizaAciklama("Arıza Açıklama: "+arizaAciklama);
                                gecici.setArizaKodu("Arıza Kodu: "+arizaKodu);
                                gecici.setInverterAdi("İnverter Adı: "+inverterAdi);
                                dataList.add(gecici);
                                Log.d("denemededede","i:"+i);

                            }

                        } catch (JSONException e){
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            final public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    JSONObject jsonObject = null;
                    String errorMessage = null;

                    switch (response.statusCode) {
                        case 400:
                            errorMessage = new String(response.data);
                            try {
                                jsonObject = new JSONObject(errorMessage);
                                String serverResponseMessage = (String) jsonObject.get("hataMesaj");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }
        }) {
            @Override
            final  public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                return param;
            }

        };

        jsonForGetRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonForGetRequest);
        return dataList;

    }
}
