package loc.example.droid.metaweather.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import loc.example.droid.metaweather.R;
import loc.example.droid.metaweather.model.Location;

public class SearchActivity extends AppCompatActivity {

    private static final String EXTRA_LATITUDE = "loc.example.droid.metaweather.extra.LATITUDE";
    private static final String EXTRA_LONGITUDE = "loc.example.droid.metaweather.extra.LONGITUDE";
    private static final String EXTRA_QUERY = "loc.example.droid.metaweather.extra.QUERY";
    private static final String SAVED_TITLE = "SAVED_TITLE";
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Resources res = this.getResources();
        mTitle = res.getString(R.string.location);

        FragmentManager fm = this.getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            Intent i = this.getIntent();
            Double lat = i.getDoubleExtra(EXTRA_LATITUDE, Double.MIN_VALUE);
            Double lng = i.getDoubleExtra(EXTRA_LONGITUDE, Double.MIN_VALUE);
            if ((lat != Double.MIN_VALUE) && (lng != Double.MIN_VALUE)) {
                f = SearchFragment.newInstance(lat, lng);
                mTitle = res.getString(R.string.search_activity_title_latlng,
                        Location.formatLatLng(lat), Location.formatLatLng(lng));
            } else {
                String query = i.getStringExtra(EXTRA_QUERY);
                f = SearchFragment.newInstance(query);
                mTitle = res.getString(R.string.search_activity_title, query);
            }
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
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

    public static Intent newIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    public static Intent newIntent(Context context, double lat, double lng) {
        Intent i = newIntent(context);
        i.putExtra(EXTRA_LATITUDE, lat);
        i.putExtra(EXTRA_LONGITUDE, lng);
        return i;
    }

    public static Intent newIntent(Context context, String query) {
        Intent i = newIntent(context);
        i.putExtra(EXTRA_QUERY, query);
        return i;
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
}
