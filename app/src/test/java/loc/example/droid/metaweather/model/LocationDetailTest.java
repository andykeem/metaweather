package loc.example.droid.metaweather.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocationDetailTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getFahrenheitWeathers() {
//        LocationDetail detail = new LocationDetail();
//        List<Weather> weathers = detail.getFahrenheitWeathers();
//        Weather weather = mock(Weather.class);

        Weather weather = mock(Weather.class);
        List<Weather> list = new ArrayList<>();
        list.add(weather);
        list.add(weather);

        LocationDetail detail = mock(LocationDetail.class);

        when(detail.getFahrenheitWeathers()).thenReturn(list);
        int actual = detail.getFahrenheitWeathers().size();

        assertEquals(2, actual);
    }
}