package com.example.mstfa.solarrelaxtab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ArizaKayit extends Fragment implements BackPressObserver{

    ArizaAdapter mArizaAdapter;
    Ariza ariza = new Ariza();

    public ArizaKayit() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ariza_kayit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        veriGetir();
    }

        public void veriGetir(){
            final LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.liste);
            mArizaAdapter = new ArizaAdapter(getContext(), ariza.getData());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mArizaAdapter);
        }

    @Override
    final public boolean isReadyToInterceptBackPress() {
        return this.isVisible();
    }

    @Override
    final public void onBackPress() {
        getActivity().finish();
        final Intent i = new Intent(this.getActivity(),Giris.class);
        startActivity(i);
    }
}