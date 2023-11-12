package com.bortxapps.goprocontrollerexample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.home.HomeCameraControlScreen
import com.bortxapps.goprocontrollerexample.screens.cameralist.views.CameraListScreen

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
        composable("cameraList") { _ ->
            CameraListScreen(
                onNavigationToStatus = { navController.navigate("cameraHome/$it") }
            )
        }

        composable("cameraHome/{address}") {
            HomeCameraControlScreen()
        }
    }
}