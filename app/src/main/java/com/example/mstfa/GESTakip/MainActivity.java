package com.example.mstfa.GESTakip;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    DrawerLayout drawerLayout;
    int tabPosition;
    static String gun,ay,yil;
    Calendar c = Calendar.getInstance();

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("SolarRelax");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        setupToolbar();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0);
        tabLayout.getTabAt(1);
        tabLayout.getTabAt(2);
        tabLayout.getTabAt(3);


        gun = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        if (gun.length() == 1) {//tek haneli ise başına sıfır koymak için kontrol
            gun = "0";
            gun += Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        }
        ay = Integer.toString(c.get(Calendar.MONTH) + 1);
        if (ay.length() == 1) {
            ay = "0";
            ay += Integer.toString(c.get(Calendar.MONTH) + 1);
        }
        yil = Integer.toString(c.get(Calendar.YEAR));
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                 tabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.hakkinda) {
            Toast.makeText(context, "hakkinda", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.deneme){
            Toast.makeText(context, "deneme", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.iletisim){
            Toast.makeText(context, "iletisim", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.cikis){
            System.exit(0);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    Context context=this;
    //toolbar tıklama
    ArizaKayit arizaKayit = new ArizaKayit();
    @Override
    final public boolean onOptionsItemSelected(MenuItem item) {
        // Action Bar öğelerindeki basılmaları idare edelim
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.date:
                DatePickerDialog dpd = new DatePickerDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        yil = Integer.toString(year);
                        ay = Integer.toString(month+1);
                        if(ay.length()==1) {
                            ay = "0";
                            ay+=Integer.toString(month+1);
                        }
                        gun = Integer.toString(dayOfMonth);
                        if(gun.length()==1) {
                            gun = "0";
                            gun+=Integer.toString(dayOfMonth);
                        }
                        if(tabPosition==3){//ArizaKayit tab
                            Ariza ariza = new Ariza();
                            if(ariza.serverResponseMessage!=null){
                                Toast.makeText(context, ""+ariza.serverResponseMessage, Toast.LENGTH_SHORT).show();
                            }
                            else{
                               // getFragmentManager().beginTransaction.add(R.layout.ariza_kayit, arizaKayit, "myFailrueRecordTag");

                               //tarihe göre Arıza Kayıtları getir
                            }
                        }

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    final static public String getGun(){
        return gun;
    }
    final static public String getAy(){
        return ay;
    }
    final static public String getYil(){
        return yil;
    }
    //tarih seçimi
    @Override
    final public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainactivity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Toolbar
   final private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //NavigationView ekranda açmak için kullancagimiz iconu ActionBar'da gösterilmesini sagladik
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

    }

    //TabLayout bağlayıcı
   final private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AnlikGuc(),"Anlık Güç");
        adapter.addFragment(new UretimAnaliz(), "Üretim");
        adapter.addFragment(new HataAnaliz(),"Hata Analiz");
        adapter.addFragment(new ArizaKayit(), "Arıza Kayıt");
        adapter.addFragment(new ArizaliCihazlar(), "Arızalı Cihazlar");
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(adapter);
    }

    //fragment'da geri tuşu kontrolü için
    @Override
    final public void onBackPressed() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BackPressObserver) {
                BackPressObserver observer = (BackPressObserver) fragment;
                if (observer.isReadyToInterceptBackPress()) {
                    observer.onBackPress();
                    return;
                }
            }
        }
        super.onBackPressed();
    }


}
