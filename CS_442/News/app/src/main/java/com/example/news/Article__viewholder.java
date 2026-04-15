package com.example.news;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Article__viewholder extends RecyclerView.ViewHolder {

    TextView art_title;
    TextView art_date;
    TextView art_author;
    TextView art_desc;
    ImageView art_pic;
    TextView pgno;
    public Article__viewholder(@NonNull View itemView) {
        super(itemView);
        art_title = itemView.findViewById(R.id.Art_title);
        art_author = itemView.findViewById(R.id.Art_author);
        art_date = itemView.findViewById(R.id.Art_date);
        art_pic = itemView.findViewById(R.id.Art_image);
        art_desc = itemView.findViewById(R.id.Art_desc);
        pgno = itemView.findViewById(R.id.Art_Pgno);
    }
}
