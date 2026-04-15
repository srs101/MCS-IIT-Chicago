package com.example.news;

import android.graphics.Paint;

import java.io.Serializable;

public class NewsArticle implements Serializable {
    private String author;
    private String title;
    private String desc;
    private String url;
    private String pic_url;

    public NewsArticle(String author, String title, String desc, String url, String pic_url, String publish) {
        this.author = author;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.pic_url = pic_url;
        this.publish = publish;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    private String publish;
}
