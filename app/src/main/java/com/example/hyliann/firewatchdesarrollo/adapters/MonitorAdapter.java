package com.example.hyliann.firewatchdesarrollo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hyliann.firewatchdesarrollo.R;
import com.example.hyliann.firewatchdesarrollo.modelo.Sector;

import java.util.ArrayList;

public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder> {
    private ArrayList<Sector> sectores;

    public class MonitorViewHolder extends RecyclerView.ViewHolder {
        TextView tv_monitor;

        public MonitorViewHolder(View v) {
            super(v);
            tv_monitor = (TextView) v.findViewById(R.id.tv_temperatura);;
        }

    }

    public MonitorAdapter(ArrayList<Sector> sectores) {
        this.sectores = sectores;
    }

    @Override
    public MonitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_temperatura,null,false);
        return new MonitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MonitorViewHolder holder, int position) {
        holder.tv_monitor.setText(sectores.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return sectores.size();
    }
}
