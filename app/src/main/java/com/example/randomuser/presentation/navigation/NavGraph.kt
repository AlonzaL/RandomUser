package com.example.randomuser.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.randomuser.presentation.screens.DetailScreen
import com.example.randomuser.presentation.screens.HomeScreen
import com.example.randomuser.presentation.screens.ListScreen
import com.example.randomuser.presentation.viewModel.UserViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = userViewModel,
                onNavigateToList = {
                    navController.navigate(Screen.List.route)
                }
            )
        }

        composable(Screen.List.route) {
            ListScreen(
                viewModel = userViewModel, // Передаем ТУ ЖЕ САМУЮ ViewModel
                onUserClick = { user ->
                    val uuid = user.login?.uuid
                    if (uuid != null) {
                        navController.navigate(Screen.Detail.createRoute(uuid))
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("userUuid") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val userUuid = backStackEntry.arguments?.getString("userUuid")
            LaunchedEffect(userUuid) {
                if (userUuid != null) {
                    userViewModel.selectUser(userUuid)
                }
            }
            DetailScreen(
                viewModel = userViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(
    val route: String
) {
    object Home : Screen("home")
    object List : Screen("list")
    object Detail : Screen("detail/{userUuid}") {
        fun createRoute(userUuid: String) = "detail/$userUuid"
    }
}