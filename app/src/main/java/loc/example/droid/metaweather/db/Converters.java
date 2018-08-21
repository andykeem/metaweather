package loc.example.droid.metaweather.db;

import java.util.Date;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long val) {
        return (val == null) ? null : new Date(val);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return (date == null) ? null : date.getTime();
    }
}
