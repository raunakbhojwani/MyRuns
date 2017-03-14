package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ArrayList;

/**
 * Inspired by DatabaseDemo on course website.
 * This is a helper class that inserts to, deletes and fetches from the database
 *
 * Created by RaunakBhojwani on 2/1/17.
 * @author RaunakBhojwani
 */

public class EntriesDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
                                    MySQLiteHelper.COLUMN_INPUT_TYPE,
                                    MySQLiteHelper.COLUMN_ACTIVITY_TYPE,
                                    MySQLiteHelper.COLUMN_DATE_AND_TIME,
                                    MySQLiteHelper.COLUMN_DURATION,
                                    MySQLiteHelper.COLUMN_DISTANCE,
                                    MySQLiteHelper.COLUMN_CALORIES,
                                    MySQLiteHelper.COLUMN_HEART_RATE,
                                    MySQLiteHelper.COLUMN_AVG_PACE,
                                    MySQLiteHelper.COLUMN_AVG_SPEED,
                                    MySQLiteHelper.COLUMN_CLIMB,
                                    MySQLiteHelper.COLUMN_COMMENT,
                                    MySQLiteHelper.COLUMN_GPS_DATA};

    public EntriesDataSource (Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    //Create a new, or open an existing database to write to and read from
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    //Close the database
    public void close() {
        dbHelper.close();
    }

    //Take the exercise entry object and add it to the database, inputting
    //the appropriate values into the their respective columns
    public long createExerciseEntry(ExerciseEntry eEntry) {

        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_INPUT_TYPE, eEntry.getmInputType());
        values.put(MySQLiteHelper.COLUMN_ACTIVITY_TYPE, eEntry.getmActivityType());
        long dateAndTime = eEntry.getmDateTime().getTimeInMillis();
        values.put(MySQLiteHelper.COLUMN_DATE_AND_TIME, dateAndTime);
        values.put(MySQLiteHelper.COLUMN_DURATION, eEntry.getmDuration());
        values.put(MySQLiteHelper.COLUMN_DISTANCE, eEntry.getmDistance());
        values.put(MySQLiteHelper.COLUMN_CALORIES, eEntry.getmCalorie());
        values.put(MySQLiteHelper.COLUMN_HEART_RATE, eEntry.getmHeartRate());

        values.put(MySQLiteHelper.COLUMN_AVG_PACE, eEntry.getmAvgPace());
        values.put(MySQLiteHelper.COLUMN_AVG_SPEED, eEntry.getmAvgSpeed());
        values.put(MySQLiteHelper.COLUMN_CLIMB, eEntry.getmClimb());
        values.put(MySQLiteHelper.COLUMN_COMMENT, eEntry.getmComment());

        byte[] byteArrayLocation = eEntry.getByteArrayLocation();
        if (byteArrayLocation.length > 0) {
            values.put(MySQLiteHelper.COLUMN_GPS_DATA, byteArrayLocation);
        }

        long insertId = database.insert(MySQLiteHelper.TABLE_EXERCISE_ENTRIES, null, values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EXERCISE_ENTRIES, allColumns,
                                            MySQLiteHelper.COLUMN_ID + " = " + insertId,
                                            null, null, null, null);
        cursor.moveToFirst();

        cursor.close();

        return insertId;

    }

    //Take an exercise entry object, and set its various values to
    //values being extracted from the database columns.
    private ExerciseEntry cursorToEntry(Cursor cursor, boolean locationExists) {
        ExerciseEntry exerciseEntry = new ExerciseEntry();

        exerciseEntry.setId(cursor
                .getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID)));
        exerciseEntry.setmInputType(cursor
                .getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_INPUT_TYPE)));
        exerciseEntry.setmActivityType(cursor
                .getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ACTIVITY_TYPE)));

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(cursor
                .getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DATE_AND_TIME)));
        exerciseEntry.setmDateTime(calendar);

        exerciseEntry.setmDuration(cursor
                .getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DURATION)));
        exerciseEntry.setmDistance(cursor
                .getDouble(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DISTANCE)));
        exerciseEntry.setmCalorie(cursor
                .getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CALORIES)));
        exerciseEntry.setmHeartRate(cursor
                .getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_HEART_RATE)));

        exerciseEntry.setmAvgPace(cursor
        .getDouble(cursor.getColumnIndex(MySQLiteHelper.COLUMN_AVG_PACE)));
        exerciseEntry.setmAvgSpeed(cursor
        .getDouble(cursor.getColumnIndex(MySQLiteHelper.COLUMN_AVG_SPEED)));
        exerciseEntry.setmClimb(cursor
        .getDouble(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CLIMB)));
        exerciseEntry.setmComment(cursor
        .getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_COMMENT)));

        if (locationExists) {
            exerciseEntry.setmLocationList(cursor
                    .getBlob(cursor.getColumnIndex(MySQLiteHelper.COLUMN_GPS_DATA)));
        }

        return exerciseEntry;

    }

    //take the Id of a certain Exercise Entry object and remove it from the database
    public void deleteExerciseEntry(int Id) {
        database.delete(MySQLiteHelper.TABLE_EXERCISE_ENTRIES,
                        MySQLiteHelper.COLUMN_ID + " = " + Id, null);
    }

    //Fetch all the entries in the database and return them as a
    //list of ExerciseEntry objects
    public List<ExerciseEntry> fetchEntries() {

        List<ExerciseEntry> exerciseEntries = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EXERCISE_ENTRIES,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {

            ExerciseEntry currentEntry = cursorToEntry(cursor, false);
            exerciseEntries.add(currentEntry);
            cursor.moveToNext();

        }

        cursor.close();
        return exerciseEntries;

    }

    //Fetch a certain ExerciseEntry from the database, using its Id
    public ExerciseEntry fetchEntryByIndex(long Id) throws android.database.SQLException {

        ExerciseEntry exerciseEntry = null;
        Cursor cursor = database.query(true, MySQLiteHelper.TABLE_EXERCISE_ENTRIES, allColumns,
                MySQLiteHelper.COLUMN_ID + "=" + Id, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            exerciseEntry = cursorToEntry(cursor, true);
        }
        cursor.close();

        return exerciseEntry;
    }

}
