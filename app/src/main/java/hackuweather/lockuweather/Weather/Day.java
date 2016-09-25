package hackuweather.lockuweather.Weather;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Day {

    private long mTime;
    private String mSummary;
    private double mTemperatureMax;
    private int mIcon;
    private String photoURL;

    public Day () {

    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperatureMax() {
        return (int) Math.round(((mTemperatureMax - 32)*5)/9);
    }

    public void setTemperatureMax(double temperatureMax) {
        mTemperatureMax = temperatureMax;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }


    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public String getDayOfTheWeek() {
        Date dateTime = new Date(mTime * 1000);
        SimpleDateFormat df = new SimpleDateFormat("EEEE");
        String date = df.format(dateTime);
        Log.d("Day", date);
        return date;
    }

}
