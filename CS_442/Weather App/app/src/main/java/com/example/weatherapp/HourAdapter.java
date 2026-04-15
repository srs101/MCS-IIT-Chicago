package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourAdapter extends RecyclerView.Adapter<HourViewHolder> {
    private final List<Hours> hoursList;
    private final MainActivity mainActivity;

    public HourAdapter(List<Hours> hoursList, MainActivity mainActivity) { //constructor
        this.hoursList = hoursList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hour_vh,parent,false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        Hours hours = hoursList.get(position);
        holder.Hday.setText(hours.getHourDay());
        holder.Htime.setText(hours.getHourTime());
        holder.Hicon.setImageResource(hours.getHourIconID());
        holder.Htemp.setText(hours.getHourTemp());
        holder.Hdesc.setText(hours.getHourDesc());



    }

    @Override
    public int getItemCount() {
        return hoursList.size();
    }
}
