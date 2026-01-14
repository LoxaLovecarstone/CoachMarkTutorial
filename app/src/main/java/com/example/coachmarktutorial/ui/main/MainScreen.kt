package com.example.coachmarktutorial.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coachmarktutorial.ui.coachmark.CoachMarkOverlay
import com.example.coachmarktutorial.ui.coachmark.CoachMarkTarget
import com.example.coachmarktutorial.ui.coachmark.LocalCoachMarkState
import com.example.coachmarktutorial.ui.coachmark.coachMarkTarget
import com.example.coachmarktutorial.ui.coachmark.rememberCoachMarkState
import com.example.coachmarktutorial.ui.components.ExpandableFab
import com.example.coachmarktutorial.ui.navigation.MainNavGraph
import com.example.coachmarktutorial.ui.navigation.Route
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Route.Home.path
    val isHome = currentRoute == Route.Home.path

    var isFabExpanded by remember { mutableStateOf(false) }

    val coachMarkState = rememberCoachMarkState()

    LaunchedEffect(Unit) {
        delay(1000) // UI가 그려질 시간을 줌
        coachMarkState.setCurrentStep(CoachMarkTarget.REFRESH)
    }

    LaunchedEffect(currentRoute) {
        isFabExpanded = false
    }


    CompositionLocalProvider(LocalCoachMarkState provides coachMarkState) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                            modifier = Modifier.coachMarkTarget(CoachMarkTarget.FAB),
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
                    navController = navController,
                    innerPadding = innerPadding
                )
            }
            CoachMarkOverlay()
        }
    }
}