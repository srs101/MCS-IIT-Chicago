//Sahil Sheikh  MAD - Assignment5 A20518693
// Extra Credit :
// 1. Zulu date to other format
// 2. Smooth Transition between Portrait and Landscape Mode
// 3.
// Just in-case the app doesn't run, please run it again.
package com.example.news;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.news.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static String SEL = new String();
    private static final List<String> TOPICS = new ArrayList<>();

    private static final String TAG = "MainActivity";

    private static final String key = "6c5a6d2b28cd49759abc6304b0faf0e8";

    private static final String URL_topics = "https://newsapi.org/v2/top-headlines/sources";

    private static final String URL_Article = "https://newsapi.org/v2/top-headlines";

    private RequestQueue queue;

    private Menu topics_menu;

    static final ArrayList<NewSource> news_src_final_list = new ArrayList<>();

    static final ArrayList<NewSource> news_src_curr_list = new ArrayList<>();


    //Drawer Layout related stuff
    private DrawerLayout drawerLayout; //drawerLayout

    private ListView listView; //Listview

    private ActionBarDrawerToggle actionBarDrawerToggle; //Toggle Button

    public List<String> newsSoure = new ArrayList<>();

    public List<String> id_src = new ArrayList<>();

    public List<String> src_category = new ArrayList<>();

    private List<String > curr_list = new ArrayList<>();


    //View Pager Stuff
    private ViewPager2 view_pager;
    static final List<NewsArticle> ARTICLES = new ArrayList<>();

    private Article_adapter adapter;

    //View Binding stuff
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue= Volley.newRequestQueue(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //time-been
        //listView = findViewById(R.id.listView_item);
        //drawerLayout = findViewById(R.id.drawer_layout);
        //view_pager = findViewById(R.id.ViewPager2);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout,
                R.string.drawer_open,
                R.string.close_drawer
        );


    }
    //Creating instance of menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.topics_menu = menu;
        GetSources();
        return super.onCreateOptionsMenu(menu);
    }

    // Adding values to menu
    public void make_topic_menu(){
        topics_menu.clear();
        topics_menu.add("All"); //Adding All option
        for(int i = 0; i<TOPICS.size(); i++){
            topics_menu.add(TOPICS.get(i));
        }

    }

    public void GetSources(){

        if(checkconnection()) {

            if(news_src_final_list.isEmpty())
            {
                Uri.Builder builder = Uri.parse(URL_topics).buildUpon();
                builder.appendQueryParameter("apiKey", key);
                String url = builder.build().toString();
                Response.Listener<JSONObject> listener =
                        response -> parseData(response.toString());
                Response.ErrorListener errorListener =
                        error -> {
                            Log.d(TAG, "getAPIdata: Failed to get data ");
                            Toast.makeText(this, "Error loading data from API ", Toast.LENGTH_SHORT).show();
                        };
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                        null, listener, errorListener) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        return headers;
                    }
                };
                queue.add(jsonObjectRequest);

            } else {
                // Important for smooth Transition
                binding.drawerLayout.setBackground(null);
                //Toast.makeText(this, "Else sec of", Toast.LENGTH_SHORT).show();
                topics_menu.add("All"); //Adding All option
                for(int i = 0; i<TOPICS.size(); i++)
                    topics_menu.add(TOPICS.get(i));
                UpdateDrawer();
                if(ARTICLES.isEmpty()){
                    set_title();
                }else {
                    set_acticle_title();
                    adapter = new Article_adapter(this,ARTICLES);
                    binding.ViewPager2.setAdapter(adapter);
                    binding.ViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

                }
            }
        } else {
            Toast.makeText(this, "No Internet Connection...", Toast.LENGTH_SHORT).show();
        }


    }



    private boolean checkconnection(){
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        } else{
            return false;
        }
    }


    private void parseData(String response) { //function only called on OnCreate
        Toast.makeText(this, "Topics received from API", Toast.LENGTH_SHORT).show();

        try {
                JSONObject jsonObject = new JSONObject(response);

                JSONArray jsonArray = jsonObject.getJSONArray("sources");
                Toast.makeText(this, "Number of Sources Received " + jsonArray.length(), Toast.LENGTH_SHORT).show();

                String[] newSrc = new String[jsonArray.length()];
                if (!TOPICS.isEmpty())
                    TOPICS.removeAll(TOPICS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsObj = (JSONObject) jsonArray.get(i);
                    String topics = jsObj.getString("category");
                    String source = jsObj.getString("name");
                    String id = jsObj.getString("id");
                    newSrc[i] = source;
                /*
                newsSoure.add(source); //Full List of sources - this will not change
                src_category.add(topics); //Full List of category corresponding to source
                id_src.add(id); // Full List of id corresponding to source
                */
                    news_src_final_list.add(new NewSource(id, source, topics)); // Keeping this as the Final List which won't change

                    if (!TOPICS.contains(topics)) { // Setting the List which will contain distinct topics for option menu
                        TOPICS.add(topics);
                    }

                }
                make_topic_menu(); //Making the menu
                news_src_curr_list.addAll(news_src_final_list); //update current list
                SetDrawer(); //Setting-up the drawer

                Log.d(TAG, "parseData: " + news_src_curr_list);
                set_title();


        } catch (JSONException e){
            Log.d(TAG, "parseData: "+e);

        }

    }

    private void set_title() {
        getSupportActionBar().setTitle("News Gateway ( "+news_src_curr_list.size()+" )");
        return;
    }

    public void SetDrawer(){

        binding.listViewItem.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item,news_src_curr_list));
        binding.listViewItem.setOnItemClickListener(
                (parent, view, position, id) -> selectItem(position)
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        int menu_item = item.getItemId();
        String menu_title = (String) item.getTitle();
        Toast.makeText(this, "Option selected: "+menu_title, Toast.LENGTH_SHORT).show();
        if(!news_src_curr_list.isEmpty())
            news_src_curr_list.removeAll(news_src_curr_list);

        if(menu_title.equals("All")){
            news_src_curr_list.addAll(news_src_final_list);

        } else {
            for (int i = 0; i < news_src_final_list.size(); i++) {
                if (menu_title.equals(news_src_final_list.get(i).getNews_tpoic())) {
                    news_src_curr_list.add(news_src_final_list.get(i));
                }

            }
        }
        set_title(); // Changing App Title According to current list
        UpdateDrawer();


        return super.onOptionsItemSelected(item);
    }

    private void UpdateDrawer() {

        binding.listViewItem.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item,news_src_curr_list));

        binding.listViewItem.setOnItemClickListener(
                (parent, view, position, id) -> selectItem(position)
        );
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void selectItem(int position) {
        //Use this to
        //Toast.makeText(this, "You selected"+position, Toast.LENGTH_SHORT).show();
        binding.drawerLayout.setBackground(null);
        //binding.ViewPager2.setBackground(null);
        binding.drawerLayout.closeDrawer(binding.listViewItem);
        String selected_src = news_src_curr_list.get(position).getNews_id();
        String src_name = news_src_curr_list.get(position).getNews_name();
        Toast.makeText(this, "Opening: "+selected_src, Toast.LENGTH_SHORT).show();
        SEL = src_name;
        set_acticle_title();
        GetArticles(selected_src);
    }

    private void GetArticles(String selected_src) {
        if(checkconnection()){
            Uri.Builder builder = Uri.parse(URL_Article).buildUpon();
            builder.appendQueryParameter("sources",selected_src);
            builder.appendQueryParameter("apiKey", key);
            String url = builder.build().toString();
            Response.Listener<JSONObject> listener =
                    response -> parseArticle(response.toString());
            Response.ErrorListener errorListener =
                    error -> {
                        Log.d(TAG, "getAPIdata: Failed to get data ");
                        Toast.makeText(this, "Error loading data from API ", Toast.LENGTH_SHORT).show();
                    };
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, listener, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("User-Agent", "News-App");
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);



        }else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void set_acticle_title() {
        getSupportActionBar().setTitle(SEL);

    }

    private void parseArticle(String string) {
        Toast.makeText(this, "Received Articles from API", Toast.LENGTH_SHORT).show();

        try {
            ARTICLES.clear();
            JSONObject jsonObject = new JSONObject(string);
            JSONArray articles = jsonObject.getJSONArray("articles");
            for(int i =0; i<articles.length();i++)
            {
                String author="";
                String title="";
                String desc="";
                String url="";
                String Pic_url="";
                String publish="";
                JSONObject jb = articles.getJSONObject(i);
                if(jb.has("author"))
                    author = jb.getString("author");


                if(jb.has("title"))
                    title= jb.getString("title");


                if(jb.has("description"))
                    desc = jb.getString("description");


                if(jb.has("url"))
                    url = jb.getString("url");


                if(jb.has("urlToImage"))
                    Pic_url= jb.getString("urlToImage");

                // Converting Zulu time to Required time
                if(jb.has("publishedAt")){
                    DateTimeFormatter timeFormatter;
                    DateTimeFormatter dateTimeFormatter;
                    TemporalAccessor accessor;
                    LocalDateTime local;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        timeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                        accessor = timeFormatter.parse(jb.getString("publishedAt"));
                        dateTimeFormatter = DateTimeFormatter.ofPattern("LLL dd, yyyy kk:mm");
                        local = LocalDateTime.ofInstant(Instant.from(accessor), ZoneId.systemDefault());
                        publish = local.format(dateTimeFormatter);
                    } else {
                    publish = jb.getString("publishedAt");}
                }

                ARTICLES.add(new NewsArticle(author,title,desc,url,Pic_url,publish));

            }
            adapter = new Article_adapter(this,ARTICLES);
            binding.ViewPager2.setAdapter(adapter);
            binding.ViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

            //Toast.makeText(this, "Articles saved", Toast.LENGTH_SHORT).show();

        }catch (JSONException e){
            Log.d(TAG, "parseArticle: "+e);
        }

    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }



    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

}

/*
1. Create Dynamic Menu to get Topics
2. API KEY : 6c5a6d2b28cd49759abc6304b0faf0e8
3. Internet permissions, Add volley in implementations(gradle)
4. Get Sources and Add them in dynamic Menu
5.  Made Drawer, its Layout and added values in it.
6. Used Volley to get Articles
7. Stored them in a list and then passed it to viewPager2
8. Build features
9. Did the extra credit for smooth transition

 */