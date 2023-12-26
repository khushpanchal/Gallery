package com.khush.gallery.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.khush.gallery.data.bean.Image
import com.khush.gallery.ui.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ImageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _imageItem = MutableStateFlow<UIState<Image>>(UIState.Empty)
    val imageItem: StateFlow<UIState<Image>> = _imageItem

    init {
        val path = savedStateHandle.get("image") as? String? ?: ""
        var image: Image? = null
        kotlin.runCatching {
            val gson = Gson()
            image = gson.fromJson(path, Image::class.java)
        }
        viewModelScope.launch {
            if (image != null) {
                _imageItem.emit(UIState.Success(image!!))
            }
        }
    }
}