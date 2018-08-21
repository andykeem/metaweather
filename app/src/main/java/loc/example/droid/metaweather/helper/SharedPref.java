package loc.example.droid.metaweather.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {
    private static final String PREF_SHOW_FAHRENHEIT = "PREF_SHOW_FAHRENHEIT";

    public static SharedPreferences getSharedPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setShowFahrenheit(Context context, boolean showFahrenheit) {
        SharedPreferences sharedPref = getSharedPref(context);
        sharedPref.edit().putBoolean(PREF_SHOW_FAHRENHEIT, showFahrenheit).apply();
    }

    public static boolean getShowFahrenheit(Context context) {
        SharedPreferences sharedPref = getSharedPref(context);
        return sharedPref.getBoolean(PREF_SHOW_FAHRENHEIT, false);
    }
}
