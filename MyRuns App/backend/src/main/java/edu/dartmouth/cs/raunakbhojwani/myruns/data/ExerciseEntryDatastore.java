package edu.dartmouth.cs.raunakbhojwani.myruns.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by RaunakBhojwani on 2/22/17.
 */

public class ExerciseEntryDatastore {

    private static final Logger mLogger = Logger.
            getLogger(ExerciseEntryDatastore.class.getName());

    private static final DatastoreService mDatastore =
            DatastoreServiceFactory.getDatastoreService();

    public static boolean addToDatastore(JSONObject jsonObject) throws JSONException{

        ExerciseEntry exerciseEntry = new ExerciseEntry();
        exerciseEntry.convertFromJSON(jsonObject);

        Entity entity = new Entity("ExerciseEntry", exerciseEntry.getmId());

        entity.setProperty("rowId", exerciseEntry.getmId());
        entity.setProperty("inputType", exerciseEntry.getmInputType());
        entity.setProperty("activityType", exerciseEntry.getmActivityType());
        entity.setProperty("dateTime", exerciseEntry.getmDateTimeInMillis());
        entity.setProperty("duration", exerciseEntry.getmDuration());
        entity.setProperty("distance", exerciseEntry.getmDistance());
        entity.setProperty("calorie", exerciseEntry.getmCalorie());
        entity.setProperty("heartrate", exerciseEntry.getmHeartRate());
        entity.setProperty("avgSpeed", exerciseEntry.getmAvgSpeed());
        entity.setProperty("climb", exerciseEntry.getmClimb());
        entity.setProperty("comment", exerciseEntry.getmComment());

        mDatastore.put(entity);

        return true;
    }

    public static boolean deleteId(String Id) {

        Query deleteQuery = new Query("ExerciseEntry");
        Filter deleteFilter = new Query.FilterPredicate("rowId", Query.FilterOperator.EQUAL, Long.parseLong(Id));


        deleteQuery.setFilter(deleteFilter);

        PreparedQuery preparedQuery = mDatastore.prepare(deleteQuery);
        Entity entity = preparedQuery.asSingleEntity();

        if(entity != null) {
            mDatastore.delete(entity.getKey());
            return true;
        }
        return false;

    }

    public static List<ExerciseEntry> retrieveEntries() {
        Query fetchQuery = new Query("ExerciseEntry");
        fetchQuery.setFilter(null);

        PreparedQuery preparedQuery = mDatastore.prepare(fetchQuery);
        List<ExerciseEntry> fetched = new ArrayList<>();

        for (Entity entity : preparedQuery.asIterable()) {

            ExerciseEntry newEntry = new ExerciseEntry();

            newEntry.setmId((Long) entity.getProperty("rowId"));
            newEntry.setmInputType((String) entity.getProperty("inputType"));
            newEntry.setmActivityType((String) entity.getProperty("activityType"));
            newEntry.setmDateTime((Long) entity.getProperty("dateTime"));
            newEntry.setmDuration(Integer.parseInt(""+(Long) entity.getProperty("duration")));
            newEntry.setmDistance((Double) entity.getProperty("distance"));
            newEntry.setmCalorie(Integer.parseInt(""+(Long) entity.getProperty("calorie")));
            newEntry.setmHeartRate(Integer.parseInt(""+(Long) entity.getProperty("heartrate")));
            newEntry.setmAvgSpeed((Double) entity.getProperty("avgSpeed"));
            newEntry.setmClimb((Double) entity.getProperty("climb"));
            newEntry.setmComment((String) entity.getProperty("comment"));

            fetched.add(newEntry);
        }

        return fetched;
    }

    public static boolean deleteDatastore() {
        boolean deleteStatus = true;
        for (ExerciseEntry exerciseEntry: retrieveEntries()) {
            deleteStatus = deleteId(exerciseEntry.getmId().toString());
        }
        return deleteStatus;
    }


}
