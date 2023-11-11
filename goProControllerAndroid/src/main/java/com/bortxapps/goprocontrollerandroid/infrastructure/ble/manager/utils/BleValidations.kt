package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException

private fun checkBluetoothEnabled(context: Context) {
    if (getBluetoothAdapter(context)?.isEnabled == false) {
        throw GoProException(GoProError.BLE_NOT_ENABLED)
    }
}

private fun checkPermissionsApiCodeS(context: Context) =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && (
            context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PERMISSION_GRANTED
                    || context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) != PERMISSION_GRANTED)

private fun checkPermissionsOldApi(context: Context) =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.S
            && (context.checkSelfPermission(android.Manifest.permission.BLUETOOTH) != PERMISSION_GRANTED)

private fun checkPermissions(context: Context) {
    if (checkPermissionsApiCodeS(context) || checkPermissionsOldApi(context)) {
        throw GoProException(GoProError.MISSING_BLE_PERMISSIONS)
    }
}

private fun checkBleHardwareAvailable(context: Context) {
    if (!context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_BLUETOOTH_LE)) {
        throw GoProException(GoProError.BLE_NOT_SUPPORTED)
    }
}

suspend fun <T> launchBleOperationWithValidations(context: Context, action: suspend () -> Result<T>): Result<T> {
    return try {
        checkBleHardwareAvailable(context)
        checkBluetoothEnabled(context)
        checkPermissions(context)
        action()
    } catch (ex: GoProException) {
        Result.failure(ex)
    } catch (ex: Exception) {
        Result.failure(GoProException(GoProError.OTHER))
    }
}