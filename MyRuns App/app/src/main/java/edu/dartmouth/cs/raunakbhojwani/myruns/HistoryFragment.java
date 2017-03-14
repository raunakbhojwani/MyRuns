package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.LoaderManager;


/* This HistoryFragment.java file controls the history fragment on the main activity.
 * It displays all the user entries that have been stored in the database
 *
 * @author:RaunakB
 * February 2017
 */
public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ExerciseEntry>> {

    private static android.app.LoaderManager loaderManager;
    private static CustomArrayAdapter exerciseEntryAdapter;
    public static List<ExerciseEntry> exerciseEntries;
    private static ArrayList<FetchedEntry> storedExerciseEntries;

    public final static String  ID = "id",
            INPUT = "inputtype",
            ACTIVITY = "activitytype",
            DATEANDTIME = "dateandtime",
            DURATION = "duration",
            DISTANCE = "distance",
            CALORIES = "calories",
            HEARTRATE = "heartrate";

    private static final String TAG = "DebugTag";
    private final static String TASK = "TASK_TYPE";
    private final static String ROW_ID = "ROW_ID";
    private final static String HISTORY = "HISTORY";


    public static Context mContext;
    public static View mView;
    boolean unitIsKM;
    private int seconds, MINUTES, SECONDS;
    private double distance, DISTANCE_ROUNDED;

    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storedExerciseEntries = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);
        exerciseEntryAdapter = new CustomArrayAdapter(getActivity(), storedExerciseEntries);

        ListView history_frag_list_view = (ListView) mView.findViewById(R.id.history_frag_list_view);
        history_frag_list_view.setAdapter(exerciseEntryAdapter);

        history_frag_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (exerciseEntries.get(position).getmInputType().equals("Manual Entry")) {

                    seconds = exerciseEntries.get(position).getmDuration();
                    SECONDS = seconds%60;
                    MINUTES = (seconds - SECONDS)/60;

                    Intent displayEntryIntent = new Intent(getActivity(), DisplayEntryActivity.class);
                    displayEntryIntent.putExtra(ID, exerciseEntries.get(position).getId());
                    displayEntryIntent.putExtra(INPUT, exerciseEntries.get(position).getmInputType());
                    displayEntryIntent.putExtra(ACTIVITY, exerciseEntries.get(position).getmActivityType());
                    displayEntryIntent.putExtra(DATEANDTIME, getDate(exerciseEntries.get(position).getmDateTime().getTimeInMillis(), "hh:mm:ss MMM dd yyyy"));
                    displayEntryIntent.putExtra(DURATION, MINUTES+" minutes " + SECONDS + " seconds");
                    displayEntryIntent.putExtra(CALORIES, exerciseEntries.get(position).getmCalorie()+" calories");
                    displayEntryIntent.putExtra(HEARTRATE, exerciseEntries.get(position).getmHeartRate()+" beats per minute");

                    //Depending on the unitIsKM display in miles or kilometers
                    if(unitIsKM) {
                        distance =  exerciseEntries.get(position).getmDistance();
                        DISTANCE_ROUNDED =  Math.round(distance * 100.0 *1.61) / 100.0;
                        displayEntryIntent.putExtra(DISTANCE, DISTANCE_ROUNDED+" Kilometers");
                    }

                    else {
                        distance =  exerciseEntries.get(position).getmDistance();
                        DISTANCE_ROUNDED =  Math.round(distance * 100.0) / 100.0;

                        displayEntryIntent.putExtra(DISTANCE, DISTANCE_ROUNDED+" Miles");
                    }

                    startActivity(displayEntryIntent);

                }
                else {
                    Intent mapEntryIntent = new Intent(getActivity().getBaseContext(), GPSActivity.class);
                    mapEntryIntent.putExtra(ROW_ID, exerciseEntries.get(position).getId());
                    mapEntryIntent.putExtra(TASK, HISTORY);

                    startActivity(mapEntryIntent);
                }

            }
        });

        loaderManager = getActivity().getLoaderManager();
        mContext = getActivity();
        return mView;
    }
    // Retrieves all the entries to be loaded into the History Fragment
    private ArrayList<FetchedEntry> retrieveEntries(List<ExerciseEntry> exerciseEntries)
    {
        HistoryFragment.exerciseEntries = exerciseEntries;
        unitIsKM = unitsInKMCheck(mContext);

        ArrayList<FetchedEntry> items = new ArrayList<FetchedEntry>();
        String header, summary;

        for(int i = 0; i< HistoryFragment.exerciseEntries.size(); i++)
        {

            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            header = HistoryFragment.exerciseEntries.get(i).getmInputType() + ": " + HistoryFragment.exerciseEntries.get(i).getmActivityType() + ", "
                    + getDate(HistoryFragment.exerciseEntries.get(i).getmDateTime().getTimeInMillis(), "hh:mm:ss MMM dd yyyy");

            if(unitIsKM) {
                seconds = HistoryFragment.exerciseEntries.get(i).getmDuration();
                SECONDS = seconds%60;
                MINUTES = (seconds - SECONDS)/60;

                distance =  HistoryFragment.exerciseEntries.get(i).getmDistance();
                DISTANCE_ROUNDED =  Math.round(distance * 1.61 * 100.0) / 100.0;

                if (exerciseEntries.get(i).getmInputType().equals("Manual Entry")) {
                    summary = (DISTANCE_ROUNDED) + " Kilometers, " + MINUTES
                            + "mins " + SECONDS + "secs";
                }
                else {
                    summary = decimalFormat.format(exerciseEntries.get(i).getmDistance()/1000) + "Kilometers";
                }

            }

            else {
                seconds = HistoryFragment.exerciseEntries.get(i).getmDuration();
                SECONDS = seconds%60;
                MINUTES = (seconds - SECONDS)/60;

                distance =  HistoryFragment.exerciseEntries.get(i).getmDistance();
                DISTANCE_ROUNDED =  Math.round(distance * 100.0) / 100.0;

                if (exerciseEntries.get(i).getmInputType().equals("Manual Entry")) {
                    summary = (DISTANCE_ROUNDED) + " Miles, " + MINUTES
                            + "mins " + SECONDS + "secs";
                }
                else {
                    summary = decimalFormat.format(exerciseEntries.get(i).getmDistance()/1600) + "Miles";
                }

            }

            items.add(new FetchedEntry(header, summary));
        }

        return items;
    }
    // Checks whether the unit preference is of km or miles
    public static boolean unitsInKMCheck(Context context) {

        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);

        String[] unitDisplayOptions = context.getResources().getStringArray(
                R.array.unit_preferences_array);

        String option = settings.getString("unit_preference_settings",
                "Miles");

        String option_metric = context.getString(R.string.metric);
        if (option.equals(option_metric))
            return true;
        else
            return false;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public void onResume() {
        updateHistoryFragEntries();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void updateHistoryFragEntries() {
        loaderManager.initLoader(1, null, this).forceLoad();
    }

    @Override
    public Loader<List<ExerciseEntry>> onCreateLoader(int id, Bundle args) {
        return new ExerciseEntryLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<List<ExerciseEntry>> loader, List<ExerciseEntry> data) {

        storedExerciseEntries.clear();
        storedExerciseEntries.addAll(retrieveEntries(data));
        exerciseEntryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<ExerciseEntry>> loader) {

        storedExerciseEntries.clear();
        exerciseEntryAdapter.notifyDataSetChanged();
    }

}
