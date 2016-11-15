package hr.foi.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Korisnik on 18.11.2015..
 */
public class BluetoothStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {

//                context.stopService(new Intent(context, BeaconsMonitoringService.class));
//                MainFragment mainFragment = MainFragment.getFragment();
//                if (mainFragment != null)mainFragment.disablePayAvailability();
            } else {
//            	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            	context.registerReceiver(mReceiver, filter);
                //MainFragment mainFragment = MainFragment.getFragment();
                //if (mainFragment != null)
//                context.startService(new Intent(context, BeaconsMonitoringService.class));

            }
        } if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //Log.d("bluetoothDevice", device.getName());
            // Add the name and address to an array adapter to show in a ListView
            //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
        }


//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter);

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("bluetoothDevice", device.getName());
                // Add the name and address to an array adapter to show in a ListView
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };
    // Register the BroadcastReceiver
//	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//	registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

}
