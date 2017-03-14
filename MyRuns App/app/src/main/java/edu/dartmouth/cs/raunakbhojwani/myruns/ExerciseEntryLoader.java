package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.sql.SQLException;
import java.util.List;

/**
 * AsyncTask Loader that loads all entries from database using AsyncTasks
 * Making sure it takes place in the background and doesn't hold up the UI
 *
 * Created by RaunakBhojwani on 2/1/17.
 * @author RaunakBhojwani
 */

public class ExerciseEntryLoader extends AsyncTaskLoader<List<ExerciseEntry>>{

    public Context context;

    public ExerciseEntryLoader(Context context) {
        super(context);
        this.context = context;
    }

    //Force load the loader
    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    //Open the database and fetch all the entries in the background
    @Override
    public List<ExerciseEntry> loadInBackground() {

        EntriesDataSource dataSource = new EntriesDataSource(this.context);

        try {
            dataSource.open();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return dataSource.fetchEntries();
    }

}
