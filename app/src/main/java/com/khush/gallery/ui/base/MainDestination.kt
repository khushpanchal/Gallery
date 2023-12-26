package com.khush.gallery.ui.base

sealed class Route(
    val route: String,
    val routeWithoutArgs: String = route
) {
    object AlbumRoute : Route("albumRoute")
    object GalleryRoute : Route("galleryRoute/{albumId}", "galleryRoute")
    object ImageRoute : Route("imageRoute/{image}", "imageRoute")
}
