package loc.example.droid.metaweather.ui;

import android.text.SpannableStringBuilder;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loc.example.droid.metaweather.R;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

//@RunWith(MockitoJUnitRunner.class)
public class MainFragmentTest {

//    @Mock
//    private EditText mEtLocation;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void checkEditTextLocation() {
        MainActivity activity = spy(MainActivity.class);

        EditText etLocation = mock(EditText.class);
        doReturn(etLocation).when(activity).findViewById(R.id.et_location);

        when(etLocation.getText()).thenReturn(new SpannableStringBuilder("Los Angeles"));
    }

    @Test
    public void updateLocation() {
        MainFragment fragment = spy(new MainFragment());
        fragment.updateLocation(null);
        verify(fragment, times(1)).updateLatLng();
    }
}