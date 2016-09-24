package hackuweather.lockuweather.Weather;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Hour {
        private Long mTime;
        private String mSummary;
        private double mTemperature;
        private String mIcon;
        private String mTimezone;

        public Hour() {}


        public Long getTime() {
            return mTime;
        }

        public void setTime(Long time) {
            mTime = time;
        }

        public String getSummary() {
            return mSummary;
        }

        public void setSummary(String summary) {
            mSummary = summary;
        }

        public double getTemperature() {
            return (int) Math.round(((mTemperature - 32)*5)/9);
        }

        public int getIconId() {
            return Forecast.getIconId(mIcon);
        }

        public void setTemperature(double temperature) {
            mTemperature = temperature;
        }

        public String getIcon() {
            return mIcon;
        }

        public void setIcon(String icon) {
            mIcon = icon;
        }

        public String getTimezone() {
            return mTimezone;
        }

        public void setTimezone(String timezone) {
            mTimezone = timezone;
        }

        public String getHour () {
            SimpleDateFormat formatter = new SimpleDateFormat("h a");
            Date date = new Date(mTime * 1000);
            return formatter.format(date);
        }


}
