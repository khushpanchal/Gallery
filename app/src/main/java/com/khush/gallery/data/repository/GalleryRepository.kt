package com.khush.gallery.data.repository

import android.content.ContentResolver
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import com.khush.gallery.common.Const
import com.khush.gallery.data.bean.Album
import com.khush.gallery.data.bean.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GalleryRepository @Inject constructor(
    private val contentResolver: ContentResolver
) {

    suspend fun getImages(
        albumId: String,
        pageNum: Int,
        limit: Int = Const.LIMIT
    ): List<Image> {
        val imageList = mutableListOf<Image>()
        if (pageNum < 1) return emptyList()
        withContext(Dispatchers.IO) {
            val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Images.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }


            val imageProjection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA
            )

            val offset = (pageNum - 1) * limit

            val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"


            val imageCursor: Cursor? = contentResolver.query(
                collection,
                imageProjection,
                "${MediaStore.Images.Media.BUCKET_ID} = ?",
                arrayOf(albumId),
                sortOrder
            )

            imageCursor?.use {
                var counter = 0
                while (it.moveToNext() && counter < limit + offset) {
                    if (counter >= offset) {
                        val imageId =
                            it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                        val imagePath =
                            it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                        imageList.add(Image(imageId, imagePath))
                    }
                    counter++
                }
            }
        }
        return imageList
    }

    suspend fun getAlbumList() = flow {

        val albums = hashMapOf<String, Album>()
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val cursor: Cursor? = contentResolver.query(
            collection,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val folderId =
                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID))
                val folderName =
                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                albums[folderId] = Album(folderId, folderName)
            }
        }
        emit(albums.values.toList())
    }

}