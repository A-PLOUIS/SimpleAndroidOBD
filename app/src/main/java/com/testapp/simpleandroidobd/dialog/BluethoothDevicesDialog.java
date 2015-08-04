package com.testapp.simpleandroidobd.dialog;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.testapp.simpleandroidobd.R;

import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BluethoothDevicesDialogListener} interface
 * to handle interaction events.
 * Use the {@link BluethoothDevicesDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BluethoothDevicesDialog extends DialogFragment {

    private BluethoothDevicesDialogListener mListener;

    private Set<BluetoothDevice> m_bluetoothDevices;

    public BluethoothDevicesDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment BluethoothDevicesDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static BluethoothDevicesDialog newInstance() {
        return new BluethoothDevicesDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            m_bluetoothDevices = adapter.getBondedDevices();
        } else {
            Toast.makeText(getActivity(), "Bluetooth not supported on device", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluethooth_devices_dialog, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (BluethoothDevicesDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BluethoothDevicesDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface BluethoothDevicesDialogListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
