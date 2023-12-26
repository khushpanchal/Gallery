package com.khush.gallery.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.khush.gallery.data.bean.Image
import com.khush.gallery.data.repository.GalleryRepository

class GalleryPagingSource(
    private val albumId: String,
    private val galleryRepository: GalleryRepository
) : PagingSource<Int, Image>() {

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: 1
        val response = galleryRepository.getImages(albumId, page)
        return try {
            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.isEmpty()) null else page.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(
                e
            )
        }
    }

}