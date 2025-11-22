package com.example.randomuser.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.randomuser.screens.HomeScreen
import com.example.randomuser.viewModel.UserViewModel

@Composable
fun NavGraph(
    userViewModel: UserViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable("config_screen") {
            HomeScreen(
                onGenerateClick = { gender, nationality ->
                    // Передаем значения, полученные из HomeScreen, напрямую
                    userViewModel.startLoadingWithNewSettings(gender, nationality)
                    navController.navigate("detail")
                }
            )
        }
    }
}

sealed class Screen(
    val route: String
) {
    data object Home : Screen("home")
    data object Detail : Screen("detail")
    data object List : Screen("list")
}