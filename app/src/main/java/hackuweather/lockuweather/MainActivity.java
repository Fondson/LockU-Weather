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


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private double mLongitude;
    private double mLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


    private void getForecast(double latitude, double longitude) {
        // initialize the api with key and paramters
        String apiKey = "hackuweather2016";
        String forecastURL = "https://apidev.forecast.io/locations/" + apiKey + "/" +
                latitude + "," + longitude;

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
                        Log.v(TAG, response.body().string());
                        // can't remember what this does
                        if (response.isSuccessful()) {
                            // update the forecast with the details from the
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "COULD NOT CONNECT TO FORECAST API");
                    } catch (JSONException e) {
                        Log.e(TAG, "COULD NOT GET CURRENT WEATHER DATA FROM JSON");
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.toast_unavailable_network_message,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void alertUserAboutError() {
    }

    private void toggleRefresh() {

    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();
        JSONObject jsonForecast = new JSONObject(jsonData);
        forecast.setCurrent(getCurrentDetails(jsonForecast, timezone));
        forecast.setHourlyForecast(getHourlyForecast(jsonForecast, timezone));
        forecast.setDailyForecast(getDailyForecast(jsonForecast, timezone));
        return forecast;
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
}
