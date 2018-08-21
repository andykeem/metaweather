package loc.example.droid.metaweather.db;

import android.util.Log;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "search_history")
public class SearchHistory {

    private static final String TAG = SearchHistory.class.getSimpleName();

    @PrimaryKey(autoGenerate = true)
    private int hid;
    private String query;
    @ColumnInfo(name = "created_at")
    private Date createdAt;

    public String getQuery() {
        return query;
    }

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /*public List<SearchHistory> getAll(final AppDatabase db) {
        List<SearchHistory> result = new ArrayList<>();
        Future task = db.getExecutor().submit(new Callable() {
            @Override
            public Object call() throws Exception {
                List<SearchHistory> result = db.searchHistoryDao().getAllDesc();
                return result;
            }
        });
        try {
            result = (List<SearchHistory>) task.get();
        } catch (InterruptedException ie) {
            Log.e(TAG, ie.getMessage(), ie);
        } catch (ExecutionException ee) {
            Log.e(TAG, ee.getMessage(), ee);
        }
        return result;
    }

    public void getAllDesc(final AppDatabase db, final Handler handler) {
        db.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<SearchHistory> result = db.searchHistoryDao().getAllDesc();
                handler.obtainMessage(AppDatabase.RESULT_MSG_WHAT, result)
                        .sendToTarget();
            }
        });
    }*/

    public LiveData<List<SearchHistory>> getAllDesc(AppDatabase db) {
        return db.searchHistoryDao().getAllDesc();
    }

    public void insert(final AppDatabase db) {
        final SearchHistory model = this;
        model.setCreatedAt(new Date());
        db.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    db.searchHistoryDao().insert(model);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });
    }

    public String getStrCreatedAt() {
        return null;
    }
}
