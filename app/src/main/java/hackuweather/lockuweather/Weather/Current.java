package hackuweather.lockuweather.Weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Current {

    private int mIcon = 0;
    private String mSummary = "";
    private double mTemperature = 0;
    private String mPhotoUrl;
    private Bitmap mPhotoBitmap;
    private String mLocation = "";

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }


    public int getTemperature() {
        return (int) mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }


    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }


    public Current() {}

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public Bitmap getPhotoBitmap() {
        return mPhotoBitmap;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public void setLocation(String location){ mLocation  = location; }

    public String getLocation(){ return mLocation; }

    public void setPhotoBitmap(Bitmap photoBitmap){
        mPhotoBitmap = photoBitmap;
    }
}
