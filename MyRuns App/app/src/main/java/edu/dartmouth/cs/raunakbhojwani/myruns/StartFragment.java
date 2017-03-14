package edu.dartmouth.cs.raunakbhojwani.myruns;


import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import edu.dartmouth.cs.raunakbhojwani.myruns.Server.HistoryUploader;

/* This StartFragment.java file controls the start fragment on the main activity.
 * It ostensibly allows the user to choose their type of input, whether manual, GPS or automatic,
 * and then choose what activity they are doing, whether running or otherwise.
 *
 * @author:RaunakB
 */

public class StartFragment extends Fragment {

    Button mStart, mSync;
    public final static String ACTIVITY = "activity_type";
    public final static String INPUT = "input_type";
    public final static String TASK = "TASK_TYPE";
    public final static String NEW_TASK = "NEW";

    HistoryUploader mHistoryUploader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View startView  = inflater.inflate(R.layout.fragment_start, container, false);
        mHistoryUploader = new HistoryUploader(getActivity());

        mStart = (Button) startView.findViewById(R.id.start_button);
        mSync = (Button) startView.findViewById(R.id.sync_button);

        final Spinner mInputType = (Spinner) startView.findViewById(R.id.input_type_spinner);
        final Spinner mActivityType = (Spinner) startView.findViewById(R.id.activity_type_spinner);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputType.getSelectedItem().toString().equals("Manual Entry")) {

                    Intent manualIntent = new Intent(getActivity().getBaseContext(), ManualEntryActivity.class);
                    manualIntent.putExtra(ACTIVITY, mActivityType.getSelectedItem().toString());
                    startActivity(manualIntent);

                }
                else {
                    Intent gpsIntent = new Intent(getActivity().getBaseContext(), GPSActivity.class);

                    String activity_type = mActivityType.getSelectedItem().toString();
                    if (mInputType.getSelectedItem().toString().equals("Automatic")) {
                        activity_type = "Unknown";
                    }

                    gpsIntent.putExtra(ACTIVITY, activity_type);
                    gpsIntent.putExtra(INPUT, mInputType.getSelectedItem().toString());
                    gpsIntent.putExtra(TASK, NEW_TASK);

                    startActivity(gpsIntent);
                }
            }
        });
        mSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHistoryUploader.syncHistory();
            }
        });

        return startView;
    }

}
