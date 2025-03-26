package com.shalev.hamal.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.shalev.hamal.ui.screens.HomeScreen
import com.shalev.hamal.ui.screens.PictureScreen
import com.shalev.hamal.ui.screens.PostScreen
import com.shalev.hamal.ui.screens.VideoScreen
import com.shalev.hamal.utils.Constants.WEBSITE_URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun Navigation(
    navController: NavHostController,
    exoPlayer: ExoPlayer,
    mainScreenListState: LazyListState,
    onScreenChange: (screen: Screen) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val focusedScreen by remember {
        derivedStateOf {
            val route = backStackEntry?.destination?.route
            Screen.entries.find { route?.startsWith(it.name) == true } ?: Screen.Start
        }
    }

    LaunchedEffect(focusedScreen) {
        onScreenChange(focusedScreen)
    }

    val onVideoFullScreen = remember(navController) {
        { url: String ->
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            navController.navigate("${Screen.Video.name}/$encodedUrl")
        }
    }

    val onPictureClick = remember(navController) {
        { url: String ->
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            navController.navigate("${Screen.Picture.name}/$encodedUrl")
        }
    }

    val onPostClick = remember(navController) {
        { id: String ->
            navController.navigate("${Screen.Post.name}/$id")
        }
    }

    val onPostScreenDeactivate: () -> Unit = {
        navController.popBackStack(route = Screen.Start.name, inclusive = false)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Start.name
    ) {
        composable(route = Screen.Start.name) {
            HomeScreen(
                exoPlayer = exoPlayer,
                isFocused = focusedScreen == Screen.Start,
                listState = mainScreenListState,
                onPostClick = onPostClick,
                onVideoFullScreen = onVideoFullScreen
            )
        }
        composable(route = "${Screen.Post.name}/{$POST_ID}") { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString(POST_ID)

            PostScreen(
                id = id,
                slug = null,
                exoPlayer = exoPlayer,
                isFocused = focusedScreen == Screen.Post,
                onPictureClick = onPictureClick,
                onVideoFullScreen = onVideoFullScreen,
                onDeactivate = onPostScreenDeactivate
            )
        }
        composable(
            route = "${Screen.Post.name}/slug/{$POST_SLUG}",
            arguments = listOf(navArgument(POST_SLUG) { type = NavType.StringType; }),
            deepLinks = listOf(navDeepLink { uriPattern = "$WEBSITE_URL/{$POST_SLUG}" })
        ) { navBackStackEntry ->
            val slug = navBackStackEntry.arguments?.getString(POST_SLUG)

            PostScreen(
                id = null,
                slug = slug,
                exoPlayer = exoPlayer,
                isFocused = focusedScreen == Screen.Post,
                onPictureClick = onPictureClick,
                onVideoFullScreen = onVideoFullScreen,
                onDeactivate = onPostScreenDeactivate
            )
        }
        composable(route = "${Screen.Picture.name}/{$PICTURE_URL}") { navBackStackEntry ->
            val url = navBackStackEntry.arguments?.getString(PICTURE_URL)
            url?.let {
                PictureScreen(url)
            }
        }
        composable(route = "${Screen.Video.name}/{$VIDEO_URL}") { navBackStackEntry ->
            val url = navBackStackEntry.arguments?.getString(VIDEO_URL)
            url?.let {
                VideoScreen(
                    url = url,
                    isFocused = focusedScreen == Screen.Video,
                    exoPlayer = exoPlayer,
                    onExitFullScreen = { navController.popBackStack() }
                )
            }
        }
    }
}