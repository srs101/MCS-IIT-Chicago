package com.example.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalenderViewHolder extends RecyclerView.ViewHolder {
    TextView Cday,Ctemp,Cdesc,Cprecp,CUV,Cmorn,Cnoon,Ceven,Cnight;
    ImageView Cicon;

    public CalenderViewHolder(@NonNull View itemView) {
        super(itemView);
        Cday=itemView.findViewById(R.id.Calender_day);
        Ctemp=itemView.findViewById(R.id.Calender_temp);
        Cdesc=itemView.findViewById(R.id.Cal_desc);
        Cprecp=itemView.findViewById(R.id.Calender_precp);
        CUV = itemView.findViewById(R.id.Calender_UV);
        Cmorn=itemView.findViewById(R.id.Cal_morning);
        Cnoon=itemView.findViewById(R.id.Cal_afternoon);
        Ceven=itemView.findViewById(R.id.Cal_evening);
        Cnight=itemView.findViewById(R.id.Cal_night);
        Cicon=itemView.findViewById(R.id.Cal_icon);
    }
}
