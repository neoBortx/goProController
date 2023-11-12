package com.bortxapps.goprocontrollerexample.screens.cameralist.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bortxapps.goprocontrollerandroid.domain.data.GoProCamera
import com.bortxapps.goprocontrollerandroid.domain.data.PairedState
import com.bortxapps.goprocontrollerexample.R
import com.bortxapps.goprocontrollerexample.screens.cameralist.CameraListScreenState
import com.bortxapps.goprocontrollerexample.screens.cameralist.CameraListViewModel
import com.bortxapps.goprocontrollerexample.screens.cameralist.intent.CameraListScreenIntent
import com.bortxapps.goprocontrollerexample.ui.theme.GoProControllerExampleTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraListScreen(
    viewModel: CameraListViewModel = koinViewModel(),
    onNavigationToStatus: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    AskForPermissions ({
        LaunchedEffect(Unit) {
            viewModel.intentChannel.trySend(CameraListScreenIntent.SearchCameras)
        }
        Screen(state, onNavigationToStatus) {
            coroutineScope.launch {
                viewModel.intentChannel.send(it)
            }
        }
    }, {
        NoPermissionsWindow(it)
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    state: CameraListScreenState,
    onNavigationToStatus: (String) -> Unit,
    onSendIntent: (CameraListScreenIntent) -> Unit
) {
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (state) {
                    is CameraListScreenState.Error -> {
                        ErrorText(this, state.error)
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun NoPermissionsWindow(permissions: List<PermissionState>) {
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
                val textToShow = if (permissions.any { permissionState -> permissionState.status.shouldShowRationale }) {
                    "The camera is important for this app. Please grant the permission."
                } else {
                    "Camera permission required for this feature to be available. " + "Please grant the permission"
                }
                Text(
                    text = textToShow,
                    textAlign = TextAlign.Center,
                    style = typography.headlineSmall,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Button(
                    onClick = {
                        permissions.forEach { permissionState -> permissionState.launchPermissionRequest() }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Request permission")
                }
            }
        }
    }
}

@Composable
fun FinishedScreen(
    columnScope: ColumnScope,
    onConnectToDevice: (String) -> Unit,
    nearbyCameras: List<GoProCamera> = listOf()
) {
    CameraList(columnScope, onConnectToDevice, nearbyCameras)
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
private fun Loading(columnScope: ColumnScope) {
    columnScope.apply {
        CircularProgressIndicator(
            modifier = Modifier
                .width(75.dp)
                .height(75.dp)
                .padding(16.dp)
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

@Preview
@Composable
fun PreviewLoadingScreen() {
    val numberRows = 10
    Screen(CameraListScreenState.Loading((1..numberRows).map { GoProCamera("Go pro $it", "address $it", PairedState.values().random()) }), {}, {})
}
@Preview
@Composable
fun PreviewEmptyScreen() {
    Screen(CameraListScreenState.EmptyList, {}, {})
}

@Preview
@Composable
fun PreviewFinishedScreen() {
    val numberRows = 15
    Screen(CameraListScreenState.Finished((1..numberRows).map { GoProCamera("Go pro $it", "address $it", PairedState.values().random()) }), {}, {})
}