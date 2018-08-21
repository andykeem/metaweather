package loc.example.droid.metaweather.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SearchHistoryDao {

    @Query("SELECT * FROM search_history ORDER BY hid DESC")
    LiveData<List<SearchHistory>> getAllDesc();

    @Insert
    void insert(SearchHistory history);
}
