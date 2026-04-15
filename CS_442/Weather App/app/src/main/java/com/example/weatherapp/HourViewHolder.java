package com.example.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HourViewHolder extends RecyclerView.ViewHolder {
    TextView Hday,Htime,Htemp,Hdesc;
    ImageView Hicon;
    public HourViewHolder(@NonNull View itemView) {
        super(itemView);
        Hday = itemView.findViewById(R.id.hour_day);
        Htime = itemView.findViewById(R.id.hour_time);
        Hicon = itemView.findViewById(R.id.hour_icon);
        Htemp = itemView.findViewById(R.id.hour_temp);
        Hdesc = itemView.findViewById(R.id.hour_descrip);
    }
}
