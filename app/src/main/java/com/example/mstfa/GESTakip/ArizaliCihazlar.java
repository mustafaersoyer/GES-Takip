package com.example.mstfa.GESTakip;

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

public class ArizaliCihazlar extends Fragment implements BackPressObserver{
    public ArizaliCihazlar() {
    }
    RecyclerView recyclerView;
    ArizaliCihazAdapter mArizaliCihazAdapter;

    @Override
    final public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.arizali_cihazlar, container, false);
    }
    @Override
    final public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ArizaliCihaz arizaliCihaz = new ArizaliCihaz();
        recyclerView = (RecyclerView)getView().findViewById(R.id.arizaliCihazListe);
        mArizaliCihazAdapter = new ArizaliCihazAdapter(this.getActivity(),arizaliCihaz.getData());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mArizaliCihazAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
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
