package loc.example.droid.metaweather.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import loc.example.droid.metaweather.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOCATION_REQUEST = 0;
    private static final int LAST_LOCATION_REQUEST = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private MainFragment mFragment;
    private Location mLocation;
    private LocationRequest mLocReq;
    private FusedLocationProviderClient mLocClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create location request
        this.createLocationRequest();
        mLocClient = LocationServices.getFusedLocationProviderClient(this);

        FragmentManager fm = this.getSupportFragmentManager();
        mFragment = (MainFragment) fm.findFragmentById(R.id.fragment_container);
        if (mFragment == null) {
            mFragment = MainFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            Log.d(TAG, "resultCode: " + resultCode);
            Log.d(TAG, "intent: " + data);
            if (resultCode == Activity.RESULT_OK) {
                this.getLastLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.createLocationRequest();
            }
        } else if (requestCode == LAST_LOCATION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.getLastLocation();
            }
        }
    }

    /**
     * explains the user why s/he needs location permission
     *
     * @param reqCode
     */
    @TargetApi(23)
    protected void handleDeniedLocationPermission(int reqCode) {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            String text = this.getResources().getString(R.string.perm_location);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, reqCode);
    }

    /**
     * requests current location
     */
    @TargetApi(23)
    protected void createLocationRequest() {
        boolean canProceed = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int perm = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (perm == PackageManager.PERMISSION_GRANTED) {
                canProceed = true;
            } else {
                this.handleDeniedLocationPermission(LOCATION_REQUEST);
            }
        } else { // older than Marshmallow
            canProceed = true;
        }
        if (!canProceed) {
            return;
        }
        mLocReq = new LocationRequest();
        mLocReq.setInterval(10 * 1000L);
        mLocReq.setFastestInterval(5 * 1000L);
        mLocReq.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocReq);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                LocationSettingsStates states = locationSettingsResponse.getLocationSettingsStates();
                getLastLocation();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage(), e);
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sie) {
                        Log.e(TAG, sie.getMessage(), sie);
                    }
                }
            }
        });
    }

    /**
     * fetches the last location
     */
    @TargetApi(23)
    protected void getLastLocation() {
        boolean canProceed = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int perm = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (perm == PackageManager.PERMISSION_GRANTED) {
                canProceed = true;
            } else {
                this.handleDeniedLocationPermission(LAST_LOCATION_REQUEST);
            }
        } else { // older than Marshmallow
            canProceed = true;
        }
        if (!canProceed) {
            return;
        }
        mLocClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        mLocation = location;
                        updateUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                });
    }

    protected void updateUI() {
        if (mLocation == null) return;
        /*String text = String.format("lat: %f, lng: %f", mLocation.getLatitude(),
                mLocation.getLongitude());
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();*/
        mFragment.updateLocation(mLocation);
    }
}
