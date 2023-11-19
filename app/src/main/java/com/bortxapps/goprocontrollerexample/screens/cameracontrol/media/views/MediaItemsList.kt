package com.bortxapps.goprocontrollerexample.screens.cameracontrol.media.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItem
import kotlinx.coroutines.async

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaItemsList(
    items: List<GoProMediaItem>,
    onLoadThumbnail: suspend (String) -> ByteArray?,
    onItemClicked: (GoProMediaItem) -> Unit
) {
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
            items(items = items) { item ->
                MediaItemCard(item, onLoadThumbnail, onItemClicked)
            }
        }
    }
}

@Composable
fun MediaItemCard(item: GoProMediaItem, onLoadThumbnail: suspend (String) -> ByteArray?, onItemClicked: (GoProMediaItem) -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable { onItemClicked(item) }
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
