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
    private float y1, y2;
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
        futureView.setBackgroundResource(R.drawable.gradient_backdrop);
        Forecast mForecast = new Forecast();
        Intent intent = getIntent();
        APIKEY = intent.getStringExtra("APIKEY"); //if it's a string you stored.

        if(MainActivity.fiveDayForecast[0] != null) {
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

            dayTemps[0] = (TextView) findViewById(R.id.weatherTextDay1);
            dayTemps[0].setText((MainActivity.fiveDayForecast[0].getTemperatureMax()) + "°C");
            dayTemps[1] = (TextView) findViewById(R.id.weatherTextDay2);
            dayTemps[1].setText((MainActivity.fiveDayForecast[1].getTemperatureMax()) + "°C");
            dayTemps[2] = (TextView) findViewById(R.id.weatherTextDay3);
            dayTemps[2].setText((MainActivity.fiveDayForecast[2].getTemperatureMax()) + "°C");
            dayTemps[3] = (TextView) findViewById(R.id.weatherTextDay4);
            dayTemps[3].setText((MainActivity.fiveDayForecast[3].getTemperatureMax()) + "°C");
            dayTemps[4] = (TextView) findViewById(R.id.weatherTextDay5);
            dayTemps[4].setText((MainActivity.fiveDayForecast[4].getTemperatureMax()) + "°C");

            dayNames[0] = (TextView) findViewById(R.id.weatherNameDay1);
            dayNames[0].setText((MainActivity.fiveDayForecast[0].getDayOfTheWeek()));
            dayNames[1] = (TextView) findViewById(R.id.weatherNameDay2);
            dayNames[1].setText((MainActivity.fiveDayForecast[1].getDayOfTheWeek()));
            dayNames[2] = (TextView) findViewById(R.id.weatherNameDay3);
            dayNames[2].setText((MainActivity.fiveDayForecast[2].getDayOfTheWeek()));
            dayNames[3] = (TextView) findViewById(R.id.weatherNameDay4);
            dayNames[3].setText((MainActivity.fiveDayForecast[3].getDayOfTheWeek()));
            dayNames[4] = (TextView) findViewById(R.id.weatherNameDay5);
            dayNames[4].setText((MainActivity.fiveDayForecast[4].getDayOfTheWeek()));
        }





//        currentTemp.setText("" + mForecast.getCurrent().getTemperature() + "°C");
//        backDrop.setImageBitmap(mForecast.getCurrent().getPhotoBitmap());
    }


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            futureView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                y2 = event.getY();
                float deltaY = y2 - y1;

                if (Math.abs(deltaY) > MIN_DISTANCE) {
                    // Left to Right swipe action
                    if (y1 < y2) {
                        Intent myIntent = new Intent(this, MainActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        this.startActivity(myIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
