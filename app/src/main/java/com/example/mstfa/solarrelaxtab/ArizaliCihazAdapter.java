package com.example.mstfa.solarrelaxtab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ArizaliCihazAdapter extends RecyclerView.Adapter<ArizaliCihazAdapter.MyViewHolder>{
    ArrayList<ArizaliCihaz> mDataList;

    LayoutInflater inflater;

    public ArizaliCihazAdapter(Context context, ArrayList<ArizaliCihaz> data){
        inflater=LayoutInflater.from(context);
        this.mDataList=data;
    }
    @Override
    final public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.arizalicihaz_list_item,parent,false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    final public void onBindViewHolder(MyViewHolder holder, int position) {
        ArizaliCihaz tiklanilanAriza = mDataList.get(position);
        holder.setData(tiklanilanAriza,position);
    }

    @Override
    final public int getItemCount() {
        return mDataList.size();
    }


    class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView arizaAciklama,inverterAdi,arizaKodu;
        public MyViewHolder(View itemView) {
            super(itemView);
            arizaAciklama = (TextView) itemView.findViewById(R.id.arizaAciklama);
            arizaKodu = (TextView) itemView.findViewById(R.id.arizaKodu);
            inverterAdi = (TextView) itemView.findViewById(R.id.inverterAdi);

        }

        final public void setData(ArizaliCihaz tiklanilanAriza, int position) {
            this.arizaAciklama.setText(tiklanilanAriza.getArizaAciklama());
            this.inverterAdi.setText(tiklanilanAriza.getInverterAdi());
            this.arizaKodu.setText(tiklanilanAriza.getArizaKodu());
        }
    }
}
