package se.thirdbase.target.model.precision;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.thirdbase.target.db.PrecisionRoundContract;
import se.thirdbase.target.db.PrecisionRoundToSeriesContract;

/**
 * Created by alexp on 2/18/16.
 */
public class PrecisionRound implements Parcelable {

    private static final int MAX_NBR_SERIES = 7;

    private List<PrecisionSeries> mPrecisionSeries;
    private int mScore;
    private int mCompetition;
    private String mNotes;
    private long mTimestamp;
    private long mDBHandle = Long.MIN_VALUE;

    public PrecisionRound(boolean competition) {
        this(new ArrayList<PrecisionSeries>(), competition, null);
    }

    public PrecisionRound(List<PrecisionSeries> precisionSeries, boolean competition, String notes) {
        this(precisionSeries, competition, notes, 0);
    }

    public PrecisionRound(List<PrecisionSeries> precisionSeries, boolean competition, String notes, long timestamp) {
        mPrecisionSeries = precisionSeries;
        mScore = calculateScore(precisionSeries);
        mCompetition = competition ? 1 : 0;
        mNotes = notes;
        mTimestamp = timestamp;
    }

    protected PrecisionRound(Parcel in) {
        mPrecisionSeries = in.createTypedArrayList(PrecisionSeries.CREATOR);
        mScore = in.readInt();
        mCompetition = in.readInt();
        mNotes = in.readString();
        mTimestamp = in.readLong();
    }

    public static final Creator<PrecisionRound> CREATOR = new Creator<PrecisionRound>() {
        @Override
        public PrecisionRound createFromParcel(Parcel in) {
            return new PrecisionRound(in);
        }

        @Override
        public PrecisionRound[] newArray(int size) {
            return new PrecisionRound[size];
        }
    };

    public void addPrecisionSeries(PrecisionSeries precisionSeries) {
        mPrecisionSeries.add(precisionSeries);
        mScore = calculateScore(mPrecisionSeries);
    }

    public void setPrecisionSeries(List<PrecisionSeries> precisionSeries) {
        mPrecisionSeries = precisionSeries;
        mScore = calculateScore(precisionSeries);
    }

    public List<PrecisionSeries> getPrecisionSeries() {
        return Collections.unmodifiableList(mPrecisionSeries);
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public int getScore() {
        return mScore;
    }

    public boolean getCompetition() {
        return mCompetition > 0;
    }

    public void setmCompetition(boolean competition) {
        mCompetition = competition ? 1 : 0;
    }

    public int getMaxNbrSeries() {
        return MAX_NBR_SERIES;
    }

    public int getNbrSeries() {
        return mPrecisionSeries.size();
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    private int calculateScore(List<PrecisionSeries> precisionSeries) {
        int score = 0;

        for (PrecisionSeries series : precisionSeries) {
            score += series.getScore();
        }

        return score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mPrecisionSeries);
        dest.writeInt(mScore);
        dest.writeInt(mCompetition);
        dest.writeString(mNotes);
        dest.writeLong(mTimestamp);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (PrecisionSeries series : mPrecisionSeries) {
            builder.append(String.format(" %d", series.getScore()));
        }
        return String.format("PrecisionRound(timestamp=%d, score=%d,%s)", mTimestamp, mScore, builder.toString());
    }

    /*** Database handling ***/

    public long getDBHandle() {
        return mDBHandle;
    }

    public long store(SQLiteDatabase db) {
        if (mDBHandle == Long.MIN_VALUE) {
            List<Long> ids = new ArrayList<>();

            for (PrecisionSeries series : mPrecisionSeries) {
                long id = series.getDBHandle();
                ids.add(id);
            }

            ContentValues values = new ContentValues();
            /*
            String[] columns = {
                    PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_1,
                    PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_2,
                    PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_3,
                    PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_4,
                    PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_5,
                    PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_6,
                    PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_7
            };

            int size = mPrecisionSeries.size();
            for (int i = 0; i < size; i++) {
                values.put(columns[i], ids.get(i));
            }
            */


            values.put(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SCORE, getScore());
            values.put(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_COMPETITION, getCompetition() ? 1 : 0);
            values.put(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_NOTES, getNotes());
            values.put(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_DATE_TIME, System.currentTimeMillis());

            mDBHandle = db.insert(PrecisionRoundContract.TABLE_NAME, null, values);

            PrecisionRoundToSeriesMapper.store(db, this);
        } else {
            update(db);
        }

        return mDBHandle;
    }

    public void update(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        String[] columns = {
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SCORE,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_NOTES,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_DATE_TIME,
        };

        values.put(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SCORE, getScore());
        values.put(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_NOTES, getNotes());
        values.put(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_DATE_TIME, System.currentTimeMillis());

        db.update(PrecisionRoundContract.TABLE_NAME, values, PrecisionRoundContract.PrecisionRoundEntry._ID + "=?", new String[]{"" + mDBHandle  });
    }

    public static PrecisionRound fetch(SQLiteDatabase db, long id) {
        String selection = PrecisionRoundContract.PrecisionRoundEntry._ID + "=?";
        String[] args = new String[]{"" + id};
        List<PrecisionRound> precisionRounds = fetchSelection(db, selection, args, null, null, null, null);

        return precisionRounds.size() == 1 ? precisionRounds.get(0) : null;
    }

    public static List<PrecisionRound> fetchAll(SQLiteDatabase db, String orderBy) {
        return fetchSelection(db, null, null, null, null, orderBy, null);
    }

    public static List<PrecisionRound> fetchSelection(SQLiteDatabase db, String selection, String[] selectionArgs,
                                                      String groupBy, String having, String orderBy, String limit) {
        String[] columns = {
                PrecisionRoundContract.PrecisionRoundEntry._ID,
                /*
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_1,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_2,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_3,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_4,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_5,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_6,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_7,
                */
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SCORE,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_COMPETITION,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_NOTES,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_DATE_TIME
        };

        Cursor cursor = db.query(PrecisionRoundContract.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy,
                limit);

        List<PrecisionRound> precisionRounds = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {

            try {
                while (!cursor.isAfterLast()) {
                    PrecisionRound round = PrecisionRound.fromCursor(db, cursor);
                    precisionRounds.add(round);
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
        }

        return precisionRounds;
    }

    private static PrecisionRound fromCursor(SQLiteDatabase db, Cursor cursor) {
        //List<PrecisionSeries> precisionSeries = new ArrayList<>();

        long id = cursor.getLong(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry._ID));

        List<PrecisionSeries> precisionSeries = PrecisionRoundToSeriesMapper.fetch(db, id);

        /*
        long seriesId = cursor.getLong(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_1));
        PrecisionSeries series = PrecisionSeries.fetch(db, seriesId);
        if (series != null) { precisionSeries.add(series); }

        seriesId = cursor.getLong(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_2));
        series = PrecisionSeries.fetch(db, seriesId);
        if (series != null) { precisionSeries.add(series); }

        seriesId = cursor.getLong(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_3));
        series = PrecisionSeries.fetch(db, seriesId);
        if (series != null) { precisionSeries.add(series); }

        seriesId = cursor.getLong(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_4));
        series = PrecisionSeries.fetch(db, seriesId);
        if (series != null) { precisionSeries.add(series); }

        seriesId = cursor.getLong(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_5));
        series = PrecisionSeries.fetch(db, seriesId);
        if (series != null) { precisionSeries.add(series); }

        seriesId = cursor.getLong(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_6));
        series = PrecisionSeries.fetch(db, seriesId);
        if (series != null) { precisionSeries.add(series); }

        seriesId = cursor.getLong(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_SERIES_7));
        series = PrecisionSeries.fetch(db, seriesId);
        if (series != null) { precisionSeries.add(series); }
        */

        int competition = cursor.getInt(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_COMPETITION));

        String notes = cursor.getString(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_NOTES));

        long timestamp = cursor.getLong(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_DATE_TIME));


        PrecisionRound precisionRound = new PrecisionRound(precisionSeries, competition > 0, notes, timestamp);

        precisionRound.mDBHandle = id;

        return precisionRound;
    }
}
