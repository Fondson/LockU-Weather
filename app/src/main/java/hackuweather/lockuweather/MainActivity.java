package hackuweather.lockuweather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hackuweather.lockuweather.Weather.Current;
import hackuweather.lockuweather.Weather.Day;
import hackuweather.lockuweather.Weather.Forecast;
import hackuweather.lockuweather.model.SlidrConfig;
import hackuweather.lockuweather.model.SlidrInterface;
import hackuweather.lockuweather.model.SlidrPosition;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private TextClock lockTimeHr;
    private TextClock lockTimeMin;
    private LinearLayout clockView;
    private ImageView backDrop;
    private ImageView weatherIcon;
    private static FrameLayout overallView;
    private TextView currentTemp;
    static String APIKEY = "HackuWeather2016";
    private String mLocationKey = "335315";
    private OkHttpClient okHttpClient = new OkHttpClient();
    private float x1, x2;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Forecast mForecast;
    private static final int NETWORK_PERM_CODE = 0;
    static final int MIN_DISTANCE = 150;
    public static String[] NETWORK_PERM = {"android.permission.ACCESS_NETWORK_STATE"
            , "android.permission.INTERNET"
            , "android.permission.ACCESS_FINE_LOCATION"
            , "android.permission.ACCESS_COARSE_LOCATION"};
    private SlidrConfig config;
    private SlidrInterface slidrInterface;
    private double mLatitude = 43.6532;
    private double mLongitude = 79.3832;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityContainer.setMainActivity(this);
        setContentView(R.layout.activity_main);

        lockTimeHr = (TextClock) findViewById(R.id.lock_time_hr);
        lockTimeMin = (TextClock) findViewById(R.id.lock_time_min);
        clockView = (LinearLayout) findViewById(R.id.clockView);
        overallView = (FrameLayout) findViewById(R.id.overallView);
        backDrop = (ImageView) findViewById(R.id.weatherBackdrop);
        currentTemp = (TextView) findViewById(R.id.weatherText);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);

        backDrop.setImageResource(R.drawable.default_wallpaper);
        backDrop.setScaleType(ImageView.ScaleType.FIT_XY);
        clockView.bringToFront();

        config = new SlidrConfig.Builder()
                .position(SlidrPosition.HORIZONTAL)
                .sensitivity(0.75f)
                .scrimColor(Color.TRANSPARENT)
                .scrimStartAlpha(1f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edgeSize(1.0f) // The % of the screen that counts as the edge, default 18%
                .build();

        slidrInterface=Slidr.attach(this, config);

        initializeLocationManager();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
        startService(new Intent(this, UpdateService.class));
    }

    public static FrameLayout getBackground(){
        return overallView;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            overallView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show();
                    }

                    // Right to left swipe action
                    else {
                        Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    private void initializeLocationManager(){

        mForecast = new Forecast();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !hasPermissions(this,NETWORK_PERM)) {
            requestPermissions(NETWORK_PERM, NETWORK_PERM_CODE);
        }
        else {
            getKey();
            getDays();
            getCurrent();
            // initialize a location listener
            startService(new Intent(this, LocationService.class));
        }
    }

    public void getCurrent() {
        // initialize the api with key and parameters
        String apiKey = "hackuweather2016";
        String forecastURL = "http://apidev.accuweather.com/currentconditions/v1/"+ mLocationKey + ".json?apikey=" + APIKEY
                + "&getPhotos=true";

        if (isNetworkAvailable()) {
            // display refresh animation (i.e. make the animation spin)
            toggleRefresh();
            // connect to the api using ok http
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(forecastURL).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // turn the animation off
                            toggleRefresh();
                        }
                    });
                    // display an error message to the user
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // if a valid response is relieved
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // turn off the refresh animation
                            toggleRefresh();
                        }
                    });
                    try {
                        // store the data from the response
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        // can't remember what this does
                        if (response.isSuccessful()) {
                            // update the forecast with the details from the
                            Current current = getCurrentDetails(jsonData.substring(1, jsonData.length() - 1));
                            mForecast.setCurrent(current);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        updateDisplay();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "COULD NOT CONNECT TO FORECAST API");
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.toast_unavailable_network_message,
                    Toast.LENGTH_LONG).show();
        }
    }


    public void getKey() {
        // initialize the api with key and parameters
        String forecastURL = "\n" +
                "http://api.accuweather.com/locations/v1/cities/geoposition/search.json?q=" + mLatitude +
                "," + mLongitude + "&apikey=" + APIKEY;

        if (isNetworkAvailable()) {
            // display refresh animation (i.e. make the animation spin)
            toggleRefresh();
            // connect to the api using ok http
            Request request = new Request.Builder().url(forecastURL).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // turn the animation off
                            toggleRefresh();
                        }
                    });
                    // display an error message to the user
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // if a valid response is relieved
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // turn off the refresh animation
                            toggleRefresh();
                        }
                    });
                    try {
                        // store the data from the response
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        // can't remember what this does
                        if (response.isSuccessful()) {
                            mLocationKey = getLocationKey(jsonData);
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "COULD NOT CONNECT TO FORECAST API");
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.toast_unavailable_network_message,
                    Toast.LENGTH_LONG).show();
        }
    }

    private String getLocationKey(String jsonData) {
        String key = "3380050";
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            key = jsonObject.getString("Key");
            Log.d(TAG, key);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return key;
    }

    private void updateDisplay() throws IOException {
        currentTemp.setText("" + mForecast.getCurrent().getTemperature() + "°C");
        weatherIcon.setImageResource(mForecast.getCurrent().getIconId());
        backDrop.setImageBitmap(mForecast.getCurrent().getPhotoBitmap());
    }

    private Current getCurrentDetails(String jsonData) {
        Current currentWeather = new Current();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            // get the weather icon
            int iconID = jsonObject.getInt("WeatherIcon");
            currentWeather.setIcon(iconID);
            Log.d(TAG, Integer.toString(iconID));

            // get the summary
            String summary = jsonObject.getString("WeatherText");
            currentWeather.setSummary(summary);
            Log.d(TAG, summary);

            JSONObject tempJson = jsonObject.getJSONObject("Temperature");
            JSONObject metricJson = tempJson.getJSONObject("Metric");

            //get the currentWeather temp
            int currentTemp = metricJson.getInt("Value");
            currentWeather.setTemperature(currentTemp);
            Log.d("currentWeather", Integer.toString(currentTemp));

            JSONArray photos  = jsonObject.getJSONArray("Photos");
            JSONObject photo = photos.getJSONObject(0);

            // get the photo url
            String pictureURL = photo.getString("PortraitLink");
            currentWeather.setPhotoUrl(pictureURL);
            Bitmap bitmap = getBitmapFromURL(pictureURL);
            currentWeather.setPhotoBitmap(bitmap);
            Log.d(TAG, pictureURL);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, Boolean.toString(currentWeather == null));
        return currentWeather;
    }


    public void getDays() {
        // initialize the api with key and paramters
        String forecastURL = "http://apidev.accuweather.com/forecasts/v1/daily/5day/"+ mLocationKey + "?apikey=" + APIKEY;

        if (isNetworkAvailable()) {
            // display refresh animation (i.e. make the animation spin)
            toggleRefresh();
            // connect to the api using ok http

            Request request = new Request.Builder().url(forecastURL).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // turn the animation off
                            toggleRefresh();
                        }
                    });
                    // display an error message to the user
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // if a valid response is relieved
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // turn off the refresh animation
                            toggleRefresh();
                        }
                    });
                    try {
                        // store the data from the response
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        // can't remember what this does
                        if (response.isSuccessful()) {
                            // update the forecast with the details from the
                            Day[] days = getDaysDetails(jsonData);
                            mForecast.setDailyForecast(days);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "COULD NOT CONNECT TO FORECAST API");
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.toast_unavailable_network_message,
                    Toast.LENGTH_LONG).show();
        }
    }

    private Day[] getDaysDetails(String jsonData) {
        Day[] days = new Day[5];
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray dailyForecasts = jsonObject.getJSONArray("DailyForecasts");
            for (int i = 0; i < 5; i++) {
                Day day = new Day();
                JSONObject dayJSON = dailyForecasts.getJSONObject(i);

                // set the time of the forecast
                int time = dayJSON.getInt("EpochDate");
                day.setTime(time);
                Log.d(TAG, Integer.toString(time));

                JSONObject jsonTemperature = dayJSON.getJSONObject("Temperature").
                        getJSONObject("Maximum");

                int maxTemperature = jsonTemperature.getInt("Value");
                day.setTemperatureMax(maxTemperature);
                Log.d(TAG, Integer.toString(maxTemperature));

                JSONObject jsonDayObject = dayJSON.getJSONObject("Day");

                int iconId = jsonDayObject.getInt("Icon");
                day.setIcon(iconId);
                Log.d(TAG, Integer.toString(iconId));

                String summary = jsonDayObject.getString("IconPhrase");
                day.setSummary(summary);

                days[i] = day;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return days;
    }

    private void alertUserAboutError() {
    }

    private void toggleRefresh() {

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    protected void onResume(){
        (findViewById(R.id.slidable_content)).setAlpha(1f);
        super.onResume();
    }

    //handles permission requests
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case NETWORK_PERM_CODE:
                if (grantResults.length >= 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getKey();
                    getDays();
                    getCurrent();
                    // initialize a location listener
                    startService(new Intent(this, LocationService.class));
                }
                break;
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }
}
