package com.bortxapps.goprocontrollerexample.screens.cameracontrol.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bortxapps.goprocontrollerexample.R
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands.CameraCommandsScreen
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.media.CameraMediaScreen
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.status.CameraStatusScreen
import com.bortxapps.goprocontrollerexample.ui.theme.GoProControllerExampleTheme
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeCameraControlScreen(viewModel: HomeCameraControlViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Screen(state)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(state: CameraHomeScreenState) {
    GoProControllerExampleTheme {
        val selectedTab = remember { mutableStateOf(HomeItems.STATUS) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("GoPro Controller Example")
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.purple_200))
                )
            },
            bottomBar = {
                if (state is CameraHomeScreenState.Connected) {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { },
                            label = { Text(text = "Status") },
                            selected = selectedTab.value == HomeItems.STATUS,
                            onClick = {
                                selectedTab.value = HomeItems.STATUS
                            }
                        )

                        NavigationBarItem(
                            icon = { },
                            label = { Text(text = "Commands") },
                            selected = selectedTab.value == HomeItems.COMMANDS,
                            onClick = {
                                selectedTab.value = HomeItems.COMMANDS
                            }
                        )

                        NavigationBarItem(
                            icon = { },
                            label = { Text(text = "Media") },
                            selected = selectedTab.value == HomeItems.MEDIA,
                            onClick = {
                                selectedTab.value = HomeItems.MEDIA
                            }
                        )
                    }
                }
            }
        ) {
            Column {
                when (state) {
                    is CameraHomeScreenState.Loading -> Loading()
                    is CameraHomeScreenState.Error -> ErrorText(this)
                    is CameraHomeScreenState.Connected -> HomeScreen(selectedTab.value)
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

@Composable
private fun HomeScreen(selectedTab: HomeItems) {
    when (selectedTab) {
        HomeItems.STATUS -> {
            CameraStatusScreen()
        }

        HomeItems.COMMANDS -> {
            CameraCommandsScreen()
        }

        HomeItems.MEDIA -> {
            CameraMediaScreen()
        }
    }
}