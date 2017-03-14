package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * An adapted version of ArrayAdapter that allows me to have a
 * title and summary, that are both sought from FetchedEntry
 * and are then displayed in the HistoryFragment
 *
 * Created by RaunakBhojwani on 2/1/17.
 * @author RaunakBhojwani
 */

public class CustomArrayAdapter extends ArrayAdapter<FetchedEntry> {

    private final ArrayList<FetchedEntry> entriesList;
    private final Context context;

    // In the constructor, initialize the context and list of FetchedEntry objects
    public CustomArrayAdapter(Context context, ArrayList<FetchedEntry> entriesList) {
        super(context, R.layout.custom_list_layout, entriesList);
        this.context = context;
        this.entriesList = entriesList;
    }

    //In getView, create and inflater, and then inflate the list layout
    //Then, retrieve the text views for the header and summary, and set the accordingly.
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View list_view = inflater.inflate(R.layout.custom_list_layout, parent, false);

        TextView header_text_view = (TextView) list_view.findViewById(R.id.entry_header);
        TextView summary_text_view = (TextView) list_view.findViewById(R.id.entry_summary);

        header_text_view.setText(entriesList.get(pos).getHeader());
        summary_text_view.setText(entriesList.get(pos).getSummary());

        return list_view;
    }
}
