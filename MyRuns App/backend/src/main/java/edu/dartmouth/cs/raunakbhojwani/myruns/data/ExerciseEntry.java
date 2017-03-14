package edu.dartmouth.cs.raunakbhojwani.myruns.data;

import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.codehaus.jackson.map.util.JSONPObject;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by RaunakBhojwani on 2/22/17.
 */

@Entity
public class ExerciseEntry {

    @Id
    public Long mId;

    public String mInputType;         // Manual, GPS or Automatic
    public String mActivityType;      // Running, cycling etc.

    public int mDuration;             // Exercise duration in SECONDS
    public double mDistance;          // Distance traveled. Either in meters or feet.
    public int mCalorie;              // Calories burnt
    public int mHeartRate;            // Heart rate

    public double mAvgPace;            // Average pace
    public double mAvgSpeed;           // Average speed
    public double mClimb;              // Climb. Either in meters or feet.
    public double mCurrentSpeed;
    public String mComment;            // Comments

    public String formattedDate;
    @Index
    public Calendar mDateTime;        // Calendar

    public ExerciseEntry() {
        this.mInputType = "";
        this.mActivityType = "";
        this.mDuration = 0;
        this.mDistance = 0;
        this.mCalorie = 0;
        this.mHeartRate = 0;

        this.mAvgPace = 0;
        this.mAvgSpeed = 0;
        this.mClimb = 0;
        this.mCurrentSpeed = 0;
        this.mComment = "";
        this.formattedDate = "";

        this.mDateTime = Calendar.getInstance();
    }

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

        mCurrentSpeed = -1;

    }

    //Getter and setter methods for each class variable
    public Long getmId() {
        return mId;
    }

    public void setmId(Long id) {
        this.mId = id;
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

    public String getDateTime(long time, String dateTime) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        return dateFormat.format(calendar.getTime());
    }

    public long getmDateTimeInMillis() {
        return mDateTime.getTimeInMillis();
    }

    public void setmDateTime(long dateTime) {
        this.mDateTime.setTimeInMillis(dateTime);
        this.formattedDate = getDateTime(dateTime, "hh:mm:ss MM dd yyyy");
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int duration) {
        this.mDuration = duration;
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

    public JSONObject convertFromJSON(JSONObject jsonObject) {

        try {
            setmId(jsonObject.getLong("id"));
            setmInputType(jsonObject.getString("inputType"));
            setmActivityType(jsonObject.getString("activityType"));
            setmDateTime(jsonObject.getLong("dateTime"));
            setmDuration(jsonObject.getInt("duration"));
            setmDistance(jsonObject.getDouble("distance"));
            setmCalorie(jsonObject.getInt("calorie"));
            setmHeartRate(jsonObject.getInt("heartrate"));
            setmAvgSpeed(jsonObject.getDouble("avgSpeed"));
            setmClimb(jsonObject.getDouble("climb"));
            setmComment(jsonObject.getString("comment"));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonObject;
    }

}
