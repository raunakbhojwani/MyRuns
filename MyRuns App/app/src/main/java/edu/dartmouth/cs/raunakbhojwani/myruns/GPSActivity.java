package edu.dartmouth.cs.raunakbhojwani.myruns;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * GPSActivity.java (MapDisplayActivity) is a class that extends FragmentActivity and is
 * used to implement the Google Maps part of the MyRuns application.
 * Displays previously collected information from HistoryFragment, OR
 * tracks new data when the user is collecting information
 *
 * Created by RaunakBhojwani on 2/8/17.
 * @author RaunakBhojwani
 */

public class GPSActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EntriesDataSource mDataSource;
    private ExerciseEntry mExerciseEntry;

    private LocationService mLocationService;
    private Intent mTrackingIntent;
    private Marker mStartMarker;
    private Marker mEndMarker;
    private Polyline mMapTrace;
    private UpdatedLocationReceiver mUpdatedLocationReceiver;

    private boolean isBound;
    private boolean isNewTask;
    private boolean isZoomed;

    private TextView ActivityTypeTextView;
    private TextView AvgSpeedTextView;
    private TextView CurrentSpeedTextView;
    private TextView ClimbTextView;
    private TextView CalorieTextView;
    private TextView DistanceTextView;


    private static final String TAG = "DebugTag";
    private final static String ACTIVITY = "activity_type";
    private final static String TASK = "TASK_TYPE";
    private final static String NEW_TASK = "NEW";
    private final static String ROW_ID = "ROW_ID";


    /**
     * Inspired by:
     * http://stackoverflow.com/questions/20673620/how-to-trigger-broadcast-receiver-when-gps-is-turn-on-off
     * http://stackoverflow.com/questions/5240246/broadcastreceiver-for-location
     *
     * Used as a means of conveying changes in location to the GPSActivity from LocationService,
     * which in turn accesses the LocationList from the ExerciseEntry object.
     */


    //Receive a broadcast indicating a change in location
    public class UpdatedLocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "Location Change Broadcast Received");
            updateTrace();          // update the trace on the map
            updateExerciseStats();  // Update the data from the Exercise Entry

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        // Retrieve the map fragment and check if it is ready
        if (mMap == null) {
            Log.d(TAG, "Map Set Up Accessed");
            SupportMapFragment mapFragment = (SupportMapFragment)
                    getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        // Try to open the database
        mDataSource = new EntriesDataSource(this);
        try {
            mDataSource.open();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Set up the broadcast receiver which will receive updates from LocationService.
        mUpdatedLocationReceiver = new UpdatedLocationReceiver();
        Intent locationIntent = getIntent();
        Bundle locationExtras = locationIntent.getExtras();

        // if the locationExtras are null, cancel all the notifications
        if (locationExtras == null) {
            NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notifManager.cancelAll();
            finish();
            return;
        }

        if(locationExtras.getString(TASK).equals(NEW_TASK)){
            isNewTask = true;
        }
        else {
            isNewTask = false;
        }

        // Access the text views for all the TextViews on the map
        ActivityTypeTextView = (TextView) findViewById(R.id.map_activity_type);
        AvgSpeedTextView = (TextView) findViewById(R.id.map_avg_speed);
        CurrentSpeedTextView = (TextView) findViewById(R.id.map_current_speed);
        ClimbTextView = (TextView) findViewById(R.id.map_climb);
        CalorieTextView = (TextView) findViewById(R.id.map_calories);
        DistanceTextView = (TextView) findViewById(R.id.map_distance);

        //If it is a new task, start tracking the activity type, if it is an old one,
        //get rid of the save and cancel buttons and try to read the location from database
        if (isNewTask) {
            String activityType = locationExtras.getString(ACTIVITY);
            startTracking(activityType);
        }
        else {
            (findViewById(R.id.map_save_button)).setVisibility(View.GONE);
            (findViewById(R.id.map_cancel_button)).setVisibility(View.GONE);

            long rowId = locationExtras.getLong(ROW_ID);
            try {
                mExerciseEntry = mDataSource.fetchEntryByIndex(rowId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //When the app is being paused, unbind the service and unregister the broadcast receiver
    @Override
    protected void onPause() {

        if (isNewTask) {
            unregisterReceiver(mUpdatedLocationReceiver);
        }

        if (isBound) {
            unbindService(mServiceConnection);
            isBound = false;
        }

        super.onPause();
    }

    // Check if the app is being closed or if the orientation is changing
    // if it is, stop tracking the user
    @Override
    protected void onDestroy (){
        super.onDestroy();
        if (isFinishing()) {
            stopTracking();
        }
    }

    //If save button is clicked, check if ExerciseEntry isnt null and update the duration
    //and add it to the database
    public void onSaveClicked(View v) {

        v.setEnabled(false);

        if (mExerciseEntry != null) {

            //If there is an overall activity count, check which has
            //the majority and set that as the final activity type
            if (mExerciseEntry.getmOverallActivityCount()!= null &&
                    mExerciseEntry.getmOverallActivityCount().size() > 0) {

                ArrayList<String> overallActivityCount = mExerciseEntry.getmOverallActivityCount();

                int activity_running = 0, activity_walking = 0,
                        activity_standing = 0, activity_unknown = 0;
                String finalActivityType;

                for (int i = 0; i < overallActivityCount.size(); i++) {
                    if (overallActivityCount.get(i).equals("Running")) {
                        activity_running++;
                    }
                    else if (overallActivityCount.get(i).equals("Walking")) {
                        activity_walking++;
                    }
                    else if (overallActivityCount.get(i).equals("Standing")) {
                        activity_standing++;
                    }
                    else {
                        activity_unknown++;
                    }
                }

                if (activity_running > activity_walking &&
                        activity_running > activity_standing &&
                        activity_running > activity_unknown) {
                    finalActivityType = "Running";
                }
                else if (activity_walking > activity_running &&
                        activity_walking > activity_standing &&
                        activity_walking > activity_unknown) {
                    finalActivityType = "Walking";
                }
                else if (activity_standing > activity_running &&
                        activity_standing > activity_walking &&
                        activity_standing > activity_unknown) {
                    finalActivityType = "Standing";
                }
                else {
                    finalActivityType = "Unknown";
                }

                mExerciseEntry.setmActivityType(finalActivityType);
            }

            mExerciseEntry.updateDuration();
            new AsyncTaskAdd().execute(mExerciseEntry);
        }

        stopTracking();
        finish();
    }

    //Stop tracking the location and close the app
    public void onCancelClicked(View v) {
        v.setEnabled(false);
        stopTracking();
        finish();
    }

    //Stop tracking the location and go back
    @Override
    public void onBackPressed() {
        stopTracking();
        super.onBackPressed();
    }

    //If it isn't a new task, give the option to delete it
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem menuitem;
        if (!isNewTask) {
            menuitem = menu.add(Menu.NONE, 0, 0, "Delete");
            menuitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                //Handle deletetion
                if (mExerciseEntry != null) {
                    mDataSource.deleteExerciseEntry((int) (long) mExerciseEntry.getId());
                }
                finish();
                return true;
            default:
                finish();
                return false;
        }
    }

    // Register the broadcast receiver and update the location
    // trace as well as the on screen stats
    @Override
    protected void onResume() {
        super.onResume();
        if (isNewTask) {
            IntentFilter intentFilter = new IntentFilter(UpdatedLocationReceiver.class.getName());
            registerReceiver(mUpdatedLocationReceiver, intentFilter);
            updateTrace(); // update the trace on the map
            updateExerciseStats(); // Update the data from the Exercise Entry
        }

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder service) {

            LocationService.TrackingServiceBinder serviceBinder = (LocationService.TrackingServiceBinder) service;

            // if the tracking service is connected save the service
            mLocationService = serviceBinder.getService();
            // if the tracking service is connected save the exercise entry
            mExerciseEntry = serviceBinder.getExerciseEntry();
        }

        public void onServiceDisconnected(ComponentName name) {
            // if the tracking service is disconnected, set tracking service to nulll
            mLocationService = null;
        }

    };

    //When it starts tracking, start the service and bind it to this connection
    private void startTracking(String activityType) {
        mTrackingIntent = new Intent(this, LocationService.class);
        mTrackingIntent.putExtra(ACTIVITY, activityType);


        startService(mTrackingIntent);
        bindService(mTrackingIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        isBound = true;

    }

    //When it stops tracking, unbind the connection and stop the service
    private void stopTracking() {

        if (mLocationService != null) {
            if (isBound) {
                unbindService(mServiceConnection);
                isBound = false;
            }
            stopService(mTrackingIntent);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near DUBAI, UAE
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(new LatLng(25.2, 55.3)).title("Dubai Marker"));
        mMapTrace = mMap.addPolyline(new PolylineOptions());
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0,0)));
        updateTrace();          // update the trace on the map
        updateExerciseStats();  // Update the data from the Exercise Entry

    }

    private void updateTrace() {
        if (!(mExerciseEntry == null || mExerciseEntry.getmLocationList().size() == 0)) {

            // Retrieve the location list from the Entry object.
            ArrayList<LatLng> locationTrace = mExerciseEntry.getmLocationList();

            // Retrieve the start and endpoints of the locationTrace by indexing
            LatLng locationStart = locationTrace.get(0);
            LatLng locationEnd = locationTrace.get(locationTrace.size() - 1);

            //Draw these endpoints on the map using the drawables
            if (mStartMarker == null) {
                mStartMarker = mMap.addMarker(new MarkerOptions().position(locationStart)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.green_dot)));
            }
            else {
                mStartMarker.setPosition(locationStart);
            }

            mMapTrace.setPoints(locationTrace);

            if (mEndMarker == null) {
                mEndMarker = mMap.addMarker(new MarkerOptions().position(locationEnd)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot)));
            }
            else {
                mEndMarker.setPosition(locationEnd);
            }

            // Move the camera to appropriate points
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationEnd));
            if(!isZoomed){
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                isZoomed = true;
            }

        }
    }

    private void updateExerciseStats() {
        if (mExerciseEntry != null) {

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String type = "Type: "+(mExerciseEntry.getmActivityType());
            String currentSpeed, avgSpeed, climb, distance;
            String calorie = "Calorie: "
                    + decimalFormat.format(mExerciseEntry.getmCalorie());

            if(HistoryFragment.unitsInKMCheck(getApplicationContext())){
                currentSpeed = "Current speed: "
                        + decimalFormat.format(mExerciseEntry.getmCurrentSpeed()* 36 / 10) + " km/h";

                avgSpeed = "Average speed: "
                        + decimalFormat.format(mExerciseEntry.getmAvgSpeed() * 36 / 10) + " km/h";

                climb = "Climb: "
                        + decimalFormat.format(mExerciseEntry.getmClimb()/1000) + " km" ;

                distance = "Distance: "
                        + decimalFormat.format(mExerciseEntry.getmDistance()/1000) + " km";

            }
            else {
                currentSpeed = "Current speed: "
                        + decimalFormat.format((mExerciseEntry.getmCurrentSpeed()* 36 / 10)/1.6) + " miles/h";

                avgSpeed = "Average speed: "
                        + decimalFormat.format((mExerciseEntry.getmAvgSpeed() * 36 / 10 )/1.6)+ " miles/h";

                climb = "Climb: "
                        + decimalFormat.format(mExerciseEntry.getmClimb()/1600) + " miles" ;

                distance = "Distance: "
                        + decimalFormat.format(mExerciseEntry.getmDistance()/1600) + " miles";
            }


            ActivityTypeTextView.setText(type);
            AvgSpeedTextView.setText(avgSpeed);
            CurrentSpeedTextView.setText(currentSpeed);
            ClimbTextView.setText(climb);
            CalorieTextView.setText(calorie);
            DistanceTextView.setText(distance);

        }

    }

    private class AsyncTaskAdd extends AsyncTask<ExerciseEntry, Void, String>
    {
        @Override
        protected String doInBackground(ExerciseEntry... params)
        {

            long Id = mDataSource.createExerciseEntry(params[0]);
            return ""+Id;

        }


        //OnPostExecute: Send entry saved toast notification
        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(getApplicationContext(), "Entry #" + result + " saved.", Toast.LENGTH_SHORT).show();
        }
    }

}