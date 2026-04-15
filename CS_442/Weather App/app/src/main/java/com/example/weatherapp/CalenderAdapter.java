package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalenderAdapter extends RecyclerView.Adapter<CalenderViewHolder> {
    private final List<Calender> calenderList;

    private final CalenderActivity calenderActivity;

    public CalenderAdapter(List<Calender> calenderList, CalenderActivity mainActivity) {
        this.calenderList = calenderList;
        this.calenderActivity = mainActivity;
    }

    @NonNull
    @Override
    public CalenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_layout,parent,false);
        return new CalenderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalenderViewHolder holder, int position) {
        Calender calender = calenderList.get(position);
        holder.Cday.setText("    "+calender.getC_day());
        holder.Ctemp.setText(" "+calender.getC_temp());
        holder.Cdesc.setText(calender.getC_desc());
        holder.Cprecp.setText(calender.getC_precp());
        holder.CUV.setText(calender.getC_UV());
        holder.Cmorn.setText(calender.getCmorn());
        holder.Cnoon.setText(calender.getCafter());
        holder.Ceven.setText(calender.getCeven());
        holder.Cnight.setText(calender.getCnight());
        holder.Cicon.setImageResource(calender.getIconId());

    }



    @Override
    public int getItemCount() {
        return calenderList.size();
    }
}
