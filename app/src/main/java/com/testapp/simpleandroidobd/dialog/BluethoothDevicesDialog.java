package com.testapp.simpleandroidobd.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Paired Devices");
        builder.setAdapter(new ListDevicesAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                        m_bluetoothDevices.toArray(new BluetoothDevice[m_bluetoothDevices.size()])),
                null);
        return builder.create();
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
     */
    public interface BluethoothDevicesDialogListener {
        void connectToBluetoothDevice(String p_address);
    }

    private class ListDevicesAdapter extends ArrayAdapter<BluetoothDevice> {
        public ListDevicesAdapter(Context context, int resource, BluetoothDevice[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_1, null);
            }
            final BluetoothDevice currentItem = getItem(position);

            ((TextView) convertView.findViewById(android.R.id.text1)).setText(currentItem.getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.connectToBluetoothDevice(currentItem.getAddress());
                }
            });


            return convertView;
        }
    }

}
