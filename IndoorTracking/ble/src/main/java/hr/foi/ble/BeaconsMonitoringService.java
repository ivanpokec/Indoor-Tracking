package hr.foi.ble;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
/*
import com.magsense.mobilisis.beacondetector.MainActivity;
import com.magsense.mobilisis.customelements.MyLog;
import com.magsense.mobilisis.data.Misc;
import com.magsense.mobilisis.data.Sensor;
*/
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;


public class BeaconsMonitoringService extends Service implements BluetoothAdapter.LeScanCallback{

    private static final String TAG = "BeaconsService";
    private static int nearestRssi;
    private static boolean scanRunning = false;
    private BluetoothAdapter mBluetoothAdapter;
    private static ArrayList<BluetoothDevice> scanedDevices;
    private static Date lastScanTime;
    private static Date notificationSetDate;
    private static ArrayList<BluetoothDevice> bluetoothDevices;
    private static ArrayList<Sensor> scanedSensors;
    private static Handler mainHandler;

    // Binder given to clients
//    public final IBinder mBinder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
//    public class LocalBinder extends Binder {
//        BeaconsMonitoringService getService() {
//            // Return this instance of LocalService so clients can call public methods
//            return BeaconsMonitoringService.this;
//        }
//    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    public void start(){

        Log.d(TAG, "Beacons monitoring service created");
        Toast.makeText(this, "Beacons monitoring service started", Toast.LENGTH_SHORT).show();

        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();
        scanedDevices = new ArrayList<BluetoothDevice>();
        //mainFragment = MainFragment.getFragment();
        bluetoothDevices = new ArrayList<BluetoothDevice>();
        scanedSensors = new ArrayList<Sensor>();
        scanRunning = true;
        nearestRssi = -120;
        startScan();
    }

    @Override
    public void onCreate() {
        // Configure BeaconManager.
        Log.d(TAG, "Beacons monitoring service created");
        Toast.makeText(this, "Beacons monitoring service started", Toast.LENGTH_SHORT).show();

        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();
        scanedDevices = new ArrayList<BluetoothDevice>();
        //mainFragment = MainFragment.getFragment();
        bluetoothDevices = new ArrayList<BluetoothDevice>();
        scanedSensors = new ArrayList<Sensor>();
        scanRunning = true;
        nearestRssi = -120;
        startScan();
    }

    public void stopService(){
        mHandler.removeCallbacks(mStopRunnable);
        if (mBluetoothAdapter != null) mBluetoothAdapter.stopLeScan(this);
        mHandler.removeCallbacksAndMessages(null);
        scanRunning = false;
        stopSelf();
    }

    @Override
    public void onDestroy() {
        try {
            Log.d(TAG, "Beacons monitoring service destroyed");
            mHandler.removeCallbacks(mStopRunnable);
            if (mBluetoothAdapter != null)mBluetoothAdapter.stopLeScan(this);
            mHandler.removeCallbacksAndMessages(null);
            scanRunning = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

        Log.i(TAG, "New LE Device: " + device.getName() + " @ " + rssi + " mac: "+device.getAddress());

//        mainFragment = MainFragment.getFragment();
//        MainActivity mainActivity = MainActivity.getMainActivity();
//        Date dateNow = new Date();

//        if (device.getName() != null && rssi > nearestRssi && rssi < 0 && device.getName().contains("ACPS")){
//
//
//            if (notificationSetDate != null){
//                long dateDiff = dateNow.getTime() - notificationSetDate.getTime();
//                if (dateDiff > 300000){
////                    if (!mainActivity.isAppInFront()) addNotification(device);
//                }
//            }else {
//
////                if (!mainActivity.isAppInFront()) addNotification(device);
//            }
//        }
        if (device.getName() != null && device.getName().contains("ACPS")){

//            updateSensorSignal(device,scanRecord, rssi);


            Message msg = mainHandler.obtainMessage(2,device);
            Bundle bundle = new Bundle();
            bundle.putInt("rssi", rssi);
            bundle.putByteArray("scanRecord", scanRecord);
            msg.setData(bundle);
            mainHandler.sendMessage(msg);

//            if (lastScanTime == null)  setActivePaymentSensor();
//            lastScanTime = new Date();


        }

    }


    private void addNotification(BluetoothDevice device){

        notificationSetDate = new Date();
//        Intent dialogIntent = new Intent(this, MainActivity.class);
//        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        PendingIntent pIntent = PendingIntent.getActivity(this,0,dialogIntent,0);

        Notification noti = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
//                .setContentText(getString(R.string.ready_to_pay))
//                .setSmallIcon(R.drawable.parkomlogo)
                //.setContentIntent(pIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[] {1000, 1000})
                .build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(NotificationTypes.READY_TO_PAY, noti);

    }

    private void stopScan() {
        mBluetoothAdapter.stopLeScan(this);

        try {
            if (scanRunning) {
                //sleep(5000);
                startScan();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //setProgressBarIndeterminateVisibility(false);
    }

    private void startScan() {

        try {
            if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                nearestRssi = -120;
                mBluetoothAdapter.startLeScan(this);
                scanRunning = true;
                //setProgressBarIndeterminateVisibility(true);
                Date timeNow = new Date();
                if (lastScanTime != null) {
                    long timeDiff = timeNow.getTime() - lastScanTime.getTime();
                    if (timeDiff > 15000) {
//                        mainFragment = MainFragment.getFragment();
//                        if (mainFragment != null) mainFragment.disablePayAvailability();
                        scanedDevices.clear();
                        scanedSensors.clear();
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                        mNotificationManager.cancel(NotificationTypes.READY_TO_PAY);
                        lastScanTime = null;
                        //notificationSetDate = null;
                    }else {

                        //setActivePaymentSensor();
                    }
                }




                mHandler.postDelayed(mStopRunnable, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setActivePaymentSensor(){

        if (!scanedSensors.isEmpty()){
            ArrayList<Sensor> sensorsToRemove;
            Sensor nearestSensor = null;
            Date timeNow = new Date();

            ListIterator<Sensor> sensorListIterator = scanedSensors.listIterator();
            int nearestSensorSignal = -120;
            while (sensorListIterator.hasNext()){
                Sensor sensor = sensorListIterator.next();

                long timeDiff = timeNow.getTime() - sensor.getLastScannTime();
                if (timeDiff > 15000) {
                    sensorListIterator.remove();
                }
                else if (sensor.getSnrSignalR() > nearestSensorSignal) {
                    nearestSensor = sensor;
                    nearestSensorSignal = sensor.getSnrSignalR();

                }
            }

            if (nearestSensor != null) {

               mainHandler.obtainMessage(1,scanedSensors).sendToTarget();

//                mainFragment = MainFragment.getFragment();
//                if (mainFragment != null) {
//                    mainFragment.GetSensorLocation(nearestSensor);
                    //mainFragment.updatePayAvailability(nearestSensor);


//                }
            }
        }

    }

    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan();
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            BluetoothGattCharacteristic characteristic;
            switch (msg.what) {

            }
        }
    };

    public static void refresh(){

        if (scanedDevices != null)scanedDevices.clear();
        if (scanedSensors != null)scanedSensors.clear();
        lastScanTime = null;

        nearestRssi = -120;
    }


    private void updateSensorSignal(BluetoothDevice device, byte[] data, int rssi){

        try {
            if (bluetoothDevices == null ){
                bluetoothDevices = new ArrayList<BluetoothDevice>();
            }
            if (scanedSensors == null){
                scanedSensors = new ArrayList<Sensor>();
            }
            boolean deviceExists = false;



            for (Sensor sensor : scanedSensors) {
                if (sensor.getSnrBleMac().equalsIgnoreCase(device.getAddress())) {
                    deviceExists = true;

                    String[] splited = ByteArrayToString(data).split(" ");

                    if (splited.length >= 12) {

                        //int canIndex = Math.abs(Misc.getInt(splited[12]));
//                        int x = Math.abs(Misc.getInt(splited[4]));
//                        int y = Math.abs(Misc.getInt(splited[5]));
//                        int z = Math.abs(Misc.getInt(splited[6]));
                        int r = sensor.getSnrSignalR();
//
                       //Log.d("DEBUG", "CanIndex -> " + canIndex+ " mac: "+device.getAddress());
//                        Log.d("DEBUG", "X -> " + x);
//                        Log.d("DEBUG", "Y -> " + y);
//                        Log.d("DEBUG", "Z -> " + z);
//
//                        int xr = sensor.getSnrSignalX();
//                        int yr = sensor.getSnrSignalY();
//                        int zr = sensor.getSnrSignalZ();
//
//
//                        if (xr == 0) xr = x;
//                        else xr += (x - xr) * 0.1;
//
//
//                        if (yr == 0) yr = y;
//                        else yr += (y - yr) * 0.1;
//
//
//                        if (zr == 0) zr = z;
//                        else zr += (z - zr) * 0.1;
//
                        if (r == 0) r = rssi;
                        else r += (rssi - r) * 0.1;

                       // sensor.setSnrSignalCanIndex(canIndex);
//                        sensor.setSnrSignalX(xr);
//                        sensor.setSnrSignalY(yr);
//                        sensor.setSnrSignalZ(zr);
                        sensor.setSnrSignalR(r);
                        sensor.setBluetoothDevice(device);

//                        MyLog.d("CALCULATION", "Sensor: "+ sensor.getSnrBleMac() + " X: " + x + " XR:" + xr);
//                        MyLog.d("CALCULATION", "Sensor: "+ sensor.getSnrBleMac() + " Y: " + y + " YR:" + yr);
//                        MyLog.d("CALCULATION", "Sensor: "+ sensor.getSnrBleMac() + " Z: " + z + " ZR:" + zr);
                        Log.d("CALCULATION", "Sensor: "+ sensor.getSnrBleMac() + " RSSI: " + rssi + " R: "+r);

                        Date dateLastScann = new Date();
                        sensor.setLastScannTime(dateLastScann.getTime());
                        //float factor = ss / 250.0f;

                        //r = (int)factor * 250;
                        //r = ss;
                        //double r = Math.sqrt((x*x)+(y*y)+(z*z));



                    }

                }
            }if (!deviceExists){
                Sensor sensor = new Sensor();
                sensor.setSnrBleMac(device.getAddress());
                sensor.setBluetoothDevice(device);
                sensor.setSnrSignalR(rssi);
                String[] splited = ByteArrayToString(data).split(" ");

                if (splited.length >= 12) {

//                    int canIndex = Math.abs(Misc.getInt(splited[12]));
//                    sensor.setSnrSignalCanIndex(canIndex);
//                    //sensor.setBluetoothDevice(device);
//                    Log.d("DEBUG", "CanIndex -> " + canIndex+ " mac: "+device.getAddress());
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


    public static boolean isScanRunning() {
        return scanRunning;
    }

    public static void setScanRunning(boolean scanRunning) {
        BeaconsMonitoringService.scanRunning = scanRunning;
    }

    public Handler getMainHandler() {
        return mainHandler;
    }

    public static void setMainHandler(Handler mainHandler) {
        BeaconsMonitoringService.mainHandler = mainHandler;
    }
}
