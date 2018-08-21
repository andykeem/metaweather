package loc.example.droid.metaweather.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import loc.example.droid.metaweather.R;
import loc.example.droid.metaweather.db.AppDatabase;
import loc.example.droid.metaweather.db.SearchHistory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final int REQUEST_LOCATION = 0;
    private Location mLocation;
    private EditText mEtLocation;
    private TextView mTvLat;
    private TextView mTvLng;
    private RecyclerView mRvHistoryList;
    private Button mBtSearch;
    private List<SearchHistory> mSearchHistories = new ArrayList<>();

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        this.getSearchHistory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mEtLocation = v.findViewById(R.id.et_location);
        mTvLat = v.findViewById(R.id.tv_lat);
        mTvLng = v.findViewById(R.id.tv_lng);
        mRvHistoryList = v.findViewById(R.id.rv_history_list);
        mRvHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBtSearch = v.findViewById(R.id.bt_search);
        /**
         * 1) if user typed in location/keyword then search based on the keyword
         * 2) if user didn't type but we were able to find lat/lng then search based on that
         * 3) if (1) and (2) both failed, then alert the user to type the location
         */
        mBtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mEtLocation.getText())) {
                    Intent activity = SearchActivity.newIntent(getContext(),
                            mEtLocation.getText().toString());
                    startActivity(activity);
                } else if (hasLatLng()) {
                    Intent activity = SearchActivity.newIntent(getContext(),
                            mLocation.getLatitude(), mLocation.getLongitude());
                    startActivity(activity);
                } else {
                    String msg = getResources().getString(R.string.err_missing_location);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.updateLatLng();
        return v;
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public void updateLocation(Location loc) {
        mLocation = loc;
        this.updateLatLng();
    }

    protected void updateLatLng() {
        if (mLocation != null) {
            mTvLat.setText(String.valueOf(mLocation.getLatitude()));
            mTvLng.setText(String.valueOf(mLocation.getLongitude()));
        }
    }

    protected boolean hasLatLng() {
        Double lat = null;
        Double lng = null;
        if (mLocation != null) {
            lat = mLocation.getLatitude();
            lng = mLocation.getLongitude();
        }
        return ((lat != null) && (lng != null));
    }

    protected void updateSearchHistoryList() {
        if (mSearchHistories.isEmpty()) return;
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(mSearchHistories);
        mRvHistoryList.setAdapter(adapter);
    }

    protected void getSearchHistory() {
        AppDatabase db = AppDatabase.getInstance(this.getContext());
        LiveData<List<SearchHistory>> histories = db.searchHistoryDao().getAllDesc();
        histories.observe(this, new Observer<List<SearchHistory>>() {
            @Override
            public void onChanged(List<SearchHistory> searchHistories) {
                mSearchHistories = searchHistories;
                updateSearchHistoryList();
            }
        });
    }

    protected void setSearchHistories(List<SearchHistory> histories) {
        mSearchHistories = histories;
        this.updateSearchHistoryList();
    }

    private static class DbHandler extends Handler {
        private MainFragment mFragment;

        public DbHandler(MainFragment fragment) {
            WeakReference<MainFragment> weakRef = new WeakReference<>(fragment);
            mFragment = weakRef.get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AppDatabase.RESULT_MSG_WHAT) {
                List<SearchHistory> histories = (List<SearchHistory>) msg.obj;
                mFragment.setSearchHistories(histories);
            }
        }
    }

    private class SearchHistoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SearchHistory mItem;
        private TextView mTvQuery;
        private TextView mTvCreatedAt;

        public SearchHistoryVH(View itemView) {
            super(itemView);
            mTvQuery = itemView.findViewById(R.id.tv_query);
            mTvCreatedAt = itemView.findViewById(R.id.tv_created_at);
            itemView.setOnClickListener(this);
        }

        public void bindItem(SearchHistory item) {
            mItem = item;
            mTvQuery.setText(mItem.getQuery());
            mTvCreatedAt.setText(mItem.getCreatedAt().toString());
        }

        @Override
        public void onClick(View v) {
            Intent activity = SearchActivity.newIntent(getContext(), mItem.getQuery());
            startActivity(activity);
        }
    }

    private class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryVH> {

        private List<SearchHistory> mItems;

        public SearchHistoryAdapter(List<SearchHistory> items) {
            mItems = items;
        }

        @Override
        public SearchHistoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.list_item_search_history, parent, false);
            return new SearchHistoryVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchHistoryVH holder, int position) {
            SearchHistory item = mItems.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
}
