package hackuweather.lockuweather.Weather;

public class Current {

    private String mIcon = "";
    private String mSummary = "";
    private double mTemperature = 0;

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }


    public int getTemperature() {
        return (int) Math.round(((mTemperature - 32)*5)/9);
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
}
