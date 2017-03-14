package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Used to create, manage, and update the entries database
 * Created by RaunakBhojwani on 2/1/17.
 * @author RaunakBhojwani
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_EXERCISE_ENTRIES = "exerciseentries";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INPUT_TYPE = "input_type";
    public static final String COLUMN_ACTIVITY_TYPE = "activity_type";
    public static final String COLUMN_DATE_AND_TIME = "date_time";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_HEART_RATE = "heartrate";
    public static final String COLUMN_AVG_PACE = "avg_pace";
    public static final String COLUMN_AVG_SPEED = "avg_speed";
    public static final String COLUMN_CLIMB = "climb";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_GPS_DATA = "gps_data";



    public static final String DATABASE_NAME = "exercises.db";
    public static final int DATABASE_VERSION = 3;

    public static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EXERCISE_ENTRIES + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_INPUT_TYPE + " TEXT NOT NULL, " + COLUMN_ACTIVITY_TYPE + " TEXT NOT NULL, "
            + COLUMN_DATE_AND_TIME + " DATETIME not null, " + COLUMN_DURATION + " INTEGER, "
            + COLUMN_DISTANCE + " FLOAT, " + COLUMN_CALORIES + " INTEGER, " + COLUMN_HEART_RATE
            + " INTEGER, " + COLUMN_AVG_PACE + " INTEGER, " + COLUMN_AVG_SPEED + " INTEGER, "
            + COLUMN_CLIMB + " INTEGER, " + COLUMN_COMMENT + " TEXT, " + COLUMN_GPS_DATA + " BLOB " + ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE_ENTRIES);
        onCreate(db);
    }


}
