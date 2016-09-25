package hackuweather.lockuweather;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Fondson on 2016-09-24.
 */
public class LocationService extends Service {
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private double mLongitude;
    private double mLatitude;

    private static float LOCATION_REFRESH_DISTANCE = -1;
    private static long LOCATION_REFRESH_TIME = 50;

    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("longlat", Double.toString(mLatitude) + " " + Double.toString(mLongitude));
        try {
            if (MainActivity.hasPermissions(this, MainActivity.NETWORK_PERM)) {
                mLocationListener = new CustomLocationListener();
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, mLocationListener);
            }
        }
        catch (Exception e){
            Log.d("longlat", e.getMessage());
        }
        return START_STICKY;
    }
    public class CustomLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            Log.d("longlat", Double.toString(mLatitude) + " " + Double.toString(mLongitude));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            // display a toast saying gps unavailable
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
            // display a toast saying gps unavailable
        }
    }
}
