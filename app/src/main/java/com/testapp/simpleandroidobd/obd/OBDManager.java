package com.testapp.simpleandroidobd.obd;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.util.Log;

import com.testapp.simpleandroidobd.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by PIERRE-LOUIS Antonio on 04/08/2015.
 */
public class OBDManager {

    private static final String LOG_TAG = OBDManager.class.getSimpleName();
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothDevice m_device;
    private BluetoothSocket m_socket;

    //For LOg
    private String currentCommand;
    private Long m_commandStart, m_commandEnd;

    public void connectToOBDReader(String p_address) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            this.m_device = adapter.getRemoteDevice(p_address);
        }
        if (m_device != null) {
            try {
                m_socket = m_device.createRfcommSocketToServiceRecord(SPP_UUID);
                m_socket.connect();
                LogUtils.createNewResultFileForConnexion();
                //Reset OBD
                sendCommand("AT Z", m_socket.getOutputStream());
                readRawData(m_socket.getInputStream());
                //Turn off echo
                sendCommand("AT E0", m_socket.getOutputStream());
                readRawData(m_socket.getInputStream());
                //Timeout to 248ms
                sendCommand("AT ST " + Integer.toHexString(0xFF & 62), m_socket.getOutputStream());
                readRawData(m_socket.getInputStream());
                //Protocol to AUTO(0)
                sendCommand("AT SP " + "0", m_socket.getOutputStream());
                readRawData(m_socket.getInputStream());

            } catch (Exception e) {
                LogUtils.logError(e);
            }
        }
    }

    public ArrayList<Integer> launchOBDCommand(final String p_command) {
        if (m_socket != null) {
            try {
                sendCommand(p_command, m_socket.getOutputStream());
                return readResponse(m_socket.getInputStream());
            } catch (IOException e) {
                LogUtils.logError(e);
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private void sendCommand(String p_command, OutputStream out) throws IOException {
        Log.d(LOG_TAG, "Write command " + p_command);
        currentCommand = p_command;
        out.write((p_command + '\r').getBytes());
        out.flush();
        m_commandStart = SystemClock.elapsedRealtime();
    }

    private ArrayList<Integer> readResponse(InputStream in) throws IOException {
        String rawData = readRawData(in);

        // read string each two chars
        ArrayList<Integer> buffer = new ArrayList<>();
        buffer.add((m_commandEnd.intValue() - m_commandStart.intValue()));
        int begin = 0;
        int end = 2;
        while (end <= rawData.length()) {
            buffer.add(Integer.decode("0x" + rawData.substring(begin, end)));
            begin = end;
            end += 2;
        }
        return buffer;
    }

    private String readRawData(InputStream in) throws IOException {
        byte b;
        StringBuilder res = new StringBuilder();

        // read until '>' arrives
        while ((char) (b = (byte) in.read()) != '>') {
            res.append((char) b);
        }
        m_commandEnd = SystemClock.elapsedRealtime();

        String rawData = res.toString();
        LogUtils.logResult(currentCommand, rawData, m_commandEnd - m_commandStart);

        rawData = rawData.replaceAll("\\s", "")
                .replaceAll("SEARCHING", "")
                .replaceAll("(BUS INIT)|(BUSINIT)|(\\.)", "");

        Log.d(LOG_TAG, "RAWDATA = " + rawData);

        return rawData;
    }

    public void disconnectFromOBDReader() throws IOException {
        if (m_socket != null) {
            m_socket.close();
            m_socket = null;
        }
    }
}
