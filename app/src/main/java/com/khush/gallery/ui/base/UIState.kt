package com.khush.gallery.ui.base

sealed interface UIState<out T> {
    data class Success<T>(val data: T) : UIState<T>
    data class Failure<T>(val throwable: Throwable, val data: T? = null) : UIState<T>
    object Loading : UIState<Nothing>
    object Empty : UIState<Nothing>
}
