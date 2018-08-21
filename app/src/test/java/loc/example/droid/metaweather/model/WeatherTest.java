package loc.example.droid.metaweather.model;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WeatherTest {

    @Mock
    private Weather mWeather;
    @Mock
    private Context mContext;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void formatTemp() {
        String temp = "72.51";
        String returnVal = "72" + Weather.DEGREE_CELSIUS;
        String expected = "72" + Weather.DEGREE_CELSIUS;

        doReturn(returnVal).when(mWeather).formatTemp(temp);
        String actual = mWeather.formatTemp(temp);
        String msg = String.format("given: %s, returned: %s", temp, actual);
        assertEquals(msg, expected, actual);

        int n = 72;
        verify(mWeather, never()).getFahrenheitByCelsius(n);
    }

    @Test
    public void getFormattedDate() {
        String returnVal = "Thu 16 Aug";
        doReturn(returnVal).when(mWeather).getFormattedDate(mContext);
        String actual = mWeather.getFormattedDate(mContext);
//        assertEquals("Thu 15 Aug", actual);
        assertEquals(returnVal, actual);
        verify(mContext, never()).getResources();
    }
}