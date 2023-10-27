package com.bortxapps.goprocontrollerexample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bortxapps.goprocontrollerexample.screens.cameralist.CameraListScreen
import com.bortxapps.goprocontrollerexample.screens.media.CameraMediaScreen
import com.bortxapps.goprocontrollerexample.screens.status.CameraStatusScreen


@Composable
fun GoProNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "cameraList"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("cameraList") {
            CameraListScreen(
                onNavigationToStatus = { navController.navigate("cameraStatus/$it") }
            )
        }

        composable("cameraStatus/{address}") {
            CameraStatusScreen(
            )
        }

        composable("cameraMedia") {
            CameraMediaScreen(
            )
        }
    }
}