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
    private float snrLatitude;
    private float snrLongitude;
    private String snrName;
    private String snrSerial;
    private String snrBleMac;
    private String snrCountryName;
    private String snrCityName;
    private String snrStreetName;
    private String snrAdrStreetNumber;
    private String snrPznName;
    private String snrSptLabel;
    private String snrBussId;
    private int snrCanIndex;
    private int snrParkingSpotId;

    private int sptAddressId;
    private int sptParkingSpaceId;
    private int sptParkingZoneTypeId;
    private int sptSpotTypeId;
    private String pznPrice;
    private String conCurrency;
    private int pznMaxDuration;
    private int snrSignalCanIndex;
    private int snrSignalX;
    private int snrSignalY;
    private int snrSignalZ;
    private int snrSignalR;
    private BluetoothDevice bluetoothDevice;
    private boolean sptOccupied;

    private long lastScannTime;

    public Sensor() {
    }

    public static ArrayList<Sensor> ParseSensors (JSONArray jsonArray){

        try {
            ArrayList<Sensor> sensors = new ArrayList<Sensor>();
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                int snrSensorId = -1;
                String label = "";
                String latitude = "";
                String longitude = "";
                String adress = "Adress: ";
                String parking = "Parking: ";
                String snrSerial = "";
                String snrBleMac = "";
                int snrCanIndex = -1;
                int snrParkingSpotId = -1;

                String snrCountryName = "";
                String snrCityName = "";
                String snrStreetName = "";
                String snrStreetNumber = "";
                String snrPznName = "";
                String snrBusId = "";
                int sptAddressId = -1;
                int sptParkingSpaceId = -1;
                int sptParkingZoneTypeId = -1;
                int sptSpotTypeId = -1;
                String snrPznPrice = "";

                Sensor sensor = new Sensor();

                //if (jsonObject.has("snr_sensor_id")) snrSensorId = Misc.getInt(jsonObject.getString("snr_sensor_id"));
                if (jsonObject.has("cou_name")) snrCountryName = jsonObject.getString("cou_name");
                if (jsonObject.has("cty_name")) snrCityName = jsonObject.getString("cty_name");
                if (jsonObject.has("adr_street_name")) snrStreetName = jsonObject.getString("adr_street_name");
                if (jsonObject.has("spt_street_number")) snrStreetNumber = jsonObject.getString("spt_street_number");
                if (jsonObject.has("pzn_name")) snrPznName = jsonObject.getString("pzn_name");
                if (jsonObject.has("pzn_price")) snrPznPrice = jsonObject.getString("pzn_price");
                if (jsonObject.has("spt_label")) label = jsonObject.getString("spt_label");
                if (jsonObject.has("snr_latitude")) latitude = jsonObject.getString("snr_latitude");
                if (jsonObject.has("snr_longitude")) longitude = jsonObject.getString("snr_longitude");
                if (jsonObject.has("cty_name")) adress += jsonObject.getString("cty_name");
                if (jsonObject.has("adr_street_name")) adress += ", "+jsonObject.getString("adr_street_name");
                if (jsonObject.has("spt_street_number")) adress += ", "+jsonObject.getString("spt_street_number");

                if (jsonObject.has("spt_label")) parking += jsonObject.getString("spt_label");
                if (jsonObject.has("pzn_name")) parking += ", "+jsonObject.getString("pzn_name");
                if (jsonObject.has("snr_serial_no")) snrSerial = jsonObject.getString("snr_serial_no");
                if (jsonObject.has("snr_ble_mac")) snrBleMac = jsonObject.getString("snr_ble_mac");
                if (jsonObject.has("snr_sensor_bus_id")) snrBusId = jsonObject.getString("snr_sensor_bus_id");
                //if (jsonObject.has("snr_can_index")) snrCanIndex = Misc.getInt(jsonObject.getString("snr_can_index"));
                //if (jsonObject.has("snr_parking_spot_id")) snrParkingSpotId = Misc.getInt(jsonObject.getString("snr_parking_spot_id"));

                //if (jsonObject.has("spt_address_id")) sptAddressId = Misc.getInt(jsonObject.getString("spt_address_id"));
                //if (jsonObject.has("spt_parking_space_id")) sptParkingSpaceId = Misc.getInt(jsonObject.getString("spt_parking_space_id"));
                //if (jsonObject.has("spt_parking_zone_type_id")) sptParkingZoneTypeId = Misc.getInt(jsonObject.getString("spt_parking_zone_type_id"));
                //if (jsonObject.has("spt_spot_type_id")) sptSpotTypeId = Misc.getInt(jsonObject.getString("spt_spot_type_id"));
                if (jsonObject.has("spt_occupied") && !jsonObject.isNull("spt_occupied")) sensor.setSptOccupied(jsonObject.getBoolean("spt_occupied"));

                Log.d("SENSOR", label + " " + latitude + " " + longitude);

//                float lat = Misc.getFloat(latitude);
//                float lng = Misc.getFloat(longitude);


                sensor.setSnrSensorId(snrSensorId);
                sensor.setSnrSerial(snrSerial);
                sensor.setSnrBleMac(snrBleMac);
                sensor.setSnrSptLabel(label);
//                sensor.setSnrLatitude(lat);
//                sensor.setSnrLongitude(lng);
                sensor.setSnrCityName(snrCityName);
                sensor.setSnrCountryName(snrCountryName);
                sensor.setSnrStreetName(snrStreetName);
                sensor.setSnrAdrStreetNumber(snrStreetNumber);
                sensor.setSnrPznName(snrPznName);
                sensor.setSnrBussId(snrBusId);
                sensor.setSnrCanIndex(snrCanIndex);

                sensor.setSnrParkingSpotId(snrParkingSpotId);
                sensor.setSnrSignalCanIndex(-1);
                sensor.setSnrSignalX(0);
                sensor.setSnrSignalY(0);
                sensor.setSnrSignalZ(0);
                sensor.setSptAddressId(sptAddressId);
                sensor.setSptParkingSpaceId(sptParkingSpaceId);
                sensor.setSptParkingZoneTypeId(sptParkingZoneTypeId);
                sensor.setSptSpotTypeId(sptSpotTypeId);
                sensor.setPznPrice(snrPznPrice);
                //branchSensors.add(sensor);
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
            String label = "";
            String latitude = "";
            String longitude = "";
            String adress = "Adress: ";
            String parking = "Parking: ";
            String snrSerial = "";
            String snrBleMac = "";
            int snrCanIndex = -1;
            int snrParkingSpotId = -1;

            String snrCountryName = "";
            String snrCityName = "";
            String snrStreetName = "";
            String snrStreetNumber = "";
            String snrPznName = "";
            String snrBusId = "";
            int sptAddressId = -1;
            int sptParkingSpaceId = -1;
            int sptParkingZoneTypeId = -1;
            int sptSpotTypeId = -1;
            String snrPznPrice = "";
            String conCurrency = "";
            int pznMaxDuration = -1;

            //if (jsonObject.has("snr_sensor_id")) snrSensorId = Misc.getInt(jsonObject.getString("snr_sensor_id"));
            if (jsonObject.has("cou_name")) snrCountryName = jsonObject.getString("cou_name");
            if (jsonObject.has("cty_name")) snrCityName = jsonObject.getString("cty_name");
            if (jsonObject.has("adr_street_name")) snrStreetName = jsonObject.getString("adr_street_name");
            if (jsonObject.has("spt_street_number")) snrStreetNumber = jsonObject.getString("spt_street_number");
            if (jsonObject.has("pzn_name")) snrPznName = jsonObject.getString("pzn_name");
            if (jsonObject.has("pzn_price")) snrPznPrice = jsonObject.getString("pzn_price");

            if (jsonObject.has("spt_label")) label = jsonObject.getString("spt_label");
            if (jsonObject.has("snr_latitude")) latitude = jsonObject.getString("snr_latitude");
            if (jsonObject.has("snr_longitude")) longitude = jsonObject.getString("snr_longitude");
            if (jsonObject.has("cty_name")) adress += jsonObject.getString("cty_name");
            if (jsonObject.has("adr_street_name")) adress += ", "+jsonObject.getString("adr_street_name");
            if (jsonObject.has("spt_street_number")) adress += ", "+jsonObject.getString("spt_street_number");

            if (jsonObject.has("spt_label")) parking += jsonObject.getString("spt_label");
            if (jsonObject.has("pzn_name")) parking += ", "+jsonObject.getString("pzn_name");
            if (jsonObject.has("snr_serial_no")) snrSerial = jsonObject.getString("snr_serial_no");
            if (jsonObject.has("snr_ble_mac")) snrBleMac = jsonObject.getString("snr_ble_mac");
            if (jsonObject.has("snr_sensor_bus_id")) snrBusId = jsonObject.getString("snr_sensor_bus_id");
//            if (jsonObject.has("snr_can_index")) snrCanIndex = Misc.getInt(jsonObject.getString("snr_can_index"));
//            if (jsonObject.has("snr_parking_spot_id")) snrParkingSpotId = Misc.getInt(jsonObject.getString("snr_parking_spot_id"));
//
//            if (jsonObject.has("spt_address_id")) sptAddressId = Misc.getInt(jsonObject.getString("spt_address_id"));
//            if (jsonObject.has("spt_parking_space_id") && !jsonObject.isNull("spt_parking_space_id")) sptParkingSpaceId = Misc.getInt(jsonObject.getString("spt_parking_space_id"));
//            if (jsonObject.has("spt_parking_zone_type_id")) sptParkingZoneTypeId = Misc.getInt(jsonObject.getString("spt_parking_zone_type_id"));
//            if (jsonObject.has("spt_spot_type_id")) sptSpotTypeId = Misc.getInt(jsonObject.getString("spt_spot_type_id"));

            if (jsonObject.has("pzn_price")) snrPznPrice = jsonObject.getString("pzn_price");
            if (jsonObject.has("con_currency")) conCurrency = jsonObject.getString("con_currency");
            if (jsonObject.has("pzn_max_duration")) pznMaxDuration = jsonObject.getInt("pzn_max_duration");
            if (jsonObject.has("spt_occupied") && !jsonObject.isNull("spt_occupied")) sensor.setSptOccupied(jsonObject.getBoolean("spt_occupied"));

            Log.d("SENSOR", label + " " + latitude + " " + longitude);

//            float lat = Misc.getFloat(latitude);
//            float lng = Misc.getFloat(longitude);

            sensor.setSnrSensorId(snrSensorId);
            sensor.setSnrSerial(snrSerial);
            sensor.setSnrBleMac(snrBleMac);
            sensor.setSnrSptLabel(label);
//            sensor.setSnrLatitude(lat);
//            sensor.setSnrLongitude(lng);
            sensor.setSnrCityName(snrCityName);
            sensor.setSnrCountryName(snrCountryName);
            sensor.setSnrStreetName(snrStreetName);
            sensor.setSnrAdrStreetNumber(snrStreetNumber);
            sensor.setSnrPznName(snrPznName);
            sensor.setSnrBussId(snrBusId);
            sensor.setSnrCanIndex(snrCanIndex);

            sensor.setSnrParkingSpotId(snrParkingSpotId);
            sensor.setSnrSignalCanIndex(-1);
            sensor.setSnrSignalX(0);
            sensor.setSnrSignalY(0);
            sensor.setSnrSignalZ(0);
            sensor.setSnrSignalR(0);
            sensor.setSptAddressId(sptAddressId);
            sensor.setSptParkingSpaceId(sptParkingSpaceId);
            sensor.setSptParkingZoneTypeId(sptParkingZoneTypeId);
            sensor.setSptSpotTypeId(sptSpotTypeId);
            sensor.setLastScannTime(0);
            sensor.setPznPrice(snrPznPrice);
            sensor.setConCurrency(conCurrency);
            sensor.setPznMaxDuration(pznMaxDuration);

            return sensor;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getSnrPznName() {
        return snrPznName;
    }

    public void setSnrPznName(String snrPznName) {
        this.snrPznName = snrPznName;
    }

    public float getSnrLatitude() {
        return snrLatitude;
    }

    public void setSnrLatitude(float snrLatitude) {
        this.snrLatitude = snrLatitude;
    }

    public float getSnrLongitude() {
        return snrLongitude;
    }

    public void setSnrLongitude(float snrLongitude) {
        this.snrLongitude = snrLongitude;
    }

    public String getSnrName() {
        return snrName;
    }

    public void setSnrName(String snrName) {
        this.snrName = snrName;
    }

    public String getSnrCountryName() {
        return snrCountryName;
    }

    public void setSnrCountryName(String snrCountryName) {
        this.snrCountryName = snrCountryName;
    }

    public String getSnrCityName() {
        return snrCityName;
    }

    public void setSnrCityName(String snrCityName) {
        this.snrCityName = snrCityName;
    }

    public String getSnrAdrStreetNumber() {
        return snrAdrStreetNumber;
    }

    public void setSnrAdrStreetNumber(String snrAdrStreetNumber) {
        this.snrAdrStreetNumber = snrAdrStreetNumber;
    }

    public int getSnrSensorId() {
        return snrSensorId;
    }

    public void setSnrSensorId(int snrSensorId) {
        this.snrSensorId = snrSensorId;
    }

    public String getSnrSerial() {
        return snrSerial;
    }

    public void setSnrSerial(String snrSerial) {
        this.snrSerial = snrSerial;
    }

    public String getSnrBleMac() {
        return snrBleMac;
    }

    public void setSnrBleMac(String snrBleMac) {
        this.snrBleMac = snrBleMac;
    }

    public String getSnrStreetName() {
        return snrStreetName;
    }

    public void setSnrStreetName(String snrStreetName) {
        this.snrStreetName = snrStreetName;
    }

    public String getSnrSptLabel() {
        return snrSptLabel;
    }

    public void setSnrSptLabel(String snrSptLabel) {
        this.snrSptLabel = snrSptLabel;
    }

    public String getSnrBussId() {
        return snrBussId;
    }

    public void setSnrBussId(String snrBussId) {
        this.snrBussId = snrBussId;
    }

    public int getSnrCanIndex() {
        return snrCanIndex;
    }

    public void setSnrCanIndex(int snrCanIndex) {
        this.snrCanIndex = snrCanIndex;
    }



    public int getSnrParkingSpotId() {
        return snrParkingSpotId;
    }

    public void setSnrParkingSpotId(int snrParkingSpotId) {
        this.snrParkingSpotId = snrParkingSpotId;
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

    public int getSptAddressId() {
        return sptAddressId;
    }

    public void setSptAddressId(int sptAddressId) {
        this.sptAddressId = sptAddressId;
    }

    public int getSptParkingSpaceId() {
        return sptParkingSpaceId;
    }

    public void setSptParkingSpaceId(int sptParkingSpaceId) {
        this.sptParkingSpaceId = sptParkingSpaceId;
    }

    public int getSptParkingZoneTypeId() {
        return sptParkingZoneTypeId;
    }

    public void setSptParkingZoneTypeId(int sptParkingZoneTypeId) {
        this.sptParkingZoneTypeId = sptParkingZoneTypeId;
    }

    public int getSptSpotTypeId() {
        return sptSpotTypeId;
    }

    public void setSptSpotTypeId(int sptSpotTypeId) {
        this.sptSpotTypeId = sptSpotTypeId;
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

    public String getPznPrice() {
        return pznPrice;
    }

    public void setPznPrice(String pznPrice) {
        this.pznPrice = pznPrice;
    }

    public String getConCurrency() {
        return conCurrency;
    }

    public void setConCurrency(String conCurrency) {
        this.conCurrency = conCurrency;
    }

    public int getPznMaxDuration() {
        return pznMaxDuration;
    }

    public void setPznMaxDuration(int pznMaxDuration) {
        this.pznMaxDuration = pznMaxDuration;
    }

    public boolean isSptOccupied() {
        return sptOccupied;
    }

    public void setSptOccupied(boolean sptOccupied) {
        this.sptOccupied = sptOccupied;
    }




    public static HashMap<String, String> getKnownSensors(){

        HashMap<String,String> knownSensors = new HashMap<>();

        knownSensors.put("A0:E6:F8:4F:43:E9","1C1C2");
        knownSensors.put("A0:E6:F8:4F:43:C6","1C2C1");
        knownSensors.put("A0:E6:F8:4F:49:9E","1C1B2");
        knownSensors.put("A0:E6:F8:4F:AE:21","Einfahrt C1");
        knownSensors.put("A0:E6:F8:4F:47:92","Einfahrt Level 2");
        knownSensors.put("A0:E6:F8:4F:43:CB","2C1C2");
        knownSensors.put("A0:E6:F8:4F:43:AB","2B2C1");
        knownSensors.put("A0:E6:F8:4F:AB:FE","2B1B2");
        knownSensors.put("A0:E6:F8:4F:43:8C","2A2B1");
        knownSensors.put("A0:E6:F8:4F:49:BA","2A1A2");
        knownSensors.put("A0:E6:F8:4F:45:6B","2C2C1");
        knownSensors.put("A0:E6:F8:4F:43:D5","2C1B2");
        knownSensors.put("A0:E6:F8:4F:43:EE","2B2B1");
        knownSensors.put("A0:E6:F8:4F:AE:0B","2B1A2");
        knownSensors.put("A0:E6:F8:4F:47:B2","2A2A1");
        knownSensors.put("A0:E6:F8:4F:43:D4","Einfahrt Level 2");
        knownSensors.put("A0:E6:F8:4F:AF:97","Einfahrt Level 3 UP");
        knownSensors.put("A0:E6:F8:4F:43:DF","3C1C2");
        knownSensors.put("A0:E6:F8:4F:45:01","3B2C1");
        knownSensors.put("A0:E6:F8:4F:AF:8B","3B1B2");
        knownSensors.put("A0:E6:F8:4F:43:C9","3A2B1");
        knownSensors.put("A0:E6:F8:4F:45:18","3A1A2");
        knownSensors.put("A0:E6:F8:4F:43:B7","3C2C1");
        knownSensors.put("AO:E6:F8:4F:47:8A","3C1B2");
        knownSensors.put("A0:E6:F8:4F:43:E5","3B2B1");
        knownSensors.put("A0:E6:F8:4F:47:F0","3B1A2");
        knownSensors.put("A0:E6:F8:4F:45:7C","3A2A1");
        knownSensors.put("A0:E6:F8:4F:49:AF","Einfahrt Level 3");
        knownSensors.put("A0:E6:F8:4F:49:BF","Einfahrt Level 4 UP");
        knownSensors.put("A0:E6:F8:4F:43:B9","4C1C2");
        knownSensors.put("A0:E6:F8:4F:43:91","4B2C1");
        knownSensors.put("A0:E6:F8:4F:49:CA","4B1B2");
        knownSensors.put("A0:E6:F8:4F:49:80","4A2B1");
        knownSensors.put("A0:E6:F8:4F:45:35","4A1A2");
        knownSensors.put("A0:E6:F8:4F:43:FC","4C2C1");
        knownSensors.put("A0:E6:F8:4F:47:9B","4C1B2");
        knownSensors.put("A0:E6:F8:4F:43:83","4B2B1");
        knownSensors.put("A0:E6:F8:4F:45:2C","4B1A2");
        knownSensors.put("A0:E6:F8:4F:43:DB","4A2A1");
        knownSensors.put("A0:E6:F8:4F:45:73","Einfahrt Level 4");
        knownSensors.put("A0:E6:F8:4F:49:99","Einfahrt Level 5 UP");
        knownSensors.put("A0:E6:F8:4F:47:A7","5C1C2");
        knownSensors.put("A0:E6:F8:4F:45:4F","5B2C1");
        knownSensors.put("A0:E6:F8:4F:47:EC","5B1B2");
        knownSensors.put("A0:E6:F8:4F:49:BE","5A2B1");
        knownSensors.put("A0:E6:F8:4F:49:A4","5A1A2");
        knownSensors.put("A0:E6:F8:4F:45:4E","5C2C1");
        knownSensors.put("A0:E6:F8:4F:43:B1","5C1B2");
        knownSensors.put("24:71:89:ED:36:C5","5B2B1");
        knownSensors.put("A0:E6:F8:4F:AD:6D","5B1A2");
        knownSensors.put("A0:E6:F8:4F:47:8F","5A2A1");
        knownSensors.put("A0:E6:F8:4F:43:A9","Einfahrt Level 5");
        knownSensors.put("24:71:89:ED:29:C2","Einfahrt Level 6 UP");
        knownSensors.put("24:71:89:ED:15:09","Ausfahrt Rampe 2");
        knownSensors.put("24:71:89:ED:1E:92","Ausfahrt Rampe 3");
        knownSensors.put("24:71:89:ED:20:FC","Ausfahrt Rampe 4");
        knownSensors.put("24:71:89:ED:17:64","Ausfahrt Rampe 5");
        knownSensors.put("24:71:89:ED:17:71","Ausfahrt Rampe 6");
        knownSensors.put("24:71:89:ED:15:0E","6A1A2");
        knownSensors.put("24:71:89:ED:17:11","6A2B1");
        knownSensors.put("24:71:89:ED:17:47","6A2A1");
        knownSensors.put("24:71:89:ED:19:F4","6B1B2");
        knownSensors.put("24:71:89:ED:1E:EB","6B1A2");
        knownSensors.put("24:71:89:ED:19:52","6B2C1");
        knownSensors.put("24:71:89:ED:19:90","Einfahrt Level 6");
        knownSensors.put("24:71:89:ED:32:34","Einfahrt Level 7 UP");
        knownSensors.put("24:71:89:ED:13:71","6B2B1");
        knownSensors.put("24:71:89:ED:2D:9D","6C1C2");
        knownSensors.put("24:71:89:ED:1B:67","6C1B2");
        knownSensors.put("24:71:89:ED:2C:5D","6C2C1");
        knownSensors.put("24:71:89:ED:1E:9E","Ausfahrt Rampe 7");
        knownSensors.put("24:71:89:ED:3B:49","7A1A2");
        knownSensors.put("24:71:89:ED:1D:46","7A2B1");
        knownSensors.put("24:71:89:ED:39:7A","7A2A1");
        knownSensors.put("24:71:89:ED:22:28","7B1B2");
        knownSensors.put("24:71:89:ED:22:0C","7B1A2");
        knownSensors.put("24:71:89:ED:1B:0F","7B2C1");
        knownSensors.put("24:71:89:ED:36:9E","Einfahrt Level 7");
        knownSensors.put("24:71:89:ED:27:CE","Einfahrt Level 8 UP");
        knownSensors.put("24:71:89:ED:3B:40","7B2B1");
        knownSensors.put("24:71:89:ED:3B:52","7C1C2");
        knownSensors.put("24:71:89:ED:29:DD","7C1B2");
        knownSensors.put("24:71:89:ED:1E:CA","7C2C1");
        knownSensors.put("24:71:89:ED:1D:68","Ausfahrt Rampe 8");
        knownSensors.put("24:71:89:ED:1D:2E","8A1A2");
        knownSensors.put("24:71:89:ED:23:B8","8A2B1");
        knownSensors.put("24:71:89:ED:22:02","8A2A1");
        knownSensors.put("24:71:89:ED:29:CD","8B1B2");
        knownSensors.put("24:71:89:ED:3B:74","8B1A2");
        knownSensors.put("24:71:89:ED:23:84","8B2B1");
        knownSensors.put("24:71:89:ED:19:80","Einfahrt Level 8");
        knownSensors.put("24:71:89:ED:36:E7","8B2C1");
        knownSensors.put("24:71:89:ED:20:D8","Reserve 1");
        knownSensors.put("24:71:89:ED:15:47","Reserve 2");
        knownSensors.put("24:71:89:ED:3B:2F","Reserve 3");
        knownSensors.put("24:71:89:ED:1E:B0","Reserve 4");

        knownSensors.put("A0:E6:F8:4F:AF:AB","T1");
        knownSensors.put("A0:E6:F8:4F:AF:BB","T2");
        knownSensors.put("A0:E6:F8:4F:AF:BC","T3");
        knownSensors.put("A0:E6:F8:4F:AD:69","T4");


        return knownSensors;

    }


}
