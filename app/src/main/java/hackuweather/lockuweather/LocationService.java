package hackuweather.lockuweather;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Fondson on 2016-09-24.
 */
public class LocationService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }
}
