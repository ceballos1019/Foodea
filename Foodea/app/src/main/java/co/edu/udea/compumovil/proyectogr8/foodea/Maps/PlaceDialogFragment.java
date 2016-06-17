package co.edu.udea.compumovil.proyectogr8.foodea.Maps;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import co.edu.udea.compumovil.proyectogr8.foodea.R;

/**
 * Created by user on 9/06/2016.
 */
public class PlaceDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    private String title;
    private String message;


    public PlaceDialogFragment(String title, String message){
        this.title = title;
        this.message = message;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Set the dialog characteristics
        builder.setMessage(message)
                .setTitle(title);

        // Add the buttons
        builder.setPositiveButton(R.string.details, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                mListener.onDialogPositiveClick(PlaceDialogFragment.this);

            }
        });
        builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                mListener.onDialogNegativeClick(PlaceDialogFragment.this);

            }
        });

        //return the AlertDialog object
        return builder.create();


    }
}
