package com.example.mstfa.solarrelaxtab;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.ArrayList;

//Tablayout fragmentları bağlayıcı

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> mFragmentListTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    final public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    final public int getCount() {
        return mFragmentList.size();
    }

    final public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentListTitles.add(title);
    }

    @Override
    final public CharSequence getPageTitle(int position) {
        return mFragmentListTitles.get(position);
        // sadece icon istiyorsak return null yapmak yeterli
        //Tablayout başlıklar
    }
}