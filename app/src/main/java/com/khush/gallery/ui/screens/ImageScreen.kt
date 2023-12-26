package com.khush.gallery.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khush.gallery.R
import com.khush.gallery.data.bean.Image
import com.khush.gallery.ui.base.ShowError
import com.khush.gallery.ui.base.ShowLoading
import com.khush.gallery.ui.base.UIState
import com.khush.gallery.ui.components.ImageLayout
import com.khush.gallery.viewmodels.ImageViewModel

@Composable
fun ImageScreen(
    imageViewModel: ImageViewModel = hiltViewModel()
) {
    val imageUiState: UIState<Image> by imageViewModel.imageItem.collectAsStateWithLifecycle()

    when (imageUiState) {
        is UIState.Loading -> {
            ShowLoading()
        }

        is UIState.Failure -> {
            ShowError(
                text = stringResource(id = R.string.something_went_wrong),
            )
        }

        is UIState.Success -> {
            ImageLayout((imageUiState as UIState.Success<Image>).data)
        }

        is UIState.Empty -> {

        }
    }

}