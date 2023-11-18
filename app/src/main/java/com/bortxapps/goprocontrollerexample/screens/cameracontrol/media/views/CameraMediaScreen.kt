package com.bortxapps.goprocontrollerexample.screens.cameracontrol.media.views

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItem
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItemType
import com.bortxapps.goprocontrollerexample.R
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.media.CameraMediaScreenState
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.media.CameraMediaViewModel
import com.bortxapps.goprocontrollerexample.ui.theme.GoProControllerExampleTheme
import kotlinx.coroutines.async
import org.koin.androidx.compose.koinViewModel

@Composable
fun CameraMediaScreen(viewModel: CameraMediaViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Screen(state, viewModel::onLoadThumbnail)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(state: CameraMediaScreenState, onLoadThumbnail: suspend (String) -> ByteArray?) {
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
                        is CameraMediaScreenState.Loading -> Loading()
                        is CameraMediaScreenState.Error -> ErrorText(this)
                        is CameraMediaScreenState.MediaList -> StateList(it, onLoadThumbnail)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StateList(state: CameraMediaScreenState.MediaList, onLoadThumbnail: suspend (String) -> ByteArray?) {
    Column {
        Text(
            text = "Media content",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(items = state.items) { item ->
                MediaItem(item, onLoadThumbnail)
            }
        }
    }
}

@Composable
private fun MediaItem(item: GoProMediaItem, onLoadThumbnail: suspend (String) -> ByteArray?) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Column(Modifier.padding(vertical = 4.dp, horizontal = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

                var image by remember { mutableStateOf<ByteArray?>(null) }
                LaunchedEffect(item.fileFullUrl) {
                    image = async { onLoadThumbnail(item.fileFullUrl) }.await()
                }

                AsyncImage(
                    model = image,
                    contentDescription = "thumbnail",
                    modifier = Modifier.size(100.dp)
                )

                Column(
                    Modifier
                        .wrapContentWidth()
                        .padding(top = 16.dp, start = 8.dp)
                ) {
                    Text(
                        text = item.fileName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.wrapContentHeight()
                    )
                    Text(
                        text = item.mediaType.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(top = 8.dp)
                    )

                    Text(
                        text = "Size:  ${item.getSize()}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(top = 8.dp)
                    )
                }

                Spacer(
                    Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .background(Color.Green)
                )

                Text(
                    text = item.getCreationDate(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(
                            top = 8.dp
                        )
                )
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
    columnScope.run {
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

@Preview(showBackground = true)
@Composable
private fun PreviewItem() {
    MediaItem(
        GoProMediaItem(
            fileName = "GOPR0001.JPG",
            fileFullUrl = "100/GOPRO/GOPR0001.JPG",
            mediaId = "2342425",
            filePath = "",
            fileSize = 131431341234,
            creationTimeStamp = 342142467788,
            modTimeStamp = 2342431423424,
            thumbnailUrl = "",
            screenNailUrl = "",
            mediaType = GoProMediaItemType.VIDEO,
            mediaHeight = 0,
            mediaWidth = 0,
            photoWithHDR = null,
            photoWithWDR = null,
            audioOption = null,
            videoFraneRateNumerator = null,
            videoFraneRateDenominator = null,
            videoDurationSeconds = null,
            videoWithImageStabilization = null
        ),
        onLoadThumbnail = { null }
    )
}