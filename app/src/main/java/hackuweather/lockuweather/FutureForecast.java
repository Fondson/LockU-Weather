package hackuweather.lockuweather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import hackuweather.lockuweather.Weather.Day;
import hackuweather.lockuweather.Weather.Forecast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FutureForecast extends AppCompatActivity {

    private String APIKEY;
    static final int MIN_DISTANCE = 150;
    private float x1, x2;
    private FrameLayout futureView;
    private ImageView[] dayIcons = new ImageView[5];
    private TextView[] dayNames = new TextView[5];
    private TextView[] dayTemps = new TextView[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_future_forecast);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        futureView = (FrameLayout) findViewById(R.id.futureView);
        Forecast mForecast = new Forecast();
        Intent intent = getIntent();
        APIKEY = intent.getStringExtra("APIKEY"); //if it's a string you stored.

        dayIcons[0] = (ImageView) findViewById(R.id.weatherIconDay1);

        dayIcons[0].setImageResource(mForecast.getIconId(MainActivity.fiveDayForecast[0].getIconId()));
        dayIcons[1] = (ImageView) findViewById(R.id.weatherIconDay2);
        dayIcons[1].setImageResource(mForecast.getIconId(MainActivity.fiveDayForecast[1].getIconId()));
        dayIcons[2] = (ImageView) findViewById(R.id.weatherIconDay3);
        dayIcons[2].setImageResource(mForecast.getIconId(MainActivity.fiveDayForecast[2].getIconId()));
        dayIcons[3] = (ImageView) findViewById(R.id.weatherIconDay4);
        dayIcons[3].setImageResource(mForecast.getIconId(MainActivity.fiveDayForecast[3].getIconId()));
        dayIcons[4] = (ImageView) findViewById(R.id.weatherIconDay5);
        dayIcons[4].setImageResource(mForecast.getIconId(MainActivity.fiveDayForecast[4].getIconId()));




//        currentTemp.setText("" + mForecast.getCurrent().getTemperature() + "°C");
//        backDrop.setImageBitmap(mForecast.getCurrent().getPhotoBitmap());
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            futureView.setSystemUiVisibility(
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
                    if (x1 > x2) {
                        Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(this, MainActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        this.startActivity(myIntent);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
