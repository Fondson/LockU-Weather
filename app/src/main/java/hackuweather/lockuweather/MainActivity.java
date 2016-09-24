package hackuweather.lockuweather;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import hackuweather.lockuweather.Weather.Current;
import hackuweather.lockuweather.Weather.Forecast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static float LOCATION_REFRESH_DISTANCE = 100;
    private static long LOCATION_REFRESH_TIME = 2000;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Forecast mForecast;
    private static final int NETWORK_PERM_CODE = 0;
    private String[] NETWORK_PERM = {"android.permission.ACCESS_NETWORK_STATE","android.permission.INTERNET"};

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private double mLongitude;
    private double mLatitude;

    OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M
                && checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE")!= PackageManager.PERMISSION_GRANTED
                && checkCallingOrSelfPermission("android.permission.INTERNET")!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(NETWORK_PERM, NETWORK_PERM_CODE);
        }
        mForecast = new Forecast();
        getCurrent();
        // initialize a location listener
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                //your code here
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                // display a toast saying gps unavailable
            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                // display a toast saying gps unavailable
            }
        };
    }

    private void getCurrent() {
        // initialize the api with key and paramters
        String apiKey = "hackuweather2016";
        String forecastURL = "http://apidev.accuweather.com/currentconditions/v1/"+ "335315" + ".json?apikey=" + apiKey;

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
                            Current current = getCurrentDetails(jsonData.substring(1, jsonData.length() -1));
                            mForecast.setCurrent(current);
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

    private Current getCurrentDetails(String jsonData) {
        Current currentWeather = new Current();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            // get the weather icon
            int iconID = jsonObject.getInt("WeatherIcon");
            currentWeather.setIcon(Integer.toString(iconID));
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



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, Boolean.toString(currentWeather == null));
        return currentWeather;
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

    private void initalizeLocationManger() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        };
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
    }

    //handles permission requests
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions,int[] grantResults){
        switch (permsRequestCode){
            case NETWORK_PERM_CODE:
                if (grantResults.length>=0
                        && grantResults[0]==PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }
    }
}
