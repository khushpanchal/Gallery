package com.khush.gallery.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.khush.gallery.data.GalleryPagingSource
import com.khush.gallery.data.bean.Image
import com.khush.gallery.data.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val galleryRepository: GalleryRepository
) : ViewModel() {

    private val _galleryResponse: MutableStateFlow<PagingData<Image>> =
        MutableStateFlow(PagingData.empty())
    var galleryResponse = _galleryResponse.asStateFlow()
        private set

    init {
        val albumId = (savedStateHandle.get("albumId") as? String?) ?: ""
        viewModelScope.launch(Dispatchers.IO) {
            Pager(
                config = PagingConfig(
                    10, enablePlaceholders = true
                )
            ) {
                GalleryPagingSource(albumId, galleryRepository)
            }.flow.cachedIn(viewModelScope).collect {
                _galleryResponse.value = it
            }
        }
    }
}