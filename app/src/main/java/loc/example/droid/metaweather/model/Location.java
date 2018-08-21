package loc.example.droid.metaweather.model;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class Location {
    private int distance;
    private String title;
    @SerializedName("location_type")
    private String type;
    private int woeid;
    @SerializedName("latt_long")
    private String latLng;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWoeid() {
        return woeid;
    }

    public void setWoeid(int woeid) {
        this.woeid = woeid;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public static String formatLatLng(Double val) {
        return new DecimalFormat("#00.0000").format(val);
    }
}
