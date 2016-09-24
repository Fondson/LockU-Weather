package hackuweather.lockuweather.Weather;

public class Current {

    private String mIcon;
    private String mSummary;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int) Math.round(((mTemperature - 32)*5)/9);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public double getPrecipChance() {
        int mPrecipPercentage = (int) Math.round(mPrecipChance * 100);
        return mPrecipPercentage;
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
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

}
