package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import java.util.ArrayList;

/* This MyRunsViewPagerAdapter.java file defines the adapter that controls the sliding tab layout.
 * Inspired by the demo code shown in the class lectures
 *
 * @author:RaunakB
 */

public class MyRunsViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public static final int START=0, HISTORY=1, SETTINGS=2;
    public static final String UI_TAB_START="START", UI_TAB_HISTORY="HISTORY", UI_TAB_SETTINGS="SETTINGS";

    public MyRunsViewPagerAdapter(FragmentManager fragManager, ArrayList<Fragment> fragments) {
        super(fragManager);
        this.fragments = fragments;
    }


    public Fragment getItem (int position) {
        return fragments.get(position);
    }

    public int getCount() {
        return fragments.size();
    }

    public CharSequence getPageTitle(int position) {
        switch(position) {
            case START:
                return UI_TAB_START;
            case HISTORY:
                return UI_TAB_HISTORY;
            case SETTINGS:
                return UI_TAB_SETTINGS;
            default:
                break;
        }
        return null;
    }

}
