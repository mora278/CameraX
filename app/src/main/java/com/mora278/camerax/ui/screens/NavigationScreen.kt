package com.mora278.camerax.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mora278.camerax.ui.viewmodels.CameraXViewModel

enum class Routes(val id: String) {
    Home("Home"),
    CameraX("Camera X")
}

@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Home.id,
        builder = {
            composableForHome(navController)
            composableForCameraX()
        }
    )
}

private fun NavGraphBuilder.composableForHome(navController: NavHostController) {
    composable(route = Routes.Home.id) {
        HomeScreen(navigate = navController::navigate)
    }
}

private fun NavGraphBuilder.composableForCameraX() {
    composable(route = Routes.CameraX.id) {
        val cameraXViewModel: CameraXViewModel = viewModel()
        CameraXScreen(
            cameraXViewModel = cameraXViewModel
        )
    }
}