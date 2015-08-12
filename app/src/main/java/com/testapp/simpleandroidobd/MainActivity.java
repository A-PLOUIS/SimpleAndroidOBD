package com.testapp.simpleandroidobd;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.testapp.simpleandroidobd.dialog.BluetoothDevicesDialog;
import com.testapp.simpleandroidobd.dialog.ConnexionProgressDialog;
import com.testapp.simpleandroidobd.obd.OBDManager;
import com.testapp.simpleandroidobd.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BluetoothDevicesDialog.BluethoothDevicesDialogListener {

    private OBDManager m_obdManager;
    private Handler m_handler;

    private TextView m_txtRpm;
    private Button m_buttonStarttStop;


    private BluetoothDevicesDialog m_dlgBluetoothDevices;
    private ConnexionProgressDialog m_waitingDialog;

    private Boolean m_bisStart = Boolean.TRUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_txtRpm = (TextView) findViewById(R.id.txt_rpm);
        m_obdManager = new OBDManager();

        m_handler = new Handler();
        m_buttonStarttStop = (Button) findViewById(R.id.btn_start_stop);
        m_buttonStarttStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_bisStart) {
                    m_buttonStarttStop.setText("Stop");
                    m_bisStart = Boolean.FALSE;
                    m_handler = new Handler();
                    m_handler.post(new RPMRetriever());
                } else {
                    try {
                        m_handler = null;
                        m_obdManager.disconnectFromOBDReader();
                        m_buttonStarttStop.setText("Start");
                        m_bisStart = Boolean.FALSE;
                    } catch (IOException e) {
                        Toast.makeText(getBaseContext(),
                                "Couldn't disconnect to OBD Reader\n" + e.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                        LogUtils.logError(e);
                        e.printStackTrace();
                    }
                }
            }
        });


        m_waitingDialog = ConnexionProgressDialog.newInstance("Connecting to ODB Interface");
        m_dlgBluetoothDevices = BluetoothDevicesDialog.newInstance();
        m_dlgBluetoothDevices.show(getSupportFragmentManager(), "dialog_bluetooth_devices");
    }

    @Override
    protected void onDestroy() {
        try {
            m_obdManager.disconnectFromOBDReader();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Couldn't disconnect to OBD Reader", Toast.LENGTH_SHORT).show();
            LogUtils.logError(e);
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void connectToBluetoothDevice(String p_address) {
        try {
            m_waitingDialog.show(getSupportFragmentManager(), "dialog_connexion_progress");
            m_obdManager.connectToOBDReader(p_address);
            m_waitingDialog.dismiss();
        } catch (IOException e) {
            m_waitingDialog.dismiss();
            Log.d("CONNECTION", "Failed to connect to " + p_address);
            Toast.makeText(this, "Couldn't connect to OBD Reader\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            LogUtils.logError(e);
        }
    }

    private int computeEngineRPM(ArrayList<Integer> p_data) {
        return (p_data.get(2) * 256 + p_data.get(3)) / 4;
    }

    private class RPMRetriever implements Runnable {

        @Override
        public void run() {
            try {
                ArrayList<Integer> result = m_obdManager.launchOBDCommand("01 0C");
                if (result == null) {
                    Toast.makeText(getBaseContext(), "Null RPM", Toast.LENGTH_SHORT).show();
                } else if (result.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Empty RPM", Toast.LENGTH_SHORT).show();
                } else {
                    Integer rpm = computeEngineRPM(result);
                    m_txtRpm.setText(rpm.toString());
                }
                if (m_handler != null) {
                    m_handler.post(new RPMRetriever());
                }
            } catch (Exception e) {
                LogUtils.logError(e);
                Toast.makeText(getBaseContext(), "Error retrieving rpm\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
