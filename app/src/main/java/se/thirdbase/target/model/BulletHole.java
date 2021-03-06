package se.thirdbase.target.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import se.thirdbase.target.db.BulletHoleContract;

public class BulletHole implements Parcelable {
    final BulletCaliber mCaliber;
    float mRadius;
    float mAngle;
    long mDBHandle = Long.MIN_VALUE;

    public BulletHole(BulletCaliber caliber, float radius, float angle) {
        this.mCaliber = caliber;
        this.mRadius = radius;
        this.mAngle = angle;
    }

    protected BulletHole(Parcel in) {
        mCaliber = (BulletCaliber) in.readSerializable();
        mRadius = in.readFloat();
        mAngle = in.readFloat();
    }

    public BulletHole copy() {
        return new BulletHole(mCaliber, mRadius, mAngle);
    }

    public void move(float deltaX, float deltaY) {
        float x = (float) (mRadius * Math.cos(mAngle)) + deltaX;
        float y = (float) (mRadius * Math.sin(mAngle)) + deltaY;

        mRadius = (float) Math.sqrt(x * x + y * y);
        mAngle = (float) Math.atan2(y, x);
    }

    public PointF toCartesianCoordinates() {
        float x = (float) (mRadius * Math.cos(mAngle));
        float y = (float) (mRadius * Math.sin(mAngle));

        return new PointF(x, y);
    }


    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    public BulletCaliber getCaliber() {
        return mCaliber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mCaliber);
        dest.writeFloat(mRadius);
        dest.writeFloat(mAngle);
    }

    public static final Creator<BulletHole> CREATOR = new Creator<BulletHole>() {
        @Override
        public BulletHole createFromParcel(Parcel in) {
            return new BulletHole(in);
        }

        @Override
        public BulletHole[] newArray(int size) {
            return new BulletHole[size];
        }
    };

    public String toString() {
        return String.format("%s r=%.2f a=%.2f", mCaliber, mRadius, mAngle);
    }

    /*** Database handling ***/

    public long getDBHandle() {
        return mDBHandle;
    }

    public long store(SQLiteDatabase db) {
        if (mDBHandle == Long.MIN_VALUE) {
            ContentValues values = new ContentValues();

            values.put(BulletHoleContract.BulletHoleEntry.COLUMN_NAME_CALIBER, getCaliber().ordinal());
            values.put(BulletHoleContract.BulletHoleEntry.COLUMN_NAME_ANGLE, getAngle());
            values.put(BulletHoleContract.BulletHoleEntry.COLUMN_NAME_RADIUS, getRadius());

            mDBHandle = db.insert(BulletHoleContract.TABLE_NAME, null, values);
        } else {
            update(db);
        }

        return mDBHandle;
    }

    public void update(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(BulletHoleContract.BulletHoleEntry.COLUMN_NAME_CALIBER, getCaliber().ordinal());
        values.put(BulletHoleContract.BulletHoleEntry.COLUMN_NAME_ANGLE, getAngle());
        values.put(BulletHoleContract.BulletHoleEntry.COLUMN_NAME_RADIUS, getRadius());

        db.update(BulletHoleContract.TABLE_NAME, values, BulletHoleContract.BulletHoleEntry._ID, new String[]{"" + mDBHandle});
    }

    public static BulletHole fetch(SQLiteDatabase db, long id) {
        String[] columns = {
                BulletHoleContract.BulletHoleEntry._ID,
                BulletHoleContract.BulletHoleEntry.COLUMN_NAME_CALIBER,
                BulletHoleContract.BulletHoleEntry.COLUMN_NAME_ANGLE,
                BulletHoleContract.BulletHoleEntry.COLUMN_NAME_RADIUS
        };

        Cursor cursor = db.query(BulletHoleContract.TABLE_NAME,
                columns,
                BulletHoleContract.BulletHoleEntry._ID + "=?",
                new String[] {"" + id},
                null,
                null,
                null,
                null);

        BulletHole hole = null;

        if (cursor != null && cursor.moveToFirst()) {
            try {
                hole = fromCursor(db, cursor);
            } finally {
                cursor.close();
            }
        }

        return hole;
    }

    private static BulletHole fromCursor(SQLiteDatabase db, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(BulletHoleContract.BulletHoleEntry._ID));
        BulletCaliber caliber = BulletCaliber.values()[cursor.getInt(cursor.getColumnIndex(BulletHoleContract.BulletHoleEntry.COLUMN_NAME_CALIBER))];
        float angle = cursor.getFloat(cursor.getColumnIndex(BulletHoleContract.BulletHoleEntry.COLUMN_NAME_ANGLE));
        float radius = cursor.getFloat(cursor.getColumnIndex(BulletHoleContract.BulletHoleEntry.COLUMN_NAME_RADIUS));

        BulletHole bulletHole = new BulletHole(caliber, radius, angle);
        bulletHole.mDBHandle = id;

        return bulletHole;
    }
}
