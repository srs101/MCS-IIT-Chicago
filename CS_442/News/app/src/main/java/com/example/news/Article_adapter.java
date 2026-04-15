package com.example.news;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Article_adapter extends RecyclerView.Adapter<Article__viewholder> {
    private final MainActivity mainActivity;

    private final List<NewsArticle> newsArticles;

    public Article_adapter(MainActivity mainActivity, List<NewsArticle> newsArticles) {
        this.mainActivity = mainActivity;
        this.newsArticles = newsArticles;
    }
    @NonNull
    @Override
    public Article__viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_layout,parent,false);

        return new Article__viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Article__viewholder holder, int position) {

    NewsArticle newsArticle = newsArticles.get(position);

    String title = newsArticle.getTitle();
    if(!title.isEmpty()){
        holder.art_title.setVisibility(View.VISIBLE);
        holder.art_title.setText(newsArticle.getTitle());
    } else {
        holder.art_title.setVisibility(View.GONE);
    }

    String auth = newsArticle.getAuthor();
    if(!newsArticle.getAuthor().equals("null")){
        holder.art_author.setVisibility(View.VISIBLE);
        holder.art_author.setText(newsArticle.getAuthor());
    }else {
        holder.art_author.setVisibility(View.GONE);
    }

    String date = newsArticle.getPublish();
    if(!newsArticle.getPublish().equals("null")){
        holder.art_date.setVisibility(View.VISIBLE);
        holder.art_date.setText(newsArticle.getPublish());
    }else {
        holder.art_date.setVisibility(View.GONE);
    }
        String img_url = newsArticle.getPic_url();

    Picasso pi =Picasso.get();
    if(!img_url.isEmpty()){
        pi.load(img_url).error(R.drawable.brokenimage).placeholder(R.drawable.loading).into(holder.art_pic);

    }else {
        holder.art_pic.setImageResource(R.drawable.noimage);
    }
    holder.art_desc.setMovementMethod(new ScrollingMovementMethod());
    String desc = newsArticle.getDesc();
    if(!newsArticle.getDesc().equals("null")){
        holder.art_desc.setVisibility(View.VISIBLE);
        holder.art_desc.setText(newsArticle.getDesc());
    }else {
        holder.art_desc.setVisibility(View.GONE);

    }
    int pg = position+1;
    String pgno = String.valueOf(new StringBuilder().append(pg).append(" out of ").append(getItemCount()));
    holder.pgno.setText(pgno);
    //setting onclick listener

    holder.art_title.setOnClickListener(v -> open_link(newsArticle.getUrl()));
    holder.art_desc.setOnClickListener(v -> open_link(newsArticle.getUrl()));
    holder.art_pic.setOnClickListener(v -> open_link(newsArticle.getUrl()));


    }

    private void open_link(String url) {
        //Toast.makeText(this, "Opening Website Page: "+can_url, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        mainActivity.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }
}
