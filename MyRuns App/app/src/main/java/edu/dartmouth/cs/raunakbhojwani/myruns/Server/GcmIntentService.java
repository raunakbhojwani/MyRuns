package edu.dartmouth.cs.raunakbhojwani.myruns.Server;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.os.Handler;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.dartmouth.cs.raunakbhojwani.myruns.EntriesDataSource;

/**
 * Created by RaunakBhojwani on 2/22/17.
 */

public class GcmIntentService extends IntentService {

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) { // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.d("TAGG","message");
                showToast(extras.getString("message"));

                String messageReceived = extras.getString("message");
                String [] deletionMessage = messageReceived.split(":");

                EntriesDataSource mDatasource = new EntriesDataSource(this);

                try {
                    mDatasource.open();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }

                Log.d("TAGG", "Deletion Message Length"+deletionMessage.length);

                if (deletionMessage.length == 2) {
                    if (deletionMessage[0].equals("DeleteThis")) {
                        mDatasource.deleteExerciseEntry(Integer.parseInt(deletionMessage[1]));
                        Log.d("TAGG", "Deleted the entry");
                    }
                }

            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}