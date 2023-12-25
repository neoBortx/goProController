package com.bortxapps.goprocontrollerexample.screens.cameralist.views

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AskForPermissions(okWindow: @Composable () -> Unit, errorWindow: @Composable (permissions: List<PermissionState>) -> Unit) {
    Log.d("CameraListScreen", "AskForPermissions")

    val permissions = mutableListOf<PermissionState>()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Camera permission state
        permissions.add(
            rememberPermissionState(
                Manifest.permission.BLUETOOTH_SCAN
            )
        )
        permissions.add(
            rememberPermissionState(
                Manifest.permission.BLUETOOTH_CONNECT
            )
        )
    } else {
        permissions.add(
            rememberPermissionState(
                Manifest.permission.BLUETOOTH
            )
        )
    }

    permissions.add(
        rememberPermissionState(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    permissions.add(
        rememberPermissionState(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (permissions.all { it.status.isGranted }) {
        Log.d("CameraListScreen", "Camera permission Granted")
        okWindow()
    } else {
        Log.d("CameraListScreen", "Camera permission NOT Granted")
        errorWindow(permissions)
    }
}