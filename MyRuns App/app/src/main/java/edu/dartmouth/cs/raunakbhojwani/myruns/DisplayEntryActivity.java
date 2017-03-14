package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.widget.EditText;

import java.sql.SQLException;
/**
 * DisplayEntryActivity.java displays all the entries once they have been read from the database
 * Entries can also be deleted using the delete button
 *
 * Created by RaunakBhojwani on 2/1/17.
 * @author RaunakB
 */
public class DisplayEntryActivity extends AppCompatActivity {

    long Id;
    private EntriesDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_entry);

        //Once the views are set, create a EntriesDataSource object
        //and open the database using the .open() method
        dataSource = new EntriesDataSource(this);
        try {
            dataSource.open();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        //Get the database intent
        Intent dbIntent = getIntent();

        //And from that, get the values of all the columns of
        //the database in order to display
        Id = dbIntent.getLongExtra(HistoryFragment.ID, -1);

        String input = dbIntent.getStringExtra(HistoryFragment.INPUT);
        String activity = dbIntent.getStringExtra(HistoryFragment.ACTIVITY);
        String dateAndTime = dbIntent.getStringExtra(HistoryFragment.DATEANDTIME);
        String duration = dbIntent.getStringExtra(HistoryFragment.DURATION);
        String distance = dbIntent.getStringExtra(HistoryFragment.DISTANCE);
        String calories = dbIntent.getStringExtra(HistoryFragment.CALORIES);
        String heartRate = dbIntent.getStringExtra(HistoryFragment.HEARTRATE);

        //Proceed to set the edit text's text for each of the values
        EditText input_type_edit_text = (EditText)findViewById(R.id.input_type_edit_text);
        input_type_edit_text.setText(input);

        EditText activity_type_edit_text = (EditText)findViewById(R.id.activity_type_edit_text);
        activity_type_edit_text.setText(activity);

        EditText date_edit_text = (EditText)findViewById(R.id.date_edit_text);
        date_edit_text.setText(dateAndTime);

        EditText duration_edit_text = (EditText)findViewById(R.id.duration_edit_text);
        duration_edit_text.setText(duration);

        EditText distance_edit_text = (EditText)findViewById(R.id.distance_edit_text);
        distance_edit_text.setText(distance);

        EditText calories_edit_text = (EditText)findViewById(R.id.calories_edit_text);
        calories_edit_text.setText(calories);

        EditText heart_rate_edit_text = (EditText)findViewById(R.id.heart_rate_edit_text);
        heart_rate_edit_text.setText(heartRate);

    }

    // Inflate the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entry_menu, menu);
        return true;
    }

    //Method to deal with when delete button is clicked
    //Delete the ExerciseEntry that is currently selected on a background thread
    public boolean deleteClicked() {
        AsyncTaskDelete databaseDeletion = new AsyncTaskDelete();
        databaseDeletion.execute((int) Id);
        finish();
        return true;
    }

    // Extended AsyncTask method that deletes the exercise entry object in the background
    private class AsyncTaskDelete extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params)
        {
            dataSource.deleteExerciseEntry(params[0]);
            return null;
        }
    }
}
