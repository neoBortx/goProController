package com.bortxapps.goprocontrollerexample.screens.cameralist.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bortxapps.goprocontrollerandroid.domain.data.GoProCamera
import com.bortxapps.goprocontrollerandroid.domain.data.PairedState
import com.bortxapps.goprocontrollerexample.R

@Composable
fun CameraList(
    columnScope: ColumnScope,
    onConnectToDevice: (String) -> Unit,
    nearbyCameras: List<GoProCamera> = listOf()
) {
    columnScope.run {
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
                        }
                    )
                ) {
                    CameraRow(onConnectToDevice, camera)
                }
            }
        }
    }
}

@Composable
private fun CameraRow(onConnectToDevice: (String) -> Unit, camera: GoProCamera){
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
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Connect")
        }
    }
}