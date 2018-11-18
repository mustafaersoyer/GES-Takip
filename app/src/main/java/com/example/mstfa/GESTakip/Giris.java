package com.example.mstfa.GESTakip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Giris extends AppCompatActivity {
    EditText kAdi,kSifre;
    Button btnGiris;
    private ProgressDialog progress;
    static String tesisID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        setTitle("");
        final UretimAnaliz fr = new UretimAnaliz();

        kAdi = (EditText) findViewById(R.id.txt_kAdi);
        kSifre = (EditText) findViewById(R.id.txt_kSifre);

        btnGiris = (Button) findViewById(R.id.btnGiris);

        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = ProgressDialog.show(Giris.this,"",
                        "Lütfen Bekleyiniz", true);
                String url="http://192.168.30.240:80/giris.php";

                StringRequest jsonForPostRequest = new StringRequest(
                        Request.Method.POST,url,
                        new Response.Listener<String>() {
                            @Override
                            final public void onResponse(String response) {

                                progress.dismiss();

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    int tesisId=jsonObject.getInt("tesisId");
                                    if(jsonObject.getString("giris").equals("1")) {
                                        final Intent i = new Intent(Giris.this,MainActivity.class);
                                        tesisID= jsonObject.getString("tesisId");
                                        startActivity(i);
                                        finish();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    final  public void onErrorResponse(VolleyError error) {
                        progress.dismiss();

                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){

                            JSONObject jsonObject = null;
                            String errorMessage = null;

                            switch(response.statusCode){
                                case 400:
                                    errorMessage = new String(response.data);

                                    try {
                                        jsonObject = new JSONObject(errorMessage);
                                        final String serverResponseMessage =  (String)jsonObject.get("hataMesaj");
                                        Toast.makeText(getApplicationContext(),""+serverResponseMessage,Toast.LENGTH_LONG).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                            }
                        }
                    }

                }) {

                    @Override
                    final protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();

                        params.put("kullaniciAdi1",kAdi.getText().toString());
                        params.put("sifre1",kSifre.getText().toString());

                        return params;
                    }

                    @Override
                    final public Map<String, String> getHeaders() throws AuthFailureError {

                        Map<String, String> param = new HashMap<String, String>();

                        return param;
                    }

                };

                jsonForPostRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppController.getInstance().addToRequestQueue(jsonForPostRequest);

            }
        });
    }
    final public static String getTesisID(){
        final String tesisId=tesisID;
        return tesisId;
    }
}