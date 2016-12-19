package hr.foi.indoortracking;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.ble.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    SessionManager manager;
    private static ArrayList<Sensor> scanedSensors;
    private static long mLastFiltrationTime;
    private static TextView txtCurrentLocation;
    private static int treashold = -99;
    private static float factor = 0.1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = new SessionManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txtCurrentLocation = (TextView) findViewById(R.id.txtCurrentLocation);

        scanedSensors = new ArrayList<Sensor>();
        mLastFiltrationTime = new Date().getTime();
        writeSensorsToLog();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            this.finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        if (id == R.id.action_profil) {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);

        }

        if(id== R.id.action_logout) {
            manager.setPreferences(MainActivity.this, "id", "");
            manager.setPreferences(MainActivity.this, "name", "");
            manager.setPreferences(MainActivity.this, "username", "");

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_trenutna) {

        } else if (id == R.id.nav_lokacije) {

        } else if (id == R.id.nav_kretanja) {

        } else if (id == R.id.nav_korisnici) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onResume() {
        super.onResume();


        checkPermissions();

        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(MainActivity.this, "Uređaj ne podržava BLE!", Toast.LENGTH_SHORT).show();

            } else {

                if (!mBluetoothAdapter.isEnabled()) {
                    //Misc.openBluetoothSettings(AllStaticData.getmContext());
                }else {
                    if (!BeaconsMonitoringService.isScanRunning()) startService(new Intent(this, BeaconsMonitoringService.class));
                    BeaconsMonitoringService.setMainHandler(mHandler);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        BeaconsMonitoringService.setMainHandler(mHandler);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {


            if (BeaconsMonitoringService.isScanRunning()) stopService(new Intent(this, BeaconsMonitoringService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            try {
                switch (msg.what) {


                    case 1:
                        if (msg.obj instanceof ArrayList) {
                            ArrayList<Sensor> scanedSensors = (ArrayList<Sensor>) msg.obj;

                        }
                        break;
                    case 2:
                        if (msg.obj instanceof BluetoothDevice){

                            BluetoothDevice device = (BluetoothDevice) msg.obj;

                            if (msg.getData() != null){
                                Bundle data = msg.getData();
                                int rssi = data.getInt("rssi");
                                byte[] scanRecord = data.getByteArray("scanRecord");
                                updateSensorSignal(device,scanRecord,rssi);

//                                MyTaskParams myTaskParams = new MyTaskParams(device,scanRecord,rssi);
//                                UpdateSensorSignalAsync updateSensorSignalAsync = new UpdateSensorSignalAsync();
//                                updateSensorSignalAsync.execute(myTaskParams);
                            }

//                            if (lvBeacons.getAdapter() == null) {
//                                BeaconsAdapter beaconsAdapter = new BeaconsAdapter(mContext, scanedSensors);
//                                lvBeacons.setAdapter(beaconsAdapter);
//                            }else {
//                                BeaconsAdapter beaconsAdapter = (BeaconsAdapter) lvBeacons.getAdapter();
//                                beaconsAdapter.notifyDataSetChanged();
//                            }

                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void updateSensorSignal(BluetoothDevice device, byte[] data, int rssi){

        try {

            if (scanedSensors == null){
                scanedSensors = new ArrayList<Sensor>();
            }
            boolean deviceExists = false;



            for (Sensor sensor : scanedSensors) {
                if (sensor.getSnrBleMac().equalsIgnoreCase(device.getAddress())) {
                    deviceExists = true;

                    String[] splited = ByteArrayToString(data).split(" ");

                    if (splited.length >= 12) {

                        int r = sensor.getSnrSignalR();

                        if (r == 0) r = rssi;
                        else {
                            int rr = rssi;
                            if (rr > 0) rr = sensor.getSnrSignalR();
                            r += (rr - r) * factor;
                        }

                        int z = sensor.getSnrSignalZ();
                        z++;

                        sensor.setSnrSignalZ(z);
                        if (r < 0) sensor.setSnrSignalR(r);
                        sensor.setSnrSignalX(rssi);
                        sensor.setBluetoothDevice(device);

                        Date dateLastScann = new Date();
                        sensor.setLastScannTime(dateLastScann.getTime());

                    }

                }
            }if (!deviceExists){
                Sensor sensor = new Sensor();
                sensor.setSnrBleMac(device.getAddress());
                sensor.setBluetoothDevice(device);
                if (rssi > 0) rssi = -30;
                sensor.setSnrSignalR(rssi);
                sensor.setSnrSignalZ(1);
                sensor.setSnrSignalX(rssi);

                String[] splited = ByteArrayToString(data).split(" ");

                if (splited.length >= 12) {

                    Date dateLastScann = new Date();
                    sensor.setLastScannTime(dateLastScann.getTime());
                }
                scanedSensors.add(sensor);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String ByteArrayToString(byte[] ba)
    {
        StringBuilder hex = new StringBuilder(ba.length * 2);
        for (byte b : ba)
            hex.append(b + " ");

        return hex.toString();
    }

    private Handler mLogHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            writeSensorsToLog();
        }
    };

    private void writeSensorsToLog(){

        try {

            if (scanedSensors != null && scanedSensors.size() > 0) {

                Date dateNow = new Date();
                boolean removeInactive = false;
                long diffFiltration = 0;
                if (mLastFiltrationTime > 0){
                    diffFiltration = dateNow.getTime() - mLastFiltrationTime;
                    if (diffFiltration > 5000) removeInactive = true;
                }
                Sensor nearestSensor = null;

                ListIterator<Sensor> sensorListIterator = scanedSensors.listIterator();
                int nearestSensorSignal = -120;
                while (sensorListIterator.hasNext()) {
                    Sensor sensor = sensorListIterator.next();
                    Date date = new Date(sensor.getLastScannTime());

                    long timeDiff = dateNow.getTime() - sensor.getLastScannTime();

                    if (timeDiff > 5000 && removeInactive) {
                        //                        sensorListIterator.remove();
                        int r = sensor.getSnrSignalR();
                        if (r > -99) r -= 2;
                        else r = -100;
                        sensor.setSnrSignalR(r);
                        if (timeDiff > 30000){
                            sensorListIterator.remove();
                        }

                    }

                    if (sensor.getSnrSignalR() > nearestSensorSignal && timeDiff < 30000 && sensor.getSnrSignalR() > treashold) {
                        nearestSensor = sensor;
                        nearestSensorSignal = sensor.getSnrSignalR();
                    }

                }
//                if (removeInactive){
//                    BeaconsAdapter beaconsAdapter = (BeaconsAdapter) lvBeacons.getAdapter();
//                    beaconsAdapter.setSensors(scanedSensors);
//                    beaconsAdapter.notifyDataSetChanged();
//                    mLastFiltrationTime = new Date().getTime();
//                }
                if (nearestSensor != null){
                    String snrName = nearestSensor.getSnrBleMac();
                    txtCurrentLocation.setText(snrName + " frame: "+nearestSensor.getSnrSignalZ());
                    //txtCurrentLocation.setText(snrName+ " " +nearestSensor.getSnrSignalZ());

                    Date date = new Date(nearestSensor.getLastScannTime());
                }
            }
            mLogHandler.postDelayed(mStopRunnable, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private boolean checkPermissions(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }
        return true;
    }

}
