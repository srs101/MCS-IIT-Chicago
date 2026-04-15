package com.example.news;

import java.io.Serializable;

public class NewSource implements Serializable {
    private String News_id;
    private String News_name;
    private String News_tpoic;

    public NewSource(String news_id, String news_name, String news_tpoic) {
        /*setNews_id(news_id);
        setNews_name(news_name);
        setNews_tpoic(news_tpoic);
        */
        this.News_id = news_id;
        this.News_name = news_name;
        this.News_tpoic = news_tpoic;
    }

    public String toString() { return News_name; }

    public String getNews_id() {
        return News_id;
    }

    public void setNews_id(String news_id) {
        News_id = news_id;
    }

    public String getNews_name() {
        return News_name;
    }

    public void setNews_name(String news_name) {
        News_name = news_name;
    }

    public String getNews_tpoic() {
        return News_tpoic;
    }

    public void setNews_tpoic(String news_tpoic) {
        News_tpoic = news_tpoic;
    }
}

