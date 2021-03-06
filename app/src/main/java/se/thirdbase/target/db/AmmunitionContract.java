package se.thirdbase.target.db;

import android.provider.BaseColumns;

/**
 * Created by alexp on 2/26/16.
 */
public class AmmunitionContract {

    public static final String TABLE_NAME = "ammunition";

    public interface AmmunitionEntry extends BaseColumns {
        String COLUMN_NAME_DATE_TIME = "date_time";
        String COLUMN_NAME_TYPE = "type";
        String COLUMN_NAME_MAKE_AND_NAME = "make_and_name";
        String COLUMN_NAME_CALIBER = "caliber";
        String COLUMN_NAME_GRAINS = "grains";
        String COLUMN_NAME_MUZZLE_VELOCITY = "velocity";
        String COLUMN_NAME_REMOVED = "removed";
    }

    public static final String SQL_CREATE_AMMUNITION =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "%s INTEGER," +
                            "%s INTEGER," +
                            "%s VARCHAR(128)," +
                            "%s INTEGER," +
                            "%s REAL," +
                            "%s INTEGER," +
                            "%s INTEGER);",
                    TABLE_NAME, AmmunitionEntry._ID,
                    AmmunitionEntry.COLUMN_NAME_DATE_TIME,
                    AmmunitionEntry.COLUMN_NAME_TYPE,
                    AmmunitionEntry.COLUMN_NAME_MAKE_AND_NAME,
                    AmmunitionEntry.COLUMN_NAME_CALIBER,
                    AmmunitionEntry.COLUMN_NAME_GRAINS,
                    AmmunitionEntry.COLUMN_NAME_MUZZLE_VELOCITY,
                    AmmunitionEntry.COLUMN_NAME_REMOVED);

    public static final String SQL_DROP_AMMUNITION = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
}
