package loc.example.droid.metaweather.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.eq;

public class ConvertersTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void fromTimestamp() {
        Date date = new Date();
        long val = date.getTime();
        Date actual = Converters.fromTimestamp(val);
        assertEquals(date, actual);
    }

    @Test
    public void dateToTimestamp() {
        Date date = new Date();
        long expected = date.getTime();
        long actual = Converters.dateToTimestamp(date);
        assertEquals(expected, actual);
    }
}