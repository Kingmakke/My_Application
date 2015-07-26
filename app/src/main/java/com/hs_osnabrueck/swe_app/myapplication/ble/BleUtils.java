package com.hs_osnabrueck.swe_app.myapplication.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * This is a utility class for BLE
 */
public class BleUtils {

    public static final int STATUS_BLE_ENABLED = 0;
    public static final int STATUS_BLUETOOTH_NOT_AVAILABLE = 1;
    public static final int STATUS_BLE_NOT_AVAILABLE = 2;
    public static final int STATUS_BLUETOOTH_DISABLED = 3;

    /**
     * Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
     * BluetoothAdapter through BluetoothManager.
     * @param context
     * @return reference to BluetoothAdapter
     */
    public static BluetoothAdapter getBluetoothAdapter(Context context) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null)
            return null;
        return bluetoothManager.getAdapter();
    }

    /**
     * Check to determine whether BLE is supported on the device. Then you can
     * selectively disable BLE-related features.
     * @param context
     * @return status, whether BLE is enabled or disabled or not supported
     */
    public static int getBleStatus(Context context) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return STATUS_BLE_NOT_AVAILABLE;
        }

        final BluetoothAdapter adapter = getBluetoothAdapter(context);
        // Checks if Bluetooth is supported on the device.
        if (adapter == null) {
            return STATUS_BLUETOOTH_NOT_AVAILABLE;
        }

        if (adapter.isEnabled())
            return STATUS_BLUETOOTH_DISABLED;

        return STATUS_BLE_ENABLED;
    }
}
