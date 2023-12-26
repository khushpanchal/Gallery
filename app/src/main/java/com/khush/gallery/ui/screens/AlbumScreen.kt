package com.khush.gallery.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khush.gallery.R
import com.khush.gallery.data.bean.Album
import com.khush.gallery.ui.base.ShowError
import com.khush.gallery.ui.base.ShowLoading
import com.khush.gallery.ui.base.UIState
import com.khush.gallery.ui.components.AlbumLayout
import com.khush.gallery.viewmodels.AlbumViewModel

@Composable
fun AlbumScreen(
    albumViewModel: AlbumViewModel = hiltViewModel(),
    itemClicked: (Album) -> Unit
) {
    val albumUiState: UIState<List<Album>> by albumViewModel.albumItem.collectAsStateWithLifecycle()

    when (albumUiState) {
        is UIState.Loading -> {
            ShowLoading()
        }

        is UIState.Failure -> {
            ShowError(
                text = stringResource(id = R.string.something_went_wrong),
                retryEnabled = true
            ) {
                albumViewModel.fetchAlbumItems()
            }
        }

        is UIState.Success -> {
            AlbumLayout(albumList = (albumUiState as UIState.Success<List<Album>>).data) {
                itemClicked(it)
            }
        }

        is UIState.Empty -> {

        }
    }
}