package com.bortxapps.goprocontrollerexample.screens.cameracontrol.media.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItem
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItemType
import com.bortxapps.goprocontrollerexample.R
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.media.CameraMediaScreenDialogState
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.media.CameraMediaScreenState
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.media.CameraMediaViewModel
import com.bortxapps.goprocontrollerexample.ui.theme.GoProControllerExampleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CameraMediaScreen(viewModel: CameraMediaViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val stateDialog by viewModel.stateDialog.collectAsState()
    Screen(state, viewModel::onLoadThumbnail, viewModel::onItemClicked)
    DialogScreen(stateDialog, viewModel::onDismissDialog)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    state: CameraMediaScreenState,
    onLoadThumbnail: suspend (String) -> ByteArray?,
    onItemClicked: (GoProMediaItem) -> Unit
) {
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
                        is CameraMediaScreenState.Loading -> MediaItemsLoading()
                        is CameraMediaScreenState.Error -> MediaItemsError(this)
                        is CameraMediaScreenState.MediaList -> MediaItemsList(it.items, onLoadThumbnail, onItemClicked)

                    }
                }
            }
        }
    }
}


@Composable
fun DialogScreen(stateDialog: CameraMediaScreenDialogState, onDismissRequest: () -> Unit) {
    when (stateDialog) {
        is CameraMediaScreenDialogState.RetrievedImageFile -> MediaItemsDialogImage(stateDialog.goProMediaItem, onDismissRequest)
        is CameraMediaScreenDialogState.RetrievedVideoFile -> MediaItemsDialogVideo(stateDialog.goProMediaItem, onDismissRequest)
        is CameraMediaScreenDialogState.RetrievedGroupImageFile -> MediaItemsDialogImageGroup(stateDialog.goProMediaItem, onDismissRequest)
        else -> {}
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewItem() {
    MediaItemCard(
        GoProMediaItem(
            fileName = "GOPR0001.JPG",
            fileFullUrl = "100/GOPRO/GOPR0001.JPG",
            fileMediaUrl = "100/GOPRO/GOPR0001.JPG",
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
            videoWithImageStabilization = null,
            groupImagesNames = emptyList()
        ),
        onLoadThumbnail = { null }, {

        }
    )
}