package hackuweather.lockuweather.Weather;

import hackuweather.lockuweather.R;

public class Forecast {

        private Current mCurrent = new Current();
        private Hour[] mHourlyForecast;
        private Day[] mDailyForecast;

        public Current getCurrent() {
            return mCurrent;
        }

        public void setCurrent(Current current) {
            mCurrent = current;
        }

        public Hour[] getHourlyForecast() {
            return mHourlyForecast;
        }

        public void setHourlyForecast(Hour[] hourlyForecast) {
            mHourlyForecast = hourlyForecast;
        }

        public Day[] getDailyForecast() {
            return mDailyForecast;
        }

        public void setDailyForecast(Day[] dailyForecast) {
            mDailyForecast = dailyForecast;
        }

        public static int getIconId(String iconString) {
            int iconId = R.drawable.weather_icon1;

            if (Integer.parseInt(iconString) <=2 || Integer.parseInt(iconString) == 30) {
                iconId = R.drawable.weather_icon1;
            }
            else if (Integer.parseInt(iconString) >= 3 && Integer.parseInt(iconString) <= 6) {
                iconId = R.drawable.weather_icon4;
            }
            else if (Integer.parseInt(iconString) >= 7 && Integer.parseInt(iconString) <= 8 ||
                    Integer.parseInt(iconString) == 32) {
                iconId = R.drawable.weather_icon3;
            }
            else if (Integer.parseInt(iconString) >= 9 && Integer.parseInt(iconString) <= 11 ||
                    Integer.parseInt(iconString) == 31) {
                iconId = R.drawable.weather_icon7;
            }
            else if (Integer.parseInt(iconString) == 12 || Integer.parseInt(iconString) == 18 ||
                    Integer.parseInt(iconString) == 39 || Integer.parseInt(iconString) == 40) {
                iconId = R.drawable.weather_icon5;
            }
            else if (Integer.parseInt(iconString) >= 13 && Integer.parseInt(iconString) <= 17 &&
                    Integer.parseInt(iconString) != 15) {
                iconId = R.drawable.weather_icon2;
            }
            else if (Integer.parseInt(iconString) == 15 || Integer.parseInt(iconString) == 41 ||
                    Integer.parseInt(iconString) == 42) {
                iconId = R.drawable.weather_icon9;
            }
            else if (Integer.parseInt(iconString) >= 19 && Integer.parseInt(iconString) <= 25 ||
                    Integer.parseInt(iconString) == 43 || Integer.parseInt(iconString) == 44) {
                iconId = R.drawable.weather_icon10;
            }
            else if (Integer.parseInt(iconString) >= 26 && Integer.parseInt(iconString) <= 29) {
                iconId = R.drawable.weather_icon11;
            }
            else if (Integer.parseInt(iconString) >= 33 && Integer.parseInt(iconString) <= 38) {
                iconId = R.drawable.weather_icon8;
            }
            return iconId;
        }

}
