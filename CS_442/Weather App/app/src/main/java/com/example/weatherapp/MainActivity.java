/*
Sahil Sheikh  A20518693 Assignment3:Weather API
Extra credit implemented:
1)Saved user setting using Shared Preference
2)Swiper to refresh and get the API data again.
3)Created a Custom App launcher icon using the free asset from "<a href="https://www.flaticon.com/free-icons/sky" title="sky icons">Sky icons created by kosonicon - Flaticon</a>s"
 */
package com.example.weatherapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final List<Hours> HourList = new ArrayList<>();
    public String location;
    TextView currentTime,currentTemp,currentFeelsLike,currentHumidity,currentUV,currentweatherdrisp,currentwinds,currentvisiblity,morningTemp,afternoTemp,eveninTemp,nightTemp,sunrise,sunset,M,E,A,N; //Not in recyclerview
    private static final String TAG = "MainActivity";
    private static final String weatherURL ="https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"; //URL for API
    private static final String APIkey ="JTUH4R2AKMK58R9244DNRE7B9"; //My key
    private RequestQueue queue;
    ImageView weathericon;
    private String TempUnit = "us";
    private RecyclerView recyclerView;

    public String location_tosend,timezone_tosend,resolvedaddress_tosend;
    public JSONArray array_tosend;
    private SwipeRefreshLayout swipeRefreshLayout;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue=Volley.newRequestQueue(this);
        Setlayout();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#D94B371C"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        //check if INTERNET AVAILABLE
        /*
        //I was using this part of code before implementing shared prefernces. After implementing shared preferences, this part of code is not necessary
        if(location==null){
            HourList.removeAll(HourList); //Removing incase if there are any values left in the list
            location="Chicago";
            TempUnit="us";
            getAPIdata(location);
        }*/
    }
    //declaring all the required views
    private void Setlayout() {
        currentTime = findViewById(R.id.current_time_date);
        currentTemp = findViewById(R.id.MainTemp);
        currentFeelsLike = findViewById(R.id.feels_like_temp);
        currentHumidity = findViewById(R.id.humidity);
        currentUV = findViewById(R.id.UVindex);
        currentweatherdrisp = findViewById(R.id.weather_descrip);
        currentwinds = findViewById(R.id.wind_conditions);
        currentvisiblity = findViewById(R.id.visiblity);
        morningTemp = findViewById(R.id.morningTemp);
        afternoTemp = findViewById(R.id.afternoonTemp);
        eveninTemp = findViewById(R.id.eveningTemp);
        nightTemp = findViewById(R.id.nightTemp);
        weathericon=findViewById(R.id.icon_dis);
        sunrise=findViewById(R.id.sunrise_time);
        sunset=findViewById(R.id.sunset_time);
        recyclerView=findViewById(R.id.recyclerViewHour);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        M = findViewById(R.id.morning);
        E = findViewById(R.id.evening);
        A = findViewById(R.id.afternoon);
        N=findViewById(R.id.night);
        swipeRefreshLayout=findViewById(R.id.swiper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(checkconnection()){
                    HourList.removeAll(HourList);
                    getAPIdata(location);
                    Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "No Internet Connection Detected", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        if (TempUnit.equals("us")) {
            menu.findItem(R.id.farheint_opt).setIcon(ContextCompat.getDrawable(this, R.drawable.units_f));
        }
        else{
            menu.findItem(R.id.farheint_opt).setIcon(ContextCompat.getDrawable(this, R.drawable.units_c));
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.location_opt)
        {
            if(checkconnection()){
                enterlocation();
            }else{
                Toast.makeText(this, "No Internet Connection Detected", Toast.LENGTH_LONG).show();
            }

            return true;
        }else if (item.getItemId() == R.id.calender_opt) {
            if(checkconnection())
            {
                Intent intent = new Intent(this,CalenderActivity.class);
                intent.putExtra("days",array_tosend.toString());
                intent.putExtra("location",location_tosend);
                intent.putExtra("Tempunit",TempUnit);
                intent.putExtra("timezone",timezone_tosend);
                intent.putExtra("resolvedAddress",resolvedaddress_tosend);
                startActivity(intent);
            }else{
                Toast.makeText(this, "No Internet Connection Detected", Toast.LENGTH_LONG).show();
            }

            return true;
        } else if (item.getItemId() == R.id.farheint_opt) {
            if(checkconnection()){
                if(TempUnit.equals("metric")){
                    TempUnit="us";
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_f));
                    if(location!=null){
                        HourList.removeAll(HourList);//clearing the list so new list can be added to its place
                        getAPIdata(location);
                    }

                }else {
                    TempUnit="metric";
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_c));
                    if(location!=null){
                        HourList.removeAll(HourList);//clearing the list so new list can be added to its place
                        getAPIdata(location);
                    }
                }
            }else{
                Toast.makeText(this, "No Internet Connection Detected", Toast.LENGTH_SHORT).show();
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void enterlocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText city = new EditText(this);
        city.setInputType(InputType.TYPE_CLASS_TEXT);
        city.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(city);

        builder.setTitle("Enter Location");
        builder.setMessage("Enter as 'City', or 'City,Country'");
        builder.setPositiveButton("OK", (dialog, id) -> {
            location = city.getText().toString();
            getAPIdata(location);

        });

        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getAPIdata(String location) {
        if(checkconnection()){
            setvisible(null);
            if(location.isEmpty()){
                location = "Chicago";
            }

            String url = weatherURL + location;
            Uri.Builder builder =Uri.parse(url).buildUpon(); //creates base and builder to build upon
            builder.appendQueryParameter("unitGroup", TempUnit);
            builder.appendQueryParameter("key", APIkey);
            String finalURL = builder.build().toString();
            Response.Listener<JSONObject> listener=
                    response -> parseJSON(response.toString());
            Response.ErrorListener error =
                    error1 -> {
                        Log.e(TAG, "Failed while hitting API on Response : "+error1);
                    };
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,finalURL,
                    null,listener,error); //takes 4 parameters:1)Request.METHOD.GET/PUT/etc() 2)URL from where we want data 3)Response listener 4)Error listener
            queue.add(jsonObjectRequest);
        }else {
            setinvisible(null);
            Log.d(TAG, "getAPIdata: NO INTERNET");

        }

    }

    public void setvisible(View view) {
        M.setVisibility(View.VISIBLE);
        E.setVisibility(View.VISIBLE);
        A.setVisibility(View.VISIBLE);
        N.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void setinvisible(View view) {

        M.setVisibility(View.INVISIBLE);
        E.setVisibility(View.INVISIBLE);
        A.setVisibility(View.INVISIBLE);
        N.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        currentTime.setText(R.string.no_connection);

    }

    private void parseJSON(String response)// function to store response in a JSON Array &setting values into MainActivity Layout as well as Hours RecycleView
    {

        try {
            JSONObject jObjMain = new JSONObject(response);

            //String address = jObjMain.getString("address");


            String timezone = jObjMain.getString("timezone");
            String resolvedAddress = jObjMain.getString("resolvedAddress");

            // days section
            JSONArray days = jObjMain.getJSONArray("days");

            //hours section
            JSONArray hours = ((JSONObject) days.get(0)).getJSONArray("hours");

            String morningTemp_data = ((JSONObject) hours.get(8)).getString("temp");
            String afternoonTemp_data = ((JSONObject) hours.get(13)).getString("temp");
            String eveningTemp_data = ((JSONObject) hours.get(17)).getString("temp");
            String nightTemp_data = ((JSONObject) hours.get(23)).getString("temp");


            // currentConditions
            JSONObject currentConditions = jObjMain.getJSONObject("currentConditions");


            String currentDateTimeEpoch = currentConditions.getString("datetimeEpoch");
            long date_time = Long.parseLong(currentDateTimeEpoch);
            Date curr_date = new Date(date_time*1000);
            SimpleDateFormat fullDate = new SimpleDateFormat("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
            SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a", Locale.getDefault());
            //SimpleDateFormat dayDate = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
            fullDate.setTimeZone(TimeZone.getTimeZone(timezone));
            timeOnly.setTimeZone(TimeZone.getTimeZone(timezone));
            String fulldate = fullDate.format(curr_date);

            String currentTemp_data = currentConditions.getString("temp");
            String currentFeelsLike_data = currentConditions.getString("feelslike");
            String currentHumidity_data = currentConditions.getString("humidity");


            String currentWindGust_data = currentConditions.getString("windgust");
            String currentWindSpeed_data = currentConditions.getString("windspeed");
            String currentWindDir_data = currentConditions.getString("winddir");
            Double wind_dir = Double.parseDouble(currentWindDir_data);
            String wind_direction = getDirection(wind_dir);
            String Wind =wind_direction+" at "+currentWindSpeed_data+(TempUnit.equals("us") ? " mph " : " kmph ")+" gusting to "+currentWindGust_data+(TempUnit.equals("us") ? " mph " : " kmph ");

            String currentVisibility_data = currentConditions.getString("visibility");
            String currentCloudCover_data = currentConditions.getString("cloudcover");
            String currentUVIndex_data = currentConditions.getString("uvindex");
            String currentConditionDesc_data = currentConditions.getString("conditions");
            String currentIcon_data = currentConditions.getString("icon");
            String currentSunriseEpoch_data = currentConditions.getString("sunriseEpoch");
            String currentSunsetEpoch_data = currentConditions.getString("sunsetEpoch");
            currentIcon_data = currentIcon_data.replace("-","_");
            int iconID =
                    this.getResources().getIdentifier(currentIcon_data, "drawable", this.getPackageName());
            if (iconID == 0) {
                Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + currentIcon_data);
            }


            //Setting values in textviews:
            getSupportActionBar().setTitle(resolvedAddress);
            currentTime.setText(fulldate);
            currentTemp.setText(currentTemp_data+"°"+(TempUnit.equals("us") ? "F" : "C"));
            currentFeelsLike.setText("Feels like: "+currentFeelsLike_data+"°"+(TempUnit.equals("us") ? "F" : "C"));
            currentHumidity.setText("Humidity: "+currentHumidity_data+"%");
            currentvisiblity.setText("Visiblity: "+currentVisibility_data+(TempUnit.equals("us") ? " mi" : " km"));
            currentUV.setText("UV Index: "+currentUVIndex_data);
            morningTemp.setText(morningTemp_data+"°"+(TempUnit.equals("us") ? "F" : "C"));
            afternoTemp.setText(afternoonTemp_data+"°"+(TempUnit.equals("us") ? "F" : "C"));
            eveninTemp.setText(eveningTemp_data+"°"+(TempUnit.equals("us") ? "F" : "C"));
            nightTemp.setText(nightTemp_data+"°"+(TempUnit.equals("us") ? "F" : "C"));
            currentweatherdrisp.setText(currentConditionDesc_data+" ("+currentCloudCover_data+"% clouds)");
            weathericon.setImageResource(iconID);
            if(currentWindGust_data!= "null"){
                currentwinds.setText("Winds: "+Wind);
            }else {
                currentwinds.setText("Winds: "+wind_direction+" at "+currentWindSpeed_data+(TempUnit.equals("us") ? " mph " : " kmph "));
            }
            long sunrisetime = Long.parseLong(currentSunriseEpoch_data);
            Date sunriseTM = new Date(sunrisetime*1000);
            String sunrise_time = timeOnly.format(sunriseTM);

            long sunsetime = Long.parseLong(currentSunsetEpoch_data);
            Date sunsetTM = new Date(sunsetime*1000);
            String sunset_time = timeOnly.format(sunsetTM);
            sunrise.setText("Sunrise: "+sunrise_time);
            sunset.setText("Sunset: "+sunset_time);


            //store values which will be sent to CalenderActivity
            array_tosend = jObjMain.getJSONArray("days");
            location_tosend = location;
            timezone_tosend = timezone;
            resolvedaddress_tosend = resolvedAddress;


            //Preparing & Storing Values in Hourlist --> Hour RecycleView
            SimpleDateFormat H24 =new SimpleDateFormat("HH");
            String H24time = H24.format(curr_date);
            int curr_hour = Integer.parseInt(H24time)+1;

            JSONArray Hourly0 = ((JSONObject) days.get(0)).getJSONArray("hours");
            JSONArray Hourly1 = ((JSONObject) days.get(1)).getJSONArray("hours");
            JSONArray Hourly2 = ((JSONObject) days.get(2)).getJSONArray("hours");
            JSONArray Hourly3 = ((JSONObject) days.get(3)).getJSONArray("hours");


            SimpleDateFormat HTime = new SimpleDateFormat("h:mm a",Locale.getDefault());
            SimpleDateFormat Hday = new SimpleDateFormat("EEEE",Locale.getDefault());
            HTime.setTimeZone(TimeZone.getTimeZone(timezone));
            Hday.setTimeZone(TimeZone.getTimeZone(timezone));

            for(int i=curr_hour;i<Hourly0.length();i++){
                String HourDay ="Today";
                String Hourtime =  ((JSONObject) Hourly0.get(i)).getString("datetimeEpoch");//getting time
                String HourTemp =  ((JSONObject) Hourly0.get(i)).getString("temp");
                String Houricon = ((JSONObject) Hourly0.get(i)).getString("icon");
                String hourlyDesc = ((JSONObject) Hourly0.get(i)).getString("conditions");

                long time = Long.parseLong(Hourtime);
                Date HourDate = new Date(time*1000);

                int Htemp = (int)Double.parseDouble(HourTemp);
                String houricon = Houricon.replace("-", "_");


                String hourlyTime = HTime.format(HourDate);
                String hourlyDay = Hday.format(HourDate);
                String hourlyTemp = Htemp+"°"+(TempUnit.equals("us") ? "F":"C");
                int hourlyIconId = this.getResources().getIdentifier(houricon,"drawable", this.getPackageName());

                if (hourlyIconId == 0) {
                    Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + hourlyIconId);
                }
                HourList.add(new Hours(HourDay,hourlyTime,hourlyIconId,hourlyTemp,hourlyDesc));


            }
            for(int i=0;i<Hourly1.length();i++){
                //String HourDay ="Today";
                String Hourtime =  ((JSONObject) Hourly1.get(i)).getString("datetimeEpoch");//getting time
                String HourTemp =  ((JSONObject) Hourly1.get(i)).getString("temp");
                String Houricon = ((JSONObject) Hourly1.get(i)).getString("icon");
                String hourlyDesc = ((JSONObject) Hourly1.get(i)).getString("conditions");

                long time = Long.parseLong(Hourtime);
                Date HourDate = new Date(time*1000);

                int Htemp = (int)Double.parseDouble(HourTemp);
                String houricon = Houricon.replace("-", "_");


                String hourlyTime = HTime.format(HourDate);
                String hourlyDay = Hday.format(HourDate);
                String hourlyTemp = Htemp+"°"+(TempUnit.equals("us") ? "F":"C");
                int hourlyIconId = this.getResources().getIdentifier(houricon,"drawable", this.getPackageName());

                if (hourlyIconId == 0) {
                    Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + hourlyIconId);
                }
                HourList.add(new Hours(hourlyDay,hourlyTime,hourlyIconId,hourlyTemp,hourlyDesc));


            }

            for(int i=0;i<Hourly2.length();i++){
                //String HourDay ="Today";
                String Hourtime =  ((JSONObject) Hourly2.get(i)).getString("datetimeEpoch");//getting time
                String HourTemp =  ((JSONObject) Hourly2.get(i)).getString("temp");
                String Houricon = ((JSONObject) Hourly2.get(i)).getString("icon");
                String hourlyDesc = ((JSONObject) Hourly2.get(i)).getString("conditions");

                long time = Long.parseLong(Hourtime);
                Date HourDate = new Date(time*1000);

                int Htemp = (int)Double.parseDouble(HourTemp);
                String houricon = Houricon.replace("-", "_");


                String hourlyTime = HTime.format(HourDate);
                String hourlyDay = Hday.format(HourDate);
                String hourlyTemp = Htemp+"°"+(TempUnit.equals("us") ? "F":"C");
                int hourlyIconId = this.getResources().getIdentifier(houricon,"drawable", this.getPackageName());

                if (hourlyIconId == 0) {
                    Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + hourlyIconId);
                }
                HourList.add(new Hours(hourlyDay,hourlyTime,hourlyIconId,hourlyTemp,hourlyDesc));

            }

            for(int i=0;i<Hourly3.length();i++){
                //String HourDay ="Today";
                String Hourtime =  ((JSONObject) Hourly3.get(i)).getString("datetimeEpoch");//getting time
                String HourTemp =  ((JSONObject) Hourly3.get(i)).getString("temp");
                String Houricon = ((JSONObject) Hourly3.get(i)).getString("icon");
                String hourlyDesc = ((JSONObject) Hourly3.get(i)).getString("conditions");

                long time = Long.parseLong(Hourtime);
                Date HourDate = new Date(time*1000);

                int Htemp = (int)Double.parseDouble(HourTemp);
                String houricon = Houricon.replace("-", "_");


                String hourlyTime = HTime.format(HourDate);
                String hourlyDay = Hday.format(HourDate);
                String hourlyTemp = Htemp+"°"+(TempUnit.equals("us") ? "F":"C");
                int hourlyIconId = this.getResources().getIdentifier(houricon,"drawable", this.getPackageName());

                if (hourlyIconId == 0) {
                    Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + hourlyIconId);
                }
                HourList.add(new Hours(hourlyDay,hourlyTime,hourlyIconId,hourlyTemp,hourlyDesc));

            }
            HourAdapter hourAdapter = new HourAdapter(HourList,this);
            recyclerView.setAdapter(hourAdapter);
            swipeRefreshLayout.setRefreshing(false);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }
//Implementing the extra credit part: Storing User Settings
    @Override
    protected void onPause() {
        super.onPause();
        if(location!=null){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref",this.MODE_PRIVATE);
        SharedPreferences.Editor Edit = sharedPreferences.edit();
        Edit.putString("location",location);
        if(TempUnit.equals("us")){
            Edit.putBoolean("tmp",true);
        }else {
            Edit.putBoolean("tmp",false);
        }

            Log.d(TAG, "onSave: Saving location:"+location);
            Log.d(TAG, "onSave: Value of temp unit stored:"+TempUnit);
        Edit.apply();}


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);
        String sp_location = sharedPreferences.getString("location","Chicago");

        boolean unit = sharedPreferences.getBoolean("tmp", Boolean.parseBoolean(""));
        if(unit){
            TempUnit="us";

        }else {
            TempUnit="metric";
        }
        //TempUnit = sp_tempUnit;
        Log.d(TAG, "onSave:Location returned: "+sp_location);
        //Log.d(TAG, "onSave: Value of temp Returned:"+sp_tempUnit+" Value stored in TempUnit: "+TempUnit);
        location=sp_location;
        HourList.removeAll(HourList);
        getAPIdata(location);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(location!=null){
            SharedPreferences sharedPreferences = getSharedPreferences("MyPref",this.MODE_PRIVATE);
            SharedPreferences.Editor Edit = sharedPreferences.edit();
            Edit.putString("location",location);
            if(TempUnit.equals("us")){
                Edit.putBoolean("tmp",true);
            }else {
                Edit.putBoolean("tmp",false);
            }

            Log.d(TAG, "onSave: Saving location:"+location);
            Log.d(TAG, "onSave: Value of temp unit stored:"+TempUnit);
            Edit.apply();}
    }

    @Override
    protected void onStart() {
        super.onStart();
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);
        String sp_location = sharedPreferences.getString("location","");

        Boolean unit = sharedPreferences.getBoolean("tmp", Boolean.parseBoolean("Chicago"));
        if(unit){
            TempUnit="us";

        }else {
            TempUnit="metric";
        }
        //TempUnit = sp_tempUnit;
        Log.d(TAG, "onSave:Location returned: "+sp_location);
        //Log.d(TAG, "onSave: Value of temp Returned:"+sp_tempUnit+" Value stored in TempUnit: "+TempUnit);
        location=sp_location;
        HourList.removeAll(HourList);
        getAPIdata(location);
    }
}

/*

I have called the volley in the main activity, which allowed me to get the resposne from the weather API in the form of a Json Object. I converted this
response to string and used its value to set the values in Text Views present in main activity.
This reponse is then sent to GetAPIdata method, this method converts the data and sets values and texts in TextViews. Later in this method I prepare the
which is required to be sent to the Calender activity.

In order to calculate the proper value for an alpha transparency value you can follow this procedure:

Given a transparency percentage, for example 20%, you know the opaque percentage value is 80% (this is 100-20=80)
The range for the alpha channel is 8 bits (2^8=256), meaning the range goes from 0 to 255.
Project the opaque percentage into the alpha range, that is, multiply the range (255) by the percentage. In this example 255 * 0.8 = 204. Round to the nearest integer if needed.
Convert the value obtained in 3., which is in base 10, to hexadecimal (base 16). You can use Google for this or any calculator. Using Google, type "204 to hexa" and it will give you the hexadecimal value. In this case it is 0xCC.
Prepend the value obtained in 4. to the desired color. For example, for red, which is FF0000, you will have CCFF0000.
 */