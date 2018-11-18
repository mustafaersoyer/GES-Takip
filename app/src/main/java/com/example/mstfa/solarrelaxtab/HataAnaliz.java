package com.example.mstfa.solarrelaxtab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HataAnaliz extends Fragment implements BackPressObserver {
    String secim="";
    ProgressDialog progress;
    ArrayList<String> EntryLabels;
    ArrayList<String> BarEntryLabels;
    ArrayList<String> PieEntryLabels;
    JSONObject jsonBody ;

    LineChart lineChart ;

    ArrayList<Entry> LineEntries ;
    ArrayList<BarEntry> BarEntries;

    LineDataSet LINEDATASET ;
    LineData LINEDATA ;

    BarDataSet BARDATASET;
    BarData BARDATA;

    PieDataSet PIEDATASET;
    PieData PIEDATA;

    BarChart barChart;
    PieChart pieChart;

    PopupMenu popupmenu;

    public float[] liste;

    String gun,ay,yil;
    boolean tamEkranMi= false;
    int gelenTesisId;
    String url;//Yapılan seçime gör GET sorgusunu belirler.
    Calendar c = Calendar.getInstance();

    public HataAnaliz() {
    }

    @Override
    final public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hata_analiz, container, false);
    }
    final public void tarihAl(){
        gun=MainActivity.getGun();
        ay=MainActivity.getAy();
        yil=MainActivity.getYil();
    }
    @Override
    final public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        gelenTesisId = Integer.parseInt(Giris.getTesisID());
        tarihAl();
        url = "http://192.168.30.240:80/hataAnaliz.php?tesisId=" + gelenTesisId + "&tarih=" + yil; //varsayılan aylık gösterimdir.

        progress = ProgressDialog.show(
                getContext(), "",
                "Lütfen Bekleyiniz", true);

        //LineChart başlangıç

        LineEntries = new ArrayList<>();

        EntryLabels = new ArrayList<String>();

        lineChart = (LineChart) getView().findViewById(R.id.lineChartHata);
        //LineChart bitiş

        //BarChart Başla

        BarEntries = new ArrayList<>();

        barChart = (BarChart) getView().findViewById(R.id.barChartHata);

        BarEntryLabels = new ArrayList<String>();

        //BarChart Bitiş

        //PieChart Başlangıç
        pieChart = (PieChart) getView().findViewById(R.id.pieChartHata);
        PieEntryLabels = new ArrayList<String>();
        //PieChart Bitiş


//LineChart Verisi
        veriGetir(new CallBack() {
            @Override
            final  public void onSuccess(float[] list) {
                for (int d = 0; d < list.length; d++) {
                    LineEntries.add(new Entry(list[d], d));
                }

                LINEDATASET = new LineDataSet(LineEntries, "Hata");
                if (EntryLabels != null) {
                    EntryLabels.clear();
                    AddValuesToEntryLabels();
                } else {
                    AddValuesToEntryLabels();
                }
                LINEDATA = new LineData(EntryLabels, LINEDATASET);
                LINEDATASET.setValueTextSize(10);
                LINEDATASET.setColor(R.color.black);
                LINEDATASET.setDrawFilled(true);
                LINEDATASET.setDrawCubic(true);
                lineChart.setDescription("");
                lineChart.setTouchEnabled(true);
                lineChart.setData(LINEDATA);
                lineChart.animateXY(1500, 1500);
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            }

            @Override
            final  public void onFail(String msg) {

            }
        });

//BarChart Verisi
        veriGetir(new CallBack() {
            @Override
            final public void onSuccess(float[] list) {
                for (int d = 0; d < list.length; d++) {
                    BarEntries.add(new BarEntry(list[d], d));
                }
                if (BarEntryLabels != null) {
                    BarEntryLabels.clear();
                    AddValuesToBarEntryLabels();
                } else {
                    AddValuesToBarEntryLabels();
                }
                BARDATASET = new BarDataSet(BarEntries, "Hata");
                BARDATA = new BarData(BarEntryLabels, BARDATASET);
                BARDATASET.setColor(R.color.black);
                BARDATASET.setValueTextSize(10);
                barChart.setDescription("");
                barChart.setTouchEnabled(true);
                barChart.setData(BARDATA);
                barChart.animateXY(1500, 1500);
                barChart.notifyDataSetChanged();
                barChart.invalidate();
            }

            @Override
            final public void onFail(String msg) {

            }
        });
//PieChart Verisi
        veriGetir(new CallBack() {
            @Override
            final public void onSuccess(float[] list) {
                if (PieEntryLabels != null) {
                    PieEntryLabels.clear();
                    AddValuesToPieEntryLabels();
                } else {
                    AddValuesToPieEntryLabels();
                }
                PIEDATASET = new PieDataSet(LineEntries, "Hata");
                PIEDATA = new PieData(PieEntryLabels, PIEDATASET);
                PIEDATASET.setColors(ColorTemplate.COLORFUL_COLORS);
                PIEDATASET.setValueTextSize(10);
                pieChart.setDescription("");
                pieChart.setTouchEnabled(true);
                pieChart.setData(PIEDATA);
                pieChart.animateXY(1500, 1500);
                pieChart.notifyDataSetChanged();
                pieChart.invalidate();
            }

            @Override
            final public void onFail(String msg) {

            }
        });


        lineChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            final  public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                return;
            }

            @Override
            final public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                return;
            }

            @Override
            final public void onChartLongPressed(MotionEvent me) {
                return;
            }

            @Override
            final public void onChartDoubleTapped(MotionEvent me) {

                return;
            }

            @Override
            final public void onChartSingleTapped(MotionEvent me) {
                popupmenu = new PopupMenu(getContext(), lineChart);
                popupmenu.getMenuInflater().inflate(R.menu.popup_menu, popupmenu.getMenu());
                if (tamEkranMi) {
                    popupmenu.getMenu().removeItem(R.id.popup_tamEkran);
                    popupmenu.getMenu().add("Küçült");
                }

                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Tam Ekran")) {
                            barChart.setVisibility(View.GONE);
                            pieChart.setVisibility(View.GONE);
                            lineChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1));
                            tamEkranMi = true;

                        } else if (menuItem.getTitle().equals("Küçült")) {

                            barChart.setVisibility(View.VISIBLE);
                            pieChart.setVisibility(View.VISIBLE);
                            lineChart.setVisibility(View.VISIBLE);
                            lineChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));

                            tamEkranMi = false;

                        } else if (menuItem.getTitle().equals("Saatlik")) {
                            tarihAl();
                            url = "http://192.168.30.240:80/hataAnaliz.php?tesisId=" + gelenTesisId + "&tarih=" + yil + "-" + ay + "-" + gun;
                            secim = "Saatlik";
                            veriGetir(new CallBack() {
                                @Override
                                final  public void onSuccess(float[] list) {
                                    LineEntries.clear();
                                    for (int d = 0; d < list.length; d++) {
                                        LineEntries.add(new Entry(list[d], d));
                                    }

                                    LINEDATASET = new LineDataSet(LineEntries, "Hata");
                                    if (EntryLabels != null) {
                                        EntryLabels.clear();
                                        AddValuesToEntryLabels();
                                    } else {
                                        AddValuesToEntryLabels();
                                    }
                                    LINEDATA = new LineData(EntryLabels, LINEDATASET);
                                    LINEDATASET.setValueTextSize(10);
                                    lineChart.setDescription("");
                                    lineChart.setTouchEnabled(true);
                                    lineChart.setData(LINEDATA);
                                    lineChart.animateXY(1500, 1500);
                                    lineChart.notifyDataSetChanged();
                                    lineChart.invalidate();
                                }

                                @Override
                                final public void onFail(String msg) {

                                }
                            });

                        } else if (menuItem.getTitle().equals("Aylık")) {
                            tarihAl();
                            url = "http://192.168.30.240:80/hataAnaliz.php?tesisId=" + gelenTesisId + "&tarih=" + yil;
                            secim = "Aylık";
                            veriGetir(new CallBack() {
                                @Override
                                final public void onSuccess(float[] list) {
                                    LineEntries.clear();
                                    for (int d = 0; d < list.length; d++) {
                                        LineEntries.add(new Entry(list[d], d));
                                    }
                                    LINEDATASET = new LineDataSet(LineEntries, "Hata");
                                    if (EntryLabels != null) {
                                        EntryLabels.clear();
                                        AddValuesToEntryLabels();
                                    } else {
                                        AddValuesToEntryLabels();
                                    }
                                    LINEDATA = new LineData(EntryLabels, LINEDATASET);
                                    LINEDATASET.setColor(R.color.black);
                                    LINEDATASET.setDrawFilled(true);
                                    LINEDATASET.setDrawCubic(true);
                                    LINEDATASET.setValueTextSize(10);
                                    lineChart.setDescription("");
                                    lineChart.setTouchEnabled(true);
                                    lineChart.setData(LINEDATA);
                                    lineChart.animateXY(1500, 1500);
                                    lineChart.notifyDataSetChanged();
                                    lineChart.invalidate();
                                }

                                @Override
                                final public void onFail(String msg) {

                                }
                            });

                        }
                        return true;
                    }
                });
                popupmenu.show();
                return;
            }

            @Override
            final public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                return;
            }

            @Override
            final public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                return;
            }

            @Override
            final public void onChartTranslate(MotionEvent me, float dX, float dY) {
                return;
            }
        });
        barChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            final public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                return;
            }

            @Override
            final public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                return;
            }

            @Override
            final  public void onChartLongPressed(MotionEvent me) {
                return;
            }

            @Override
            final public void onChartDoubleTapped(MotionEvent me) {
                return;
            }

            @Override
            final public void onChartSingleTapped(MotionEvent me) {
                popupmenu = new PopupMenu(getContext(), barChart);
                popupmenu.getMenuInflater().inflate(R.menu.popup_menu, popupmenu.getMenu());
                if (tamEkranMi) {
                    popupmenu.getMenu().removeItem(R.id.popup_tamEkran);
                    popupmenu.getMenu().add("Küçült");
                }
                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Tam Ekran")) {
                            lineChart.setVisibility(View.GONE);
                            pieChart.setVisibility(View.GONE);
                            barChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                            tamEkranMi = true;
                        } else if (menuItem.getTitle().equals("Küçült")) {
                            lineChart.setVisibility(View.VISIBLE);
                            pieChart.setVisibility(View.VISIBLE);
                            tamEkranMi = false;
                        } else if (menuItem.getTitle().equals("Saatlik")) {
                            tarihAl();
                            url = "http://192.168.30.240:80/hataAnaliz.php?tesisId=" + gelenTesisId + "&tarih=" + yil + "-" + ay + "-" + gun;
                            secim = "Saatlik";
                            veriGetir(new CallBack() {
                                @Override
                                final  public void onSuccess(float[] list) {
                                    BarEntries.clear();
                                    for (int d = 0; d < list.length; d++) {
                                        BarEntries.add(new BarEntry(list[d], d));
                                    }
                                    BARDATASET = new BarDataSet(BarEntries, "Üretim");
                                    if (BarEntryLabels != null) {
                                        BarEntryLabels.clear();
                                        AddValuesToBarEntryLabels();
                                    } else {
                                        AddValuesToBarEntryLabels();
                                    }
                                    BARDATA = new BarData(BarEntryLabels, BARDATASET);
                                    BARDATASET.setColor(R.color.black);
                                    BARDATASET.setValueTextSize(10);
                                    barChart.setDescription("");
                                    barChart.setTouchEnabled(true);
                                    barChart.setData(BARDATA);
                                    barChart.animateXY(1500, 1500);
                                    barChart.notifyDataSetChanged();
                                    barChart.invalidate();
                                }

                                @Override
                                final public void onFail(String msg) {

                                }
                            });

                        } else if (menuItem.getTitle().equals("Aylık")) {
                            tarihAl();
                            url = "http://192.168.30.240:80/hataAnaliz.php?tesisId=" + gelenTesisId + "&tarih=" + yil;
                            secim = "Aylık";
                            veriGetir(new CallBack() {
                                @Override
                                final  public void onSuccess(float[] list) {
                                    BarEntries.clear();
                                    for (int d = 0; d < list.length; d++) {
                                        BarEntries.add(new BarEntry(list[d], d));
                                    }

                                    BARDATASET = new BarDataSet(BarEntries, "Üretim");
                                    if (BarEntryLabels != null) {
                                        BarEntryLabels.clear();
                                        AddValuesToBarEntryLabels();
                                    } else {
                                        AddValuesToBarEntryLabels();
                                    }
                                    BARDATA = new BarData(BarEntryLabels, BARDATASET);
                                    BARDATASET.setColor(R.color.black);
                                    BARDATASET.setValueTextSize(10);
                                    barChart.setDescription("");
                                    barChart.setTouchEnabled(true);
                                    barChart.setData(BARDATA);
                                    barChart.animateXY(1500, 1500);
                                    barChart.notifyDataSetChanged();
                                    barChart.invalidate();

                                }

                                @Override
                                final  public void onFail(String msg) {

                                }
                            });

                        }
                        return true;
                    }
                });
                popupmenu.show();
                return;
            }

            @Override
            final public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                return;
            }

            @Override
            final public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                return;
            }

            @Override
            final public void onChartTranslate(MotionEvent me, float dX, float dY) {
                return;
            }
        });
        pieChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            final public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                return;
            }

            @Override
            final public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                return;
            }

            @Override
            final public void onChartLongPressed(MotionEvent me) {
                return;
            }

            @Override
            final public void onChartDoubleTapped(MotionEvent me) {
                return;
            }

            @Override
            final public void onChartSingleTapped(MotionEvent me) {
                popupmenu = new PopupMenu(getContext(), pieChart);
                popupmenu.getMenuInflater().inflate(R.menu.popup_menu, popupmenu.getMenu());

                if (tamEkranMi) {
                    popupmenu.getMenu().removeItem(R.id.popup_tamEkran);
                    popupmenu.getMenu().add("Küçült");
                }
                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Tam Ekran")) {
                            barChart.setVisibility(View.GONE);
                            lineChart.setVisibility(View.GONE);
                            pieChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                            tamEkranMi = true;

                        } else if (menuItem.getTitle().equals("Küçült")) {

                            barChart.setVisibility(View.VISIBLE);
                            lineChart.setVisibility(View.VISIBLE);
                            tamEkranMi = false;

                        } else if (menuItem.getTitle().equals("Saatlik")) {
                            tarihAl();
                            url = "http://192.168.30.240:80/hataAnaliz.php?tesisId=" + gelenTesisId + "&tarih=" + yil + "-" + ay + "-" + gun;
                            secim = "Saatlik";
                            veriGetir(new CallBack() {
                                @Override
                                final public void onSuccess(float[] list) {
                                    LineEntries.clear();
                                    for (int d = 0; d < list.length; d++) {
                                        LineEntries.add(new Entry(list[d], d));
                                    }
                                    PIEDATASET = new PieDataSet(LineEntries, "Üretim");
                                    if (PieEntryLabels != null) {
                                        PieEntryLabels.clear();
                                        AddValuesToPieEntryLabels();
                                    } else {
                                        AddValuesToPieEntryLabels();
                                    }
                                    PIEDATA = new PieData(PieEntryLabels, PIEDATASET);
                                    PIEDATASET.setColors(ColorTemplate.COLORFUL_COLORS);
                                    PIEDATASET.setValueTextSize(10);
                                    pieChart.setDescription("");
                                    pieChart.setTouchEnabled(true);
                                    pieChart.setData(PIEDATA);
                                    pieChart.animateXY(1500, 1500);
                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                }

                                @Override
                                final public void onFail(String msg) {

                                }
                            });

                        } else if (menuItem.getTitle().equals("Aylık")) {
                            tarihAl();
                            url = "http://192.168.30.240:80/hataAnaliz.php?tesisId=" + gelenTesisId + "&tarih=" + yil;
                            secim = "Aylık";
                            veriGetir(new CallBack() {
                                @Override
                                final public void onSuccess(float[] list) {

                                    LineEntries.clear();
                                    for (int d = 0; d < list.length; d++) {
                                        LineEntries.add(new Entry(list[d], d));
                                    }
                                    PIEDATASET = new PieDataSet(LineEntries, "Üretim");
                                    if (PieEntryLabels != null) {
                                        PieEntryLabels.clear();
                                        AddValuesToPieEntryLabels();
                                    } else {
                                        AddValuesToPieEntryLabels();
                                    }
                                    PIEDATA = new PieData(PieEntryLabels, PIEDATASET);
                                    PIEDATASET.setColors(ColorTemplate.COLORFUL_COLORS);
                                    PIEDATASET.setValueTextSize(10);
                                    pieChart.setDescription("");
                                    pieChart.setTouchEnabled(true);
                                    pieChart.setData(PIEDATA);
                                    pieChart.animateXY(1500, 1500);
                                    pieChart.notifyDataSetChanged();
                                    pieChart.invalidate();

                                }

                                @Override
                                final public void onFail(String msg) {

                                }
                            });
                        }
                        return true;
                    }
                });
                popupmenu.show();
                return;
            }

            @Override
            final public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                return;
            }

            @Override
            final public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                return;
            }

            @Override
            final public void onChartTranslate(MotionEvent me, float dX, float dY) {
                return;
            }
        });

    }


    final  public void veriGetir(final CallBack onCallBack){
        StringRequest jsonForGetRequest = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    final public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            int uzunluk = obj.length();
                            int i = 0;
                            liste = new float[uzunluk-2];

                            while (i < uzunluk - 2) {
                                i++;
                                jsonBody = obj.getJSONObject("bilgi" + i);
                                float miktar = jsonBody.getInt("hataSayi");
                                liste[i-1]=miktar;
                            }
                            onCallBack.onSuccess(liste);

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            final  public void onErrorResponse(VolleyError error) {
                progress.dismiss();
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

    final public void AddValuesToEntryLabels(){
        String saat;
        if (secim.equals("Saatlik")) { //Saatlik-aylık kontrolü
            for(int i = 0; i<24; i++){
                if(i<10)
                    saat = "0"+i+"-00";
                else
                    saat = i+"-00";
                EntryLabels.add(saat);
            }
        }else{
            EntryLabels.add("Ocak");
            EntryLabels.add("Şubat");
            EntryLabels.add("Mart");
            EntryLabels.add("Nisan");
            EntryLabels.add("Mayıs");
            EntryLabels.add("Haziran");
            EntryLabels.add("Temmuz");
            EntryLabels.add("Ağustos");
            EntryLabels.add("Eylül");
            EntryLabels.add("Ekim");
            EntryLabels.add("Kasım");
            EntryLabels.add("Aralık");
        }
    }
    final public void AddValuesToBarEntryLabels(){
        String saat;
        if (secim.equals("Saatlik")) { //Saatlik-aylık kontrolü
            for(int i = 0; i<24; i++){
                if(i<10)
                    saat = "0"+i+"-00";
                else
                    saat = i+"-00";
                BarEntryLabels.add(saat);
            }
        }else{
            BarEntryLabels.add("Ocak");
            BarEntryLabels.add("Şubat");
            BarEntryLabels.add("Mart");
            BarEntryLabels.add("Nisan");
            BarEntryLabels.add("Mayıs");
            BarEntryLabels.add("Haziran");
            BarEntryLabels.add("Temmuz");
            BarEntryLabels.add("Ağustos");
            BarEntryLabels.add("Eylül");
            BarEntryLabels.add("Ekim");
            BarEntryLabels.add("Kasım");
            BarEntryLabels.add("Aralık");
        }
    }
    final public void AddValuesToPieEntryLabels(){
        String saat;
        if (secim.equals("Saatlik")) { //Saatlik-aylık kontrolü
            for(int i = 0; i<24; i++){
                if(i<10)
                    saat = "0"+i+"-00";
                else
                    saat = i+"-00";
                PieEntryLabels.add(saat);
            }
        }else{
            PieEntryLabels.add("Ocak");
            PieEntryLabels.add("Şubat");
            PieEntryLabels.add("Mart");
            PieEntryLabels.add("Nisan");
            PieEntryLabels.add("Mayıs");
            PieEntryLabels.add("Haziran");
            PieEntryLabels.add("Temmuz");
            PieEntryLabels.add("Ağustos");
            PieEntryLabels.add("Eylül");
            PieEntryLabels.add("Ekim");
            PieEntryLabels.add("Kasım");
            PieEntryLabels.add("Aralık");
        }
    }


    @Override
    final public boolean isReadyToInterceptBackPress() {
        return this.isVisible();
        //**Another condition, i.e. !editText.getText().toString().isEmpty();**

    }
    @Override
    final public void onBackPress() {
        //Do whatever needs to be done on a back press. When finished, make sure isReadyToInterceptBackPress() will return false.
        if (tamEkranMi){
            barChart.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.VISIBLE);
            lineChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));

            tamEkranMi = false;
        }
        else{
            getActivity().finish();
            final Intent i = new Intent(this.getActivity(),Giris.class);
            startActivity(i);

        }
    }

}