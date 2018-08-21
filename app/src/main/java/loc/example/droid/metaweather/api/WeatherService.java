package loc.example.droid.metaweather.api;

import java.util.List;

import loc.example.droid.metaweather.model.Location;
import loc.example.droid.metaweather.model.LocationDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("api/location/search")
    Call<List<Location>> listLocation(@Query("lattlong") String latLng);

    @GET("api/location/search")
    Call<List<Location>> listLocationByQuery(@Query("query") String query);

    @GET("api/location/{woeid}")
    Call<LocationDetail> getLocationDetail(@Path("woeid") int woeid);
}
