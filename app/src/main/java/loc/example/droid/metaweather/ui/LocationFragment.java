package loc.example.droid.metaweather.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import loc.example.droid.metaweather.R;
import loc.example.droid.metaweather.api.WeatherApi;
import loc.example.droid.metaweather.helper.SharedPref;
import loc.example.droid.metaweather.model.LocationDetail;
import loc.example.droid.metaweather.model.Weather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    private static final String ARG_WOEID = "ARG_WOEID";
    private static final String SAVED_WOEID = "SAVED_WOEID";
    private int mWoeid;
    private LocationDetail mLocationDetail;
    private RecyclerView mRvWeatherList;
    private Context mContext;
    private String mPackageName;
    private Resources mResources;
    private WeatherListAdapter mListAdapter;
    private List<Weather> mWeathers = new ArrayList<>();
    private List<Weather> mFarWeathers = new ArrayList<>();
    private boolean mShowFahrenheit;
    private String mStrCelsius;
    private String mStrFahrenheit;
    private Call<LocationDetail> mApiCall;

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        if (this.getArguments() != null) {
            mWoeid = this.getArguments().getInt(ARG_WOEID);
            fetchLocationDetail();
        }
        mContext = this.getContext();
        mPackageName = mContext.getPackageName();
        mResources = this.getResources();
        mStrCelsius = mResources.getString(R.string.celsius);
        mStrFahrenheit = mResources.getString(R.string.fahrenheit);
        this.setHasOptionsMenu(true);
        mShowFahrenheit = SharedPref.getShowFahrenheit(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_location, container, false);
        mRvWeatherList = v.findViewById(R.id.rv_weather_list);
        mRvWeatherList.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            this.updateUI();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_WOEID, mWoeid);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_weather, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_item_weather_toggle);
        if (mShowFahrenheit) {
            item.setTitle(mStrCelsius);
        } else {
            item.setTitle(mStrFahrenheit);
        }
    }

    /**
     * toggles weather menu item between C and F degrees
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_weather_toggle:
                mShowFahrenheit = !mShowFahrenheit;
                if (mShowFahrenheit) {
                    mFarWeathers = this.getFahrenheitWeathers();
                    mListAdapter.setData(mFarWeathers);
                } else {
                    mListAdapter.setData(mWeathers);
                }
                mListAdapter.notifyDataSetChanged();
                getActivity().invalidateOptionsMenu();
                SharedPref.setShowFahrenheit(mContext, mShowFahrenheit);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mApiCall != null) {
            mApiCall.cancel();
        }
    }

    public static LocationFragment newInstance(int woeid) {
        Bundle args = new Bundle();
        args.putInt(ARG_WOEID, woeid);
        LocationFragment lf = new LocationFragment();
        lf.setArguments(args);
        return lf;
    }

    protected List<Weather> getFahrenheitWeathers() {
        if (mFarWeathers.isEmpty()) {
            mFarWeathers = mLocationDetail.getFahrenheitWeathers();
        }
        return mFarWeathers;
    }

    private void updateUI() {
        if ((mLocationDetail == null) || (mWeathers == null)) return;
        if (mWeathers.isEmpty()) return;
        if (mShowFahrenheit) {
            mFarWeathers = this.getFahrenheitWeathers();
            mListAdapter = new WeatherListAdapter(mFarWeathers);
        } else {
            mListAdapter = new WeatherListAdapter(mWeathers);
        }
        mRvWeatherList.setHasFixedSize(true);
        mRvWeatherList.setAdapter(mListAdapter);
    }

    /**
     * makes location weather api request based on woeid, then updates UI
     */
    protected void fetchLocationDetail() {
        mApiCall = WeatherApi.getWeatherService().getLocationDetail(mWoeid);
        mApiCall.enqueue(new Callback<LocationDetail>() {
            @Override
            public void onResponse(Call<LocationDetail> call, Response<LocationDetail> response) {
                mLocationDetail = response.body();
                mWeathers = mLocationDetail.getConsolidatedWeather();
                updateUI();
            }

            @Override
            public void onFailure(Call<LocationDetail> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        });
    }

    private class WeatherVH extends RecyclerView.ViewHolder {

        private Weather mItem;
        private TextView mTvDate;
        private ImageView mIvIcon;
        private TextView mTvState;
        private TextView mTvMinTemp;
        private TextView mTvMaxTemp;

        public WeatherVH(View itemView) {
            super(itemView);
            mTvDate = itemView.findViewById(R.id.tv_date);
            mIvIcon = itemView.findViewById(R.id.iv_icon);
            mTvState = itemView.findViewById(R.id.tv_state);
            mTvMinTemp = itemView.findViewById(R.id.tv_min_temp);
            mTvMaxTemp = itemView.findViewById(R.id.tv_max_temp);
        }

        public void bindItem(Weather item) {
            mItem = item;
            mTvDate.setText(mItem.getFormattedDate(mContext));
            int drawableResId = mResources.getIdentifier(mItem.getWeatherStateAbbr(),
                    "drawable", mPackageName);
            mIvIcon.setImageDrawable(mResources.getDrawable(drawableResId));
            mTvState.setText(mItem.getWeatherStateName());
            String minTemp = mItem.getFormattedMinTemp();
            String maxTemp = mItem.getFormattedMaxTemp();
            mTvMinTemp.setText(minTemp);
            mTvMaxTemp.setText(maxTemp);
        }
    }

    private class WeatherListAdapter extends RecyclerView.Adapter<WeatherVH> {

        private List<Weather> mItems;

        public WeatherListAdapter(List<Weather> items) {
            mItems = items;
        }

        @Override
        public WeatherVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.list_item_weather, parent, false);
            return new WeatherVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WeatherVH holder, int position) {
            Weather item = mItems.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void setData(List<Weather> items) {
            mItems = items;
        }
    }
}
