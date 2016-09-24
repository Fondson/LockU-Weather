package hackuweather.lockuweather;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextClock lockTimeHr;
    private TextClock lockTimeMin;
    private LinearLayout clockView;
    private ImageView backDrop;
    private FrameLayout overallView;
    private TextView mLongitudeText;
    private TextView mLatitudeText;

    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lockTimeHr = (TextClock) findViewById(R.id.lock_time_hr);
        lockTimeMin = (TextClock) findViewById(R.id.lock_time_min);
        clockView = (LinearLayout) findViewById(R.id.clockView);
        overallView = (FrameLayout) findViewById(R.id.overallView);
        backDrop = (ImageView) findViewById(R.id.weatherBackdrop);
        mLatitudeText = new TextView(this);
        mLongitudeText = new TextView(this);

        mLatitudeText.setTextColor(Color.parseColor("#FFFFFF"));
        mLongitudeText.setTextColor(Color.parseColor("#FFFFFF"));
        mLatitudeText.setText("Lat");
        mLongitudeText.setText("Long");

        clockView.addView(mLatitudeText);
        clockView.addView(mLongitudeText);

        backDrop.setImageResource(R.drawable.default_wallpaper);
        backDrop.setScaleType(ImageView.ScaleType.FIT_XY);

        clockView.bringToFront();

    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
        startService(new Intent(this, UpdateService.class));
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
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    if (x2 > x1)
                    {
                        Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
                    }

                    // Right to left swipe action
                    else
                    {
                        Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                    }

                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}


