package com.khush.gallery.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.khush.gallery.data.bean.Image

@Composable
fun GalleryLayout(
    imageList: List<Image>,
    galleryClicked: (Image) -> Unit
) {
    LazyColumn {
        items(imageList) {
            Gallery(it) { gallery ->
                galleryClicked(gallery)
            }
        }
    }
}

@Composable
fun Gallery(
    image: Image,
    onItemClick: (Image) -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)
        .clickable {
            onItemClick(image)
        }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.path)
                .build(),
            contentDescription = "icon",
            contentScale = ContentScale.Inside,
            modifier = Modifier
        )
    }
}

