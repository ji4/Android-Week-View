package com.alamkanak.weekview.sample;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by chiaying.wu on 2017/7/13.
 */

public class CalendarDialogFragment extends DialogFragment {
    Context context;
    public CalendarDialogFragment(Context context) {
        this.context = context;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.displayevents_plzselect)
                .setItems(R.array.event_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch(which){
                            case 0:
                                showDeleteDialog();

                        }



                        Log.d("which: ", String.valueOf(which));
                    }
                });
        return builder.create();
    }

    public void showDeleteDialog() {
        DialogFragment newFragment = new DeleteConfirmDialogFragment();
        newFragment.show(getFragmentManager(), "missiles");
    }
}

class DeleteConfirmDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.displayevents_confirmToDeleteEvent)
                .setPositiveButton(R.string.displayevents_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
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