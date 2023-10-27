package com.bortxapps.goprocontrollerexample.screens.status

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bortxapps.goprocontrollerexample.R
import com.bortxapps.goprocontrollerexample.ui.theme.GoProControllerExampleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CameraStatusScreen(viewModel: CameraStatusViewModel = koinViewModel()) {

    val state by viewModel.state.collectAsState()

    Screen(state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(state: CameraStatusScreenState) {

    Log.d("CameraStatusScreen", "Screen: $state")
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
                        is CameraStatusScreenState.Loading -> Loading()
                        is CameraStatusScreenState.Error -> ErrorText(this)
                        is CameraStatusScreenState.StateRetrieved -> StateList(it)
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StateList(state: CameraStatusScreenState.StateRetrieved) {
    Column {
        Text(
            text = "Camera API Version: ${state.cameraApiVersion}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(state.map.toList(), key = { state -> state.first }) { (key, value) ->
                Column(Modifier.padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Text(
                            text = key,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = value,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Divider(thickness = 1.dp, modifier = Modifier.padding(top = 4.dp))
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
                .padding(16.dp),
        )
        Text(
            text = "Retrieving data from Go Pro", textAlign = TextAlign.Center, modifier = Modifier
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
                .padding(16.dp),
        )
    }
}