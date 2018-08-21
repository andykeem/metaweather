package loc.example.droid.metaweather.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LocationDetail {

    private static final String TAG = LocationDetail.class.getSimpleName();

    @SerializedName("consolidated_weather")
    private List<Weather> consolidatedWeather = new ArrayList<>();
    private String time;
    @SerializedName("sun_rise")
    private String sunRise;
    @SerializedName("timezone_name")
    private String timezoneName;
    private List<Weather> fahrenheitWeathers = new ArrayList<>();

    public List<Weather> getConsolidatedWeather() {
        return consolidatedWeather;
    }

    public void setConsolidatedWeather(List<Weather> consolidatedWeather) {
        this.consolidatedWeather = consolidatedWeather;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSunRise() {
        return sunRise;
    }

    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }

    public String getTimezoneName() {
        return timezoneName;
    }

    public void setTimezoneName(String timezoneName) {
        this.timezoneName = timezoneName;
    }

    /**
     * clones default celsius wether info then updates only min and max temperatures
     * TODO: may need to update other instance variables if we needed
     *
     * @return
     */
    public List<Weather> getFahrenheitWeathers() {
        if (fahrenheitWeathers.isEmpty()) {
            for (Weather weather : consolidatedWeather) {
                Weather fah = new Weather();
                try {
                    fah = (Weather) weather.clone();
                } catch (CloneNotSupportedException cnse) {
                    Log.e(TAG, cnse.getMessage(), cnse);
                }
                fah.setFahrenheit(true);
                fahrenheitWeathers.add(fah);
            }
        }
        return fahrenheitWeathers;
    }
}
