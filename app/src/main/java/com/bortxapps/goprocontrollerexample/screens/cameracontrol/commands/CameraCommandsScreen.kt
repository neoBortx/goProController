package com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import com.bortxapps.goprocontrollerexample.R
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands.intent.CameraCommandsScreenIntent
import com.bortxapps.goprocontrollerexample.ui.theme.GoProControllerExampleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CameraCommandsScreen(viewModel: CameraCommandsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()

    Screen(
        state,
        onResolutionChanged = { viewModel.intentChannel.trySend(CameraCommandsScreenIntent.ChangeResolution(it)) },
        onFrameRateChanged = { viewModel.intentChannel.trySend(CameraCommandsScreenIntent.ChangeFrameRate(it)) },
        onHyperSmoothChanged = { viewModel.intentChannel.trySend(CameraCommandsScreenIntent.ChangeHyperSmooth(it)) },
        onSpeedChanges = { viewModel.intentChannel.trySend(CameraCommandsScreenIntent.ChangeSpeed(it)) },
        onPresetsChanged = {
            viewModel.intentChannel.trySend(CameraCommandsScreenIntent.ChangePresets(it))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    state: CameraCommandsScreenState,
    onResolutionChanged: (Resolution) -> Unit,
    onFrameRateChanged: (FrameRate) -> Unit,
    onHyperSmoothChanged: (HyperSmooth) -> Unit,
    onSpeedChanges: (Speed) -> Unit,
    onPresetsChanged: (Presets) -> Unit
) {
    Log.d("CameraStatusScreen", "Screen: $state")

    if (state is CameraCommandsScreenState.StateRetrieved && state.lastCommandRejected) {
        Toast.makeText(LocalContext.current, "Error -> Command rejected", Toast.LENGTH_SHORT).show()
    }

    GoProControllerExampleTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text("Go Pro Controller Example")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.purple_200))
            )
        }) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                state.let {
                    when (it) {
                        is CameraCommandsScreenState.Loading -> Loading()
                        is CameraCommandsScreenState.Error -> ErrorText(this)
                        is CameraCommandsScreenState.StateRetrieved -> CommandsList(
                            it,
                            onResolutionChanged,
                            onFrameRateChanged,
                            onHyperSmoothChanged,
                            onSpeedChanges,
                            onPresetsChanged
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommandsList(
    state: CameraCommandsScreenState.StateRetrieved,
    onResolutionChanged: (Resolution) -> Unit,
    onFrameRateChanged: (FrameRate) -> Unit,
    onHyperSmoothChanged: (HyperSmooth) -> Unit,
    onSpeedChanges: (Speed) -> Unit,
    onPresetsChanged: (Presets) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(text = "Preset mode", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        var showPresetsMenu by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { showPresetsMenu = !showPresetsMenu }, modifier = Modifier.fillMaxWidth()) {
                Row {
                    Icon(Icons.Default.ArrowDropDown, "")
                    Text(text = state.presets.toString(), Modifier.fillMaxWidth())
                }
            }
            DropdownMenu(expanded = showPresetsMenu, onDismissRequest = { showPresetsMenu = false }) {
                Presets.values().forEach {
                    DropdownMenuItem(onClick = {
                        onPresetsChanged(it)
                        showPresetsMenu = false
                    }, text = {
                        Text(text = it.name)
                    })
                }
            }
        }

        Text(text = "Resolution", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        var showResolutionMenu by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { showResolutionMenu = !showResolutionMenu }, modifier = Modifier.fillMaxWidth()) {
                Row {
                    Icon(Icons.Default.ArrowDropDown, "")
                    Text(text = state.resolution.toString(), Modifier.fillMaxWidth())
                }
            }
            DropdownMenu(expanded = showResolutionMenu, onDismissRequest = { showResolutionMenu = false }) {
                Resolution.values().forEach {
                    DropdownMenuItem(onClick = {
                        onResolutionChanged(it)
                        showResolutionMenu = false
                    }, text = {
                        Text(text = it.name)
                    })
                }
            }
        }

        Text(text = "Frame Rate", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        var showFramesMenu by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { showFramesMenu = !showFramesMenu }, modifier = Modifier.fillMaxWidth()) {
                Row {
                    Icon(Icons.Default.ArrowDropDown, "")
                    Text(text = state.frameRate.toString(), Modifier.fillMaxWidth())
                }
            }
            DropdownMenu(expanded = showFramesMenu, onDismissRequest = { showFramesMenu = false }) {
                FrameRate.values().forEach {
                    DropdownMenuItem(onClick = {
                        onFrameRateChanged(it)
                        showFramesMenu = false
                    }, text = {
                        Text(text = it.name)
                    })
                }
            }
        }

        Text(text = "Hypersmooth", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        var showHyperSmoothMenu by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { showHyperSmoothMenu = !showHyperSmoothMenu }, modifier = Modifier.fillMaxWidth()) {
                Row {
                    Icon(Icons.Default.ArrowDropDown, "")
                    Text(text = state.hyperSmooth.toString(), Modifier.fillMaxWidth())
                }
            }
            DropdownMenu(expanded = showHyperSmoothMenu, onDismissRequest = { showHyperSmoothMenu = false }) {
                HyperSmooth.values().forEach {
                    DropdownMenuItem(onClick = {
                        onHyperSmoothChanged(it)
                        showHyperSmoothMenu = false
                    }, text = {
                        Text(text = it.name)
                    })
                }
            }
        }

        Text(text = "Speed", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        var showSpeedMenu by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { showSpeedMenu = !showSpeedMenu }, modifier = Modifier.fillMaxWidth()) {
                Row {
                    Icon(Icons.Default.ArrowDropDown, "")
                    Text(text = state.speed.toString(), Modifier.fillMaxWidth())
                }
            }
            DropdownMenu(expanded = showSpeedMenu, onDismissRequest = { showSpeedMenu = false }) {
                Speed.values().forEach {
                    DropdownMenuItem(onClick = {
                        onSpeedChanges(it)
                        showSpeedMenu = false
                    }, text = {
                        Text(text = it.name)
                    })
                }
            }
        }
    }
}

@Composable
private fun Loading() {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp)
                .padding(16.dp)
        )
        Text(
            text = "Retrieving data from Go Pro",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
private fun ErrorText(columnScope: ColumnScope) {
    columnScope.apply {
        Text(
            text = "Something has going wrong",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}