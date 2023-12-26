package com.khush.gallery.ui.base

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.khush.gallery.R
import com.khush.gallery.ui.screens.AlbumScreen
import com.khush.gallery.ui.screens.GalleryScreen
import com.khush.gallery.ui.screens.ImageScreen
import java.net.URLEncoder

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            MainTopBar {
                navController.popBackStack()
            }
        }
    ) {
        MainNavHost(
            modifier = Modifier.padding(it),
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(onBackClicked: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = { onBackClicked() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.AlbumRoute.route,
        modifier = modifier
    ) {
        composable(
            route = Route.AlbumRoute.route
        ) {
            AlbumScreen {
                navController.navigate(Route.GalleryRoute.routeWithoutArgs + "/${it.id}") {
                    launchSingleTop = true
                }
            }
        }
        composable(
            route = Route.GalleryRoute.route,
            arguments = listOf(navArgument("albumId") { type = NavType.StringType })
        ) {
            GalleryScreen {
                val imageJsonString = Gson().toJson(it)
                val encodedImage = URLEncoder.encode(imageJsonString, Charsets.UTF_8.name())
                navController.navigate(Route.ImageRoute.routeWithoutArgs + "/${encodedImage}") {
                    launchSingleTop = true
                }
            }
        }
        composable(
            route = Route.ImageRoute.route,
            arguments = listOf(navArgument("image") { type = NavType.StringType })
        ) {
            ImageScreen()
        }
    }
}

