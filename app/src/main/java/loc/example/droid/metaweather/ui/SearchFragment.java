package loc.example.droid.metaweather.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import loc.example.droid.metaweather.R;
import loc.example.droid.metaweather.api.WeatherApi;
import loc.example.droid.metaweather.api.WeatherService;
import loc.example.droid.metaweather.db.AppDatabase;
import loc.example.droid.metaweather.db.SearchHistory;
import loc.example.droid.metaweather.model.Location;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();
    private static final String ARG_LAT = "ARG_LAT";
    private static final String ARG_LNG = "ARG_LNG";
    private static final String ARG_QUERY = "ARG_QUERY";
    private static final String SAVED_LAT = "SAVED_LAT";
    private static final String SAVED_LNG = "SAVED_LNG";
    private static final String SAVED_QUERY = "SAVED_QUERY";
    private Double mLat;
    private Double mLng;
    private String mQuery;
    private List<Location> mLocations = new ArrayList<>();
    private RecyclerView mRvSearchResult;
    private Resources mResources;
    private Call<List<Location>> mApiCall;

    public SearchFragment() {
        // Required empty public constructor
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        if (this.getArguments() != null) {
            Bundle args = this.getArguments();
            if (TextUtils.isEmpty(args.getString(ARG_QUERY))) {
                mLat = args.getDouble(ARG_LAT);
                mLng = args.getDouble(ARG_LNG);
                this.searchLocations();
            } else {
                mQuery = args.getString(ARG_QUERY);
                this.searchLocationsByQuery();
            }
        }
        mResources = this.getResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mRvSearchResult = v.findViewById(R.id.rv_search_result);
        mRvSearchResult.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) { // update the UI after rotation
            this.updateUI();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mLat != null) {
            outState.putDouble(SAVED_LAT, mLat);
        }
        if (mLng != null) {
            outState.putDouble(SAVED_LNG, mLng);
        }
        if (mQuery != null) {
            outState.putString(SAVED_QUERY, mQuery);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mApiCall != null) {
            mApiCall.cancel();
        }
    }

    public static SearchFragment newInstance(double lat, double lng) {
        Bundle args = new Bundle();
        args.putDouble(ARG_LAT, lat);
        args.putDouble(ARG_LNG, lng);
        SearchFragment sf = new SearchFragment();
        sf.setArguments(args);
        return sf;
    }

    public static SearchFragment newInstance(String query) {
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        SearchFragment sf = new SearchFragment();
        sf.setArguments(args);
        return sf;
    }

    protected void updateUI() {
        if (mLocations.isEmpty()) { // alerts the user for bad keyword request
            this.showLocationNotFoundDialog();
        }
        SearchResultAdapter adapter = new SearchResultAdapter(mLocations);
        mRvSearchResult.setAdapter(adapter);
    }

    protected void storeSearchKeyword() {
        AppDatabase db = AppDatabase.getInstance(getContext());
        SearchHistory history = new SearchHistory();
        history.setQuery(mQuery);
        history.insert(db);
    }

    /**
     * makes location search api request based on latitude and longitude, then updates UI
     */
    protected void searchLocations() {
        WeatherService service = WeatherApi.getWeatherService();
        String latLng = String.format("%f,%f", mLat, mLng);
        mApiCall = service.listLocation(latLng);
        mApiCall.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                mLocations = response.body();
                updateUI();
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        });
    }

    /**
     * makes location search api request based on keyword query, then updates UI
     */
    protected void searchLocationsByQuery() {
        WeatherService service = WeatherApi.getWeatherService();
        String query = mQuery;
        mApiCall = service.listLocationByQuery(query);
        mApiCall.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                mLocations = response.body();
                updateUI();
                storeSearchKeyword();
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        });
    }

    private class SearchResultVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Location mItem;
        private TextView mTvWoeid;
        private TextView mTvTitle;
        private TextView mTvType;

        public SearchResultVH(View itemView) {
            super(itemView);
            mTvWoeid = itemView.findViewById(R.id.tv_woeid);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvType = itemView.findViewById(R.id.tv_type);
            itemView.setOnClickListener(this);
        }

        public void bindItem(Location item) {
            mItem = item;
            mTvWoeid.setText(String.valueOf(mItem.getWoeid()));
            mTvTitle.setText(mItem.getTitle());
            mTvType.setText(mItem.getType());
        }

        @Override
        public void onClick(View view) {
            Intent activity = LocationActivity.newIntent(getContext());
            activity.putExtra(LocationActivity.EXTRA_WOEID, mItem.getWoeid());
            activity.putExtra(LocationActivity.EXTRA_LOCATION, mItem.getTitle());
            startActivity(activity);
        }
    }

    private class SearchResultAdapter extends RecyclerView.Adapter<SearchResultVH> {
        private List<Location> mData;

        public SearchResultAdapter(List<Location> data) {
            mData = data;
        }

        @Override
        public SearchResultVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.list_item_location, parent, false);
            return new SearchResultVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchResultVH holder, int position) {
            Location item = mData.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    protected void showLocationNotFoundDialog() {
        String msg = mResources.getString(R.string.dialog_msg_search_result_empty, mQuery);
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.dialog_title_search_result_empty)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                })
                .create()
                .show();
    }
}
