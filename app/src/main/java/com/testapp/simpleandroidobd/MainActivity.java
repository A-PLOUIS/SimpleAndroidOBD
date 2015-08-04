package com.testapp.simpleandroidobd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.testapp.simpleandroidobd.dialog.BluethoothDevicesDialog;
import com.testapp.simpleandroidobd.obd.OBDManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements BluethoothDevicesDialog.BluethoothDevicesDialogListener {

    private OBDManager m_obdManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_obdManager = new OBDManager();

        BluethoothDevicesDialog dialog = BluethoothDevicesDialog.newInstance();
        dialog.show(getSupportFragmentManager(), "dialog_bluetooth_devices");
    }

    @Override
    protected void onDestroy() {
        try {
            m_obdManager.disconnectFromOBDReader();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Couldn't disconnect to OBD Reader", Toast.LENGTH_SHORT).show();
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
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Couldn't connect to OBD Reader", Toast.LENGTH_SHORT).show();
        }
    }
}
