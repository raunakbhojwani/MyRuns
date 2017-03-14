package edu.dartmouth.cs.raunakbhojwani.myruns;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Calendar;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import android.location.Location;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ExerciseEntry.java is a class that defines what
 * information a workout entry should have.
 *
 * Essentially a helper class to keep all exercise data
 * in one data structure. Implemented as recommended in notes.
 *
 * Created by RaunakBhojwani on 2/1/17.
 * @author RaunakBhojwani
 */

public class ExerciseEntry {

    private static final String TAG = "DebugTag";

    private Long id;

    private String mInputType;         // Manual, GPS or Automatic
    private String mActivityType;      // Running, cycling etc.

    private Calendar mDateTime;        // Calendar

    private int mDuration;             // Exercise duration in SECONDS
    private double mDistance;          // Distance traveled. Either in meters or feet.
    private int mCalorie;              // Calories burnt
    private int mHeartRate;            // Heart rate

    private double mAvgPace;            // Average pace
    private double mAvgSpeed;           // Average speed
    private double mClimb;              // Climb. Either in meters or feet.
    private String mComment;            // Comments
    private ArrayList<LatLng> mLocationList; // Location list

    private Location mPreviousLocation;
    private double mCurrentSpeed;

    private ArrayList<String> mOverallActivityCount;


    //Constructor with no information, and default values
    public ExerciseEntry() {

        this.mInputType = "";
        this.mActivityType = "";

        this.mDateTime = Calendar.getInstance();

        this.mDuration = 0;
        this.mDistance = 0;
        this.mCalorie = 0;
        this.mHeartRate = 0;
        this.mAvgPace = 0;
        this.mAvgSpeed = 0;
        this.mClimb = 0;

        this.mComment = "";

        mLocationList = new ArrayList<>();
        mOverallActivityCount = new ArrayList<>();

        mCurrentSpeed = -1;
        mPreviousLocation = null;



    }

    //Constructor with specific information
    public ExerciseEntry(String inputType, String activityType, Calendar dateTime,
                         int duration, double distance, int calories, int heartRate) {

        this.mInputType = inputType;
        this.mActivityType = activityType;
        this.mDateTime = dateTime;
        this.mDuration = duration;
        this.mDistance = distance;
        this.mCalorie = calories;
        this.mHeartRate = heartRate;

        this.mAvgPace = 0;
        this.mAvgSpeed = 0;
        this.mClimb = 0;
        this.mComment = "";

        mLocationList = new ArrayList<>();
        mOverallActivityCount = new ArrayList<>();
        mCurrentSpeed = -1;
        mPreviousLocation = null;

    }


    //Getter and setter methods for each class variable
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getmInputType() {
        return mInputType;
    }

    public void setmInputType(String inputType) {
        this.mInputType = inputType;
    }

    public String getmActivityType() {
        return mActivityType;
    }

    public void setmActivityType(String activityType) {
        this.mActivityType = activityType;
    }

    public Calendar getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(Calendar dateTime) {
        this.mDateTime = dateTime;
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int duration) {
        this.mDuration = duration;
    }

    //Update the duration of activity by checking amount of time passed
    //Update average speed accordingly
    public void updateDuration() {
        mDuration = (int) ((System.currentTimeMillis()
                - mDateTime.getTimeInMillis())/1000);

        if (mDuration != 0) {
            mAvgSpeed = mDistance/mDuration;
        }
    }

    public double getmDistance() {
        return mDistance;
    }

    public void setmDistance(double distance) {
        this.mDistance = distance;
    }

    public int getmCalorie() {
        return mCalorie;
    }

    public void setmCalorie(int calorie) {
        this.mCalorie = calorie;
    }

    public int getmHeartRate() {
        return mHeartRate;
    }

    public void setmHeartRate(int heartRate) {
        this.mHeartRate = heartRate;
    }

    public double getmAvgPace() {
        return mAvgPace;
    }

    public void setmAvgPace(double avgPace) {
        this.mAvgPace = avgPace;
    }

    public double getmAvgSpeed() {
        return mAvgSpeed;
    }

    public void setmAvgSpeed(double avgSpeed) {
        this.mAvgSpeed = avgSpeed;
    }

    public double getmClimb() {
        return mClimb;
    }

    public void setmClimb(double climb) {
        this.mClimb = climb;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String comment) {
        this.mComment = comment;
    }

    public ArrayList<LatLng> getmLocationList() {
        return mLocationList;
    }

    //Convert byteArray into latlng items for the LocationList
    public void setmLocationList(byte[] byteArray) {

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();

        int[] intArray = new int[byteArray.length/Integer.SIZE];
        intBuffer.get(intArray);

        int position = intArray.length/2;

        for (int i = 0; i < position; i++) {
            LatLng latlong = new LatLng((double) intArray[i*2]/1E6F, (double) intArray[i*2+1]/1E6F);
            mLocationList.add(latlong);
        }
    }

    //Convert the LatLng from LocationList into a ByteArray
    public byte[] getByteArrayLocation() {

        int[] intArray = new int[2* mLocationList.size()];

        for (int i =0; i<mLocationList.size(); i++) {
            intArray[i*2] = (int) (mLocationList.get(i).latitude * 1E6);
            intArray[i*2 + 1] = (int) (mLocationList.get(i).longitude * 1E6);
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(intArray.length * Integer.SIZE);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(intArray);

        return byteBuffer.array();
    }

    //Add a location to the location list, updating distance,
    //calories, climb and speed accordingly
    public void insertLocation(Location location) {

        mLocationList.add(new LatLng(location.getLatitude(),
                location.getLongitude()));

        if (mPreviousLocation == null) {
            setmDistance(0);
            setmCalorie(0);
            setmAvgPace(0);
            setmAvgSpeed(0);
            setmClimb(0);
        }
        else {
            mDistance += Math.abs(location.distanceTo(mPreviousLocation));
            mCalorie = (int) (mDistance / 15.0);
            mClimb += location.getAltitude() - mPreviousLocation.getAltitude();
        }

        updateDuration();
        mCurrentSpeed = location.getSpeed();
        mPreviousLocation = location;

        Log.d(TAG, "Location inserted into EE object");

    }

    public double getmCurrentSpeed() {
        return this.mCurrentSpeed;
    }

    public ArrayList<String> getmOverallActivityCount() {
        return mOverallActivityCount;
    }

    public void addTomOverallActivityCount(String activity_type) {
        if (mOverallActivityCount != null) {
            mOverallActivityCount.add(activity_type);
        }
    }

    public JSONObject convertToJSON() {
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("id", id);
            jsonObject.put("inputType", mInputType);
            jsonObject.put("activityType", mActivityType);
            jsonObject.put("dateTime", mDateTime.getTimeInMillis());
            jsonObject.put("duration", mDuration);
            jsonObject.put("distance", mDistance);
            jsonObject.put("calorie", mCalorie);
            jsonObject.put("heartrate", mHeartRate);
            jsonObject.put("avgSpeed", mAvgSpeed);
            jsonObject.put("climb", mClimb);
            jsonObject.put("comment", mComment);

        }
        catch (JSONException e) {
            Log.d(TAG, "JSON Failure");
            return null;
        }
        return jsonObject;
    }

}
