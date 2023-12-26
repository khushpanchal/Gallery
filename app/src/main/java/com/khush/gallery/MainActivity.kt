package com.khush.gallery

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.khush.gallery.ui.base.MainNavHost
import com.khush.gallery.ui.theme.GalleryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryTheme {
                Surface {
                    var permissionGranted by remember {
                        mutableStateOf(
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                ContextCompat.checkSelfPermission(
                                    this,
                                    android.Manifest.permission.READ_MEDIA_IMAGES
                                ) == PackageManager.PERMISSION_GRANTED
                            } else {
                                ContextCompat.checkSelfPermission(
                                    this,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED
                            }
                        )
                    }

                    val launcher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestPermission()
                    ) { isGranted ->
                        permissionGranted = isGranted
                    }

                    if (permissionGranted) {
                        MainNavHost()
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(text = getString(R.string.give_storage_permission_in_settings))
                            Button(onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    launcher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                                } else {
                                    launcher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }) {
                                Text(getString(R.string.give_access_to_storage))
                            }
                        }
                    }
                }
            }
        }
    }
}
