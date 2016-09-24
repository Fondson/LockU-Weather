package hackuweather.lockuweather.Weather;

public class Current {

    private int mIcon = 0;
    private String mSummary = "";
    private double mTemperature = 0;
    private String mPhotoUrl;

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

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }
}
