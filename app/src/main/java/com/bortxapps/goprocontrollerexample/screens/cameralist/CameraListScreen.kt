package com.bortxapps.goprocontrollerexample.screens.cameralist

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bortxapps.goprocontrollerandroid.domain.data.GoProCamera
import com.bortxapps.goprocontrollerandroid.domain.data.PairedState
import com.bortxapps.goprocontrollerexample.R
import com.bortxapps.goprocontrollerexample.ui.theme.GoProControllerExampleTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CameraListScreen(
    viewModel: CameraListViewModel = koinViewModel(), onNavigationToStatus: (String) -> Unit
) {

    Log.d("CameraListScreen", "CameraListScreen")

    val state by viewModel.state.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    AskForPermissions {
        LaunchedEffect(Unit) {
            viewModel.intentChannel.trySend(CameraListScreenIntent.SearchCameras)
        }
        Screen(state, onNavigationToStatus) {
            coroutineScope.launch {
                viewModel.intentChannel.send(it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(state: CameraListScreenState, onNavigationToStatus: (String) -> Unit, onSendIntent: (CameraListScreenIntent) -> Unit) {
    GoProControllerExampleTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text("Go Pro Controller Example")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.purple_200))
            )
        }) {
            Log.d("CameraListScreen", "Screen state: $state")
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (state) {
                    is CameraListScreenState.Error -> {
                        ErrorText(this)
                        SearchButton(true, this) { onSendIntent(CameraListScreenIntent.SearchCameras) }
                    }

                    is CameraListScreenState.EmptyList -> {
                        EmptyListText(this)
                        SearchButton(true, this) { onSendIntent(CameraListScreenIntent.SearchCameras) }
                    }

                    is CameraListScreenState.Loading -> {
                        LoadingScreen(
                            this,
                            onConnectToDevice = onNavigationToStatus,
                            state.cameras
                        )
                        SearchButton(false, this) { onSendIntent(CameraListScreenIntent.SearchCameras) }
                    }

                    is CameraListScreenState.Finished -> {
                        FinishedScreen(
                            this,
                            onConnectToDevice = onNavigationToStatus,
                            state.cameras
                        )
                        SearchButton(true, this) { onSendIntent(CameraListScreenIntent.SearchCameras) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AskForPermissions(okWindow: @Composable () -> Unit) {

    Log.d("CameraListScreen", "AskForPermissions")

    val permissions = mutableListOf<PermissionState>()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Camera permission state
        permissions.add(
            rememberPermissionState(
                Manifest.permission.BLUETOOTH_SCAN,
            )
        )
        permissions.add(
            rememberPermissionState(
                Manifest.permission.BLUETOOTH_CONNECT,
            )
        )
    } else {
        permissions.add(
            rememberPermissionState(
                Manifest.permission.BLUETOOTH,
            )
        )
    }



    if (permissions.all { it.status.isGranted }) {
        Log.d("CameraListScreen", "Camera permission Granted")
        okWindow()
    } else {
        Log.d("CameraListScreen", "Camera permission NOT Granted")
        GoProControllerExampleTheme {
            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Text("Go Pro Controller Example")
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.purple_200))
                )
            }) {
                Column(modifier = Modifier.padding(it)) {
                    val textToShow = if (permissions.any { permissionState ->  permissionState.status.shouldShowRationale }) {
                        // If the user has denied the permission but the rationale can be shown,
                        // then gently explain why the app requires this permission
                        "The camera is important for this app. Please grant the permission."
                    } else {
                        // If it's the first time the user lands on this feature, or the user
                        // doesn't want to be asked again for this permission, explain that the
                        // permission is required
                        "Camera permission required for this feature to be available. " +
                                "Please grant the permission"
                    }
                    Text(
                        text = textToShow,
                        textAlign = TextAlign.Center,
                        style = typography.headlineSmall,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                    Button(
                        onClick = {
                            permissions.forEach { permissionState ->  permissionState.launchPermissionRequest() }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Request permission")
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen(
    columnScope: ColumnScope,
    onConnectToDevice: (String) -> Unit,
    nearbyCameras: List<GoProCamera> = listOf()
) {
    CameraList(columnScope, onConnectToDevice, nearbyCameras)
    Loading(columnScope)
}

@Composable
private fun FinishedScreen(
    columnScope: ColumnScope,
    onConnectToDevice: (String) -> Unit,
    nearbyCameras: List<GoProCamera> = listOf()
) {
    CameraList(columnScope, onConnectToDevice, nearbyCameras)
}

@Composable
private fun CameraList(
    columnScope: ColumnScope,
    onConnectToDevice: (String) -> Unit,
    nearbyCameras: List<GoProCamera> = listOf()
) {
    columnScope.apply {
        LazyColumn(
            modifier = Modifier
                .weight(1f, true)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(nearbyCameras) { camera ->
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (camera.pairedState) {
                            PairedState.PAIRED_LOCAL -> colorResource(id = R.color.camera_already_connected)
                            PairedState.PAIRED_OTHER -> colorResource(id = R.color.camera_not_available)
                            else -> colorResource(id = R.color.camera_available)
                        },
                    ),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_videocam_24),
                                    contentDescription = "Camera",
                                    modifier = Modifier.padding(top = 10.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
                                )
                                Text(
                                    camera.name,
                                    Modifier
                                        .wrapContentWidth()
                                        .padding(top = 10.dp, bottom = 8.dp, start = 0.dp, end = 8.dp)
                                )
                            }
                            when (camera.pairedState) {
                                PairedState.PAIRED_LOCAL -> Text(
                                    text = "Already connected",
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                                )

                                PairedState.PAIRED_OTHER -> Text(
                                    text = "Paired with other device",
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                                )

                                else -> Spacer(modifier = Modifier.height(0.dp))
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { onConnectToDevice(camera.address) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text("Connect")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Loading(columnScope: ColumnScope) {
    columnScope.apply {
        CircularProgressIndicator(
            modifier = Modifier
                .width(75.dp)
                .height(75.dp)
                .padding(16.dp),
        )
    }
}

@Composable
private fun SearchButton(enabled: Boolean, columnScope: ColumnScope, onClickButton: () -> Unit) {
    columnScope.apply {
        Button(
            onClick = { onClickButton() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = enabled
        ) {
            Text("Retry Search")
        }
    }
}

@Composable
private fun ErrorText(columnScope: ColumnScope) {
    columnScope.apply {
        Text(
            text = "Something has going wrong",
            textAlign = TextAlign.Center,
            style = typography.headlineSmall,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Composable
private fun EmptyListText(columnScope: ColumnScope) {
    columnScope.apply {
        Text(
            text = "No cameras found",
            textAlign = TextAlign.Center,
            style = typography.headlineSmall,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Preview
@Composable
fun PreviewLoadingScreen() {
    Screen(CameraListScreenState.Loading((1..10).map { GoProCamera("Go pro $it", "address $it", PairedState.values().random()) }), {}, {})
}

@Preview
@Composable
fun PreviewErrorScreen() {
    Screen(CameraListScreenState.Error, {}, {})
}

@Preview
@Composable
fun PreviewEmptyScreen() {
    Screen(CameraListScreenState.EmptyList, {}, {})
}

@Preview
@Composable
fun PreviewFinishedScreen() {
    Screen(CameraListScreenState.Finished((1..15).map { GoProCamera("Go pro $it", "address $it", PairedState.values().random()) }), {}, {})
}