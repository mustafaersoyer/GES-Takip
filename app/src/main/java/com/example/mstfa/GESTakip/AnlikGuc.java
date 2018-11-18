package com.example.mstfa.GESTakip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.anastr.speedviewlib.SpeedView;
import com.numetriclabz.numandroidcharts.GaugeChart;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class AnlikGuc extends Fragment implements BackPressObserver{
     public AnlikGuc() {
    }

    @Override
    final public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.anlikguc, container, false);
    }
    GaugeChart gauge;
    @Override
    final public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        veriGetir(new CallBack() {
            @Override
            final public void onSuccess(float[] liste) {
                SpeedView speedometer = getView().findViewById(R.id.speedView);
                speedometer.setWithTremble(false);
                speedometer.setMaxSpeed(liste[0]);
                speedometer.speedTo(liste[1]);
            }
            @Override
            final public void onFail(String msg) {

            }
        });
    }
    JSONObject jsonBody ;
    String url="http://192.168.30.240:80/tesisBilgi.php?tesisId=" + Giris.getTesisID();
    final  public void veriGetir(final CallBack onCallBack){
        StringRequest jsonForGetRequest = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    final public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            int uzunluk = obj.length();
                            int i = 0;
                            float[] liste = new float[2];

                            while (i < uzunluk - 2) {
                                i++;
                                jsonBody = obj.getJSONObject("bilgi" + i);
                                liste[0] = jsonBody.getInt("toplamGuc");
                                liste[1] = jsonBody.getInt("anlikGuc");

                            }
                            onCallBack.onSuccess(liste);

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_LONG).show();

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
                                Toast.makeText(getContext(), "" + serverResponseMessage, Toast.LENGTH_LONG).show();

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

    }

    @Override
    public boolean isReadyToInterceptBackPress() {
        return false;
    }

    @Override
    public void onBackPress() {
        getActivity().finish();
        final Intent i = new Intent(this.getActivity(),Giris.class);
        startActivity(i);
    }
}
