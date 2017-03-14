package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Calendar;

/* This ManualEntryActivity.java file controls the how the user manually inputs details of the exercise
 * It has been developed to resemble the demo MyRuns2 app given by XD.
 *
 * @author RaunakB
 */

public class ManualEntryActivity extends ListActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = "DebugTag";
    static final String[]  STATISTICS =
            new String[]{"Date", "Time", "Duration",  "Distance",
                                                "Calories", "Heart Rate", "Comment"};
    Button mSave, mCancel;
    ListView mListView;

    private ExerciseEntry mEntry;
    private EntriesDataSource mDataSource;
//    private final Calendar mCal = Calendar.getInstance();

    String activity_type, mComment = " ";
    int mDuration = 0;
    double mDistance = 0;
    int mCalories = 0;
    int mHeartRate = 0;
    int mYear = -1, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "Created ManualEntry Activity");

        super.onCreate(savedInstanceState);

        mDataSource = new EntriesDataSource(this);
        try {
            mDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final Intent activityIntent = getIntent();
        activity_type = activityIntent.getStringExtra(StartFragment.ACTIVITY);

        setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, STATISTICS));

        Log.d(TAG, "setListAdapter for ManualEntry");
        setContentView(R.layout.activity_manual_entry);

        //Instantiate buttons and listview in order to allow user interactions
        mSave = (Button) findViewById(R.id.manual_entry_save_button);
        mCancel = (Button) findViewById(R.id.manual_entry_cancel_button);
        mListView = getListView();
        Log.d(TAG, "made the list view and buttons for interation");

        mListView.setOnItemClickListener(this);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Entry has been discarded",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEntry = new ExerciseEntry("Manual Entry", "Running", Calendar.getInstance(), 0, 0, 0, 0);

                if (mYear != -1) {
                    Calendar mCal = Calendar.getInstance();
                    mCal.set(mYear, mMonth, mDay, mHour, mMinute);
                    mEntry.setmDateTime(mCal);
                }

                mEntry.setmDuration(mDuration);
                Log.d(TAG, "Duration is " + mDuration);
                mEntry.setmDistance(mDistance);
                Log.d(TAG, "Distance is " + mDistance);
                mEntry.setmCalorie(mCalories);
                Log.d(TAG, "Calories is " + mCalories);
                mEntry.setmHeartRate(mHeartRate);
                Log.d(TAG, "Heart Rate is " + mHeartRate);

                mEntry.setmActivityType(activity_type);
                Log.d(TAG, "Activity Type is " + activity_type);
                mEntry.setmComment(mComment);
                Log.d(TAG, "Comment is " + mComment);

                AsyncTaskAdd addToDatabase = new AsyncTaskAdd();
                addToDatabase.execute(mEntry);

                finish();
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String userChoice = (String) (mListView.getItemAtPosition(position));

        if (userChoice.equals("Date"))
        {
            DialogFragment dialogFragment = MyRunsDialogFragment.newFragment();
            MyRunsDialogFragment.dialogCategory = "Date";
            dialogFragment.show(getFragmentManager(), "dialog");
            Log.d(TAG, "Date Set");
        }
        else if(userChoice.equals("Time"))
        {
            MyRunsDialogFragment.dialogCategory = "Time";
            DialogFragment dialogFragment = MyRunsDialogFragment.newFragment();
            dialogFragment.show(getFragmentManager(), "dialog");
            Log.d(TAG, "Time Set");
        }
        else if(userChoice.equals("Duration"))
        {
            MyRunsDialogFragment.dialogCategory = "Duration";
            DialogFragment dialogFragment = MyRunsDialogFragment.newFragment();
            dialogFragment.show(getFragmentManager(), "dialog");
            Log.d(TAG, "Duration entered");
        }
        else if(userChoice.equals("Distance"))
        {
            MyRunsDialogFragment.dialogCategory = "Distance";
            DialogFragment dialogFragment = MyRunsDialogFragment.newFragment();
            dialogFragment.show(getFragmentManager(), "dialog");
            Log.d(TAG, "Distance entered");
        }
        else if(userChoice.equals("Calories"))
        {
            MyRunsDialogFragment.dialogCategory = "Calories";
            DialogFragment dialogFragment = MyRunsDialogFragment.newFragment();
            dialogFragment.show(getFragmentManager(), "dialog");
            Log.d(TAG, "Calories entered");
        }
        else if(userChoice.equals("Heart Rate"))
        {
            MyRunsDialogFragment.dialogCategory = "Heart Rate";
            DialogFragment dialogFragment = MyRunsDialogFragment.newFragment();
            dialogFragment.show(getFragmentManager(), "dialog");
            Log.d(TAG, "Heart Rate entered");
        }
        else if(userChoice.equals("Comment"))
        {
            MyRunsDialogFragment.dialogCategory = "Comment";
            DialogFragment dialogFragment = MyRunsDialogFragment.newFragment();
            dialogFragment.show(getFragmentManager(), "dialog");
            Log.d(TAG, "Comment entered");
        }
    }

    public void onDateSet(int year, int monthOfYear, int dayOfMonth) {

        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;

    }

    public void onTimeSet(int hourOfDay, int minuteOfHour) {

        mHour = hourOfDay;
        mMinute = minuteOfHour;

    }

    public void onDialogOKClick(String dialogName, String dialog_data){

        Log.d(TAG, "DialogOK Clicked");

        if (dialogName.equals("Comment")) {
            mComment = dialog_data;
            Log.d(TAG, "Comment is " + mComment);
        }
        else if (dialogName.equals("Activity Type")) {
            activity_type = dialog_data;
            Log.d(TAG, "Activity Type is " + activity_type);
        }


    }

    public void onDialogOKClick(String dialogName, double dialog_data){
        Log.d(TAG, "DialogOK Clicked");

        if (dialogName.equals("Distance")) {
            mDistance = dialog_data;
            Log.d(TAG, "DialogedDistance is " + mDistance);
        }


    }

    public void onDialogOKClick(String dialogName, int dialog_data) {
        Log.d(TAG, "DialogOK Clicked");
        if (dialogName.equals("Calories")) {
            mCalories = dialog_data;
            Log.d(TAG, "DialogedCalories is " + mCalories);
        }

        else if (dialogName.equals("Duration")) {
            mDuration = dialog_data;
            Log.d(TAG, "DialogedDuration is " + mDuration);
        }

        else if (dialogName.equals("Heart Rate")) {
            mHeartRate = dialog_data;
            Log.d(TAG, "Dialoged Heart Rate is " + mHeartRate);
        }

    }

    public void onDialogCancelClick() {

    }

    private class AsyncTaskAdd extends AsyncTask<ExerciseEntry, Void, String>
    {
        @Override
        protected String doInBackground(ExerciseEntry... params) {

            long Id = mDataSource.createExerciseEntry(params[0]);

            return ""+Id;

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Entry no." + result + " has been added.",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
