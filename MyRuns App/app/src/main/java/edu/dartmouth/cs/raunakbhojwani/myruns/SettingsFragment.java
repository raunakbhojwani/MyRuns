package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.os.Bundle;
import android.preference.PreferenceFragment;


/* This SettingsFragment.java file controls the settings fragment on the main activity.
 * Though there isn't much code here, the layout described and designed in preferences.xml effectively handles
 * all the user interactions since this class is an extension of the preference fragment.
 *
 * @author:RaunakB
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
