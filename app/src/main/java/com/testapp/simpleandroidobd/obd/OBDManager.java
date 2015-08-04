package com.testapp.simpleandroidobd.obd;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by PIERRE-LOUIS Antonio on 04/08/2015.
 */
public class OBDManager {

    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothDevice m_device;
    private BluetoothSocket m_socket;

    public void connectToOBDReader(String p_address) throws IOException {
        setBluetoothDevice(p_address);
        if (m_device != null) {
            m_socket = m_device.createRfcommSocketToServiceRecord(SPP_UUID);
        }
    }

    private void setBluetoothDevice(String p_address) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            this.m_device = adapter.getRemoteDevice(p_address);
        }
    }


    public void disconnectFromOBDReader() throws IOException {
        if (m_socket != null) {
            m_socket.close();
            m_socket = null;
        }
    }

}
