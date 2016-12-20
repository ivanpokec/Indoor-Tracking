package hr.foi.core;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import hr.foi.ble.BeaconsMonitoringService;
import hr.foi.ble.Sensor;

public class MainService extends Service {
    public MainService() {
    }

    public static boolean sIsStarted = false;

    private static ArrayList<Sensor> scanedSensors;
    private static long mLastFiltrationTime;
    private static int treashold = -70;
    private static float factor = 0.2f;

    private static Sensor lastSensor = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sIsStarted = true;

        Toast.makeText(this, "Service is started!", Toast.LENGTH_SHORT).show();

        HandlerThread thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        scanedSensors = new ArrayList<Sensor>();
        mLastFiltrationTime = new Date().getTime();


        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(this, "Uređaj ne podržava BLE!", Toast.LENGTH_SHORT).show();

            } else {

                if (!mBluetoothAdapter.isEnabled()) {
                    //Misc.openBluetoothSettings(AllStaticData.getmContext());
                }else {
                    if (!BeaconsMonitoringService.isScanRunning()) startService(new Intent(this, BeaconsMonitoringService.class));
                    BeaconsMonitoringService.setMainHandler(mHandler);
                    writeSensorsToLog();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        BeaconsMonitoringService.setMainHandler(mHandler);




    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void writeSensorsToLog(){

        try {

            if (scanedSensors != null && scanedSensors.size() > 0) {

                Date dateNow = new Date();
                boolean removeInactive = false;
                long diffFiltration = 0;
                if (mLastFiltrationTime > 0){
                    diffFiltration = dateNow.getTime() - mLastFiltrationTime;
                    if (diffFiltration > 1000) removeInactive = true;
                }
                Sensor nearestSensor = null;

                ListIterator<Sensor> sensorListIterator = scanedSensors.listIterator();
                int nearestSensorSignal = -120;
                while (sensorListIterator.hasNext()) {
                    Sensor sensor = sensorListIterator.next();
                    Date date = new Date(sensor.getLastScannTime());

                    long timeDiff = dateNow.getTime() - sensor.getLastScannTime();

                    if (timeDiff > 1000 && removeInactive) {
                        //                        sensorListIterator.remove();
                        int r = sensor.getSnrSignalR();
                        if (r > -99) r -= 2;
                        else r = -100;
                        sensor.setSnrSignalR(r);
                        if (timeDiff > 15000){
                            sensorListIterator.remove();
                        }

                    }

                    if (sensor.getSnrSignalR() > nearestSensorSignal && timeDiff < 15000 && sensor.getSnrSignalR() > treashold) {
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
                if (nearestSensor != null && lastSensor != nearestSensor){
                    String snrName = nearestSensor.getSnrBleMac(); //TODO: DOHVAĆANJE LOKACIJE PREMA MAC ADRESI
                    Toast.makeText(this, snrName + " frame: "+nearestSensor.getSnrSignalZ(), Toast.LENGTH_SHORT).show();
                    //txtCurrentLocation.setText(snrName + " frame: "+nearestSensor.getSnrSignalZ());
                    //txtCurrentLocation.setText(snrName+ " " +nearestSensor.getSnrSignalZ());

                    Date date = new Date(nearestSensor.getLastScannTime());
                    lastSensor = nearestSensor;
                }
            }
            mLogHandler.postDelayed(mStopRunnable, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

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


    public String ByteArrayToString(byte[] ba)
    {
        StringBuilder hex = new StringBuilder(ba.length * 2);
        for (byte b : ba)
            hex.append(b + " ");

        return hex.toString();
    }

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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        try {
//
//
//            if (BeaconsMonitoringService.isScanRunning()) stopService(new Intent(this, BeaconsMonitoringService.class));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }



//    protected void onResume() {
//        super.onResume();
//
//
//        //checkPermissions();
//
//        try {
//            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//            if (mBluetoothAdapter == null) {
//                Toast.makeText(this, "Uređaj ne podržava BLE!", Toast.LENGTH_SHORT).show();
//
//            } else {
//
//                if (!mBluetoothAdapter.isEnabled()) {
//                    //Misc.openBluetoothSettings(AllStaticData.getmContext());
//                }else {
//                    if (!BeaconsMonitoringService.isScanRunning()) startService(new Intent(this, BeaconsMonitoringService.class));
//                    BeaconsMonitoringService.setMainHandler(mHandler);
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        BeaconsMonitoringService.setMainHandler(mHandler);
//
//    }

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

}
