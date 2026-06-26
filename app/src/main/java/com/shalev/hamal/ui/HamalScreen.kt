package com.shalev.hamal.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shalev.hamal.ui.providers.ExoPlayerProvider
import com.shalev.hamal.ui.providers.WebViewPoolProvider

enum class Screen {
    Start,
    Post,
    Picture,
    Video
}

const val POST_ID = "postId"
const val POST_SLUG = "postSlug"
const val PICTURE_URL = "pictureUrl"
const val VIDEO_URL = "videoUrl"

@Composable
fun HamalApp(
    navController: NavHostController = rememberNavController()
) {
    Surface {
        ExoPlayerProvider {
            WebViewPoolProvider {
                Navigation(
                    navController = navController,
                )
            }
        }
    }
}