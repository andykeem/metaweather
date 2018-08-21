package loc.example.droid.metaweather.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import loc.example.droid.metaweather.R;

public class Weather implements Cloneable {

    public static final String DEGREE_CELSIUS = "\u2103";
    public static final String DEGREE_FAHRENHEIT = "\u2109";
    public static final String DATE_FORMAT = "EEE dd MMM";

    private String id;
    @SerializedName("weather_state_name")
    private String weatherStateName;
    @SerializedName("weather_state_abbr")
    private String weatherStateAbbr;
    @SerializedName("wind_direction_compass")
    private String windDirectionCompass;
    private String created;
    @SerializedName("applicable_date")
    private String applicableDate;
    @SerializedName("min_temp")
    private String minTemp;
    @SerializedName("max_temp")
    private String maxTemp;
    @SerializedName("the_temp")
    private String theTemp;
    @SerializedName("wind_speed")
    private String windSpeed;
    @SerializedName("wind_direction")
    private String windDirection;
    @SerializedName("air_pressure")
    private String airPressure;
    private String humidity;
    private String visibility;
    private String predictability;
    private boolean isFahrenheit;
    private Date mCreatedAt;

    public String getWeatherStateName() {
        return weatherStateName;
    }

    public void setWeatherStateName(String weatherStateName) {
        this.weatherStateName = weatherStateName;
    }

    public String getWeatherStateAbbr() {
        return weatherStateAbbr;
    }

    public void setWeatherStateAbbr(String weatherStateAbbr) {
        this.weatherStateAbbr = weatherStateAbbr;
    }

    public String getWindDirectionCompass() {
        return windDirectionCompass;
    }

    public void setWindDirectionCompass(String windDirectionCompass) {
        this.windDirectionCompass = windDirectionCompass;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getApplicableDate() {
        return applicableDate;
    }

    public void setApplicableDate(String applicableDate) {
        this.applicableDate = applicableDate;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getTheTemp() {
        return theTemp;
    }

    public void setTheTemp(String theTemp) {
        this.theTemp = theTemp;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(String airPressure) {
        this.airPressure = airPressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPredictability() {
        return predictability;
    }

    public void setPredictability(String predictability) {
        this.predictability = predictability;
    }

    @Override
    public Weather clone() throws CloneNotSupportedException {
        Weather cloned = (Weather) super.clone();
        return cloned;
    }

    public void setFahrenheit(boolean flag) {
        isFahrenheit = flag;
    }

    public String getFormattedMinTemp() {
        return formatTemp(this.minTemp);
    }

    public String getFormattedMaxTemp() {
        return formatTemp(this.maxTemp);
    }

    public String formatTemp(String temp) {
        Double val = Double.parseDouble(temp);
        int n = val.intValue();
        String degree = DEGREE_CELSIUS;
        if (this.isFahrenheit) {
            n = this.getFahrenheitByCelsius(n);
            degree = DEGREE_FAHRENHEIT;
        }
        String s = String.valueOf(n);
        StringBuilder sb = new StringBuilder(s);
        sb.append(degree);
        return sb.toString();
    }

    public int getFahrenheitByCelsius(int celsius) {
        return ((celsius * 9) / 5) + 32;
    }

    public String getFormattedDate(Context context) {
        String[] parts = this.applicableDate.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = (Integer.parseInt(parts[1]) - 1);
        int date = Integer.parseInt(parts[2]);

        Calendar cal = GregorianCalendar.getInstance();
        if ((date == cal.get(Calendar.DATE)) &&
                (month == cal.get(Calendar.MONTH)) &&
                (year == cal.get(Calendar.YEAR))) {
            return context.getResources().getString(R.string.today);
        }

        cal.set(year, month, date);
        Date d = cal.getTime();

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String val = format.format(d);
        return val;
    }
}
