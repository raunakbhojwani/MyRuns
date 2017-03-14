package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;


/* This MyRunsDialogFragment controls the dialogs thrown up by various listview items in the manual entry
 * I used the DialogFragment documents on Android's developer website as a reference for this file
 *
 * @author: RaunakB
 */
public class MyRunsDialogFragment extends DialogFragment {

    private static final String TAG = "DebugTag";
    public static String dialogCategory = "";

    public static MyRunsDialogFragment newFragment() {
        MyRunsDialogFragment dialogFragment = new MyRunsDialogFragment();
        return dialogFragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //If the date is chosen, throw up a calendar dialog
        if (dialogCategory.equals("Date")) {
            final Calendar now;
            int year, month, day;

            now = Calendar.getInstance();
            year = now.get(Calendar.YEAR);
            month = now.get(Calendar.MONTH);
            day = now.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
            {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    ((ManualEntryActivity) getActivity()).onDateSet(year, monthOfYear, dayOfMonth);
                }
            }, year, month, day);

        }

        //If the time is chosen, throw up a clock dialog
        else if(dialogCategory.equals("Time"))
        {
            final Calendar now;
            int hour, minute;
            now = Calendar.getInstance();
            hour = now.get(Calendar.HOUR_OF_DAY);
            minute = now.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener()
            {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                {
                    ((ManualEntryActivity) getActivity()).onTimeSet(hourOfDay, minute);
                }
            }, hour, minute, false);
        }

        //If the another item is chosen, throw up a regular dialog
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.fragment_my_runs_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((getActivity()));
        alertDialogBuilder.setView(dialogView);

        // This EditText variable can be used across dialogs
        final EditText userInput = (EditText) dialogView.findViewById(R.id.dialog_edittext);

        if (dialogCategory.equals("Comment"))
        {
            userInput.setInputType(InputType.TYPE_CLASS_TEXT); //Since this is a comment, another keyboard
            userInput.setHint("Any comments? Put 'em here!");
        }
        else if (dialogCategory.equals("Distance") || dialogCategory.equals("Duration")) {
            userInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        else {
            userInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        alertDialogBuilder.setTitle(dialogCategory);
        alertDialogBuilder
                .setPositiveButton(R.string.okay,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(dialogCategory.equals("Comment")) {
                                    ((ManualEntryActivity) getActivity()).onDialogOKClick(dialogCategory,
                                            userInput.getText().toString());
                                }
                                else if (dialogCategory.equals("Distance")) {
                                    ((ManualEntryActivity) getActivity()).onDialogOKClick(dialogCategory,
                                            Double.parseDouble(userInput.getText().toString()));
                                }
                                else {
                                    ((ManualEntryActivity) getActivity()).onDialogOKClick(dialogCategory,
                                            Integer.parseInt(userInput.getText().toString()));
                                }

                            }
                        })
                .setNegativeButton(R.string.ui_cancel_button_header,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((ManualEntryActivity) getActivity()).onDialogCancelClick();
                            }
                        });

        AlertDialog dialogToAlert = alertDialogBuilder.create();
        return dialogToAlert;
    }
}
