package com.testapp.simpleandroidobd.obd;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by PIERRE-LOUIS Antonio on 04/08/2015.
 */
public class OBDManager {

    private static final String LOG_TAG = OBDManager.class.getSimpleName();
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothDevice m_device;
    private BluetoothSocket m_socket;

    public void connectToOBDReader(String p_address) throws IOException {
        setBluetoothDevice(p_address);
        if (m_device != null) {
            m_socket = m_device.createRfcommSocketToServiceRecord(SPP_UUID);
        }
    }

    public byte[] launchOBDCommand(String p_command) {
        try {
            sendCommand(p_command, m_socket.getOutputStream());
            return readResponse(m_socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private void setBluetoothDevice(String p_address) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            this.m_device = adapter.getRemoteDevice(p_address);
        }
    }

    private void sendCommand(String p_command, OutputStream out) throws IOException {
        Log.d(LOG_TAG, "Write command " + p_command);
        out.write(p_command.getBytes());
    }

    private byte[] readResponse(InputStream in) throws IOException {
        byte[] buffer = new byte[4];
        in.read(buffer);
        Log.d(LOG_TAG, "Buffer size = " + buffer.length);
        Log.d(LOG_TAG, "Buffer content : " + Arrays.toString(buffer));
        return buffer;
    }

    public void disconnectFromOBDReader() throws IOException {
        if (m_socket != null) {
            m_socket.close();
            m_socket = null;
        }
    }

}
