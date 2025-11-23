package com.example.randomuser.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.randomuser.data.User
import com.example.randomuser.screens.DetailScreen
import com.example.randomuser.screens.HomeScreen
import com.example.randomuser.screens.ListScreen
import com.example.randomuser.viewModel.UserViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun NavGraph(
    userViewModel: UserViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = userViewModel,
                onNavigateToList = {
                    Log.d("ViewModelDebug", "4. Выполняется переход на UserListScreen!") // <-- ЛОГ №4
                    navController.navigate(Screen.List.route)
                }
            )
        }

        composable(Screen.List.route) {
            ListScreen(
                viewModel = userViewModel, // Передаем ТУ ЖЕ САМУЮ ViewModel
                onUserClick = { user ->
                    // Логика перехода на экран деталей остается прежней
                    val userJson = Json.encodeToString(user)
                    val encodedJson = URLEncoder.encode(userJson, "UTF-8")
                    navController.navigate(Screen.Detail.createRoute(encodedJson))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // --- Экран деталей пользователя ---
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            // Логика получения пользователя из аргументов остается прежней
            val userJson = backStackEntry.arguments?.getString("userJson") ?: return@composable
            val decodedJson = URLDecoder.decode(userJson, "UTF-8")
            val user = Json.decodeFromString<User>(decodedJson)

            DetailScreen(
                user = user,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(
    val route: String
) {
    object Home : Screen("home")

    object List : Screen("list")
    object Detail : Screen("detail/{userJson}") {
        fun createRoute(userJson: String) = "detail/$userJson"
    }
}