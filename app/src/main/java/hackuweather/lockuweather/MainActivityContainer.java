package hackuweather.lockuweather;

public class MainActivityContainer {
    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        MainActivityContainer.mainActivity = mainActivity;
    }

    static MainActivity mainActivity;


}
