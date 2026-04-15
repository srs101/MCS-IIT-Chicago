package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalenderActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";
    JSONArray jsonArray;
    private String loc;
    private String resloved_loc;
    private String tempunit;
    private String time_zone;
    private RecyclerView recyclerViewCal;

    private final List<Calender> calenderList = new ArrayList<>();


    private static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#D94B371C"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        calenderList.removeAll(calenderList);
        recyclerViewCal=findViewById(R.id.recycleview2);
        recyclerViewCal.setLayoutManager(new LinearLayoutManager(this));
        Bundle intent = getIntent().getExtras();
        if(intent!=null){
            //storing data from intent to variables
            String temp = intent.getString("days");
            loc = intent.getString("location");
            resloved_loc = intent.getString("resolvedAddress");
            tempunit = intent.getString("Tempunit");
            time_zone = intent.getString("timezone");
            getSupportActionBar().setTitle(resloved_loc+" 15 Days");
            try {
                jsonArray=new JSONArray(temp);
                parseCalenderdata(jsonArray);

            }catch (JSONException e){
                e.printStackTrace();
            }

        }



    }


    private void parseCalenderdata(JSONArray jsonArray) throws JSONException {
        //storing values we get from the JSON Array
        SimpleDateFormat dayDate = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
        dayDate.setTimeZone(TimeZone.getTimeZone(time_zone));
        for(int i=0;i<15;i++)
        {
            JSONObject arr =jsonArray.getJSONObject(i);
            String day = arr.getString("datetimeEpoch");
            String hightemp = arr.getString("tempmax");
            String lowtemp = arr.getString("tempmin");
            String desc = arr.getString("description");
            String precp = arr.getString("precipprob");
            String UV = arr.getString("uvindex");
            JSONArray h = arr.getJSONArray("hours");
            String mortemp = ((JSONObject) h.get(8)).getString("temp");
            String aftertemp = ((JSONObject) h.get(13)).getString("temp");
            String eventemp = ((JSONObject) h.get(17)).getString("temp");
            String nightemp = ((JSONObject) h.get(h.length()-1)).getString("temp");

            String icon= arr.getString("icon");
            String iconRe = icon.replace("-","_");
            int cal_icon = this.getResources().getIdentifier(iconRe,"drawable",this.getPackageName());
            if(cal_icon==0){
                Log.d(TAG, "parseCalenderdata: Icon not available ");
            }
            long datetimeEpoch = Long.parseLong(day);//Converting DateEpoch to Day and Date
            Date day_date = new Date(datetimeEpoch*1000);
            int hTemp = (int) Double.parseDouble(hightemp);//Storing maxTemp as int
            int lTemp = (int) Double.parseDouble(lowtemp);//Storing minTemp as int

            int cmor = (int)Double.parseDouble(mortemp);
            int cnoon = (int)Double.parseDouble(aftertemp);
            int ceven = (int)Double.parseDouble(eventemp);
            int cnight = (int)Double.parseDouble(nightemp);


            String c_day = dayDate.format(day_date);
            String c_temp =hTemp+"°"+(tempunit.equals("us") ? "F":"C")+" / "+lTemp+"°"+(tempunit.equals("us") ? "F":"C");
            String c_precp="Precip: "+precp+"%";
            String c_uv = "UV Index: "+UV;
            String c_morn =cmor+"°"+(tempunit.equals("us") ? "F":"C");
            String c_noon =cnoon+"°"+(tempunit.equals("us") ? "F":"C");
            String c_even = ceven+"°"+(tempunit.equals("us") ? "F":"C");
            String c_night = cnight+"°"+(tempunit.equals("us") ? "F":"C");
            Calender calenderob = new Calender(c_day,c_temp,desc,c_precp,c_uv,c_morn,c_noon,c_even,c_night,cal_icon);
            calenderList.add(calenderob);
        }
        CalenderAdapter calenderAdapter = new CalenderAdapter(calenderList,this);
        recyclerViewCal.setAdapter(calenderAdapter);

    }






}