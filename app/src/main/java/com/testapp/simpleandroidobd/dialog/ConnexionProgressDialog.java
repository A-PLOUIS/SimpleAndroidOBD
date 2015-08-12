package com.testapp.simpleandroidobd.dialog;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConnexionProgressDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnexionProgressDialog extends DialogFragment {

    private static final String ARG_MESSAGE = "message";

    private String m_message;


    public ConnexionProgressDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param p_message Message to display in Dialog.
     * @return A new instance of fragment ConnexionProgressDialog.
     */
    public static ConnexionProgressDialog newInstance(String p_message) {
        ConnexionProgressDialog fragment = new ConnexionProgressDialog();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, p_message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_message = getArguments().getString(ARG_MESSAGE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());

        dialog.setMessage(m_message);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);

        return dialog;
    }

}
