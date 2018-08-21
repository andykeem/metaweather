package loc.example.droid.metaweather.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.DecimalFormat;

import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//@RunWith(MockitoJUnitRunner.class)
public class LocationTest {

//    @Mock
//    private Location mLocation;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void formatLatLng() {
        Double val = 12.3456789;
        String expected = "12.3457";

        String actual = Location.formatLatLng(val);
        assertEquals(expected, actual);
    }
}