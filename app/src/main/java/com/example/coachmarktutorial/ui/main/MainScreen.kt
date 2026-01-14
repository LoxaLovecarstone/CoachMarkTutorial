package com.example.coachmarktutorial.ui.main

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coachmarktutorial.ui.components.ExpandableFab
import com.example.coachmarktutorial.ui.home.HomeScreen
import com.example.coachmarktutorial.ui.navigation.MainNavGraph
import com.example.coachmarktutorial.ui.navigation.Route
import com.example.coachmarktutorial.ui.post.PostScreen
import com.example.coachmarktutorial.ui.profile.ProfileScreen
import com.example.coachmarktutorial.ui.profile.edit.ProfileEditScreen
import com.example.coachmarktutorial.ui.search.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Route.Home.path
    val isHome = currentRoute == Route.Home.path

    var isFabExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(currentRoute) {
        isFabExpanded = false
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MainTopBar(
                currentRoute = currentRoute,
                onBackClick = { navController.popBackStack() },
                onRefreshClick = { /* TODO: Refresh Action */ }
            )
        },
        bottomBar = {
            MainBottomBar(
                currentRoute = currentRoute,
                onNavigate = { targetRoute ->
                    navController.navigate(targetRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        floatingActionButton = {
            if (isHome) {
                ExpandableFab(
                    isExpanded = isFabExpanded,
                    onFabClick = { isFabExpanded = !isFabExpanded },
                    onPostClick = { // 이름 변경 반영 (Write -> Post)
                        isFabExpanded = false
                        navController.navigate(Route.Post.path)
                    },
                    onSearchClick = {
                        isFabExpanded = false
                        navController.navigate(Route.Search.path)
                    }
                )
            }
        }
    ) { innerPadding ->
        MainNavGraph(
            navController  = navController,
            innerPadding = innerPadding
        )
    }
}