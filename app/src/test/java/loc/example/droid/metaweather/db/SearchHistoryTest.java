package loc.example.droid.metaweather.db;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ExecutorService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
public class SearchHistoryTest {

//    @Mock
    private SearchHistory mSearchHistory;

    @Before
    public void setUp() throws Exception {
        mSearchHistory = new SearchHistory();
    }

    @After
    public void tearDown() throws Exception {
        mSearchHistory = null;
    }

    @Test
    public void getAllDesc() {
    }

    @Test
    public void insert() {

//        SearchHistory history = new SearchHistory(); // mock(SearchHistory.class);
        AppDatabase db = mock(AppDatabase.class);

        ExecutorService service = mock(ExecutorService.class);
//        doReturn(service).when(db).getExecutor();
        when(db.getExecutor()).thenReturn(service);
        mSearchHistory.insert(db);

        verify(db, never()).getExecutor(); // times(1)).getExecutor();
    }
}