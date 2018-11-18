package com.example.mstfa.GESTakip;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

//RecyclerView bağlayıcı
public class ArizaAdapter extends RecyclerView.Adapter<ArizaAdapter.MyViewHolder> {
    ArrayList<Ariza> mDataList;
    LayoutInflater inflater;
    public ArizaAdapter(Context context, ArrayList<Ariza> data){
        inflater=LayoutInflater.from(context);
        this.mDataList=data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.ariza_list_item,parent,false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Ariza tiklanilanAriza = mDataList.get(position);
        holder.setData(tiklanilanAriza,position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView hataAciklama,inverterAdi,altTesis,tarih,hataKodu,hataDerecesi;

          public MyViewHolder(View itemView) {
                super(itemView);
                hataAciklama = itemView.findViewById(R.id.hataAciklama);
                inverterAdi = itemView.findViewById(R.id.inverterAdi);
                altTesis = itemView.findViewById(R.id.AltTesis);
                tarih = itemView.findViewById(R.id.list_tarih);
                hataKodu = itemView.findViewById(R.id.hataKodu);
                hataDerecesi = itemView.findViewById(R.id.hataDerece);
        }
        public void setData(Ariza tiklanilanAriza, int position) {
            this.hataAciklama.setText(tiklanilanAriza.getHataAciklama());
            this.inverterAdi.setText(tiklanilanAriza.getInverterAdi());
            this.altTesis.setText(tiklanilanAriza.getAltTesis());
            this.tarih.setText(tiklanilanAriza.getTarih());
            this.hataKodu.setText(tiklanilanAriza.getHataKodu());
            this.hataDerecesi.setText(tiklanilanAriza.getHataDerecesi());
        }
    }
}