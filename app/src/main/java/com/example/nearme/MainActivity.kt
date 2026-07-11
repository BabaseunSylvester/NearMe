package com.example.nearme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nearme.ui.screens.DetailScreen
import com.example.nearme.ui.screens.FavoritesScreen
import com.example.nearme.ui.screens.HomeScreen
import com.example.nearme.ui.theme.NearMeTheme
import com.example.nearme.ui.viewmodel.DetailViewModel
import com.example.nearme.ui.viewmodel.FavoritesViewModel
import com.example.nearme.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Explore", Icons.Default.Search)
    object Favorites : Screen("favorites", "Saved", Icons.Default.Favorite)
    object Detail : Screen("detail/{fsq_id}", "Detail", Icons.Default.Search)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NearMeTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val items = listOf(Screen.Home, Screen.Favorites)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentDestination?.route != Screen.Detail.route) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 0.dp
                            ) {
                                items.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = null) },
                                        label = { Text(screen.label) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            val viewModel: SearchViewModel = hiltViewModel()
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateToDetail = { id ->
                                    navController.navigate("detail/$id")
                                }
                            )
                        }
                        composable(Screen.Favorites.route) {
                            val viewModel: FavoritesViewModel = hiltViewModel()
                            FavoritesScreen(
                                viewModel = viewModel,
                                onNavigateToDetail = { id ->
                                    navController.navigate("detail/$id")
                                }
                            )
                        }
                        composable(
                            route = Screen.Detail.route,
                            arguments = listOf(navArgument("fsq_id") { type = NavType.StringType })
                        ) {
                            val viewModel: DetailViewModel = hiltViewModel()
                            DetailScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
