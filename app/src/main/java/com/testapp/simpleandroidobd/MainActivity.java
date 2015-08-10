package com.testapp.simpleandroidobd;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.testapp.simpleandroidobd.dialog.BluethoothDevicesDialog;
import com.testapp.simpleandroidobd.obd.OBDManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements BluethoothDevicesDialog.BluethoothDevicesDialogListener {

    private OBDManager m_obdManager;
    private TextView m_txtRpm;
    private BluethoothDevicesDialog m_dlgBluetoothDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_txtRpm = (TextView) findViewById(R.id.txt_rpm);
        m_obdManager = new OBDManager();
        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] result = m_obdManager.launchOBDCommand("010C");
                int rpm = computeEngineRPM(result);
                m_txtRpm.setText(rpm);
            }
        });

        m_dlgBluetoothDevices = BluethoothDevicesDialog.newInstance();
        m_dlgBluetoothDevices.show(getSupportFragmentManager(), "dialog_bluetooth_devices");
    }

    @Override
    protected void onDestroy() {
        try {
            m_obdManager.disconnectFromOBDReader();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Couldn't disconnect to OBD Reader", Toast.LENGTH_SHORT).show();
            logError(e);
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
            m_obdManager.connectToOBDReader(p_address);
            m_dlgBluetoothDevices.dismiss();
        } catch (IOException e) {
            Log.d("CONNECTION", "Failed to connect to " + p_address);
            Toast.makeText(this, "Couldn't connect to OBD Reader\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            logError(e);
        }
    }

    private int computeEngineRPM(byte[] p_data) {
        int rpm = -1;
        if (p_data.length == 4) {
            rpm = (p_data[2] * 256 + p_data[3]) / 4;
        }
        return rpm;
    }

    private void logError(Exception p_exception) {
        String logPath = Environment.getExternalStorageDirectory() + File.separator + "OBDCrashes" + File.separator + "log.txt";
        Log.d("MainActivity", "Wrote log to : " + logPath);
        try {
            FileOutputStream out = new FileOutputStream(logPath, Boolean.TRUE);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write("New Exception\n");
            writer.write("Date : " + new Date(System.currentTimeMillis()) + '\n' + '\n');
            for (StackTraceElement elem : p_exception.getStackTrace()) {
                writer.write(elem.toString() + "\n");
            }
            writer.write("\n\n\n");
            writer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
