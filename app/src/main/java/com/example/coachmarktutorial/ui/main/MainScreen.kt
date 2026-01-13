package com.example.coachmarktutorial.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.materiala3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coachmarktutorial.ui.components.ExpandableFab
import com.example.coachmarktutorial.ui.home.HomeScreen
import com.example.coachmarktutorial.ui.navigation.Route
import com.example.coachmarktutorial.ui.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // 현재 화면 감지
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Route.Home.path

    // 화면별 UI 표시 여부 & 타이틀 관리
    val isHome = currentRoute == Route.Home.path

    // TopBar 타이틀: 현재 라우트에 맞는 title을 찾음
    val currentTitle = Route.bottomBarRoutes.find { it.path == currentRoute }?.title ?: "Coach Mark"

    var isFabExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(currentRoute) {
        isFabExpanded = false
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentTitle,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    // Refresh 버튼은 '홈'에서만 보여준다!
                    if (isHome) {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Route.bottomBarRoutes.forEach { route ->
                    NavigationBarItem(
                        icon = { Icon(route.icon!!, contentDescription = route.title) },
                        label = { Text(route.title) },
                        selected = currentRoute == route.path,
                        onClick = {
                            navController.navigate(route.path) {
                                // 네비게이션 스택 관리
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
        },
        floatingActionButton = {
            // FAB는 '홈' 화면일 때만
            if (isHome) {
                ExpandableFab(
                    isExpanded = isFabExpanded,
                    onFabClick = { isFabExpanded = !isFabExpanded },
                    onWriteClick = {
                        isFabExpanded = false
                        // TODO: 글쓰기 화면 이동
                    },
                    onSearchClick = {
                        isFabExpanded = false
                        // TODO: 검색 화면 이동
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Home.path,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.Home.path) {
                HomeScreen()
            }
            composable(Route.Profile.path) {
                ProfileScreen()
            }
        }
    }
}