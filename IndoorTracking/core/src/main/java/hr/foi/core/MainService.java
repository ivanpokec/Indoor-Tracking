package hr.foi.core;

import android.app.NotificationManager;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import hr.foi.dbaccess.ApiEndpoint;
import hr.foi.dbaccess.LocationModel;
import hr.foi.dbaccess.RetrofitConnection;

import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import hr.foi.ble.BeaconsMonitoringService;
import hr.foi.ble.Sensor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainService extends Service{

    public static boolean sIsStarted = false;

    private static ArrayList<Sensor> scanedSensors;
    private static long mLastFiltrationTime;
    private static int treashold = -70;
    private static float factor = 0.2f;

    private static Sensor lastSensor = null;
    private Sensor nearestSensor = null;
    private String locationName = "";
    private String locationDesc = "";
    private String locationCat =  "";

    private final IBinder myBinder = new MyBinder();

    public MainService() {
        super();
    }



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

                    if (!BeaconsMonitoringService.isScanRunning()) {
                        Toast.makeText(this, "Uletio u if", Toast.LENGTH_SHORT).show();
                        startService(new Intent(this, BeaconsMonitoringService.class));
                    } else {
                        Toast.makeText(this, "Uletio u else", Toast.LENGTH_SHORT).show();
                        BeaconsMonitoringService.refresh();
                    }

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
        return myBinder;
        //throw new UnsupportedOperationException("Not yet implemented");
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


                if (nearestSensor != null && lastSensor != nearestSensor){
                    final String snrName = nearestSensor.getSnrBleMac();

                    int userId = LoggedUser.getUser().getUserModel().getUserId();
                    ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
                    apiService.getLocation(snrName,userId).enqueue(new Callback< LocationModel>() {
                        @Override
                        public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                            if(response.body() != null) {
                                locationName = response.body().getName();
                                locationDesc = response.body().getDescription();
                                locationCat = response.body().getCategory();
                                LoggedUser.getUser().getUserModel().setCurrentLocationName(locationName);

                                if (LoggedUser.getUser().getUserModel().getNotification() == 1) {
                                    generateNotification(locationName, nearestSensor.getSnrSignalZ());
                                }

                                int locationId = response.body().getId();

                                Intent i = new Intent();
                                i.putExtra("LocationId",locationId);
                                i.putExtra("Naziv", locationName);
                                i.putExtra("Opis", locationDesc);
                                i.putExtra("Kategorija", locationCat);
                                i.setAction("ServiceIntent");
                                sendBroadcast(i);

                            }
                        }

                        @Override
                        public void onFailure(Call<LocationModel> call, Throwable t) {
                            Toast.makeText(MainService.this, "Greska u citanju naziva lokacije.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Date date = new Date(nearestSensor.getLastScannTime());
                    lastSensor = nearestSensor;
                }
            }
            mLogHandler.postDelayed(mStopRunnable, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void generateNotification(String locationName, int notId) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setVibrate(new long[] { 0, 500, 1000, 500, 1000})
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Indoor Tracking")
                        .setContentText("Prostorija: "+ locationName);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        mNotificationManager.notify(1, mBuilder.build());


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


    public class MyBinder extends Binder {
        public MainService getService() {
            return MainService.this;
        }
    }

}
