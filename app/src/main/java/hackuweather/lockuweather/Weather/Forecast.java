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

        public static int getIconId(int iconString) {
            int iconId = R.drawable.weather_icon1;

            if (iconString <=2 || iconString == 30) {
                iconId = R.drawable.weather_icon1;
            }
            else if ((iconString) >= 3 && (iconString) <= 6) {
                iconId = R.drawable.weather_icon4;
            }
            else if ((iconString) >= 7 && (iconString) <= 8 || (iconString) == 32) {
                iconId = R.drawable.weather_icon3;
            }
            else if ((iconString) >= 9 && (iconString) <= 11 || (iconString) == 31) {
                iconId = R.drawable.weather_icon7;
            }
            else if ((iconString) == 12 || (iconString) == 18 || (iconString) == 39 ||
                    (iconString) == 40) {
                iconId = R.drawable.weather_icon5;
            }
            else if ((iconString) >= 13 && (iconString) <= 17 && (iconString) != 15) {
                iconId = R.drawable.weather_icon2;
            }
            else if ((iconString) == 15 || (iconString) == 41 || (iconString) == 42) {
                iconId = R.drawable.weather_icon9;
            }
            else if ((iconString) >= 19 && (iconString) <= 25 || (iconString) == 43 ||
                    (iconString) == 44) {
                iconId = R.drawable.weather_icon10;
            }
            else if ((iconString) >= 26 && (iconString) <= 29) {
                iconId = R.drawable.weather_icon11;
            }
            else if ((iconString) >= 33 && (iconString) <= 38) {
                iconId = R.drawable.weather_icon8;
            }
            return iconId;
        }

}
