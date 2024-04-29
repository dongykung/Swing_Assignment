package com.dkproject.swingassignment.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dkproject.swingassignment.ui.screen.favorite.MyFavoriteScreen
import com.dkproject.swingassignment.ui.screen.home.HomeScreen
import com.dkproject.swingassignment.ui.screen.home.HomeViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.log

//스크린 루트
enum class HomeRoute(
    val route: String,
    val description: String,
    val icons: ImageVector,
    val selectedIcons: ImageVector
) {
    Home(
        route = "HomeScreen",
        description = "홈",
        icons = Icons.Outlined.Home,
        selectedIcons = Icons.Filled.Home
    ),
    Favorite(
        route = "FavoriteScreen",
        description = "즐겨찾기",
        icons = Icons.Outlined.Favorite,
        selectedIcons = Icons.Filled.Favorite
    )
}

@Composable
fun HomeNavigation(
    navHostController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navHostController)
        }
    ) { padding ->
        HomeScreenNavigation(navController = navHostController, paddingValues = padding)
    }
}

@Composable
fun HomeScreenNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = HomeRoute.Home.route) {
        composable(route = HomeRoute.Home.route) {
            HomeScreen(viewModel = homeViewModel, modifier = Modifier.padding(paddingValues))
        }
        composable(route = HomeRoute.Favorite.route) {
            MyFavoriteScreen(modifier=Modifier.padding(paddingValues),viewModel = homeViewModel)
        }
    }
}


@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    //현재 루트
    val currentRoute: HomeRoute = navBackStackEntry?.destination?.route.let { currentRoute ->
        HomeRoute.entries.find { route ->
            currentRoute == route.route
        }
    } ?: HomeRoute.Home
    NavigationBar {
        val navigationItem = listOf(HomeRoute.Home, HomeRoute.Favorite)

        navigationItem.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentRoute == item,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) {
                                saveState = true
                            }
                        }
                        this.launchSingleTop = true
                        this.restoreState = true
                    }
                },
                icon = {
                    //현재 스크린이 선택 되었다면 아이콘 이미지 filled
                    if (item == currentRoute) Icon(
                        imageVector = item.selectedIcons,
                        contentDescription = item.description,
                        tint = if (item.route == HomeRoute.Favorite.route) Color.Red else Color.Black
                    )
                    //현재 스크린이 선택 되지 않았다면 아이콘 이미지 outlineed
                    else Icon(imageVector = item.icons, contentDescription = item.description)
                },
                label = {
                    Text(
                        text = item.description,
                        fontWeight = if (item == currentRoute) FontWeight.Bold else null
                    )
                }
            )
        }
    }
}