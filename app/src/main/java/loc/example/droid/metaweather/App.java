package loc.example.droid.metaweather;

import android.app.Application;
import android.os.StrictMode;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        this.setStrictMode();
    }

    protected void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
    }
}