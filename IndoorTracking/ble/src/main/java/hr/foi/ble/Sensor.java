package hr.foi.ble;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Sensor {

    private int snrSensorId;
    private String snrBleMac;
    private int snrSignalCanIndex;
    private int snrSignalX;
    private int snrSignalY;
    private int snrSignalZ;
    private int snrSignalR;
    private BluetoothDevice bluetoothDevice;

    private long lastScannTime;

    public Sensor() {
    }

    public static ArrayList<Sensor> ParseSensors (JSONArray jsonArray){

        try {
            ArrayList<Sensor> sensors = new ArrayList<Sensor>();
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                int snrSensorId = -1;
                String snrBleMac = "";
                Sensor sensor = new Sensor();

                if (jsonObject.has("snr_ble_mac")) snrBleMac = jsonObject.getString("snr_ble_mac");

                sensor.setSnrSensorId(snrSensorId);
                sensor.setSnrSignalCanIndex(-1);
                sensor.setSnrSignalX(0);
                sensor.setSnrSignalY(0);
                sensor.setSnrSignalZ(0);
                sensors.add(sensor);
            }

            return sensors;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static Sensor ParseSensor (JSONObject jsonObject){

        try {
            Sensor sensor = new Sensor();

            int snrSensorId = -1;
            String snrBleMac = "";

            if (jsonObject.has("snr_ble_mac")) snrBleMac = jsonObject.getString("snr_ble_mac");

            sensor.setSnrSensorId(snrSensorId);
            sensor.setSnrSignalCanIndex(-1);
            sensor.setSnrSignalX(0);
            sensor.setSnrSignalY(0);
            sensor.setSnrSignalZ(0);
            sensor.setSnrSignalR(0);
            sensor.setLastScannTime(0);

            return sensor;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getSnrSensorId() {
        return snrSensorId;
    }

    public void setSnrSensorId(int snrSensorId) {
        this.snrSensorId = snrSensorId;
    }

    public String getSnrBleMac() {
        return snrBleMac;
    }

    public void setSnrBleMac(String snrBleMac) {
        this.snrBleMac = snrBleMac;
    }

    public int getSnrSignalX() {
        return snrSignalX;
    }

    public void setSnrSignalX(int snrSignalX) {
        this.snrSignalX = snrSignalX;
    }

    public int getSnrSignalZ() {
        return snrSignalZ;
    }

    public void setSnrSignalZ(int snrSignalZ) {
        this.snrSignalZ = snrSignalZ;
    }

    public int getSnrSignalY() {
        return snrSignalY;
    }

    public void setSnrSignalY(int snrSignalY) {
        this.snrSignalY = snrSignalY;
    }

    public int getSnrSignalR() {
        return snrSignalR;
    }

    public void setSnrSignalR(int snrSignalR) {
        this.snrSignalR = snrSignalR;
    }

    public long getLastScannTime() {
        return lastScannTime;
    }

    public void setLastScannTime(long lastScannTime) {
        this.lastScannTime = lastScannTime;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public int getSnrSignalCanIndex() {
        return snrSignalCanIndex;
    }

    public void setSnrSignalCanIndex(int snrSignalCanIndex) {
        this.snrSignalCanIndex = snrSignalCanIndex;
    }

    public static HashMap<String, String> getKnownSensors(){

        HashMap<String,String> knownSensors = new HashMap<>();

        knownSensors.put("A0:E6:F8:4F:AF:AB","T1");
        knownSensors.put("A0:E6:F8:4F:AF:BB","T2");
        knownSensors.put("A0:E6:F8:4F:AF:BC","T3");
        knownSensors.put("A0:E6:F8:4F:AD:69","T4");

        return knownSensors;

    }
}
