package se.thirdbase.target.db;

import android.provider.BaseColumns;

/**
 * Created by alexp on 2/15/16.
 */
public final class PrecisionRoundContract {

    private static final String TAG = PrecisionRoundContract.class.getSimpleName();

    public static final String TABLE_NAME = "precision_round";

    public interface PrecisionRoundEntry extends BaseColumns {
        String COLUMN_NAME_DATE_TIME = "date_time";
        String COLUMN_NAME_SERIES_1 = "series1";
        String COLUMN_NAME_SERIES_2 = "series2";
        String COLUMN_NAME_SERIES_3 = "series3";
        String COLUMN_NAME_SERIES_4 = "series4";
        String COLUMN_NAME_SERIES_5 = "series5";
        String COLUMN_NAME_SERIES_6 = "series6";
        String COLUMN_NAME_SERIES_7 = "series7";
        String COLUMN_NAME_SCORE = "score";
        String COLUMN_NAME_COMPETITION = "competition";
        String COLUMN_NAME_NOTES = "notes";
    }

    public static final String SQL_CREATE_PRECISION =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "%s INTEGER," + // DATE_TIME
                            /*
                            "%s INTEGER," +
                            "%s INTEGER," +
                            "%s INTEGER," +
                            "%s INTEGER," +
                            "%s INTEGER," +
                            "%s INTEGER," +
                            "%s INTEGER," +
                            */
                            "%s INTEGER," + // SCORE
                            "%s INTEGER," + // COMPETITION
                            /*
                            "%s TEXT," +
                            "FOREIGN KEY(%s) REFERENCES %s(%s)," + // SERIES_1
                            "FOREIGN KEY(%s) REFERENCES %s(%s)," +
                            "FOREIGN KEY(%s) REFERENCES %s(%s)," +
                            "FOREIGN KEY(%s) REFERENCES %s(%s)," +
                            "FOREIGN KEY(%s) REFERENCES %s(%s)," +
                            "FOREIGN KEY(%s) REFERENCES %s(%s)," +
                            "FOREIGN KEY(%s) REFERENCES %s(%s));", // SERIES_7
                            */
                            "%s TEXT);",
                    TABLE_NAME, PrecisionRoundEntry._ID,
                    PrecisionRoundEntry.COLUMN_NAME_DATE_TIME,
                    /*
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_1,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_2,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_3,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_4,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_5,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_6,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_7,
                    */
                    PrecisionRoundEntry.COLUMN_NAME_SCORE,
                    PrecisionRoundEntry.COLUMN_NAME_COMPETITION,
                    PrecisionRoundEntry.COLUMN_NAME_NOTES
                    /*
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_1, PrecisionSeriesContract.TABLE_NAME, PrecisionSeriesContract.PrecisionSeriesEntry._ID,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_2, PrecisionSeriesContract.TABLE_NAME, PrecisionSeriesContract.PrecisionSeriesEntry._ID,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_3, PrecisionSeriesContract.TABLE_NAME, PrecisionSeriesContract.PrecisionSeriesEntry._ID,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_4, PrecisionSeriesContract.TABLE_NAME, PrecisionSeriesContract.PrecisionSeriesEntry._ID,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_5, PrecisionSeriesContract.TABLE_NAME, PrecisionSeriesContract.PrecisionSeriesEntry._ID,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_6, PrecisionSeriesContract.TABLE_NAME, PrecisionSeriesContract.PrecisionSeriesEntry._ID,
                    PrecisionRoundEntry.COLUMN_NAME_SERIES_7, PrecisionSeriesContract.TABLE_NAME, PrecisionSeriesContract.PrecisionSeriesEntry._ID);
                    */
            );

    public static final String SQL_DROP_PRECISION = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
}
