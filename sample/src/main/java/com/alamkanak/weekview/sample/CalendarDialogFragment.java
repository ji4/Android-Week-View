package com.alamkanak.weekview.sample;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by chiaying.wu on 2017/7/13.
 */

public class CalendarDialogFragment extends DialogFragment {
    Context context;
    long eventId;
    String eventName;

    public CalendarDialogFragment(Context context, long eventId, String eventName) {
        this.context = context;
        this.eventId = eventId;
        this.eventName = eventName;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(eventName)
                .setItems(R.array.event_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch(which){
                            case 0:
                                showDeleteDialog(eventId, eventName);
                        }
                    }
                });
        return builder.create();
    }

    public void showDeleteDialog(long eventId, String eventName) {
        DialogFragment newFragment = new DeleteConfirmDialogFragment(eventId);
        newFragment.show(getFragmentManager(), "missiles");
    }
}

class DeleteConfirmDialogFragment extends DialogFragment{
    long eventId;
    public DeleteConfirmDialogFragment(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.displayevents_confirmToDeleteEvent)
                .setPositiveButton(R.string.displayevents_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User confirmed to delete the dialog
                        DB.deleteData(getContext(), eventId);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}