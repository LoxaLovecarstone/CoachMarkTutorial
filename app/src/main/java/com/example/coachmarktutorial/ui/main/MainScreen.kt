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
import com.example.coachmarktutorial.ui.navigation.Route
import com.example.coachmarktutorial.ui.post.PostScreen
import com.example.coachmarktutorial.ui.profile.ProfileScreen
import com.example.coachmarktutorial.ui.profile.edit.ProfileEditScreen
import com.example.coachmarktutorial.ui.search.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Route.Home.path

    val showBottomBar = currentRoute in Route.bottomBarRoutes.map { it.path }
    val isHome = currentRoute == Route.Home.path

    //  Route 객체에서 제목을 찾아옴
    val currentTitle = when (currentRoute) {
        Route.Post.path -> Route.Post.title
        Route.Search.path -> Route.Search.title
        else -> Route.bottomBarRoutes.find { it.path == currentRoute }?.title ?: "Coach Mark"
    }

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
                navigationIcon = {
                    // 홈이 아니면 뒤로가기 버튼 표시
                    if (!isHome && currentRoute != Route.Profile.path) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    if (isHome) {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (showBottomBar) {
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
        NavHost(
            navController = navController,
            startDestination = Route.Home.path,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.Home.path) {
                HomeScreen()
            }
            composable(Route.Profile.path) {
                ProfileScreen(
                    onEditClick = {
                        navController.navigate(Route.ProfileEdit.path)
                    }
                )
            }
            composable(Route.Post.path) {
                PostScreen(
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Route.Search.path) {
                SearchScreen(
                    onPostClick = { postId ->
                        // [수정] this 대신 context 사용 & .show() 추가
                        Toast.makeText(context, "Clicked post: $postId", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            composable(Route.ProfileEdit.path) {
                ProfileEditScreen(
                    onSaveSuccess = {
                        navController.popBackStack() // 저장 후 뒤로가기
                    }
                )
            }
        }
    }
}