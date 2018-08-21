package loc.example.droid.metaweather.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import loc.example.droid.metaweather.R;

public class LocationActivity extends AppCompatActivity {

    public static final String EXTRA_WOEID = "loc.example.droid.metaweather.ui.extra.WOEID";
    public static final String EXTRA_LOCATION = "loc.example.droid.metaweather.ui.extra.LOCATION";
    private static final String SAVED_TITLE = "SAVED_TITLE";
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Resources res = this.getResources();
        mTitle = res.getString(R.string.weather);

        FragmentManager fm = this.getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            Intent i = this.getIntent();
            int woeid = i.getIntExtra(EXTRA_WOEID, -1);
            f = LocationFragment.newInstance(woeid);
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();

            String locationName = i.getStringExtra(EXTRA_LOCATION);
            if (!TextUtils.isEmpty(locationName)) {
                mTitle = res.getString(R.string.weather_activity_title, locationName);
            }
        }
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_TITLE, mTitle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTitle = savedInstanceState.getString(SAVED_TITLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LocationActivity.class);
    }
}
