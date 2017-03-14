package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import java.util.ArrayList;

import com.meapsoft.FFT;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * LocationService.java is a class that implements
 * the LocationListener service and uses the BroadcastReceiver in
 * the GPSActivity to communicate whenever the user's location changes.
 *
 *
 * Created by RaunakBhojwani on 2/8/17.
 * @author RaunakBhojwani
 */

public class LocationService extends Service implements SensorEventListener, LocationListener {

    private static final String TAG = "DebugTag";
    public final static String ACTIVITY = "activity_type";

    private ExerciseEntry mExerciseEntry;
    private LocationManager mLocationManager;
    private NotificationManager mNotificationManager;

    private boolean isTracking;
    private boolean onSensorChanging = false;

    private String finalActivityType = "Other";

    private final IBinder binder = new TrackingServiceBinder();

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private OnSensorChangedTask mAsyncTask;
    private static ArrayBlockingQueue<Double> mAccBuffer;

    public static final int ACCELEROMETER_BUFFER_CAPACITY = 2048;
    public static final int ACCELEROMETER_BLOCK_CAPACITY = 64;

    public class TrackingServiceBinder extends Binder {

        public ExerciseEntry getExerciseEntry() {
            return mExerciseEntry;
        }

        LocationService getService() {
            return LocationService.this;
        }

    }


    //Whenever the location updates, add a new location to
    //the exercise entry object and send an update that will draw the trace.
    @Override
    public void onLocationChanged(Location location) {
        if (location!=null && mExerciseEntry != null) {
            mExerciseEntry.insertLocation(location);
            broadcastLocationUpdate();
            Log.d(TAG, "Location Updated");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            double m = Math.sqrt(event.values[0] * event.values[0]
            + event.values[1] * event.values[1] + event.values[2] * event.values[2]);

            // Inserts the specified element into this queue if it is possible
            // to do so immediately without violating capacity restrictions,
            // returning true upon success and throwing an IllegalStateException
            // if no space is currently available. When using a
            // capacity-restricted queue, it is generally preferable to use
            // offer.

            try{
                mAccBuffer.add(new Double(m));
            }
            catch (IllegalStateException e) {

                // Exception happens when reach the capacity.
                // Doubling the buffer. ListBlockingQueue has no such issue,
                // But generally has worse performance

                ArrayBlockingQueue<Double> newBuffer = new
                        ArrayBlockingQueue<Double>(mAccBuffer.size() * 2);

                mAccBuffer.drainTo(newBuffer);
                mAccBuffer = newBuffer;
                mAccBuffer.add(new Double(m));
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onCreate() {

        isTracking = false;
        super.onCreate();
        mAccBuffer= new ArrayBlockingQueue<>(ACCELEROMETER_BUFFER_CAPACITY);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTracking(intent);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        startTracking(intent);
        return binder;
    }

    @Override
    public void onDestroy() {
        isTracking = false;
        stopLocationUpdates();
        mNotificationManager.cancelAll();

        if (onSensorChanging) {
            mAsyncTask.cancel(true);
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        super.onDestroy();
    }

    // Start tracking the users location using the PendingIntent, Locaiton Manager
    // requestLocationUpdates.
    private void startTracking(Intent intent) {

        if (!(isTracking)) {
            isTracking = true;

            Intent GPSintent = new Intent(getApplicationContext(), MainActivity.class);
            GPSintent.setAction(Intent.ACTION_MAIN);
            GPSintent.addCategory(Intent.CATEGORY_LAUNCHER);
            GPSintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent GPSPendingIntent = PendingIntent
                    .getActivity(getBaseContext(), 0, GPSintent, 0);

            Notification GPSNotification = new Notification.Builder(this)
                    .setContentTitle("MyRuns4-Raunak")
                    .setContentText("Recording your path now")
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(GPSPendingIntent)
                    .build();

            GPSNotification.flags |= Notification.FLAG_ONGOING_EVENT;
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(5, GPSNotification);




            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(true);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            String provider = mLocationManager.getBestProvider(criteria, true);
            Log.d(TAG, "Provider: " + provider);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(provider, 0, 0, LocationService.this);
            }


            mExerciseEntry = new ExerciseEntry();
            mExerciseEntry.setmActivityType(intent.getExtras().getString(ACTIVITY));
            if (mExerciseEntry.getmActivityType().equals("Unknown")) {

                mExerciseEntry.setmInputType("Automatic");

                onSensorChanging = true;
                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

                mAsyncTask = new OnSensorChangedTask();
                mAsyncTask.execute();


            }
            else {
                mExerciseEntry.setmInputType("GPS");
            }
        }
    }

    //Broadast these location updates to GPS activity so that they can be traced on the Google Map
    private void broadcastLocationUpdate() {
        Intent locationUpdateIntent = new Intent(GPSActivity.UpdatedLocationReceiver.class.getName());
        locationUpdateIntent.putExtra("update", true);
        this.sendBroadcast(locationUpdateIntent);

    }

    // Stop the location updates
    private void stopLocationUpdates() {
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            mLocationManager.removeUpdates(this);;
        }
    }

    private class OnSensorChangedTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            ArrayList<Double> featureVector = new ArrayList<>(ACCELEROMETER_BLOCK_CAPACITY + 1);
            int blockSize = 0;
            FFT fft = new FFT(ACCELEROMETER_BLOCK_CAPACITY);
            double[] accBlock = new double[ACCELEROMETER_BLOCK_CAPACITY];
            double[] re = accBlock;
            double[] im = new double[ACCELEROMETER_BLOCK_CAPACITY];

            double max = Double.MIN_VALUE;

            while (true) {
                try {
                    // need to check if the AsyncTask is cancelled or not in the while loop
                    if (isCancelled() == true) {
                        return null;
                    }

                    // Dumping buffer
                    accBlock[blockSize++] = mAccBuffer.take().doubleValue();

                    if (blockSize == ACCELEROMETER_BLOCK_CAPACITY) {
                        blockSize = 0;

                        // time = System.currentTimeMillis();
                        max = .0;
                        for (double val : accBlock) {
                            if (max < val) {
                                max = val;
                            }
                        }

                        fft.fft(re, im);

                        for (int i = 0; i < re.length; i++) {
                            double mag = Math.sqrt(re[i] * re[i] + im[i]
                                    * im[i]);
                            featureVector.add(Double.valueOf(mag));
                            im[i] = .0; // Clear the field
                        }

                        // Append max after frequency component
                        featureVector.add(Double.valueOf(max));

                        int classifiedInference = (int) WekaClassifier
                                .classify(featureVector.toArray());

                        switch (classifiedInference) {
                            case 0:
                                finalActivityType = "Standing";
                                break;
                            case 1:
                                finalActivityType = "Walking";
                                break;
                            case 2:
                                finalActivityType = "Running";
                                break;
                            default:
                                break;
                        }

                        featureVector.clear();

                        mExerciseEntry.setmActivityType(finalActivityType);
                        mExerciseEntry.addTomOverallActivityCount(finalActivityType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}