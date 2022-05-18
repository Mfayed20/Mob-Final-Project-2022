package com.example.fayed_final_project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.fayed_final_project.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import es.dmoral.toasty.Toasty;

public class WeatherMainActivity extends AppCompatActivity {

    private ImageView weatherIconImageView;
    private TextView temperatureTv, weatherTv, humidityTv, cityTv;
    private EditText weatherEt;
    final private String LOG = "Fayed";
    private String iconUrl;
    private boolean firstTime = true;
    private String city = "Berlin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // change the text of the app bar title
        getSupportActionBar().setTitle("Weather API");

        // add subtitle to the app bar
        getSupportActionBar().setSubtitle("Fayed - Final Project");


        //link graphical items to variables
        temperatureTv = findViewById(R.id.temperatureTv);
        cityTv = findViewById(R.id.cityTv);
        weatherTv = findViewById(R.id.weatherTv);
        humidityTv = findViewById(R.id.humidityTv);
        weatherIconImageView = findViewById(R.id.weatherIconInSQLite);
        weatherEt = findViewById(R.id.weatherEt);
        Button getWeatherBTN = findViewById(R.id.getWeatherBTN);
        Button goToDBActivityBTN = findViewById(R.id.goToFirebaseBTN);
        Button goToStudentListBTN = findViewById(R.id.goToFbListBTN);
        Button goToSQLiteBTN = findViewById(R.id.goToSQLiteBTN);
        Button goToSQLiteListBTN = findViewById(R.id.goToSQLiteListBTN);

        Intent intent = getIntent();
        // check if the intent has items
        if (intent.hasExtra("city")) {
            city = intent.getStringExtra("city");
        }
        generateURL(city);

        // get weather button
        getWeatherBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if the user entered a city
                if (!weatherEt.getText().toString().isEmpty()) {
                    city = weatherEt.getText().toString();
                    generateURL(city);
                } else{
                    Toasty.error(WeatherMainActivity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // go to FirebaseActivity
        goToDBActivityBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send iconUrl to DB activity using Intent
                Intent intent = new Intent(WeatherMainActivity.this, FirebaseActivity.class);
                intent.putExtra("weather", iconUrl);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });

        // FirebaseFullListActivity
        goToStudentListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherMainActivity.this, FirebaseListActivity.class);
                startActivity(intent);
            }
        });

        // SQLiteActivity
        goToSQLiteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherMainActivity.this, SQLiteActivity.class);
                intent.putExtra("weather", iconUrl);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });

        // SQLiteFullListActivity
        goToSQLiteListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(WeatherMainActivity.this, SQLiteListActivity.class);
                 startActivity(intent);
            }
        });
    }

    // create openweathermap URL
    public void generateURL(String city) {
        String key = "ceae73f848981fda58066979faec2f2e";
        // we"ll make HTTP request to this URL to retrieve weather conditions
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric";
        getWeather(url);
    }

    // get weather from openweathermap
    public void getWeather(String url) {
        // get the weather (“clouds”), along with temperature and humidity
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // get weather condition
                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject weatherObj = weather.getJSONObject(0);
                    String weatherCondition = weatherObj.getString("main");
                    // get the temperature
                    JSONObject main = response.getJSONObject("main");
                    String temperature = main.getString("temp");
                    // get the humidity
                    String humidity = main.getString("humidity");
                    // get the city
//                    String city = response.getString("name");
                    city = response.getString("name");
                    // get the icon
                    String iconCode = weatherObj.getString("icon");

                    // set icon URL
                    iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png";

                    // set the weather condition
                    temperatureTv.setText(temperature + "°C");
                    cityTv.setText("City: "+city);
                    weatherTv.setText("Weather: "+weatherCondition);
                    humidityTv.setText("humidity: "+humidity + "%");

                    // set icon
                    Glide.with(WeatherMainActivity.this).load(iconUrl).into(weatherIconImageView);


                    Log.d(LOG+"-Openweathermap", "Response received of City: " + city);
                    Log.d(LOG+"-Openweathermap", response.toString());

                    if (!firstTime){
                        Toasty.success(WeatherMainActivity.this, "City updated: " + city, Toast.LENGTH_SHORT).show();
                    }
                    firstTime = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    // log
                    Log.d(LOG+"-Openweathermap", "Exception: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d(LOG+"-openweathermap-error", "Error retrieving URL " + error.toString());
                Toasty.error(WeatherMainActivity.this, "Invalid city name", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }
}