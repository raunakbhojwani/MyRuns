package edu.dartmouth.cs.raunakbhojwani.myruns.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.dartmouth.cs.raunakbhojwani.myruns.EntriesDataSource;
import edu.dartmouth.cs.raunakbhojwani.myruns.ExerciseEntry;

/**
 * Created by RaunakBhojwani on 2/22/17.
 */

public class HistoryUploader {

    public static final String TAG = "DebugTag";
    public static String SERVER_ADDRESS = "https://indigo-aurora-159602.appspot.com";

    private Context mContext;

    public HistoryUploader (Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void syncHistory() {
        new AsyncTask<Void, Void, String>() {

            @Override
            // Get history and upload it to the server.
            protected String doInBackground(Void... arg0) {

                Log.d(TAG, "Syncing History");

                EntriesDataSource mDatasource = new EntriesDataSource(mContext);

                try {
                    mDatasource.open();
                }catch (SQLException e) {
                    e.printStackTrace();
                }

                List<ExerciseEntry> fetchedEntries = mDatasource.fetchEntries();

                JSONArray jsonArray = new JSONArray();
                for (ExerciseEntry entry: fetchedEntries) {
                    jsonArray.put(entry.convertToJSON());
                }

                String uploadState="";
                try {
                    //XD: A Map is a data structure consisting of a set of keys and values in which each key is mapped to a single value.
                    Map<String, String> params = new HashMap<>();
                    params.put("JSONKey", jsonArray.toString());
                    ServerUtilities.post(SERVER_ADDRESS+"/sync.do", params);
                    Log.d(TAG, "Synced!");
                } catch (IOException e) {
                    uploadState = "Sync failed: " + e.getCause();
                    Log.e("TAGG", "data posting error " + e);
                }

                return uploadState;
            }

            @Override
            protected void onPostExecute(String errString) {
                String resultString;
                if(errString.equals(""))
                    resultString =  "entry uploaded";
                else
                    resultString = errString;

                Log.d(TAG, resultString);
                Toast.makeText(mContext, resultString, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

}
